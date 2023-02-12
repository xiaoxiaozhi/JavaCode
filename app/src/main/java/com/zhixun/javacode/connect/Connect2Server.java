package com.zhixun.javacode.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * socket内容来自于《JAVA核心技术卷2 第四章网络》
 * 1.客户端开启套接字，并打印输入
 * 2.InetAddress获取主机Ip地址
 * 4.服务端开启套接字，并打印输出，ServerSocket.accept()获取客户端socket，再通过判断socket的ip和端口号来区分不同的客户端
 * 5.利用套接字发送信息
 * attention：DatagramSocket()是建立在UDP上的socket，Socket()是建立在TCP上的socket
 * ---------以下补充来自网络------------------
 * [socket到底是什么?](https://www.bilibili.com/video/BV12A411X7gY/?spm_id_from=333.999.0.0)
 * socket就是Linux在内核中实现TCP、UDP、ICMP这些协议后，暴露给外部调用的API
 *
 * [为什么有HTTP还要有websocket](https://www.bilibili.com/video/BV1684y1k7VP/?spm_id_from=333.788&vd_source=9cc1c08c51cf20bda524430137dc77bb)
 * HTTP和Websocket都是基于TCP的应用层协议，前者是半双工，服务端无法向客户端发送数据；后者是全双工两端都能向对方发送数据
 * 使用Http还有一个技巧就是设置超长的响应时间例如30S。使用场景，二维码登陆的时候，电脑端显示二维码服务器不立即响应，等待手机点击确认，服务器才响应电脑端。取巧的方式解决了HTTP立马登录问题
 * Websocket与socket区别,就像雷锋和雷峰塔只是名字像了,另一个值得注意的是，客户端不一样，服务端都是ServerSocket，
 */
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
        //5.发送信息
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

    //4.服务端套接字
    public static void server() {
        try {
            new Thread(new Runnable() {
                ServerSocket ss = new ServerSocket(8189);

                @Override
                public void run() {
                    while (true) {
                        try {
                            System.out.println("accept1");
                            //判断socket的ip地址端口号，来区分不同的客户端
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
