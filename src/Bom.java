
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import encrypt.DES;
import encrypt.RSA;
import entity.OTReceptMsg;

public class Bom extends PartyBase{
	private Map<String,Object> rsaKey;
	public Bom() {

		try {
			rsaKey = RSA.initKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String encryptM;

	public List<String> encryptSFormAList = new ArrayList<>();

	public boolean receptEncrpyText(){

		this.output.println("#OK#");

		try {

			encryptM = (String) this.inputStream.readObject();

			for (int i = 0; i <2*keyCount ; i++) {
				encryptSFormAList.add((String) this.inputStream.readObject());
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	@Override
	public void generateRandomKey() {
		super.generateRandomKey();
		for (int i = 0; i < keyCount; i++) {
			this.randomKeyInts.add(Integer.parseInt(DES.generateKey()));
		}
	}

	public boolean encryptSForStep1B(){

		this.generateRandomKey();

		for (int i = 0; i < 2*keyCount; i++) {
			try {
				String key = DES.paddingNumStr(this.randomKeyInts.get(i));
				this.encryptSList.add(DES.encrypt(textS,key));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public String productDeclares(){
		String beginDeclare = "[Beginning of B's Declaration]\n";
		String denotation = "[Denotation:]\n";
		StringBuilder content = new StringBuilder();
		String statement = "[Statement:] I acknowledge having received the mail, which result from decrypting C by F\n";
		String endDeclare = "[End of B's Declaration]\n";
		for (int i = 0; i < this.encryptSFormAList.size()-1; i++) {
			content.append(encryptSFormAList.get(i)).append(',');
		}
		content.append(encryptSFormAList.get(this.encryptSFormAList.size()-1)).append(';');
		for (int i = 0; i < this.encryptSList.size()-1; i++) {
			content.append(encryptSList.get(i)).append(',');
		}
		content.append(encryptSList.get(this.encryptSList.size()-1)).append(';');
		content.append('\n');

		return beginDeclare + denotation + content.toString() +  statement + endDeclare;
	}

	public boolean sendSignToA(){

		String declaration = this.productDeclares();
		try {

			String sign = RSA.sign(declaration, RSA.getPrivateKey(rsaKey));

			this.output.println("Step1B");
			if(!this.inputBr.readLine().equals("#OK#")){
				System.err.println("A can not receiver step1B command");
				return false;
			}

			this.outputStream.writeObject(sign);

			this.outputStream.writeObject(declaration);

			this.outputStream.writeObject(RSA.getPublicKey(rsaKey));
			this.outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean step1BForBom(){

		if(!encryptSForStep1B()){
			return false;
		}
		return sendSignToA();
	}

	public int calA0(){
		int a0 = Integer.parseInt(secretByTransmitList.get(keyCount)) ^ Integer.parseInt(secretByTransmitList.get(0));

		return a0;
	}
	public static void main(String[] args) throws IOException {
		Socket client = new Socket("127.0.0.1", 8888);
		Bom bom = new Bom();
		bom.output = new PrintWriter(client.getOutputStream(), true);
		bom.inputBr = new BufferedReader(new InputStreamReader(client.getInputStream()));
		bom.outputStream = new ObjectOutputStream(client.getOutputStream());
		bom.inputStream = new ObjectInputStream(client.getInputStream());

		String line=bom.inputBr.readLine();

		if(line.equals("Step1A")){
			if(bom.receptEncrpyText()){
				System.out.println("Step1A: bom success");
			}else{
				System.err.println("Step1A: bom error");
			}
		}else{
			return;
		}

		if(bom.step1BForBom()){
			System.out.println("Step1B: bom success");
		}else{
			System.err.println("Step1B: bom error");
		}

		PSESubprotocol.receptMsgFromOtherByOT(bom);

		PSESubprotocol.pseStep1(bom,bom.randomKeyInts);

		PSESubprotocol.pseReceptStep2(bom);

		PSESubprotocol.pseSendStep2(bom);

		if(bom.verifyOTAndWholeSecret()){

			int a0 = bom.calA0();
			try {
				String realMessage = DES.decrypt(bom.encryptM,DES.paddingNumStr(a0));
				System.out.println("---bom had decrypy message, message: "+realMessage+"----");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("bom decrypt message fail");
				return;
			}
		}else{
			System.err.println("there is cheat, bom can not know message");
		}

		bom.inputBr.close();
		bom.output.close();
		bom.inputStream.close();
		bom.outputStream.close();
		client.close();

	}


}

