package org.apache.cloud.debugger.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiji@apache.org
 */
public class HelloWorld {

    private long num;

    public int add(int a, int b) {
        int r = a + b;

        // System.out.println("-->>");

//        StringBuilder sb = new StringBuilder();
//        sb.append(a).append(" + ").append(b).append(" = ");
        int[] testArray = new int[100];
        List<InvokeHelper> helpers = new ArrayList<>();
//        System.out.println(sb.append(r));
//        {
//            String result = ("debug: " + new InvokeHelper().stepInto());
//            System.out.println(result);
//            {
//                int test = 10;
//                int ab = test * 1;
//            }
//            {
//                int test = 10;
//                int ab = test * 1;
//            }
//        }

        return r;
    }

    //    public boolean isGreater(int a, int b) {
//        boolean r = false;
//        if (a > b) {
//            r = true;
//        } else {
//            r = false;
//        }
//
//        return r;
//    }
//
    public int sum(int start, int loop) {
        int sum = start;
        for (int i = 0; i < loop; i++) {
            sum += i;
        }

        return sum;
    }

    public void trycatch() {
        try {
            int a = 12 / 0;
        } catch (Throwable e) {

        }
    }
//
//    public int sum3(int start, int loop) {
//        int sum = start;
//        for (int i = 0; i < loop; i++) {
//            new InvokeHelper().stepInto();
//            for (int j = 0; j < i; j++) {
//                sum += j;
//            }
//        }
//
//        return sum;
//    }
//
//    public int sum2(int start, int loop) {
//        int sum = start, i = 0;
//        while (i++ < loop) {
//            sum += i;
//        }
//
//        return sum;
//    }
//
//    public int switchCase(int a) {
//        int r = 0;
//        switch (a) {
//            case 1:
//            case 2: {
//                r = 2;
//                break;
//            }
//            case 3:
//            case 4: {
//                r = 4;
//                break;
//            }
//            default:
//                r = -1;
//        }
//        return r;
//    }

}
