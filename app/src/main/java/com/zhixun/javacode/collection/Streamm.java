package com.zhixun.javacode.collection;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * 函数式接口：
 * 函数式接口(Functional Interface)有且仅有一个抽象方法
 * 1.Java 8为函数式接口引入了一个新注解@FunctionalInterface，主要用于编译级错误检查，加上该注解，当你写的接口不符合函数式接口定义的时候，编译器会报错。
 * 2.函数式接口里允许定义静态方法。因为静态方法不能是抽象方法，是一个已经实现了的方法，所以是符合函数式接口的定义的。
 * 3.函数式接口里允许定义 java.lang.Object 里的 public 方法,因为任何一个函数式接口的实现，默认都继承了 Object 类,，这些方法对于函数式接口来说，不被当成是抽象方法
 * 默认函数：
 * 默认函数就是接口可以有实现方法，而且不需要实现类去实现其方法，方法明前加default关键字
 * 为什么要有这个特性？
 * 首先，之前的接口是个双刃剑，好处是面向抽象而不是面向具体编程，缺陷是，当需要修改接口时候，
 * 需要修改全部实现该接口的类，目前的 java 8 之前的集合框架没有 foreach 方法，通常能想到的解决办法是在JDK里给相关的接口添加新的方法及实现。
 * 然而，对于已经发布的版本，是没法在给接口添加新方法的同时不影响已有的实现。所以引进的默认方法。他们的目的是为了解决接口的修改与现有的实现不兼容的问题
 *
 * 方法引用
 * 抽象类：
 * 1.包含一个抽象方法的类必须是抽象类
 * 2.抽象类和抽象方法必须使用abstract关键字
 * 3.抽象方法只需要声明而不需要实现
 * 4.抽象类不能实例化，必须被子类继承，子类必须实现抽象方法
 * 5.由4可知抽象类不能使用final关键字修饰，因为final关键字修饰的类不能被继承
 *
 * Stream:流并不存储数据，流的操作并不改变数据源。stream()串行流 parallelStream()并行流
 * 操作流程：（集合框架、数组）创建一个刘------->中间操作新生成一个流------->终端操作
 *  流操作被分为中间操作和终端操作
 * 1.中间操作 filter, map, find, match, sorted
 * 2.终端操作 reduce foreach  sum collection  产生optional类型的有 max min findFirst findAny anyMatch allMatch noneMath
 * Stream（流）是一个来自数据源的元素队列并支持聚合操作
 * 3.基本类型流 IntStream、LongStream、DoubleStream 流库中有专门的类型用来存储基本类型流 。
 *  short char byte boolean可以使用IntStream，而对于float使用DoubleStream
 *
 */
public class Streamm {

