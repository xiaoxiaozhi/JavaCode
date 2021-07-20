package com.zhixun.javacode.class_;

import java.util.Random;

/**
 * <h1>理解Class类型</h1>
 * <p>Class类的对象作用是运行时提供或获得某个对象的类型信息</p>
 * <p>Java中每个类都有一个Class对象，每当我们编写并且编译一个新创建的类就会产生一个对应Class对象
 * 并且这个Class对象会被保存在同名.class文件里</p>
 * <p>基本类型 (boolean, byte, char, short, int, long, float, and double)有Class对象，数组有Class对象，就连关键字void也有Class对象（void.class）。
 * Class对象对应着java.lang.Class类，如果说类是对象抽象和集合的话，那么Class类就是对类的抽象和集合</p>
 * <ol>
 * <li><b>Class加载流程</b></li>
 * <p><b>加载</b>，这是由类加载器（ClassLoader）执行的。
 * 通过一个类的全限定名来获取其定义的二进制字节流（Class字节码），
 * 将这个字节流所代表的静态存储结构转化为方法去的运行时数据接口，
 * 根据字节码在java堆中生成一个代表这个类的java.lang.Class对象。</p>
 * <p><b>链接。</b>在链接阶段将验证Class文件中的字节流包含的信息是否符合当前虚拟机的要求，
 * 为静态域分配存储空间并设置类变量的初始值（默认的零值），并且如果必需的话，
 * 将常量池中的符号引用转化为直接引用。</p>
 * <p><b>初始化。</b>到了此阶段，才真正开始执行类中定义的java程序代码。
 * 用于执行该类的静态初始器和静态初始块，如果该类有父类的话，则优先对其父类进行初始化。</p>
 * <p><b>得到实例</b>以上3个阶段是Class对象的加载过程。再通过new关键字和 class.newInstance()获取实例 </p>
 * <li><b>newInstance()方法和new关键字区别</b></li>
 * <p>newInstance()实际上是把new这个方式分解为两步，即首先调用Class加载方法加载某个类生成Class对象，然后实例化</p>
 * <p>newInstance: 弱类型。低效率。只能调用无参构造。 </p>
 * <p>new: 强类型。相对高效。能调用任何public构造。</p>
 * <li><b>Class的加载方式</b></li>
 * <p>Class.class JVM将使用类装载器, 将类装入内存,不做类的初始化工作.返回Class的对象</p>
 * <p>Class.forName() 装入类,并做类的静态初始化，返回Class的对象</p>
 * </ol>
 */
public class ClassTest {
	public static void main(String... args) {
		Class t = T1.class;// JVM将使用类装载器, 将类装入内存,不做类的初始化工作.返回Class的对象,对应Class加载流程 处于加载阶段
		t.getClassLoader();
		System.out.println(T1.ii + "");// 打印值1没有触发初始化，这是因为ii属于编译期静态常量，
		// 在编译阶段通过常量传播优化的方式将T1类的常量ii存储到了一个称为NotInitialization类的常量池中，
		// 在以后对T1类常量ii的引用实际都转化为对NotInitialization
		// 类对自身常量池的引用，所以在编译期后，对编译期常量的引用都将在NotInitialization类的常量池获取，这也就是引用编译期静态常量不会触发Initable类初始化的重要原因
		System.out.println(T1.oo + "");// Class对象被初始化，因为oo在编译器并不能确定值，必须初始化Class才能获取到oo的真实值
		//
		try {
			/**<b>和new T2()的效果是一样的，这是因为java虚拟机(JVM)中的类加载器子系统会
			 // 将对应Class对象加载到JVM中，然后JVM再根据这个类型信息相关的Class对象
			 // 创建我们需要实例对象或者提供静态变量的引用值。  不管你创建多少个实例对象内存中只存在一个Class对象
			 这也解释了 static 关键字修饰的属性为什么能常驻内存。原因在下面有解释</b>
			 */
			T2 t2 = (T2) Class.forName("kotlin.com.myapplication.test.ClassTest$T2").newInstance();
			// 主要是第二个参数。true ，初始化Class对象 false 不初始化Class对象此时相当月T2.class
			Class.forName("kotlin.com.myapplication.test.ClassTest$T2", true, T2.class.getClassLoader());

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		new T3();
		new T3();
		try {
			// 根据打印结果可以看出，不管从Class.forname()还是new关键字创建实例对象 ，静态代码块只执行了一遍
			// 这是因为静态代码在对象实例化过程中只执行一遍并常驻与内存中。注意此时只是分配静态成员变量的存储空间，不包含实例成员变量
			// 再实例化多少次，Class对象也只有一个所以不会再执行静态代码
			Class.forName("kotlin.com.myapplication.test.ClassTest$T2");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Class c = T1.class;
		c = T2.class;
		System.out.println(c.getName());
		Class<?> clazz = Integer.class;// Class 泛型化
		clazz = Number.class;
		System.out.println(clazz.getName());
	}

	public static class T1 {
		public final static int ii = 1;
		static final int oo = new Random(47).nextInt(1000);

		static {
			System.out.println("创建T1");
		}
	}

	public static class T2 {
		static {
			System.out.println("创建T2");
		}
	}

	public static class T3 {
		static {
			System.out.println("创建T3");
		}
	}
}
