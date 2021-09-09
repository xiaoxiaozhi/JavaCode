package com.zhixun.javacode.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class Connect2Server {
    public static void main(String[] main) {
        socket();
        address();
        addressArray();
        try {
            System.out.println("本地地址" + InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        server();
        sendData();
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
            byte[] temp = address.getAddress();//ip地址放进四字节数组
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //3.InetAddress 获取主机的全部ip地址
    public static void addressArray() {
        try {
            InetAddress[] addressArray = InetAddress.getAllByName("baidu.com");
            for (InetAddress address : addressArray) {
                System.out.println(address.getHostAddress());
//                byte[] temp = address.getAddress();
//                for (byte b : temp) {
//                    System.out.print("" + (b & 0x000000FF));
//                }
            }
//            InetAddress.getAllByName("");
//            Arrays.asList(address.getAddress()).stream().forEach(b -> System.out.println(" "+(b )));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void server() {
        try {
            new Thread(new Runnable() {
                ServerSocket ss = new ServerSocket(8189);

                @Override
                public void run() {
                    while (true) {
                        try {
                            System.out.println("accept1");
                            Socket socket = ss.accept();
                            System.out.println("accept2");
                            addSocket(socket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addSocket(Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in = socket.getInputStream();
                    Scanner scanner = new Scanner(in);
                    while (scanner.hasNextLine()) {
                        System.out.println("server accept--- " + scanner.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void sendData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (Socket socket = new Socket()) {
                    Thread.sleep(3000);
                    socket.connect(new InetSocketAddress("127.0.0.1", 8189), 15 * 1000);
                    OutputStream out = socket.getOutputStream();
//                    out.write(123);
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
                    Scanner scanner = new Scanner(System.in);
                    while (scanner.hasNextLine()) {
                        pw.println(scanner.nextLine());
                        pw.flush();
                        System.out.println("---client send over---");
                    }
                    socket.shutdownInput();//关闭输入流
                    socket.shutdownOutput();//关闭输出流
                    socket.isInputShutdown();//判断输入流被关闭
                    socket.isOutputShutdown();//判断输出流被关闭
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
