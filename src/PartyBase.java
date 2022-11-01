
import entity.OTReceptMsg;
import encrypt.DES;
import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class  PartyBase {
	public BufferedReader inputBr=null;
	public  PrintWriter output=null;
	public ObjectInputStream inputStream =null;
	public ObjectOutputStream outputStream =null;
	public List<OTReceptMsg> secretByOTList = new ArrayList<>();
	public List<String> secretByTransmitList= new ArrayList<String>();

	public int keyCount = 5;

	public String textS = "I get it";

	public List<Integer> randomKeyInts = new ArrayList<>();

	public List<String> encryptSList = new ArrayList<>();

	public void generateRandomKey(){
		for (int i = 0; i < keyCount; i++) {
			randomKeyInts.add(Integer.parseInt(DES.generateKey()));
		}
	}

	public boolean verifyOTAndWholeSecret(){
		int idx =0;
		for (OTReceptMsg otReceptMsg: secretByOTList){
			if(!otReceptMsg.getMsg().equals(secretByTransmitList.get(idx+otReceptMsg.getIdx()*keyCount))){
				return false;
			}
			idx ++;
		}
		return true;
	}
}
