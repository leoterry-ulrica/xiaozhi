package com.dist.bdf.base.security;

import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;

import javax.crypto.Cipher;

/**
 * rsa 非对称加密解密
 * @author weifj
 *
 */
public class DesEncryptRSA implements DesEncryptAdapter {

	/**
	 * 算法名称
	 */
	private static final String ALGORITHOM = "RSA";
	
	/**
	 * 保存生成的密钥对的文件路径
	 */
	public static final String SecurityStorePath = "/dist_security";

	/**
	 * 保存生成的密钥对的文件名称
	 */
	private static final String RSA_PAIR_FILENAME = "/DIST_RSA_PAIR.key";

	/**
	 * 密钥大小
	 */
	private static final int KEY_SIZE = 1024;

	/**
	 *  默认的安全服务提供者
	 */
	private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();

	private static KeyPairGenerator keyPairGen = null;

	private static KeyFactory keyFactory = null;

	/**
	 * 缓存的密钥对
	 */
	private static KeyPair oneKeyPair = null;

	private static File rsaPairFile = null;
	/**
	 * rsa公钥指数参数，hex后的值
	 */
	private static final String DefaultPublicExponent = "010001";
	/**
	 * rsa公钥模参数，hex后的值
	 */
	private static final String DefaultPublicModulus = 
	"00c0a06103efa58cfa21fe341055b41ae002c861d0d2485d0ad1fc4edad57a17f3096aaa3bc489a09cb068444b20490a568748ecfea6efa70908dc51d468ec51eb622dc7bb5fad89dc1e795edb73ef7b42ca7d319df3af2256b4fe00b5920cc4a414e9e43715329dedd517d28c53b9d7ed33e20beca882acd260c0eed8f290545f";
	/**
	 * rsa私钥指数参数，hex后的值
	 */
	private static final String DefaultPrivateExponent = "045beff94ebac077143e7458f849243a27bf90efea5720afe64c6c1ccfdeefc2cf89d7e8faf221faa693bd7a4ef46e9828e564e39a0084fdad0cc1bdb653585c678b867c5dd945fff7ee32fc6197442961bffe674a34dbf0301e0dd8aa5fb2c1c5cd37bbb5229d6324ebc834565a3087209820fd901677e000b95a92631f0731";
	/**
	 * rsa私钥模参数，hex后的值
	 */
	private static final String DefaultPrivateModulus = "00c0a06103efa58cfa21fe341055b41ae002c861d0d2485d0ad1fc4edad57a17f3096aaa3bc489a09cb068444b20490a568748ecfea6efa70908dc51d468ec51eb622dc7bb5fad89dc1e795edb73ef7b42ca7d319df3af2256b4fe00b5920cc4a414e9e43715329dedd517d28c53b9d7ed33e20beca882acd260c0eed8f290545f";
	
	static {
		try {
			keyPairGen = KeyPairGenerator.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
			keyFactory = KeyFactory.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
		} catch (NoSuchAlgorithmException ex) {
		}
		rsaPairFile = new File(getRSAPairFilePath());
	}

	/**
	    * 获取适配器实例
	    * @return
	    */
	public static DesEncryptAdapter getInstance() {

		return InnerDesEncryptAdapter.INSTANCE;
	}

	static class InnerDesEncryptAdapter {

		private final static DesEncryptAdapter INSTANCE = new DesEncryptRSA();
	}

	private DesEncryptRSA() {
	}

	/**
	 * 生成并返回RSA密钥对。
	 */
	private static synchronized KeyPair generateKeyPair() {
		try {
			keyPairGen.initialize(KEY_SIZE, new SecureRandom());
			oneKeyPair = keyPairGen.generateKeyPair();
			saveKeyPair(oneKeyPair);
			return oneKeyPair;
		} catch (InvalidParameterException ex) {
		} catch (NullPointerException ex) {
		}
		return null;
	}

