package cn.rejiejay.application.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSONObject;

/**
 * JAVA 数字签名 加密解密的方法
 * 
 * @author _rejeijay
 * @Date 2019年7月3日22:02:01
 */
public class DigitalSignature {
	/**
	 * 通过请求参数生成 密钥
	 * md5 加密的前16位 就是 AES-128-CBC密匙
	 * @param reqParam 请求的参数
	 * @return key
	 */
	public static String ReqParamToMD5(String reqParam) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5"); // 生成一个MD5加密计算摘要
		md.update(reqParam.getBytes("UTF-8")); // 前端是 cipher.update(data, 'utf8', 'binary');
		// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
		byte[] secretBytes = md.digest();
		// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
		return new BigInteger(1, secretBytes).toString(16);
	}
	
	/**
	 * 数字加密方法
	 * 前端同等实现 java那边使用AES 128位填充模式：AES/CBC/PKCS7Padding加密方法，在nodejs中采用对应的aes-128-cbc加密方法就能对应上，因为有使用向量（iv），所以nodejs中要用createCipheriv方法，而不是createCipher。
	 * 加密使用 AES-128-CBC对称加密算法(128位AES/ECB/PKCS7Padding加密/解密) + BASE64
	 * @param reqParam 请求参数 不管是get请求还是post请求，一律转为_utf-8字符串
	 * @param username 用户姓名
	 * @param token 凭证
	 * @return BASE64和AES-128-CBC对称加密算法 数字加密签名
	 * @throws Exception
	 */
	public static String EncryptSignature(String reqParam, String username, String token) throws Exception {
		/**
		 * 生成需要加密的内容(最后解密出来的也是此内容
		 */ 
		JSONObject jsonObjContent = new JSONObject(); // 解密应该不需要注意顺序，但是前端加密的时候保险起见还是用这种顺序吧
		jsonObjContent.put("username", username);
		jsonObjContent.put("token", token);
		String content = jsonObjContent.toString();
		
		/**
		 * 开始加密
		 */
		String reqParamMD5= ReqParamToMD5(reqParam);
		String sKey = reqParamMD5.substring(0, 32); // 密钥key 需要为32位。
		SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes("utf-8"), "AES"); 
		String ivParameter = reqParamMD5.substring(reqParamMD5.length() - 16); // 向量iv 后16位
		IvParameterSpec ips = new IvParameterSpec(ivParameter.getBytes("utf-8"));
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // "算法/模式/补码方式"
		// 使用CBC模式，需要一个向量iv，可增加加密算法的强度  
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
		byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));  
		
        return Base64.getEncoder().encodeToString(encrypted); // 此处使用BASE64做转码。
	}

    /**
     * 解密数字凭证获取 token _timestamp _username
     * 先 BASE64 解密， 再使用AES-128-CBC对称加密算法解密(128位AES/ECB/PKCS7Padding加密/解密)
     * AES密匙：
     *     getMethod是get密匙： 整条 _url参数 request.getQueryString 就是问号后面的值
     *     getMethod是post密匙： _json body字符串；（所以 POST请求必须是JSON格式，校验到非JSON格式的可以直接拒绝请求
	 * @param reqParam 请求参数 不管是get请求还是post请求，一律转为_utf-8字符串
	 * @param digitalSignatureEncodedString 数字签名
	 * @return JSON 格式的字符串
	 * @throws Exception
     */
	public static String DecodeSignature(String reqParam, String digitalSignatureEncodedString) throws Exception {
		// 解密用到的对称密匙
		String reqParamMD5= ReqParamToMD5(reqParam);
		String sKey = reqParamMD5.substring(0, 32); // 密钥key 需要为32位。
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		String ivParameter = reqParamMD5.substring(reqParamMD5.length() - 16); // 向量iv 后16位
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes("utf-8"));
		
		SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes("utf-8"), "AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		byte[] original = cipher.doFinal(Base64.getDecoder().decode(digitalSignatureEncodedString));
		
		return new String(original, "utf-8");
	}

}
