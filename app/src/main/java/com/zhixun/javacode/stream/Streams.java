package com.zhixun.javacode.stream;

import androidx.core.content.res.TypedArrayUtils;

import com.zhixun.javacode.module.Employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Stream:流并不存储数据，流的操作并不改变数据源。stream()串行流 parallelStream()并行流
 * 操作流程：（集合框架、数组）创建一个流------->中间操作新生成一个流------->终端操作
 * 流操作被分为中间操作和终端操作
 * 1.中间操作 filter, map, find, match, sorted
 * 2.终端操作 reduce foreach  sum collection  产生optional类型的有 max min findFirst findAny anyMatch allMatch noneMath
 * Stream（流）是一个来自数据源的元素队列并支持聚合操作
 * 3.基本类型流 IntStream、LongStream、DoubleStream 流库中有专门的类型用来存储基本类型流 。
 * short char byte boolean可以使用IntStream，而对于float使用DoubleStream
 */
public class Streams {
    public static void main(String[] arg) {
        System.out.println("-----创建流-----");
        generateStream();
        System.out.println("-----中间操作-----");
        middleOperation();
        System.out.println("-----终止操作-----");
        end();
    }

    //1. 创建流
    static void generateStream() {
        //1.1 集合获取流  stream()和parallelStream()创建并行流和串行流
        List<String> lists = new ArrayList<>();
        lists.stream();
        //1.2 数组获取流 Arrays.stream()
        Arrays.stream(new String[]{"", ""});
        //1.3 可变参数获取流 Stream.of()
        Stream.of("", "");
        //1.4 无限流
        Stream.iterate(0, n -> n + 2); //UnaryOperator 继承自Function接口
        //1.4.1 无限流
        Stream.generate(() -> Math.random());
    }

    //2. 中间操作
    static void middleOperation() {
        Stream<Employee> stream = Stream.of(new Employee("张三", 30, 18000),
                new Employee("李四", 28, 20000),
                new Employee("王五", 24, 25000),
                new Employee("王五", 24, 25000));
        //2.1 惰性处理
        Stream<Employee> employeeStream = stream.filter((e) -> {
            System.out.println("中间操作");
            return e.getAge() > 24;
        });//多个中间操作可以连接起来成为一个流水线，除非流水线上触发终止操作，否则中间操作不会执行任何处理，而在终止操作是一次性处理完毕，这被称为惰性求值
        employeeStream.forEach(System.out::println);//惰性求值
        //2.2 内部迭代
        Stream.generate(() -> Math.random()).filter(x -> x > 0.5).limit(10).forEach(System.out::println);//filter迭代过程由 Stream api完成，叫内部迭代
        //2.3 筛选与切片
        // filter     筛选
        // skip(n)    跳过前n个元素
        // limit(n)   限制
        // 2.3.1 distance() 去重 Employee 重写 hashCode()和equals()
        Stream<Employee> stream1 = Stream.of(new Employee("张三", 30, 18000),
                new Employee("李四", 28, 20000),
                new Employee("王五", 24, 25000),
                new Employee("王五", 24, 25000));
        stream1.distinct().forEach(System.out::println);
        //2.4 映射
        mapping();
        //2.5 排序
        sorted();
    }

    //2.4 映射
    static void mapping() {
        Stream<String> stringStream = Stream.of("aa", "bb", "cc", "dd");
        stringStream.map((x) -> x.toUpperCase()).forEach(System.out::print);//函数型接口，接收一个元素，返回新元素
        Stream<Employee> stream = Stream.of(new Employee("张三", 30, 18000),
                new Employee("李四", 28, 20000),
                new Employee("王五", 24, 25000),
                new Employee("王五", 24, 25000));
        stream.map(Employee::getName).forEach(System.out::print);//map映射用于提取员工名字
        //2.4.1 flatMap() 将流中流连接成一个流
        Stream<String> stream1 = Stream.of("aa", "bb", "cc", "dd");
        stream1.flatMap(Streams::getCharStream).forEach(System.out::println);//如果用map将得到Stream<Stream<Character>>,flatMap 得到Stream<Character>
    }


    //2.5 排序
    static void sorted() {
        //2.5.1 自然排序 ，已经继承了 Comparable
        Stream<String> strStream = Stream.of("bb", "vv", "ee", "aa");
        strStream.sorted().forEach(System.out::println);

        //2.5.2 定制排序，实现了 Comparator接口
        Stream<Employee> stream = Stream.of(new Employee("张三", 30, 18000),
                new Employee("李四", 28, 20000),
                new Employee("王五", 30, 25000));
        stream.sorted((e1, e2) -> {
            if (e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            } else {
                return e1.getAge() - e2.getAge();
            }
        }).forEach(System.out::println);

    }

