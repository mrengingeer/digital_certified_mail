

import entity.OTReceptMsg;
import encrypt.DES;
import encrypt.RSA;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import sun.security.krb5.internal.crypto.Des;

public class ObliviousTransfer {

	public static void OT1to2Sender(PartyBase sender,String M0, String M1){

		try {

			Map<String, Object> keyMap0 = RSA.initKey();

			Map<String, Object> keyMap1 = RSA.initKey();

			String r0 = RSA.getPublicKey(keyMap0);
			String r1 =RSA.getPublicKey(keyMap1);
			sender.outputStream.writeObject(r0);
			sender.outputStream.writeObject(r1);
			sender.outputStream.flush();

			String desKey = (String) sender.inputStream.readObject();

			String decryptDesKey0;
			try {
				decryptDesKey0 = RSA.decryptByPrivateKey(desKey, RSA.getPrivateKey(keyMap0));
			}catch(Exception e){
//				System.err.println("no select 0");
				decryptDesKey0 = "00000000";
			}
			String decryptDesKey1;
			try {
				decryptDesKey1 = RSA.decryptByPrivateKey(desKey, RSA.getPrivateKey(keyMap1));
			}catch (Exception e){
//				System.err.println("no select 1");
				decryptDesKey1 = "00000000";
			}

			String desEncrypt0 = DES.encrypt(M0, decryptDesKey0);
			String desEncrypt1 = DES.encrypt(M1,decryptDesKey1);

			sender.outputStream.writeObject(desEncrypt0);
			sender.outputStream.writeObject(desEncrypt1);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static OTReceptMsg OT1to2Receiver(PartyBase receiver){

		try {

			String rsaPk0 = (String) receiver.inputStream.readObject();
			String rsaPk1 = (String) receiver.inputStream.readObject();

			String desKey = DES.generateKey();

			Random random = new Random();
			int bitIdx = random.nextInt(2);
			String realRSAPK = bitIdx == 0?rsaPk0:rsaPk1;


			String encryptDesKey = RSA.encryptByPublicKey(desKey, realRSAPK);
	
			receiver.outputStream.writeObject(encryptDesKey);receiver.outputStream.flush();
	
			String recMsg0 = (String) receiver.inputStream.readObject();
			String recMsg1 = (String) receiver.inputStream.readObject();
			String realRecMsg = bitIdx == 0?recMsg0:recMsg1;

			String decryptMsg = DES.decrypt(realRecMsg, desKey);
			return new OTReceptMsg(bitIdx, decryptMsg);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//7gd3BJxbDTU=  33552969
		return null;
	}
}
