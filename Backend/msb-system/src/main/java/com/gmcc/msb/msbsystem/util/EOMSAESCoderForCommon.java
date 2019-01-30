package com.gmcc.msb.msbsystem.util;

import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmcc.msb.msbsystem.entity.org.EOMSRequestEntity;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class EOMSAESCoderForCommon {
	private static final Logger _LOG = Logger.getLogger(EOMSAESCoderForCommon.class);

	public static final String CHARSET_UTF8 = "UTF-8";

	/**
	 * 密钥算法
	 */
	public static final String KEY_ALGORITHM = "AES";

	/**
	 * 加密/解密算法 / 工作模式 / 填充方式<br>
	 * jdk1.6支持PKCS5Padding填充方式<br>
	 */
	public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

	/**
	 * 初始化密钥生成器的种子
	 */
	private static final String AES_SEED = "EOMSAESCoderForCommon#seed#Bten7YZx3CZTc1un2dWdtkjex6vKWAzObFujo78Ll82CuxqW6VPCC5aJORa8m/WEGGqQLJ/Zf8vso4JKZ8K/sM0X//SRCd4PS2FHXdHKDY4tS2K5ssX60/8Vi/zKjmJNI3EjBg==";
	private static final String AES_SEED_BASE64;

	/**
	 * 密钥，base64编码
	 */
	private static final String KEY_BASE64 = "yRo44twHJ1gsaFq9TYdjAw==";

	/**
	 * 初始化向量IV
	 */
	private static final String IVSPEC_BASE64 = "8wv90rBvMfOWRTCiFjES1w==";

	static {
		AES_SEED_BASE64 = base64EncodeUTF8(AES_SEED);
	}

	private static String base64Encode(byte[] binaryData) {
//		try {
//			return new String(Base64.encodeBase64(binaryData), CHARSET_UTF8);
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException(e);
//		}
		return Base64.encodeBase64String(binaryData);
	}

	private static byte[] base64Decode(String base64String) {
//		try {
//			return Base64.decodeBase64(base64String.getBytes(CHARSET_UTF8));
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException(e);
//		}
		return Base64.decodeBase64(base64String);
	}

	private static String base64EncodeUTF8(String inputData) {
		try {
			return base64Encode(inputData.getBytes(CHARSET_UTF8));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static SecureRandom getSecureRandom() throws IOException {
		return new SecureRandom(base64Decode(AES_SEED_BASE64));
	}

	private static IvParameterSpec getIvParameterSpec() {
		return new IvParameterSpec(base64Decode(IVSPEC_BASE64));
	}

	/**
	 * 获取密钥，将base64编码密钥转换成二进制密钥
	 *
	 * @return
	 */
	public static byte[] getKey() {
		return base64Decode(KEY_BASE64);
	}

	/**
	 * 生成密钥
	 *
	 * @return
	 * @throws Exception
	 */
	public static byte[] initKey() throws Exception {
		// AES要去密钥长度为128位、192位、256位。
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
		keyGenerator.init(128, getSecureRandom());
		// 生成密钥
		SecretKey secretKey = keyGenerator.generateKey();
		// 获得密钥的二进制编码形式
		return secretKey.getEncoded();
	}

	/**
	 * 转换密钥
	 *
	 * @param key
	 *            二进制密钥
	 * @return Key 密钥
	 */
	private static Key toKey(byte[] key) {
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}

	/**
	 * 加密
	 *
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @return
	 * @throws Exception
	 * @throws
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 还原密钥
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		// 初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, k, getIvParameterSpec());
		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return 解密数据
	 * @throws Exception
	 * @throws
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 还原密钥
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		// 初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, k, getIvParameterSpec());
		return cipher.doFinal(data);
	}

	/**
	 * 加密
	 *
	 * @param inputData
	 *            待加密数据
	 * @return 加密之后字符串
	 * @throws IOException
	 */
	public static String encryptUTF8(String inputData) throws Exception {
		byte[] data = inputData.getBytes(CHARSET_UTF8);
		byte[] key = getKey();
		byte[] encryptData = encrypt(data, key);
		return base64Encode(encryptData);
	}

	/**
	 * 解密
	 *
	 * @param inputData
	 *            待解密数据
	 * @return 解密完成数据
	 * @throws Exception
	 */
	public static String decryptUTF8(String inputData) throws Exception {
		byte[] data = base64Decode(inputData);
		byte[] key = getKey();
		byte[] decryptData = decrypt(data, key);
		return new String(decryptData, CHARSET_UTF8);
	}

	private static void test1() throws Exception{
		// key对比...begin...
		_LOG.info("test1...key对比...begin...");
		System.out.println("test1...key对比...begin...");

		List<Boolean> listKeyEquals = new ArrayList<Boolean>();
		List<byte[]> listKey = new ArrayList<byte[]>();
		byte[] listKeyItem = null;

		for(int i = 0; i < 10000; i++){
			byte[] arrKeyInitKey = initKey();
			byte[] arrKeyBase64Key = getKey();
			boolean isKeyEquals = Arrays.equals(arrKeyInitKey, arrKeyBase64Key);

			if(!listKeyEquals.contains(isKeyEquals)){
				listKeyEquals.add(isKeyEquals);
			}

			if(listKey.size() == 0){
				listKey.add(arrKeyInitKey);
			}

			for(int aaa = 0; aaa < listKey.size(); aaa++){
				listKeyItem = listKey.get(aaa);

				if(!Arrays.equals(listKeyItem, arrKeyInitKey)){
					listKey.add(arrKeyInitKey);
				}

				if(!Arrays.equals(listKeyItem, arrKeyBase64Key)){
					listKey.add(arrKeyBase64Key);
				}
			}

//			System.out.println("test1...key对比，base64Key、initKey：" + Arrays.equals(arrKeyInitKey, arrKeyBase64Key));
//			System.out.println("test1...key.initKey>>>>>" + Arrays.toString(arrKeyInitKey));
//			System.out.println("test1...key.base64Key>>>" + Arrays.toString(arrKeyBase64Key));
		}

		System.out.println("test1...listKeyEquals:" + listKeyEquals);
		System.out.println("test1...listKey:" + listKey);
		System.out.println("test1...key对比...end...");
		// key对比...end...
	}

	private static void test2() throws Exception{
		List<Boolean> listStringEquals = new ArrayList<Boolean>();
		Random random = new Random();

		for(int i = 0; i < 100; i++){
			// 加密、解密对比...1...begin...
			String str = "加密、解密对比...1...begin...等我我改过来╮(╯_╰)╭===/glgl@#**GLHJ999+++我我我";

			str += random.nextDouble();

			String str_000 = encryptUTF8(str);
			String str_100 = decryptUTF8(str_000);
			boolean isEquals = str.equals(str_100);

//			System.out.println("====原始字符串\t:" + str);
//			System.out.println("加密之后字符串\t:" + str_000);
//			System.out.println("解密之后字符串\t:" + str_100);
//			System.out.println("加密、解密对比\t:" + isEquals);

			if(!listStringEquals.contains(isEquals)){
				listStringEquals.add(isEquals);
			}

			// 加密、解密对比...1...end...
		}

		System.out.println("test2...加密、解密结果对比：" + listStringEquals);
	}

	private static void test3() throws Exception{
		List<Boolean> listByteEquals = new ArrayList<Boolean>();
		List<Boolean> listStringEquals = new ArrayList<Boolean>();
		Random random1 = new Random();
		Random random2 = new Random();
		int strMaxLen = 100000;
		StringBuilder sbInfo = new StringBuilder();

		for(int aaa = 0; aaa < 10000; aaa++){
			sbInfo.setLength(0);

			for(int i = 0; i < random1.nextInt(strMaxLen); i++){
				sbInfo.append((char) random2.nextInt(Character.MAX_VALUE));
			}

			String strSrc = sbInfo.toString(); // 原始字符串
			byte[] arrSrcData = strSrc.getBytes("UTF-8"); // 原始字符串.字节数组

			List<byte[]> listKey = Arrays.asList(initKey(), getKey());
			byte[] ekey = listKey.get(random1.nextInt(Integer.MAX_VALUE) % 2);
			byte[] dkey = listKey.get(random2.nextInt(Integer.MAX_VALUE) % 2);

			byte[] arrEncryptData = encrypt(arrSrcData, ekey);
			byte[] arrDecryptData = decrypt(arrEncryptData, dkey);
			String strDec = new String(arrDecryptData, "UTF-8");

			// 加密前、解密后的字节数组对比。
			boolean isByteEquals = Arrays.equals(arrSrcData, arrDecryptData);
			// 字符串对比
			boolean isStringEquals = strSrc.equals(strDec);

			if(!listByteEquals.contains(isByteEquals)){
				listByteEquals.add(isByteEquals);
			}

			if(!listStringEquals.contains(isStringEquals)){
				listStringEquals.add(isStringEquals);
			}
		}

		System.out.println("test3...加密、解密对比，字节数组\t:" + listByteEquals);
		System.out.println("test3...加密、解密对比，字符串\t:" + listStringEquals);
		System.out.println("test3...字符串对比不一定相等，编码问题...");
	}

	public static void main(String[] args) throws Exception {
//		test1();
//		test2();
//		test3();

        EOMSRequestEntity entity = new EOMSRequestEntity();
        entity.setOperation("ZHZX_ALL_ORGS");
        entity.setSystem("MSB");
        Map<String, String> data = new HashMap<>();
        data.put("fields", "ORGID,PARENTORGID,ORGNAME");
        entity.setData(data);

        ObjectMapper mapper = new ObjectMapper();
        String asString = mapper.writeValueAsString(entity);


        System.out.println(encryptUTF8(asString));

//        System.out.println(decryptUTF8("8a0L2CGqgrSwc8MPAOv62Wuve7OGi+cv5fhl2umu5x1YPbv1fG9mpZ/hHWdC+UAbgrB4JaoWeX1JEeLIyhiDVxgWBrmss1xfozBkRngczM4="));
	}

}
