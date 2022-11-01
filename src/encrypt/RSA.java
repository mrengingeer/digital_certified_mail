
package encrypt;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class RSA{
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";

        public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}
        
        public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	public static String sign(String data, String privateKey) throws Exception {
		return sign(data.getBytes(), privateKey);
	}


	public static String sign(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = decryptBASE64(privateKey);

		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		signature.update(data);

		return encryptBASE64(signature.sign());
	}

	public static boolean verify(String data, String publicKey, String sign) throws Exception {
		return verify(data.getBytes(), publicKey, sign);
	}

	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {

		byte[] keyBytes = decryptBASE64(publicKey);

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		PublicKey pubKey = keyFactory.generatePublic(keySpec);

		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);

		return signature.verify(decryptBASE64(sign));
	}


	public static String decryptByPrivateKey(String data, String key) throws Exception {
		return new String(decryptByPrivateKey(decryptBASE64(data), key));
	}

	public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decryptBASE64(key);

		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return cipher.doFinal(data);
	}

	public static String decryptByPublicKey(String data, String key) throws Exception {
		return new String(decryptByPublicKey(decryptBASE64(data), key));
	}

	public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decryptBASE64(key);

		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(data);
	}


	public static String encryptByPublicKey(String data, String key) throws Exception {
		return encryptBASE64(encryptByPublicKey(data.getBytes(), key));
	}

	public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decryptBASE64(key);

		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return cipher.doFinal(data);
	}


	public static String encryptByPrivateKey(String data, String key) throws Exception {
		return encryptBASE64(encryptByPrivateKey(data.getBytes(), key));
	}

	public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decryptBASE64(key);

		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return cipher.doFinal(data);
	}

	public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);

		return encryptBASE64(key.getEncoded());
	}

	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);

		return encryptBASE64(key.getEncoded());
	}

	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

		keyPairGen.initialize(1024);

		KeyPair keyPair = keyPairGen.generateKeyPair();

		PublicKey publicKey = keyPair.getPublic();

		PrivateKey privateKey = keyPair.getPrivate();

		Map<String, Object> keyMap = new HashMap<String, Object>(2);

		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}


	public static void main(String[] args) {
		try {
			Map<String, Object> map = RSA.initKey();
			String publicKey = RSA.getPublicKey(map);
			String privateKey = RSA.getPrivateKey(map);
			System.out.println("Public key：" + publicKey);
			System.out.println("Private key：" + privateKey);
			String data = "sdfisfhdsfdohssdfsfdsfdsfafdsfsfafsfdsfdsfcxvxcvxvcxvvxv";
			String sign = sign(data, RSA.getPrivateKey(map));
			boolean verify = verify(data, RSA.getPublicKey(map), sign);
			System.out.println(verify);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