	/**
	 * 返回生成/读取的密钥对文件的路径。
	 */
	private static String getRSAPairFilePath() {
		
		String basePath = (new File("").getAbsolutePath())+SecurityStorePath;
		File dir = new File(basePath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		// String urlPath = DesEncryptRSA.class.getResource("/").getPath();
		System.out.println("ras key : "+dir.getAbsolutePath() + RSA_PAIR_FILENAME);
		return (dir.getAbsolutePath() + RSA_PAIR_FILENAME);
	}

	/**
	 * 若需要创建新的密钥对文件，则返回 {@code true}，否则 {@code false}。
	 */
	private static boolean isCreateKeyPairFile() {
		// 是否创建新的密钥对文件
		boolean createNewKeyPair = false;
		if (!rsaPairFile.exists() || rsaPairFile.isDirectory()) {
			createNewKeyPair = true;
		}
		return createNewKeyPair;
	}

	/**
	 * 将指定的RSA密钥对以文件形式保存。
	 * 
	 * @param keyPair 要保存的密钥对。
	 */
	private static void saveKeyPair(KeyPair keyPair) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = FileUtils.openOutputStream(rsaPairFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(keyPair);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(fos);
		}
	}

	/**
	 * 返回RSA密钥对。
	 */
	public KeyPair getKeyPair() {
		// 首先判断是否需要重新生成新的密钥对文件
		if (isCreateKeyPairFile()) {
			// 直接强制生成密钥对文件，并存入缓存。
			return generateKeyPair();
		}
		if (oneKeyPair != null) {
			return oneKeyPair;
		}
		return readKeyPair();
	}

