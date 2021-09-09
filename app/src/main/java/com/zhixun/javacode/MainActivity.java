package com.zhixun.javacode;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSocket(Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in = socket.getInputStream();
//                    InputStreamReader ir = new InputStreamReader(in);
//                    char[] chars = new char[1024];
//                    int len;
//                    while ((len = ir.read(chars)) != 1) {
//                        System.out.println("accept3---" + new String(chars, 0, len));
//                    }
                    Scanner scanner = new Scanner(in);
                    while (scanner.hasNextLine()) {
                        System.out.println("accept3---" + scanner.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}