package com.dist.bdf.base.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/*
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
*/
public class Coder {

    /** 
     * MAC算法可选以下多种算法：
     * <pre> 
     * HmacMD5  
     * HmacSHA1  
     * HmacSHA256  
     * HmacSHA384  
     * HmacSHA512 
     * </pre> 
     * 默认选用HmacMD5。
     */
    public static final String MAC = "HmacMD5";

    /**
     * 用于使用HmacMD5和MD5加密后的字节数组转为字符串时不出现乱码。
     */
    public static final int RADIX_DM5 = 16;

    /**
     * 用于使用SHA加密后的字节数组转为字符串时不出现乱码。
     */
    public static final int RADIX_SHA = 32;

    /**
     * SHA算法名
     */
    public static final String SHA = "SHA";

    /**
     * MD5算法名
     */
    public static final String MD5 = "MD5";
    /**
     * 默认编码
     */
    private static final String defaultEncoding = "utf-8";

    /**
     *使用BASE64算法转码。
     * @param source 明文。
     * @return 转码之后的密文。
     * @throws UnsupportedEncodingException 
     */
    public static String encryptByBASE64(String source) throws UnsupportedEncodingException {
        byte[] byteArray = source.getBytes(defaultEncoding);
        //BASE64Encoder base64 = new BASE64Encoder();
        return Base64.encodeBase64String(byteArray);//base64.encode(byteArray);
    }

    /**
     *使用BASE64算法转码。
     * @param source 明文为byte[]。
     * @return 转码之后的密文。
     */
    public static String encryptByBASE64(byte[] source) {
        //BASE64Encoder base64 = new BASE64Encoder();
        return Base64.encodeBase64String(source);// base64.encodeBuffer(source);
    }

    /**
     *使用BASE64算法解码。
     * @param code 被BASE64算法加密明文得到的密文String。
     * @return 解码之后的明文。
     */
    public static String decryptByBASE64(String code) throws IOException {
        byte[] byteArray =  Base64.decodeBase64(code);//new BASE64Decoder().decodeBuffer(code);
       
        return new String(byteArray,defaultEncoding);
    }

    /**
     *使用BASE64算法解码。
     * @param code 被BASE64算法加密明文得到的密文byte[]。
     * @return 解码之后的明文。
     */
    public static byte[] decryptByBASE64ToByteArray(String code) throws IOException {
        return  Base64.decodeBase64(code);//new BASE64Decoder().decodeBuffer(code);
    }

    /**
     *将加密后得到的字节数组转化为字符串。<p>
     *如果加密后的字节数组直接转成字符串，那这个字符串里会是乱码，需要经过处理之后才可以用于输出。
     * @param code 加密后的数组。
     * @param algorithm 算法名字。
     * @return 转化后的字符串。
     */
    public static String convertCode2String(byte[] code, String algorithm) {
        BigInteger bigInteger = new BigInteger(code);
        String str = "";
        if (algorithm.equals(MAC) || algorithm.equals(MD5)) {
            str = bigInteger.toString(RADIX_DM5);
        } else if (algorithm.equals(SHA)) {
            str = bigInteger.toString(RADIX_SHA);
        } else {
            str = bigInteger.toString();
        }
        return str;
    }

    /**
     * 对明文加密的过程。<p>
     *由于SHA、MD5算法都会调用MessageDigest提供算法加密数据，其加密过程几乎一致，故将加密过程提出为该方法，便于复用。
     * @param source 明文。
     * @param algorithm 算法名字。
     * @return 转成字符串后的密文。
     */
    public static String updatesDigest(String source, String algorithm) throws NoSuchAlgorithmException {
        byte[] byteArray = source.getBytes();
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(byteArray);
        byte[] encryptStrArray = md.digest();
        return convertCode2String(encryptStrArray, algorithm);

    }

    /**
     *使用MD5算法加密。
     * @param source明文。
     * @return MD5加密后的密文。
     */
    public static String encryptByMD5(String source) throws NoSuchAlgorithmException {
        return updatesDigest(source, MD5);
    }

    /**
     *使用MD5算法加密后使用BASE64转码。
     * @param source 明文。
     * @return MD5加密后用BASE64转码的密文。
     * @throws UnsupportedEncodingException 
     */
    public static String encryptByMD5WithBASE64(String source) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return encryptByBASE64(encryptByMD5(source));
    }

    /**
     *使用SHA算法对字符串加密。
     * @param source 明文。
     * @return SHA加密后的密文。
     */
    public static String encryptBySHA(String source) throws NoSuchAlgorithmException {
        return updatesDigest(source, SHA);
    }

    /**
     *使用SHA算法加密后使用BASE64转码。
     * @param source 明文。
     * @return SHA加密后用BASE64转码的密文。
     * @throws UnsupportedEncodingException 
     */
    public static String encryptBySHAWithBASE64(String source) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return encryptByBASE64(encryptBySHA(source));
    }

    /**
     *生成HMAC密匙。<p>
     *HMAC算法是基于密钥的Hash算法的认证协议。需要先生成密匙，将密匙公布，在加密时也需要使用密匙加密。
     * @return 由BASE64算法转码后的HMAC密匙。
     * @throws UnsupportedEncodingException 
     */
    public static String generateHMACSecretKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(MAC);
        SecretKey secretKey = keyGenerator.generateKey();
        return encryptByBASE64(convertCode2String(secretKey.getEncoded(), MAC));
    }

    /**
     *使用HMAC算法加密。
     * @param source 明文。
     * @param secretKey HMAC密匙。
     * @return HMAC算法加密的密文字符串。
     */
    public static String encryptByHMAC(String source, String secretKey) throws IOException, NoSuchAlgorithmException,
            InvalidKeyException {
        SecretKey sk = new SecretKeySpec(decryptByBASE64(secretKey).getBytes(), MAC);
        Mac mac = Mac.getInstance(sk.getAlgorithm());
        mac.init(sk);
        return convertCode2String(mac.doFinal(source.getBytes()), MAC);

    }

    /**
     *使用HMAC算法加密后使用BASE64转码。
     * @param source 明文。
     * @param secretKey HMAC密匙。
     * @return HMAC算法加密后用BASE64转码的密文字符串。
     */
    public static String encryptByHMACWithBASE64(String source, String secretKey) throws IOException,
            NoSuchAlgorithmException, InvalidKeyException {
        return decryptByBASE64(encryptByHMAC(source, secretKey));
    }
}
