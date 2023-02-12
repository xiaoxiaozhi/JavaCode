package com.zhixun.javacode.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.zip.CRC32;
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
 * TODO show()展示了不同流的读取速度，写入速度还没有做对比， FileChannel有 append 续写模式吗？值得探究
 */
public class Io {

    private final static String TAG = "Io";

    public static void main(String... args) {
//        inout();
//        operatePath();// Paths Android8.0开始支持使用设备有限
//        operateFile();
        WriterTest();
//        show(Paths.get("121759.mp4"));
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
    private static void WriterTest() {
        //1.字节输入流转字符输入流( 与Scanner相比 reader 能添加缓存机制，效率更高)
        Reader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);//带字符编码的字节输入流转换成 字符流读入
        new BufferedReader(reader);
        try {
            //2.字节输出流转文本输出流
            PrintWriter printWriter = new PrintWriter("123.txt", String.valueOf(StandardCharsets.UTF_8));
            System.out.println("StandardCharsets.UTF_8 = " + String.valueOf(StandardCharsets.UTF_8));
            printWriter.print("123");
            //等同于
            new BufferedWriter(new OutputStreamWriter(new FileOutputStream("123.txt"), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
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
     * 操作路径
     */
    private static void operatePath() {
        //Path 和 Files 是java7添加进来的
        //Path 是一个抽象路径，不必对着某个实际存在的文件
        String absolutle = System.getProperty("user.dir");//java 项目绝对路径
        Path workPath = Paths.get(absolutle);
        System.out.println("生成一个路径 = " + workPath);
        System.out.println(workPath.resolve("any"));//如果传入的路径和workPath一样 则返回 workPath，否则在workPath后面跟着any
        System.out.println("产生兄弟路径resolveSibling = " + workPath.resolveSibling("any"));
        workPath.normalize();//移除冗余路径例如 /home/../at/.q  到 /home/at/q
        System.out.println("返回根路径getRoot = " + workPath.getRoot());

//        Path path = Paths.get("home", "456.data");
//        try (Scanner scanner = new Scanner(System.in); BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
//
//            String str;
//            while ((str = scanner.nextLine()).equals("exit")) {
//                bw.write(str);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(path.toAbsolutePath());
    }

    /**
     * Files 类操作文件
     */
    static void operateFile() {
        //3.从文件读入文本
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(System.getProperty("user.dir"), "build.gradle"));//把整个文件读入内存，如果是大文件会造成内存溢出
            List<String> lists = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "build.gradle"));//一行行读入，实际上每一行BufferReader.readLine()
            Stream<String> streams = Files.lines(Paths.get(System.getProperty("user.dir"), "build.gradle"), StandardCharsets.UTF_8);//如果每一行太大，那么将行转换成独行对象Stream<String>,返回的是java8的流
            Path outFile = Paths.get(System.getProperty("user.dir"), "dir1", "dir2", "introduce.txt");
            //对于还未创建的文件或文件夹Files.isRegularFile和isDirectory 区分不了，先创建文件的父目录，再创建文件
            if (Files.isRegularFile(outFile)) {
                System.out.println("是文件1");
            }
            if (!Files.exists(outFile.getParent())) {
                Files.createDirectories(outFile.getParent());
            }
            if (!Files.exists(outFile)) {
                Files.createFile(outFile);
            }
            if (Files.isRegularFile(outFile)) {
                System.out.println("是文件2");
            }
            Files.write(outFile, "8月3天气晴".getBytes());//由于下面打开 关闭输出流的操作,这里没写进去，原因有待探究
            Files.write(outFile, "8月4天气晴".getBytes(Charset.forName("UTF-8")), StandardOpenOption.APPEND);//续文件

            //以上方法适用于处理中小型文件，想要处理大文件或者二进制文件还是需要输入输出流,Files 创建输入输出流
            OutputStream outputStream = Files.newOutputStream(outFile);
            Files.newInputStream(outFile);
            Files.newBufferedReader(outFile);
            BufferedWriter bufferedWriter = Files.newBufferedWriter(outFile);
            if (Files.isDirectory(Paths.get(System.getProperty("user.dir"), "dir", "dirrr", "dirrrr"))) {
                System.out.println("是目录1");
            }
            Files.createDirectories(Paths.get(System.getProperty("user.dir"), "dir", "dirrr", "dirrrr"));//根据path创建一个目录
            if (Files.isDirectory(Paths.get(System.getProperty("user.dir"), "dir", "dirrr", "dirrrr"))) {
                System.out.println("是目录2");
            }
            outputStream.close();
            bufferedWriter.close();
            //Files.copy(srcPath,desPath);//如果目标路径存在，赋值或移动将会失败
//          Files.move(srcPath,desPath);
//            Files.copy(srcPath,desPath,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.COPY_ATTRIBUTES);//覆盖原有文件
//            Files.move(srcPath,desPath, StandardCopyOption.ATOMIC_MOVE);//移动覆盖原有文件
//            Files.copy(path,outPutStream);将path复制到输出流
//            Files.copy(inputStream,path)//将输入流复制到path
//            Files.delete(outFile);//删除文件，如果不存在会抛出异常，使用下面的删除方法;
//            Files.deleteIfExists(outFile);
//            Files.isSameFile("")//判断是否是同一个文件
            Files.size(outFile);//获取文件大小
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * crc校验文件,获取校验和 演示 内存映射操作文件速度
     */
    static long crcFile() {
        Path path = Paths.get("121759.mp4");
        CRC32 crc32 = new CRC32();
        //内部通过管道 读取文件
        try (InputStream fileInputStream = Files.newInputStream(path)) {
            int read;
            while ((read = fileInputStream.read()) > 0) {
                crc32.update(read);
            }
            return crc32.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 检测不同流读取速度
     *
     * @param filePath
     */
    static long checksumInputStream(Path filePath) {
        long current = System.currentTimeMillis();
        CRC32 crc = new CRC32();
        try (InputStream in = Files.newInputStream(filePath)) {
            byte[] b = new byte[1024];
            int num;
            while ((num = in.read(b)) != -1) {
                crc.update(b, 0, num);
            }
            System.out.println("普通流计算用时1 = " + (System.currentTimeMillis() - current));
            return crc.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 检测不同流读取速度
     *
     * @param filePath
     */
    static long checksumInputStream1(Path filePath) {
        long current = System.currentTimeMillis();
        CRC32 crc = new CRC32();
        try (InputStream in = Files.newInputStream(filePath)) {
            int num;
            while ((num = in.read()) != -1) {
                crc.update(num);
            }
            System.out.println("普通流计算用时2 = " + (System.currentTimeMillis() - current));
            return crc.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    static long checksumBufferInputStream(Path path) {
        long current = System.currentTimeMillis();
        CRC32 crc = new CRC32();
        try (InputStream in = new BufferedInputStream(Files.newInputStream(path))) {
            byte[] b = new byte[1024];
            int num;
            while ((num = in.read(b)) != -1) {
                crc.update(b, 0, num);
            }
            System.out.println("Buffer流计算用时 = " + (System.currentTimeMillis() - current));
            return crc.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param path
     * @return
     */
    static long checksumRandomAccessFile(Path path) {
        long current = System.currentTimeMillis();
        CRC32 crc = new CRC32();
        try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")) {
            byte[] b = new byte[1024];
            int num;
            while ((num = file.read(b)) != -1) {
                crc.update(b, 0, num);
            }
            System.out.println("随即访问文件计算用时 = " + (System.currentTimeMillis() - current));
            return crc.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    static long checksumFileChannel(Path path) {
        long current = System.currentTimeMillis();
        CRC32 crc = new CRC32();
        byte[] byteArray = new byte[1024];
        try (FileChannel fc = FileChannel.open(path)) {
            ByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            while (buffer.hasRemaining()) {
                ByteBuffer temp = buffer.get(byteArray);
                System.out.println(temp);
                crc.update(temp);
            }
            System.out.println("内存映射计算用时 = " + (System.currentTimeMillis() - current));
            return crc.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 1.每次读取1K字节比每次读取一个字节速度快上几百倍
     * 2.每次读1字节：内存映射最快、缓冲区输入流BufferedInputStream齐次，不过相差不大。不过内存映射适合大文件，日常使用推荐缓冲区流
     *
     * @param path
     */
    static void show(Path path) {
        checksumInputStream(path);
        checksumInputStream1(path);
        checksumBufferInputStream(path);
        checksumRandomAccessFile(path);
        checksumFileChannel(path);
    }

    /**
     * 创建多级目录下的文件，多级目录不一定存在
     *
     * @param filePath
     * @return
     */
    public Path createFile(Path filePath) {
        try {
            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filePath;
    }

}
