package com.zhixun.javacode.connect;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class Connect2Server {
    public static void main(String[] main) {
        socket();
        address();
    }

    //1.使用socket套接字访问主机
    public static void socket() {
        //Socket socket = new Socket("129.6.15.28", 13); 该方法会一直等待下去直到主机回复(实际上并没有还是会超时)，可以先建立套接字
        Scanner scanner = null;
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("129.6.15.28", 13), 15 * 1000);
            scanner = new Scanner(socket.getInputStream(), "UTF-8");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line + "");
            }
            if (socket.isClosed()) System.out.println("连接已被关闭");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    //2.InetAddress 获取
    public static void address() {
        try {
            InetAddress address = InetAddress.getByName("baidu.com");
            System.out.println(address.getHostAddress());
            byte[] temp = address.getAddress();
            for (byte b : temp) {
                System.out.println("" + (b & 0x000000FF));
            }
//            Arrays.asList(address.getAddress()).stream().forEach(b -> System.out.println(" "+(b )));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


}
