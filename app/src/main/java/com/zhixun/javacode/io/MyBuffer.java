package com.zhixun.javacode.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 介绍buffer类
 * https://blog.csdn.net/xialong_927/article/details/81044759
 * https://zhuanlan.zhihu.com/p/56876443
 * 缓冲区的好处-----------------------------------------------------
 * 1.减少物理访问次数
 * 2.缓冲区反复使用减少分配和回收内存的次数
 * buffer <------ByteBuffer \ CharBuffer IntBuffer  其中MappedByteBuffer extend ByteBuffer 专门用于内存映射
 * --注意-- StringBuffer与以上这些类没有关系
 * 缓冲区有4个属性---------------------------------------------------
 * Capacity	容量，即可以容纳的最大数据量；在缓冲区创建时被设定并且不能改变
 * Limit	表示缓冲区的当前终点，不能对缓冲区超过极限的位置进行读写操作。且极限是可以修改的。例如position则指向了数组的初始位置，
 * Position	位置，表示下一个可读取的数据的位置；这里当我们一个一个读取数据的时候，position就会依次往下切换，当与limit重合时，就表示当前ByteBuffer中已没有可读取的数据了
 * Mark	    标记，调用mark()来设置mark=position，再调用reset()可以让position恢复到标记的位置
 * TODO 怎么判断ByteBuffer处于读模式还是写模式？？？重复调用flip()会导致 position=0,limit=0 ，所以要在flip之前判断是否已经处于读模式，可是怎么判断呢？？？
 */
public class MyBuffer {
    public static void main(String[] arg) {
//        createBuffer();
        operateBuffer();
    }

    static void createBuffer() {
        System.out.println("before alocate:" + Runtime.getRuntime().freeMemory() + " B " + (Runtime.getRuntime().freeMemory() >> 20) + "MB");
        //ByteBuffer是抽象类，提供了四种方式创建ByteBuffer------------------------------------------------------------
        //1. 从堆空间中分配一个容量大小为capacity的byte数组作为缓冲区的byte数据存储器
        ByteBuffer buffer = ByteBuffer.allocate(102400 << 3);
        System.out.println("buffer = " + buffer);

        System.out.println("after alocate:" + Runtime.getRuntime().freeMemory());

        // 是不使用JVM堆栈而是通过操作系统来创建内存块用作缓冲区，它与当前操作系统能够更好的耦合，
        // 因此能进一步提高I/O操作速度。但是分配直接缓冲区的系统开销很大，因此只有在缓冲区较大并长期存在，
        // 或者需要经常重用时，才使用这种缓冲区
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(102400 << 3);
        System.out.println("directBuffer = " + directBuffer);
//        System.out.println("after  alocate:" + Runtime.getRuntime().freeMemory());
        byte[] bytes = new byte[32];
        //这个缓冲区的数据会存放在byte数组中，bytes数组或buff缓冲区任何一方中数据的改动都会影响另一方。
        //其实ByteBuffer底层本来就有一个bytes数组负责来保存buffer缓冲区中的数据，通过allocate方法系统会帮你构造一个byte数组
        buffer = ByteBuffer.wrap(bytes);
        System.out.println(buffer);
        //在上一个方法的基础上可以指定偏移量和长度，这个offset也就是包装后byteBuffer的position，
        // 而length呢就是limit-position的大小，从而我们可以得到limit的位置为length+position(offset)
        buffer = ByteBuffer.wrap(bytes, 10, 10);
        System.out.println(buffer);
        buffer.mark();
    }

    static void operateBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(7);
        System.out.println("写入三个字节");
        // 往buffer中写入3个字节的数据
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        System.out.println("buffer-----" + buffer);
        System.out.println("remaining()-----" + buffer.remaining());//写模式查看还有多少可用的空间，读模式查看ByteBuffer的大小,这时候和limit效果一致
        //读取limit
        System.out.println("limit()-----" + buffer.limit());
        //设置limit
        System.out.println("limit(num)-----" + buffer.limit(buffer.capacity() - 1));
        //reset():写入字节后标记,接着执行reset就会恢复到mark标记的位置，相比rewind，reset如果有mark就恢复到mark，没有等同于rewind
        buffer.put((byte) 4);
        System.out.println("mark()-----" + buffer.mark());
        System.out.println("reset()-----" + buffer.reset());
        //rewind()方法，使缓冲区 position = 0，mark重置为-1,相比reset
        System.out.println("rewind()-----" + buffer.rewind());//写之前 执行这个方法，buffer.clear()//读之前执行这个方法
        System.out.println("array-----" + buffer.array()[1]);//返回实现此缓冲区的 byte 数组,调用此方法之前要调用 hasArray 方法，以确保此缓冲区具有可访问的底层实现数组。

        ByteBuffer buffer1 = ByteBuffer.allocate(10);
        for (int i = 0; i < 10; i++) {
            buffer1.put((byte) i);
        }
        //切换为读取模式，清除mark标记。使limit = position上次写入的位置；position=0;
        System.out.println("flip()-----" + buffer1.flip());
        //compat() 其主要作用在于在读取模式下进行数据压缩，并且方便下一步继续写入数据。例如：10个字节，先读取3个字节 此时position =3；
        //这时候写入会从3开始，覆盖掉未读取的字节，这时候执行compat，缓冲区数组整体向左前移3个字节，position = 7；
        buffer1.get(new byte[3]);//读取3个字节position=3
        System.out.println("compact()-----" + buffer1.compact() + " hashcode = " + buffer1.hashCode());//[compact详解](https://vimsky.com/examples/usage/bytebuffer-compact-method-in-java-with-examples.html)
        //clear() position = 0;limit = capacity;mark = -1;  有点初始化的味道，但是并不影响底层byte数组的内容, 注意看hashCode，buffer每执行一次实例都不相同
        System.out.println("clear()-----" + buffer1.clear() + " buffer1.get() = " + buffer1.get(1) + " hashcode = " + buffer1.hashCode());
        //get()读取一个字节，position位置+1，get(int);读取指定位置值，position不变 这样的函数还有 getInt()读取四个字节 position+4， getInt(int)读取指定位置四个字节，position不变......等等
        System.out.println("get()-----" + buffer1.get() + " get(0) = " + buffer1.get(0) + " hashcode = " + buffer1.hashCode());//获取
        System.out.println("position(int)-----" + buffer1.position(0));//设置position的位置
        System.out.println("buffer.put(new byte[0])-----" + buffer1.put(new byte[0]) + " 添加 0字节数组position不偏移");//添加 new byte[0] 效果
    }

    /**
     *
     */
    static void memoryMap() {
        Path path = Paths.get("121759.mp4");
        try (     //从文件中获取一个通道
                  FileChannel fileChannel = FileChannel.open(path);) {

            //获取一个byteBuffer缓冲区，fileChannel.map
            ByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());//获取
            //顺序读缓冲区
            while (byteBuffer.hasRemaining()) {
                byteBuffer.get();
            }
            //随机读缓冲区
            for (int i = 0; i < byteBuffer.limit(); i++) {
                byteBuffer.get(i);
            }
            //java 默认字节高位在前，如果要处理低位在前的二进制也可以
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

            //查看缓冲区当前的字节序列,true 高位再前
            byteBuffer.order();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
