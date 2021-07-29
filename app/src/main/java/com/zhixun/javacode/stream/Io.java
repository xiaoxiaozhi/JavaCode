package com.zhixun.javacode.stream;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

/**
 * InputStream 有一个抽象方法 read() 遇到输入源结尾的时候返回-1;
 * OutPutStream 定义了一个抽象方法 write() 它可以向一个输出位置写入字节
 * 这两个方法在执行时都将阻塞，直至字节确实被写出或者读入,这样其他的线程就有机会执行有用的工作
 * int able = in.available() 能够检查当前可读入的字节数量 if(able>0) in.read();
 * 完成读写之后 调用in.close() 方法关闭流释放系统资源
 * InputStream、OutPutStream 和 Reader 、Writer
 * 1.前者可以以字节形式操作任意JAVA对象 后者 操作 Unicode 文本
 * 2.前者只继承了Closeable 接口后者 继承了 Closeable、Flushable、Readable、Appendable
 * 3.CharBuffer implement Readable,Appendable 拥有按顺序和随机读写访问的方法
 */
public class Io {

    private final static String TAG = "Io";

    public static void main(String... args) {
//        inout();
        operatePath();
    }

    /**
     * 输入与输出
     */
    private static void inout() {
        //in.nextLine();//从控制台输入一行，按enter结束
//        in.next();//从控制台输入字符，以空格作为分割，有几个空格就读入几次，按空格结束
//        in.nextInt();//从控制台输入一个整数,以空格作为分割,有几个空格就读入几次，当输入的字符不是空格报错，按空格结束
        System.out.println("请输入一行,输入 exit 结束程序");//打印 标准输出流 到控制台窗口
        Scanner in = new Scanner(System.in);//从控制台接收 标准输入流  需要和Scanner配合
        StringBuffer sb = new StringBuffer();
        String str;
        while (!(str = in.nextLine()).equals("exit")) {
            sb.append(str);
            System.out.println(sb.toString());
            sb.delete(0, sb.length());
            str = null;
        }
//        while (!(str = in.next()).equals("exit")) {
//            sb.append(str);
//            System.out.println(sb.toString());
//            sb.delete(0, sb.length());
//            str = null;
//        }
//        int next;
//        while ((next = in.nextInt()) != -1) {
//            System.out.println(next);
//        }
        in.close();
    }

    /**
     * 输入与输出流
     */
    private static void inputStreamTest() {
        //----------------------1.组合输入输出流---------------------------
//        ZipInputStream GZIPInputStream//从压缩文件 获取字节
//        new InputStream().read()//从 InputStream对象获取字节
//        new DataInputStream().readDouble()  //从DataInputStream对象获取数值
//        new FileInputStream("filePath") // 从文件获取字节
//        不同的类对应从不同的java类型读入字节流
        //---------------------2.通过组合获取不同的功能---------------------
        try {
            new ZipInputStream(new BufferedInputStream(new FileInputStream("path")));//读入一个压缩文件，带缓冲区，以ZipInputStream结尾解析压缩文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 文本输入与输出
     *
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    private static void WriterTest() throws FileNotFoundException, UnsupportedEncodingException {
        //1.字节输入流转字符输入流( 与Scanner相比 reader 能添加缓存机制，效率更高)
        Reader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);//带字符编码的字节输入流转换成 字符流读入
        new BufferedReader(reader);
        //2.字节输出流转文本输出流
        PrintWriter printWriter = new PrintWriter("123.txt", String.valueOf(StandardCharsets.UTF_8));
        //等同于
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream("123.txt"), StandardCharsets.UTF_8));
        printWriter.print("123");

        //3.从文件读入文本
        try {
            byte[] bytes = Files.readAllBytes(new File("").toPath());//把整个文件读入内存，如果是大文件会造成内存溢出
            List<String> lists = Files.readAllLines(new File("").toPath());//一行行读入，实际上每一行BufferReader.readLine()
            Stream<String> streams = Files.lines(new File("").toPath(), StandardCharsets.UTF_8);//如果每一行太大，那么将行转换成独行对象Stream<String>,返回的是java8的流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读写二进制流
     */
    private static void rwBinaryStream() {
// DataInput 和  DataOutput
//        DataOutput.writeInt() 总是将一个数字 按照4字节输出，高位补0
//        DataOutput.writeByte() 总是将一个数字 按照1字节输出,多余丢失

    }

    /**
     * 随即访问文件
     */
    private static void randomFile() {
//        RandomAccessFile //在文件的任何位置读取和写入文件
        try {
            RandomAccessFile random = new RandomAccessFile("", "r");//"rw"
            random.getFilePointer();//获取当前的文件位置
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 操作文件
     */
    private static void operatePath() {
        //Path 和 Files 是java7添加进来的
        //Path 是一个抽象路径，不必对着某个实际存在的文件
        System.getProperty("user.dir");//java 项目绝对路径
        Path path = Paths.get("home", "456.data");
//        Files.createFile(path);
        try (Scanner scanner = new Scanner(System.in); BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            String str;
            while ((str = scanner.nextLine()).equals("exit")) {
                bw.write(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(path.toAbsolutePath());
    }

}
