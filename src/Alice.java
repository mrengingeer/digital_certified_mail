import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import encrypt.DES;
import encrypt.RSA;



public class Alice extends PartyBase{
	public int a0;
	public String encryptM;
	public String message;

	public Alice(String message) {
		this.message = message;
	}

	public void generateRandomKeyStep1A(){
		a0 = Integer.parseInt(DES.generateKey());
		this.generateRandomKey();
		for (int i = 0; i < this.keyCount; i++) {
			this.randomKeyInts.add(a0 ^ this.randomKeyInts.get(i));
		}
	}

	public boolean encyptTextMAndTextSStep1A(){
		try {
			String key0 = DES.paddingNumStr(a0);
			encryptM = DES.encrypt(message,key0);
			for (int i = 0; i < 2*keyCount; i++) {
				String key = DES.paddingNumStr(this.randomKeyInts.get(i));
				this.encryptSList.add(DES.encrypt(textS,key));
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.encryptSList.clear();
			return false;
		}
		return true;
	}

	public boolean transmitencyptTextStep1A(){

		this.output.println("Step1A");
		try {
			String line = this.inputBr.readLine();
			if(!line.equals("#OK#")){
				return false;
			}

			this.outputStream.writeObject(encryptM);

			for (int i = 0; i < 2*keyCount; i++) {
				this.outputStream.writeObject(this.encryptSList.get(i));
			}
			this.outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean step1AForAlice(){

		this.generateRandomKeyStep1A();

		if(!this.encyptTextMAndTextSStep1A()){
			return false;
		}

		return this.transmitencyptTextStep1A();
	}

	public boolean verifySign(){

		this.output.println("#OK#");
		boolean verifyFlag;
		try {

			String sign = (String) this.inputStream.readObject();

			String originText = (String) this.inputStream.readObject();

			String pk = (String) this.inputStream.readObject();

			verifyFlag = RSA.verify(originText,pk,sign);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return verifyFlag;
	}
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(8888);
		Socket client = server.accept();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("please input you want send message:");
		String mailMessage = br.readLine();
		Alice alice = new Alice(mailMessage);

		alice.inputBr = new BufferedReader(new InputStreamReader(client.getInputStream()));
		alice.output = new PrintWriter(client.getOutputStream(),true);
		alice.inputStream = new ObjectInputStream(client.getInputStream());
		alice.outputStream = new ObjectOutputStream(client.getOutputStream());

		alice.step1AForAlice();

		if(!alice.inputBr.readLine().equals("Step1B")){
			System.err.println("protocol step is wrong");
			return;
		}

		if(alice.verifySign()){

			System.out.println("------receipt from Bom is valid, B had recepted the message-------");
		}else{
			System.err.println("receipt from Bom is error");
		}

		PSESubprotocol.pseStep1(alice,alice.randomKeyInts);

		PSESubprotocol.receptMsgFromOtherByOT(alice);

		PSESubprotocol.pseSendStep2(alice);

		PSESubprotocol.pseReceptStep2(alice);

		if(alice.verifyOTAndWholeSecret()){
			System.out.println("------alice send finish,the receiver is no problem------");
		}else{
			System.out.println("alice send finish,the receiver is not correct");
		}

		alice.inputBr.close();
		alice.outputStream.close();
		alice.outputStream.close();
		alice.inputStream.close();
		server.close();

	}
}