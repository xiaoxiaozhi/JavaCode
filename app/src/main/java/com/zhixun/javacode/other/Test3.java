package com.zhixun.javacode.other;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
//        int num = 0x8181;
//        byte[] temp = new byte[2];
//        temp[0] = (byte) ((num & 0xff00) >> 8);
//        System.out.println("temp[0] = " + Integer.toHexString(temp[0]));
//        temp[1] = (byte) (num & 0xff);
//        System.out.println("temp[1] = " + Integer.toHexString(temp[1]));
//
//        int n = 0x88;
//        System.out.println("i>>7 = " + Integer.toBinaryString((n >> 6) & 0x000000ff));
//        byte[] temp = new byte[]{0x01, 0x7d, 0x01, 0x02};
//        List<Byte> lists = new ArrayList<>();
//        for (int i = 0; i < temp.length; i++) {
//            if (temp[i] == 0x7D) {
//                lists.add((byte) (temp[i] + temp[i + 1] - 0x01));
//                i++;
//                continue;
//            }
//            lists.add(temp[i]);
//        }
//        System.out.println(" ---" + lists.toString());
//        String date = "00 01 01 10 13 30";
//        List<Integer> lists = Arrays.asList(0x00, 0x01, 0x01, 0x10, 0x13, 0x30);
//        byte[] temp = new byte[]{0x00, 0x01, 0x01, 0x10, 0x13, 0x30};
//        System.out.println("bcd1---" + bcd2Str(ASCII_To_BCD(date.getBytes(), date.length())));
//
//        StringBuffer stringBuffer = new StringBuffer();
//        for (int str : lists) {
//            stringBuffer.append(hextoBcd(str));
//
//        }
//        System.out.println("bcd2---" + stringBuffer.toString());
        byte b = (byte) 0xFd;
        System.out.println("0xFD" + (b & 0x000000FF));
    }

    public static String hextoBcd(int hex) {


        String bcd = DecimaltoBcd(String.valueOf(hex));

        return bcd;
    }

    public static String DecimaltoBcd(String str) {
        String b_num = "";
        for (int i = 0; i < str.length(); i++) {

            String b = Integer.toBinaryString(Integer.parseInt(str.valueOf(str.charAt(i))));

            int b_len = 4 - b.length();

            for (int j = 0; j < b_len; j++) {
                b = "0" + b;
            }
            b_num += b;
        }

        return b_num;
    }

    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    private static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    private static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }
}