    //3 终止操作
    static void end() {
        Stream<Employee> stream = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 28, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        //3.1 allMatch匹配所有元素
        System.out.println(stream.allMatch(x -> x.getStatues() == Employee.Statues.BUSY));//
        //3.2 anyMatch匹配任一
        Stream<Employee> stream1 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 28, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        System.out.println(stream1.anyMatch(x -> x.getStatues() == Employee.Statues.BUSY));//
        //3.3 noneMatch没有匹配任何元素 3.1的负面
        Stream<Employee> stream2 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 28, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        System.out.println(stream2.noneMatch(x -> x.getStatues() == Employee.Statues.FREE));//
        //3.4 返回流中第一个元素
        Stream<String> stream3 = Stream.of("aa", "bb", "cc", "dd", "ee", "ff");
        System.out.println(stream3.findFirst());//
        //3.5 返回并行流中任意元素
        Stream<String> stream4 = Stream.of("aa", "bb", "cc", "dd", "ee", "ff", "zz", "vv");
        System.out.println(stream4.parallel().findAny());//
        //3.6 返回流中元素数量
        System.out.println(Stream.of("aa", "bb", "cc", "dd", "ee", "ff", "zz", "vv").count());
        //3.7 返回流中最大值
        System.out.println(Stream.of("aa", "bb", "cc", "dd", "ee", "ff", "zz", "vv").max(String::compareTo));
        //3.8 返回流中最小值
        Stream<Employee> stream5 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 28, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        System.out.println(stream5.map(Employee::getSalary).min(Integer::compareTo));// 先提取工资，然后利用Integer的比较方法
        //3.9 规约
        statute();
        //3.10 收集
        collection();
    }

    //3.10 收集
    static void collection() {
        Stream<Employee> stream = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 28, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        System.out.println(stream.map(Employee::getName).collect(Collectors.toList()));//Collector不是一个函数式接口，所以Collectors工具类提供各种Collector
        //3.10.1 平均值
        Stream<Employee> stream1 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 28, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        System.out.println(stream1.map(Employee::getSalary).collect(Collectors.averagingInt(Integer::intValue)));
        //3.10.2 总和
        Stream<Employee> stream2 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 28, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        System.out.println(stream2.map(Employee::getSalary).collect(Collectors.summingInt(Integer::intValue)));
        //3.10.3 最大值
        Stream<Employee> stream3 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 28, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        System.out.println(stream3.map(Employee::getSalary).collect(Collectors.maxBy(Integer::compareTo)));
        //3.10.3 最小值
        Stream<Employee> stream4 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 28, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        System.out.println("收集---最小值 = " + stream4.map(Employee::getSalary).collect(Collectors.minBy(Integer::compareTo)));
        //3.10.4 分组
        Stream<Employee> stream5 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 28, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        Map<Employee.Statues, List<Employee>> map = stream5.collect(Collectors.groupingBy(Employee::getStatues));//分组标志
        System.out.println("收集---分组 = " + map);
        //3.10.5 分组 多级分组
        Stream<Employee> stream6 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 24, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        Map<Employee.Statues, Map<String, List<Employee>>> maps = stream6.collect(Collectors.groupingBy(Employee::getStatues, Collectors.groupingBy(e -> {
            if (e.getAge() < 28) {
                return "青年";
            } else {
                return "中年";
            }
        })));//分组标志
        System.out.println("收集---分组---多级分组 = " + maps);
        //3.10.6 分区
        Stream<Employee> stream7 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 24, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        Map<Boolean, List<Employee>> collect = stream7.collect(Collectors.partitioningBy(e -> e.getSalary() > 20000));
        System.out.println("收集---分区 = " + collect);
        //3.10.6 统计
        Stream<Employee> stream8 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 24, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        IntSummaryStatistics statistics = stream8.collect(Collectors.summarizingInt(Employee::getSalary));
        System.out.println("收集---统计 ∑ = " + statistics.getSum() + " 平均值 = " + statistics.getAverage() + " 最大值 = " + statistics.getMax() + " 最小值 = " + statistics.getMin() + " 数量 = " + statistics.getCount());
        //3.10.7 连接字符串
        Stream<Employee> stream9 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 24, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        String str = stream9.map(Employee::getName).collect(Collectors.joining(",","<<",">>"));//中间、开始、末尾
        System.out.println("收集---连接 = " + str);
    }

    //3.9 规约
    static void statute() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println(stream.reduce((x, y) -> x + y));//将流中元素反复结合起来，得到一个值
        Stream<Employee> stream1 = Stream.of(new Employee("张三", 30, 18000, Employee.Statues.BUSY),
                new Employee("李四", 28, 20000, Employee.Statues.BUSY),
                new Employee("王五", 30, 25000, Employee.Statues.VOCATION));
        //3.9.1 总和
        System.out.println(stream1.map(Employee::getSalary).reduce(Integer::sum));//计算工资,总和
        //3.9.2 最大值
//        System.out.println(stream1.map(Employee::getSalary).reduce(Integer::max));//计算工资,最大值
        //3.9.3 最小值
//        System.out.println(stream1.map(Employee::getSalary).reduce(Integer::min));//计算工资,最小值
    }

    static Stream<Character> getCharStream(String str) {
        return str.chars().mapToObj((i) -> Character.valueOf((char) i));
//        List<Character> lists = new ArrayList<>();
//        or (char c : str.toCharArray()) {
//            lists.add(c);
//        }
//        return lists.stream();
    }
}
