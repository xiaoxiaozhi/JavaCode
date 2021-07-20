package com.zhixun.javacode.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test2 {

	public static void main(String[] args) {
		HashMap<String, String> map = new HashMap<>();
		map.put("1", "23");
		map.put("2", "34");
		map.remove("1");
		map.get("1");
		map.get("2");
		System.out.print(map.get("1"));
		System.out.println(map.get("2"));
		System.out.println(55 % 60);
		set();
		System.out.println("判断第一个字符是否汉字" + judgeFirstChar1("苏苏su"));
	}

	// java 求一个list集合中出现次数最多的一项，和此项的出现次数
	// 效率很高的统计方法。利用正则表达式统计
	private static void set() {
		String regex;
		Pattern p;
		Matcher m;
		List<String> list = new ArrayList();
		// list.add("苏E05EV8");
		list.add("苏E05EV8");
		list.add("黑V8");
		list.add("苏EO5EV8");
		list.add("苏EO5EV8");
		list.add("赣");
		list.add("苏E05EV8");
		list.add("EO5EV8");
		list.add("苏EO5EU8");
		list.add("苏3O5EV8");
		String tmp = "";
		String tot_str = list.toString();
		// System.out.println(tot_str); //[aa, aa, aa, aa, bb, bb, cc, cc, dd, ed]
		int max_cnt = 0;
		String max_str = "";
		for (String str : list) {
			if (tmp.equals(str))
				continue;
			tmp = str;
			regex = str;
			p = Pattern.compile(regex);
			m = p.matcher(tot_str);
			int cnt = 0;
			while (m.find()) {
				cnt++;
			}
			// System.out.println(str + ":" + cnt);
			if (cnt > max_cnt) {
				max_cnt = cnt;
				max_str = str;
			}
		}
		System.out.println("字符串 " + max_str + " 出现的最大次数：" + max_cnt); // 字符串 aa 出现的最大次数：4
	}

	public static String judgeFirstChar(String str) {
		String regex = "[0-9]";
		char c = str.charAt(0);
		// if (c >= '0' && c <= '9') {
		if (Pattern.matches(regex, c + "")) {
			return "intChar"; // 数字
		}

		regex = "[a-zA-Z]";
		// if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')){
		if (Pattern.matches(regex, c + "")) {
			return "charChar"; // 字符
		}

		regex = "([\u4E00-\u9FA5]{1,})";
		if (Pattern.matches(regex, c + "")) {
			return "chineseChar"; // 汉字
		}

		return null; // 其他
	}

	public static boolean judgeFirstChar1(String str) {
		char c = str.charAt(0);
		String regex = "([\u4E00-\u9FA5]{1,})";
		return Pattern.matches(regex, c + "");
	}

}
