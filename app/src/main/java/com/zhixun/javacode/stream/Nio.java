package com.zhixun.javacode.stream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.zip.CRC32;

public class Nio {
    public static void main(String[] arg) {
//        memoryMap();
        fileLock();
    }

    /**
     * 内存映射
     */
    static void memoryMap() {
        Path path = Paths.get("121759.mp4");
        try (     //从文件中获取一个通道，既能读又能写
                  FileChannel fileChannel = FileChannel.open(path);) {

            //获取一个byteBuffer缓冲区，fileChannel.map
            ByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());//获取
            //顺序读缓冲区,一次读一个字节数组的数组的速度比一次读一个字节快很多
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
            System.out.println(byteBuffer.order());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件锁
     */
    static void fileLock() {
        Path path = Paths.get("121759.mp4");
        //注意这里的文件打开模式是WRITE
        try (FileChannel fc = FileChannel.open(path, StandardOpenOption.WRITE);) {
            FileLock fl = fc.lock();//阻塞线程，直到可以获取这个锁,之后这个文件将一直处于锁定状态，直到通道关闭，或者fileLock.release()

            //文件锁由java虚拟机持有，如果两个程序由同一个虚拟机启动，那就不可能每个程序都获得一个文件锁（对同一个文件）
            //第二次上锁（lock或者tryLock）直接抛出异常
            //OverlappingFileLockException试图获取某个文件区域上的锁定，但是api则解释：获取不到锁时返回null
//            FileChannel fc1 = FileChannel.open(path, StandardOpenOption.WRITE);) {
//            System.out.println(" " + fc1.tryLock());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileChannel fc1 = FileChannel.open(path, StandardOpenOption.WRITE);) {
            System.out.println(" " + fc1.tryLock());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 使用结束记得调用 通道  和 锁
        try (FileChannel fc2 = FileChannel.open(path, StandardOpenOption.WRITE);
             FileLock fl2 = fc2.lock(0, fc2.size(), false);
        ) {
            //锁定从position开始size大小的文件区域，shared值是否共享，shared只能控制 指定文件区域，shared默认是false
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
