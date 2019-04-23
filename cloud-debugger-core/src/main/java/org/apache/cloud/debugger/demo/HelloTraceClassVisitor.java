package org.apache.cloud.debugger.demo;

import org.objectweb.asm.util.Textifier;

import java.io.IOException;

/**
 * @author yiji@apache.org
 */
public class HelloTraceClassVisitor {
    public static void main(String[] args) throws IOException {
        Textifier.main(new String[]{HelloWorld.class.getName()});
    }
}
