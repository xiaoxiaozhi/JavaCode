package com.zhixun.javacode.stream;

import android.util.Log;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Io {
    private final static String TAG = "Io";

    public static void main(String... args) {
        path();
    }

    private static void path() {
        //Path 是一个目录
        Path path = Paths.get("home", "456.data");
        System.out.println(path.toString());
    }
}