	public static void main(String... args) {
		// 函数式接口 lambda表达式可以隐式的实例一个函数式接口。
		GreetingService gs = msg -> System.out.println(msg);
		gs.sayMessage("");
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
		System.out.println("打印所有数字");
		eval(list, n -> true);// 打印所有 数字
		System.out.println("打印所有偶数");
		eval(list, n -> n % 2 == 0);// 打印所有偶数
		System.out.println("打印所有奇数");
		eval(list, n -> n % 2 != 0);
		// 默认函数
		Child child = new Child();
		child.print();// 覆盖父接口的默认方法。可以选择覆盖哪个父类，也可以都选。也可以不选.调用形式 父类.super.方法();

		// 方法引用
		// 方法可以引用final的方法变量和非final的方法变量。但是
		// 应用之后不能做修改。否则会报错。例如下面num=5.会导致上面报错。具体原因不明了以后再看。 全局变量没关系
		// lambda表达式匿名实现了函数式接口，有参数有方法体，而方法引用更直白，
		// 直接引用方法作为方法体，连参数都不再显示。所以方法引用是一种更简洁易懂的Lambda表达式
		GreetingService gService = System.out::println;// 直接实例了函数式接口

		referenceMethod(System.out::printf);// 方法引用的方法，参数必须和该函数式接口的抽象方法参数一致
		// MathOperation mOperation
		// =System.out::println;//例如该方法的函数式接口有两个参数，而println只有一个参数，结果报错
		// 四种方法引用类型 静态方法Class::method 实例方法entry::method this::method 构造方法引用 Class::new
		// 静态方法引用
		String[] stringArray = { "Barbara", "James", "Mary", "John", "Patricia", "Robert", "Michael", "Linda" };
		Arrays.sort(stringArray, String::compareToIgnoreCase);
		// 构造方法引用
		MathOperation mo = String::new;// 构造函数的参数和函数接口的参数一致，函数接口返回是构造方法的实例。也就是说函数接口的参数和返回和构造方法的参数和返回一致。构造方法返回该类的实例
		// Stream 。 返回一个数据流后面通过collection方法形成一个列表。
		List<String> strings = Arrays.asList("始bc", "bc", "bc", "efg", "", "1", "jkl完");
		strings.stream();// 从集合框架产生流
		strings.parallelStream(); // 从集合框架产生流
		Stream.of("不", "定", "长", "参", "数", "变", "成", "流");// 从不定长参数产生流实际调用的是这个Arrays.stream
		int from = 0, to = 5;
		Arrays.stream(strings.toArray(), from, to); // 截取数组 从from(包括) to(不包括)部分产生一个流
		System.out.println("-----无限流--------");
		// 无限流 Stream.iterate和Stream.generate
		Stream.generate(Math::random).limit(3).forEach(System.out::println);// 产生一个值的流Supplier 函数式接口是T get(). 返回一个参数
		Stream.iterate("words", str -> {
			return str + "1";
		}).limit(3).forEach(System.out::println);// 这是一个无限流 对一个值反复循环计算。例如
		// 初始值0，进入n->n+1 计算
		// ，返回的值在第二轮继续进入n->n+1
		Stream.iterate(0, n -> n + 1).limit(10);// limit(10)截取10个元素产生一个新流，经常和无限流配合。
		Stream<Double> dStream = Stream.generate(() -> {
			System.out.println("开始生成无限流_" + System.currentTimeMillis());

			return Math.random();
		});// 和limit相反舍弃前5个元素
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("获取无限流结果_" + System.currentTimeMillis());
		dStream.limit(10).skip(5).forEach(System.out::println);// skip和limit相反舍弃前5个元素.从打印结果看。实际上只有执行了终端操作（约简）中间操作财被执行。
		Stream.empty();// 创建一个没有元素的流

		// Predicate 动作函数式接口 boolean test(T t) 输入的参数匹配到动作返回true，否则false。也就是说通过匹配条件返回true
		strings.stream().filter(string -> !string.isEmpty());
		strings.stream().map(s -> s + "");// Function 函数式接口是R apply(T t)。对每一个参数应用此函数返回
		// flatMap的作用是接受一个 Stream<Stream<R>> 转换为Stream<T> 也就是把流的流展开。例如下面的例子 把每一个单词拆成
		// 字符流。
		// 从map(words ->
		// words.chars())得到Stream<IntStream>。然后通过flatMap方法，对每一个IntStream转换成
		// String类型。最后得到一个Stream<String>
		strings.stream().map(words -> words.chars()).flatMap(Streamm::letters).forEach(System.out::println);
		"开始words".chars().forEach(c -> System.out.println((char) c));// chars()把每一个字符 转换为10进制Unicode，然后输出int流。
		Stream.concat(Stream.of("12345"), Stream.of("上山打老虎")).forEach(System.out::println);// 连接流
		strings.stream().distinct().forEach(System.out::println);// distinct 剔除重复元素，获得剩余的部分 原理object.equals()
		Arrays.asList(new Person("小明", 18), new Person("小红", 18), new Person("小明", 18)).stream().distinct()
				.forEach(System.out::println);// distinct 去重原理和集合框架去重一样。除了要复写equals还要复写hashCode。
		strings.stream().sorted().forEach(System.out::println);// 对基本类型排序 从小到大
		System.out.println("-------peek------");// 跟map类似但它对流中的每个元素操作后不返回，所以peek方法产生的流跟原来的流一样（java核心技术说产生了另一个流）。peek翻译过来有偷窥的意思
		strings.stream().distinct().peek(s -> {
			System.out.println("操作了peek " + s);
		}).map(s -> {
			System.out.println("执行了map " + s);
			return s + "1";
		}).count();// 从打印结果看
		System.out.println("-------简单约简------");
		Optional<String> optional = null;
		strings.stream().max(String::compareToIgnoreCase).ifPresent((s) -> {
			System.out.println("最大" + s);
		});// 获取最大的那个 String，max的comparator
		// 的函数是借口compar有两个参数。compareToIgonreCase只有一个参数也不知到怎么方法引用成功。（compareToIgnoreCase不是静态方法，String::compareToIgnoreCase是第一个参数调用此方法和第二个参数比）
		strings.stream().min(String::compareToIgnoreCase).ifPresent((s) -> {
			System.out.println("最小" + s);
		});// 最小值没有取值失败，“” 是最小的字符串
		// optional.flatMap 还没测试
		strings.stream().findAny().ifPresent(System.out::println);
		// 收集结果
		System.out.println("----收集结果----");// 对这些没做深入了解只照抄了方法
		// strings.stream().forEach(action);//无序
		// strings.stream().forEachOrdered(action);//有序
		strings.stream().toArray();
		String[] sa = strings.stream().toArray(String[]::new);// 转化成数组 IntFunction <T[]>apply(int value) value
		strings.stream().toArray(value -> new String[value]);// 是流中元素个数。例如 new String[value]创建value大小的数组
		// String[] sa = strings.stream().toArray();//不传的情况下，返回Object[]数组
		List<String> list2 = strings.stream().collect(Collectors.toList());// 转化为列表
		Set<String> list3 = strings.stream().collect(Collectors.toSet());// 还能toMap();
		TreeSet<String> list4 = strings.stream().collect(Collectors.toCollection(TreeSet::new));// 还可以放在其他列表类里面
		String s1 = strings.stream().collect(Collectors.joining());// 流中的字符串合并成一个字符串
		String s2 = strings.stream().collect(Collectors.joining(","));// 间隔加逗号
		IntSummaryStatistics iStatistics = strings.stream().collect(Collectors.summarizingInt(String::length));
		iStatistics.getAverage();
		iStatistics.getCount();
		iStatistics.getMax();
		iStatistics.getMin();
		iStatistics.getSum();
		// 收集到map中
		System.out.println("----收集到map中----");
		// 约简操作
		System.out.println("----约简操作----");
		Stream<Integer> iStream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
		// System.out.println("约简操作加" + intStream.reduce((x, y) -> {
		// System.out.println("x="+x+" y="+y);return x + y;}).get());//从打印结果可以看到，(x op
		// y)op z
		iStream.reduce(Integer::sum);// 用这行代码代替上面的
//		iStream.reduce(0, Integer::sum);// 用这行代码代替上面的 如果 流为空就返回0 注意：一个流的reduce方法只能用一次，再用会报错
		// 基本流类型 ，将Integer类型包装在Stream<Integer>中的效率很低，对其他基本类型也一样 double float short long char byte boolean  流中有专门的类型用来存储基本类型。
		//short chart byte boolean 使用IntStream ；float使用DoubleStream
		System.out.println("----基本流类型----");
		IntStream intStream = IntStream.of(1,2,3,4);
		Arrays.stream(new int[]{1,2,3,4});//获取 intStream的两种方式。
		IntStream.range(0, 10).forEach(x->System.out.print(x));//0-》9
		System.out.println("");
		IntStream.rangeClosed(0, 10).forEach(x->System.out.print(x));//0-》10
		//对象流和基本类型流之间互相转换
		strings.stream().mapToInt(String::length);//对象流转为 基本类型流
		Stream<Integer> streamm = IntStream.range(0, 10).boxed();//基本类型转为对象流
	}

