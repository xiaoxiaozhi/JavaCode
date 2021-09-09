package com.zhixun.javacode.connect;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UrlCode {
    public static void main(String[] arg) {
        try {
            //URI 统一资源标识符，URI类不包含访问资源的任何方法，唯一的作用就是解析。例如mailto:cay@hostmann.com(java 不能处理该协议也就不能用url)
            //url 统一资源定位符，是一种特殊的 URI，url 只能作用于java知道的协议比如 http https ftp
            URL url = new URL("http://www.baidu.com/");
            InputStream inputStream = url.openStream();
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }

            //调用URLConnection 从url获取更多的信息
            URLConnection urlConnection = url.openConnection();
            //建立连接之前设置 连接属性
            urlConnection.setDoOutput(true);//设置连接属性建立 outputStream
            urlConnection.connect();//与服务器建立套接字连接，默认情况下只建立InputStream还可以查询头信息
            Map<String, List<String>> map = urlConnection.getHeaderFields();
//            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//                System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
//            }
            System.out.println("last Date = " +new Date(urlConnection.getLastModified()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
