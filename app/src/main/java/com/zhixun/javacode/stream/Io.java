package com.zhixun.javacode.stream;

import android.util.Log;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        path();
        InputStream inputStream;
        System.out.println(System.getProperty("user.dir"));
    }

    private static void path() {
        //Path 是一个目录
        Path path = Paths.get("home", "456.data");
        System.out.println(path.toString());
    }

    private static void InputStreamTest() {
//        Reader
//        ZipInputStream GZIPInputStream//读取压缩文件

//        new InputStream()
//        DataInputStream dataInputStream = new DataInputStream("");
    }


}
