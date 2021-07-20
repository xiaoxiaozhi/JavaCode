package com.zhixun.javacode.other;

import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.CRC32;

import javax.xml.transform.Templates;

public class Test {

	public static void main(String[] args) {
		int ori = 200;
		System.out.println("原始byte值：" + ori);
		Byte b = (byte) ori;
		System.out.println("java中byte值：" + b);
		Integer i = b.intValue();
		System.out.println("转换后的int值：" + i);
		System.out.println("存储的2进制值：" + Integer.toBinaryString(0x0001));
		Integer i_trans = i & 0xFF;
		System.out.println("&0xFF后的：" + i_trans);
		List<Byte> bs = new ArrayList<>();

		byte[] cmd = new byte[] { (byte) 0xa5, // 报头
				0x01, // 版本
				0, // 目的地址高位
				0x0001, // 目的地址低位
				0x0, // 源地址高位
				0x0000, // 源地址低位
				0x00, // FLAG
				0x0, // 请求id高位
				0x03, // 请求id低位
				0x11, // 类型状态 0x11锁控制
				0x0, // 数据长度高位
				0x3, // 数据长度低位
				0x1, 0x1, 0x1

		};
		// bs.addAll(Arrays.asList(cmd));
		CRC32 crc32 = new CRC32();
		crc32.update(cmd);
		System.out.println(crc32.getValue());
		System.out.println("crc32转成二进制字符串" + Long.toBinaryString(crc32.getValue()));
		System.out.println("crc32转成字符串位数" + Long.toBinaryString(crc32.getValue()).length());

		// byte[] crc_byte = stringSpilt1(Long.toBinaryString(crc32.getValue()), 8);
		byte[] crc_byte = get4byteCRC(cmd);
		System.out.println("crc32值-开始-------");
		for (int j = 0; j < crc_byte.length; j++) {
			System.out.println(Integer.toBinaryString(crc_byte[j]));
		}
		System.out.println("crc32值结束----");
		byte[] dd = new byte[cmd.length + crc_byte.length];
		System.arraycopy(cmd, 0, dd, 0, cmd.length);
		System.arraycopy(crc_byte, 0, dd, cmd.length, crc_byte.length);
		for (int j = 0; j < dd.length; j++) {
			System.out.println(Integer.toBinaryString(dd[j]));
		}

	}

	public static List<String> stringSpilt(String s, int spiltNum) {
		int startIndex = 0;
		int endIndex = spiltNum;
		int lenght = s.length();
		List<String> subs = new ArrayList<>();// 创建可分割数量的数组
		boolean isEnd = true;

		while (isEnd) {

			if (endIndex >= lenght) {
				endIndex = lenght;
				isEnd = false;
			}
			subs.add(s.substring(startIndex, endIndex));
			startIndex = endIndex;
			endIndex = endIndex + spiltNum;
		}
		return subs;
	}

	public static byte[] stringSpilt1(String s, int spiltNum) {
		int startIndex = 0;
		int endIndex = spiltNum;
		int lenght = s.length();
		List<String> subs = new ArrayList<>();// 创建可分割数量的数组
		boolean isEnd = true;

		while (isEnd) {
			if (endIndex >= lenght) {
				endIndex = lenght;
				isEnd = false;
			}
			subs.add(s.substring(startIndex, endIndex));
			startIndex = endIndex;
			endIndex = endIndex + spiltNum;
		}
		byte[] bytes = new byte[subs.size()];
		for (int i = 0; i < subs.size(); i++) {
			System.out.printf("二进制字符串%s", subs.get(i));
			BigInteger bi = new BigInteger(subs.get(i), 2);
			bytes[i] = bi.byteValue();
			System.out.println("转16进制整数" + bi.toString(16));
		}
		return bytes;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				hv = "0" + hv;
			}
			stringBuilder.append(" 0x" + hv);
		}
		return stringBuilder.toString();
	}

	public static byte[] get4byteCRC(byte[] buffer) {
		CRC32 crc32 = new CRC32();
		crc32.update(buffer);
		byte[] mCrc32Buf = new byte[4];
		mCrc32Buf[0] = (byte) ((crc32.getValue() & 0xFF000000) >> 24);
		
		System.out.println("转16进制整数" + Integer.toHexString(mCrc32Buf[0]));
		mCrc32Buf[1] = (byte) ((crc32.getValue() & 0x00FF0000) >> 16);
		System.out.println("转16进制整数" + Integer.toHexString(mCrc32Buf[1]));
		mCrc32Buf[2] = (byte) ((crc32.getValue() & 0x0000FF00) >> 8);
		System.out.println("转16进制整数" + Integer.toHexString(mCrc32Buf[2]));
		mCrc32Buf[3] = (byte) (crc32.getValue() & 0x000000FF);
		System.out.println("转16进制整数" + Integer.toHexString(mCrc32Buf[3]));
		// mCrc32Buf[0] = (byte)(crc32.getValue() & 0x000000FF);
		// mCrc32Buf[1] = (byte)((crc32.getValue() & 0x0000FF00) >> 8);
		// mCrc32Buf[2] = (byte)((crc32.getValue() & 0x00FF0000) >> 16);
		// mCrc32Buf[3] = (byte)((crc32.getValue() & 0xFF000000) >> 24);
		return mCrc32Buf;
	}

	public static void Testint2Bytes() {
		int i = 20180713;
		byte[] arr = new byte[4];
		arr[0] = (byte) (i >> 24); // 通过debug可以看到arr[0] = -23,也就是10010111
		arr[1] = (byte) (i >> 16); // 通过debug可以看到arr[1] = -18,也就是10010010 
		arr[2] = (byte) (i >> 8); // 通过debug可以看到arr[2] = 51, 也就是00110011
		arr[3] = (byte) i; // 通过debug可以看到arr[3] = 1, 也就是00000001
		for (int j = 0; j < arr.length; j++) {
			System.out.println(arr[j]);
		}
	}
}