	static int sd(String s1, String s2) {
		if (s1.charAt(0) < s2.charAt(0)) {
			return -1;
		} else if (s1.charAt(0) == s2.charAt(0)) {
			return 0;
		} else {
			return 1;
		}
	}

	static Stream<String> letters(IntStream is) {
		return is.mapToObj(u -> String.valueOf((char) u));
	}

	interface MathOperation<T, R> {
		String operation();

		static void printHello() {
			System.out.println("Hello");
		}
	}

	public interface Converter<T1, T2> {
		String convert(int i);

		// 普通接口也可以有静态方法
		static void s() {

		}
	}

	@FunctionalInterface
	interface GreetingService {
		void sayMessage(String message);

		// 允许有静态方法
		static void printHello() {
			System.out.println("Hello");
		}

		@Override
		boolean equals(Object obj);

		// 方法名前加default关键字。如果多个接口的默认方法同名，则子类可以重写默认方法或者指定实现哪个接口的默认方法例如Class.super.print();
		default void def() {
			System.out.println("这是一个默认函数");
		}

		default void print() {
			System.out.println("我是一辆三轮车!");
		}
	}

	interface Vehicle {
		default void print() {
			System.out.println("我是一辆车!");
		}

		static void blowHorn() {
			System.out.println("按喇叭!!!");
		}
	}

	static class Child implements GreetingService, Vehicle {

		@Override
		public void sayMessage(String message) {
		}

		@Override
		public void print() {
			Vehicle.super.print();
			GreetingService.super.print();
			System.out.println("我是一辆四轮车!");
			Vehicle.blowHorn();
			// 如果执行这个方法， 打印结果为 我是一辆车，我是一辆三轮车，我是一辆四轮车。从上到下每一句都执行
		}

	}

	private abstract class T {
		abstract void t();
	}

	private class T222 extends T {

		@Override
		void t() {
		}

	}

	public static void eval(List<Integer> list, Predicate<Integer> predicate) {
		for (Integer integer : list) {
			if (predicate.test(integer)) {
				System.out.println(integer);
			}
		}
	}

	public static void referenceMethod(GreetingService gs) {
		gs.sayMessage("123456789");
	}

	static class Person {
		private String name;
		private int age;

		public Person(String name, int age) {
			this.name = name;
			this.age = age;
		}
		/**
		 * 对象去重，不仅要复写equals还要复写hashCode
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Person)) {
				return false;
			}
			Person person = (Person) obj;
			return this.name.equals(((Person) obj).name) && this.age == person.age;
		}

		@Override
		public String toString() {
			return this.name;
		}

		@Override
		public int hashCode() {
			return this.name.hashCode() * age;
		}

	}
}