	// 同步读出保存的密钥对
	private KeyPair readKeyPair() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = FileUtils.openInputStream(rsaPairFile);
			ois = new ObjectInputStream(fis);
			oneKeyPair = (KeyPair) ois.readObject();
			return oneKeyPair;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeQuietly(ois);
			IOUtils.closeQuietly(fis);
		}
		return null;
	}

	/**
	 * 根据给定的系数和专用指数构造一个RSA专用的公钥对象。
	 * 
	 * @param modulus 系数。
	 * @param publicExponent 专用指数。
	 * @return RSA专用公钥对象。
	 */
	public RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) {
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
		try {
			return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
		} catch (InvalidKeySpecException ex) {
		} catch (NullPointerException ex) {
		}
		return null;
	}

	/**
	 * 根据给定的系数和专用指数构造一个RSA专用的私钥对象。
	 * 
	 * @param modulus 系数。
	 * @param privateExponent 专用指数。
	 * @return RSA专用私钥对象。
	 */
	public RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) {
		RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus),
				new BigInteger(privateExponent));
		try {
			return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
		} catch (InvalidKeySpecException ex) {
		} catch (NullPointerException ex) {
		}
		return null;
	}

	/**
	 * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的私钥对象。
	 * 
	 * @param modulus 系数。
	 * @param privateExponent 专用指数。
	 * @return RSA专用私钥对象。
	 */
	public RSAPrivateKey getPrivateKeyHex(String hexModulus, String hexPrivateExponent) {
		if (StringUtils.isBlank(hexModulus) || StringUtils.isBlank(hexPrivateExponent)) {
			return null;
		}
		byte[] modulus = null;
		byte[] privateExponent = null;
		try {
			modulus = Hex.decodeHex(hexModulus.toCharArray());
			privateExponent = Hex.decodeHex(hexPrivateExponent.toCharArray());
		} catch (DecoderException ex) {
		}
		if (modulus != null && privateExponent != null) {
			return generateRSAPrivateKey(modulus, privateExponent);
		}
		return null;
	}

	/** 
     * 使用模和指数生成RSA私钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus 
     *            模 
     * @param exponent 
     *            指数 
     * @return 
     */  
    public RSAPrivateKey getPrivateKey(String modulus, String exponent) {  
        try {  
            BigInteger b1 = new BigInteger(modulus);  
            BigInteger b2 = new BigInteger(exponent);  
 
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);  
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
	/**
	 * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的公钥对象。
	 * 
	 * @param modulus 系数。
	 * @param publicExponent 专用指数。
	 * @return RSA专用公钥对象。
	 */
	public RSAPublicKey getPublidKeyHex(String hexModulus, String hexPublicExponent) {
		if (StringUtils.isBlank(hexModulus) || StringUtils.isBlank(hexPublicExponent)) {
			return null;
		}
		byte[] modulus = null;
		byte[] publicExponent = null;
		try {
			modulus = Hex.decodeHex(hexModulus.toCharArray());
			publicExponent = Hex.decodeHex(hexPublicExponent.toCharArray());
		} catch (DecoderException ex) {
		}
		if (modulus != null && publicExponent != null) {
			return generateRSAPublicKey(modulus, publicExponent);
		}
		return null;
	}
	
	/** 
     * 使用模和指数生成RSA公钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus 
     *            模 
     * @param exponent 
     *            指数 
     * @return 
     */  
    public RSAPublicKey getPublicKey(String modulus, String exponent) {  
        try {  
            BigInteger b1 = new BigInteger(modulus);  
            BigInteger b2 = new BigInteger(exponent);  
 
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);  
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  

	/**
	 * 使用指定的公钥加密数据。
	 * 
	 * @param publicKey 给定的公钥。
	 * @param data 要加密的数据。
	 * @return 加密后的数据。
	 */
	public byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
		Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
		ci.init(Cipher.ENCRYPT_MODE, publicKey);
		return ci.doFinal(data);
	}

	/**
	 * 使用指定的私钥解密数据。
	 * 
	 * @param privateKey 给定的私钥。
	 * @param data 要解密的数据。
	 * @return 原数据。
	 */
	protected byte[] decrypt(PrivateKey privateKey, byte[] data) throws Exception {
	
		Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
		ci.init(Cipher.DECRYPT_MODE, privateKey);
		return ci.doFinal(data);
	}

	/**
	 * 使用给定的公钥加密给定的字符串。
	 * <p />
	 * 若 {@code publicKey} 为 {@code null}，或者 {@code plaintext} 为 {@code null} 则返回 {@code
	 * null}。
	 * 
	 * @param publicKey 给定的公钥。
	 * @param plaintext 字符串。
	 * @return 给定字符串的密文。
	 */
	protected String encryptString(PublicKey publicKey, String plaintext) {
		if (publicKey == null || plaintext == null) {
			return null;
		}
		byte[] data = plaintext.getBytes();
		try {
			byte[] en_data = encrypt(publicKey, data);
			return new String(Hex.encodeHex(en_data));
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * 使用给定的私钥解密给定的字符串。
	 * <p />
	 * 若私钥为 {@code null}，或者 {@code encrypttext} 为 {@code null}或空字符串则返回 {@code null}。
	 * 私钥不匹配时，返回 {@code null}。
	 * 
	 * @param privateKey 给定的私钥。
	 * @param encrypttext 密文。
	 * @return 原文字符串。
	 */
	public String decryptString(PrivateKey privateKey, String encrypttext) {
		if (privateKey == null || StringUtils.isBlank(encrypttext)) {
			return null;
		}
		try {
			byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
			byte[] data = decrypt(privateKey, en_data);
			return new String(data);
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * 使用默认的私钥解密，结果再把文本字幕倒转
	 * 
	 * @param encryptText 密文。
	 * @return {@code encryptText} 的原文字符串。
	 */
	public String decryptReverse(String encryptText) {

		String text = this.decrypt(encryptText);
		if (text == null) {
			return null;
		}
		return StringUtils.reverse(text);
	}

	/**
	 * 返回已初始化的默认的公钥
	 * @return
	 */
	public RSAPublicKey getDefaultPublicKey() {
		
		return this.getPublidKeyHex(DefaultPublicModulus, DefaultPublicExponent);
	/*	KeyPair keyPair = getKeyPair();
		if (keyPair != null) {
			return (RSAPublicKey) keyPair.getPublic();
		}
		return null;*/
	}

	/**
	 * 返回已初始化的默认的私钥
	 * @return
	 */
	public RSAPrivateKey getDefaultPrivateKey() {
		
		return this.getPrivateKeyHex(DefaultPrivateModulus, DefaultPrivateExponent);
		
		/*KeyPair keyPair = getKeyPair();
		if (keyPair != null) {
			return (RSAPrivateKey) keyPair.getPrivate();
		}
		return null;*/
	}

	/**
	 * 使用默认的公钥加密给定的字符串。
	 * <p />
	 * 若{@code plaintext} 为 {@code null} 则返回 {@code null}。
	 * 
	 * @param plaintext 字符串。
	 * @return 给定字符串的密文。
	 */
	@Override
	public String encrypt(String text) {

		if (text == null) {
			return null;
		}

		// KeyPair keyPair = getKeyPair();
		try {
			byte[] data = text.getBytes(SecurityDefine.Charset);
			byte[] en_data = encrypt(this.getDefaultPublicKey(), data);
			return new String(Hex.encodeHex(en_data));
		} catch (NullPointerException ex) {
		} catch (Exception ex) {
		}
		return null;

	}

	/**
	 * 使用默认的私钥解密给定的字符串。
	 * <p />
	 * 若{@code encrypttext} 为 {@code null}或空字符串则返回 {@code null}。
	 * 私钥不匹配时，返回 {@code null}。
	 * 
	 * @param encrypttext 密文。
	 * @return 原文字符串。
	 */
	@Override
	public String decrypt(String text) {

		if (StringUtils.isBlank(text)) {
			return null;
		}
		//KeyPair keyPair = getKeyPair();
		try {
			byte[] en_data = Hex.decodeHex(text.toCharArray());
			byte[] data = decrypt(this.getDefaultPrivateKey(), en_data);
			return new String(data);
		} catch (NullPointerException ex) {
		} catch (Exception ex) {
		}
		return null;

	}
	/**
	 * 获取默认公钥的模
	 * @param isDefault 是否默认 
	 * @return
	 */
	public String getPublicModulus(boolean isDefault) {

		if(isDefault)
			return DefaultPublicModulus;
		
		RSAPublicKey publicKey = this.getDefaultPublicKey();
		if(null == publicKey) return null;
		
		 // 模  
	    String modulus = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
	    return modulus;
	    
	}
	/**
	 * 获取默认公钥的指数
	 * @param isDefault 是否默认
	 * @return
	 */
	public String getPublicExponent(boolean isDefault) {
		
		if(isDefault)
			return DefaultPublicExponent;
		
		RSAPublicKey publicKey = this.getDefaultPublicKey();
		if(null == publicKey) return null;
		
		// 公钥指数  
	    String publicExponent = new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
	    return publicExponent;
	}
    

	public static void main(String[] args) throws Exception {

		DesEncryptRSA rsa = (DesEncryptRSA)DesEncryptRSA.getInstance();
		
	/*	RSAPublicKey pubkey = rsa.getDefaultPublicKey();
		RSAPrivateKey privatekey = rsa.getDefaultPrivateKey();
		
		System.out.println("RSA公钥："+pubkey.toString());
		
		System.out.println("RSA公钥模："+ new String(Hex.encodeHex(pubkey.getModulus().toByteArray())));
		System.out.println("RSA公钥指数："+new String(Hex.encodeHex(pubkey.getPublicExponent().toByteArray())));
		
		System.out.println("RSA私钥模："+ new String(Hex.encodeHex(privatekey.getModulus().toByteArray())));
		System.out.println("RSA私钥指数："+new String(Hex.encodeHex(privatekey.getPrivateExponent().toByteArray())));*/
	
		String data = "123";
		System.out.println("原文："+data);
		String codeString = rsa.encrypt(data);

		System.out.println("RSA加密："+codeString);
		System.out.println("RSA解密："+rsa.decrypt(codeString));
		
	}
}
