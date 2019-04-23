package org.apache.cloud.debugger.demo;

/**
 * @author yiji@apache.org
 */
public class InvokeHelper {

    public String stepInto() {
        int j = 0;
        System.out.println(j * 100);
        return String.valueOf(j * 100);
    }

}
