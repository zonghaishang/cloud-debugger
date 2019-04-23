package org.apache.cloud.debugger.demo;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

import java.io.IOException;

/**
 * @author yiji@apache.org
 */
public class HelloServiceExport {

    public static void main(String[] args) throws IOException {

        ServiceConfig<HelloService> service = new ServiceConfig<>();
        service.setApplication(new ApplicationConfig("cloud-debugger"));
        service.setRegistry(new RegistryConfig("N/A"));
        service.setInterface(HelloService.class);
        service.setRef(new HelloServiceImpl());
        service.export();

        System.in.read();

    }

}
