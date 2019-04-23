package org.apache.cloud.debugger.demo;

/**
 * @author yiji@apache.org
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Welcome " + name;
    }

}
