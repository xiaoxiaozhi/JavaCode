package com.zhixun.javacode.collection;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Optionnall {
	public static void main(String... args) {
		// 创建一个Optional类型,传入null算无值状态
		Optional<String> optional;
		optional = Optional.ofNullable(null);// 不报错.传入参数为null情况下返回Optional.empty();不为null情况下返回Optional.of()
		// optional = Optional.of(null);//报错
		if (optional.isPresent()) {
			// 有值情况下的方法
			System.out.println("有值");
			// optional.ifPresent((s)->result.add(s));//例如如果该值存在加入列表，处理完不会获得任何返回值。
			System.out.println(optional.map((s) -> s + "1"));// 如果想要获取值，用map方法
		} else {
			// 无值情况下的三个方法
			System.out.println("无值");
			System.out.println(optional.orElse("orElse无值的情况下返回一个值"));
			System.out.println(optional.orElseGet(() -> {
				return "orElseGet无值的情况下返回一个值";
			}));// 有值返回，无值用函数式接口返回一个值
			optional.orElseThrow(IllegalStateException::new);// 没有值的时候抛出一个异常 。
			System.out.println(optional.get());// 如果optional传进去的是null就报错
		}
//		optional.fl
//		Optional.empty();
	}
}