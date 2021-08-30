package com.zhixun.javacode.other;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Test3 {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    // Log.w("Test1", "i = " + i);
                    System.out.println("i = " + i);
                    if (i == 50) {
                        System.out.println("终止");
                        ses.shutdownNow();
                    }
                    if (i == 60) {
                        System.out.println("break");
                        break;
                    }
                }
            }
        });
        Hslx sf=new Hslx();
        sf.sdsa();
    }

    static class Hslx {
        HashMap<String, String> hashMap = new HashMap<>();

        public void sdsa() {
            hashMap.put("12", "23");
        }
    }
}
