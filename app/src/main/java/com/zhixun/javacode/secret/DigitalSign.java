package com.zhixun.javacode.secret;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * 介绍数字签名 方法。
 * 消息摘要：数据的数字指纹，例如SHA1安全散列算法可将任何数据无论数据有多长都能压缩为160位20字节的序列。总共存在2^160个数字指纹，和MD5一样因为存在某种规律被废弃。后来的增强如SHA256 SHA384 SHA512
 *
 *
 */
public class DigitalSign {
	public static void main(String[] args) {
		try {
			MessageDigest alg = MessageDigest.getInstance("SHA-1");// 获取计算SHA1指纹的对象
			// IntStream.range(startInclusive, endExclusive)
			// Stream.of("测试".chars()).peek(}).forEach(b->{System.out.println("");});;
			Stream.of("测试".getBytes()).forEach(bs -> {
				alg.update(bs);//一次接一个数组 然后计算
				try {
					System.out.println(new String(alg.digest(),"utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				for (byte b : bs) {
					alg.update(b);//一次接一个数组成员，接完后计算
				}
				System.out.println(new String(alg.digest()));
			});
			alg.reset();//重置摘要
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
