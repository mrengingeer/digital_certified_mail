package encrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

public class DES {

	private final static String DES = "DES";
	private final static String ENCODE = "GBK";
	private final static String defaultKey = "12345678";
	public static String intToBinary32(int i, int bitNum){
		String binaryStr = Integer.toBinaryString(i);
		while(binaryStr.length() < bitNum){
			binaryStr = "0"+binaryStr;
		}
		return binaryStr;
	}
	public static String paddingNumStr(int num){
		String numStr = String.valueOf(num);
		for (int i = numStr.length(); i < 8; i++) {
			numStr = "0" + numStr;
		}
		return numStr;
	}

	public static void main(String[] args) throws Exception {
		String data = "I get it";
//		/BHmI7JB0RQ8NCddMmHh3Q==
//		57737801
		String bs =intToBinary32(1,32);
		String secret = encrypt(data, "83676287");
		String secret1 = encrypt(data, "868064623");
		System.out.println(decrypt("/BHmI7JB0RQ8NCddMmHh3Q==","57737801"));
		System.out.println(encrypt(data));
		System.out.println(decrypt(encrypt(data)));

	}

	public static String generateKey(){
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

	public static String encrypt(String data) throws Exception {
		byte[] bt = encrypt(data.getBytes(ENCODE), defaultKey.getBytes(ENCODE));
		String strs = new BASE64Encoder().encode(bt);
		return strs;
	}


	public static String decrypt(String data) throws IOException, Exception {
		if (data == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] buf = decoder.decodeBuffer(data);
		byte[] bt = decrypt(buf, defaultKey.getBytes(ENCODE));
		return new String(bt, ENCODE);
	}


	public static String encrypt(String data, String key) throws Exception {
		byte[] bt = encrypt(data.getBytes(ENCODE), key.getBytes(ENCODE));
		String strs = new BASE64Encoder().encode(bt);
		return strs;
	}


	public static String decrypt(String data, String key) throws IOException,
			Exception {
		if (data == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] buf = decoder.decodeBuffer(data);
		byte[] bt = decrypt(buf, key.getBytes(ENCODE));
		return new String(bt, ENCODE);
	}


	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {

		SecureRandom sr = new SecureRandom();


		DESKeySpec dks = new DESKeySpec(key);


		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);


		Cipher cipher = Cipher.getInstance(DES);


		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}


	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {

		SecureRandom sr = new SecureRandom();


		DESKeySpec dks = new DESKeySpec(key);


		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);


		Cipher cipher = Cipher.getInstance(DES);


		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}
}
