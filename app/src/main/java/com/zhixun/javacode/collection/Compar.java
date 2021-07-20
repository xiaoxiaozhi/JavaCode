package com.zhixun.javacode.collection;

import java.util.Comparator;
import java.util.stream.Stream;
/**
 * 只要实现Comparable 接口的对象直接就成为一个可以比较的对象，但是需要修改源代码. 流
 * Comparable相当于“内部比较器”，而Comparator相当于“外部比较器”。从下面例子Stream.sort()和Stream.sort(Comparator)可以看出。实现Comparable接口的Person
 * 是一个可以比较的对象。如果没实现此接口,sort(Comparator) 传进去比较器就能实现排序，
 * Comparable是排序接口 ，通过Collections.sort或Arrays.sort进行自动排序。
 */
public class Compar {

	public static void main(String[] args) {
		Comparable<String> comparable = str -> 1;
		// 排序默认是升序
		// Stream.of(new Person("小明", 18), new Person("小红", 20), new Person("小明",
		// 18)).distinct()
		// .sorted((Person o1, Person o2) -> o1.age -
		// o2.age).forEach(System.out::println);
		Stream.of(new Person("小明", 18), new Person("小红", 20), new Person("小明", 18)).distinct()
				.sorted((Person o1, Person o2) -> o1.age - o2.age).forEach(System.out::println);
		System.out.println(Stream.of("C", "b", "d", "1", "开始").min(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);//默认是升序。max取排序好的最后一个。min取第一个。
			}
		}).get());
	}

	static class Person implements Comparable<Person> {
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
			return this.name + " " + this.age;
		}

		@Override
		public int hashCode() {
			return this.name.hashCode() * age;
		}

		@Override
		public int compareTo(Person o) {
			return this.age - o.age;
		}

	}

}
