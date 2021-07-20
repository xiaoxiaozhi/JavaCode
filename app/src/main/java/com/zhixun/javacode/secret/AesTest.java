package com.zhixun.javacode.secret;

import com.zhixun.javacode.util.EncryptUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class AesTest {
	static String iv = "0102030405060708";
	// 加解密 密钥
	static String keybytes = "1234567812345678";
	// 加解密算法/模式/填充方式
	final static String algorithmStr = "AES/CBC/PKCS5Padding";
	static byte[] encryptAES2Base64;
	static byte[] decryptBase64AES;
	static String content = "hahahhahhh";

	public static void main(String[] args) {

		encryptAES2Base64 = EncryptUtils.encryptAES2Base64(content.getBytes(), keybytes.getBytes(), algorithmStr,
				iv.getBytes());
		System.out.println("encryptAES2Base64--->" + new String(encryptAES2Base64));
		decryptBase64AES = EncryptUtils.decryptBase64AES(new String(encryptAES2Base64).getBytes(), keybytes.getBytes(), algorithmStr,
				iv.getBytes());
		System.out.println("decryptBase64AES--->" + new String(decryptBase64AES));
		CertificateFactory certificate_factory;
		try {
			//https://www.cnblogs.com/kyrios/p/tls-and-certificates.html 生成证书 到crt这里就可以了用openssl生成公钥不好使
			//获取公钥
			certificate_factory = CertificateFactory.getInstance("X.509");
			// CA_Name是证书的路径或File文件
			FileInputStream file_inputstream = new FileInputStream("D:\\ssl.crt");
			X509Certificate x509certificate = (X509Certificate) certificate_factory
					.generateCertificate(file_inputstream);
			PublicKey publicKey = x509certificate.getPublicKey();
			// newPublicKey就是证书的公钥
			String newPublicKey = new BigInteger(1, publicKey.getEncoded()).toString(16);
			System.out.println("公钥：" + newPublicKey);
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//浮点型--->byte[]---->文本               文本---->byte[]----> 浮点型
		double b=-9.6;
		System.out.println(new String(double2Bytes(b),0,double2Bytes(b).length));
		System.out.println(bytes2Double(double2Bytes(b)));
	};
	public static byte[] double2Bytes(double d) {
		long value = Double.doubleToRawLongBits(d);
		byte[] byteRet = new byte[8];
		for (int i = 0; i < 8; i++) {
			byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
		}
		return byteRet;
	}


	public static double bytes2Double(byte[] arr) {
		long value = 0;
		for (int i = 0; i < 8; i++) {
			value |= ((long) (arr[i] & 0xff)) << (8 * i);
		}
		return Double.longBitsToDouble(value);
	}
}
