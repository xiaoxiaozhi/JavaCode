package com.zhixun.javacode.other;

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
                        ses.shutdownNow();//不起作用
                    }
                    if (i == 60) {
                        System.out.println("break");
                        break;
                    }
                }
            }
        });
//        System.out.println(new byte[]{(byte) 0xff, (byte) 0xff});
        int num = 0x8181;
        byte[] temp = new byte[2];
        temp[0] = (byte) ((num & 0xff00) >> 8);
        System.out.println("temp[0] = " + Integer.toHexString(temp[0]));
        temp[1] = (byte) (num & 0xff);
        System.out.println("temp[1] = " + Integer.toHexString(temp[1]));

        int i = 0x88;
        System.out.println("i>>7 = " + Integer.toBinaryString((i >> 6) & 0x000000ff));
    }

}
