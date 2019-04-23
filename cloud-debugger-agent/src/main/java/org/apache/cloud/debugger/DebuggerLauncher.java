package org.apache.cloud.debugger;

import org.apache.cloud.debugger.asm.RecorderTransformer;
import org.apache.cloud.debugger.asm.VisitorMetadata;
import org.apache.cloud.debugger.spy.Spy;
import org.apache.cloud.debugger.visitor.VisitorListenerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

/**
 * @author yiji@apache.org
 */
public class DebuggerLauncher {

    private final static Logger logger = LoggerFactory.getLogger(DebuggerLauncher.class);

    /**
     * VM Options:
     * -javaagent:/Users/Jason/opensource/cloud-debugger/cloud-debugger-agent/target/cloud-debugger-agent-1.0.0-SNAPSHOT-jar-with-dependencies.jar
     * =/Users/Jason/opensource/cloud-debugger/cloud-debugger-spy/target/cloud-debugger-spy-1.0.0-SNAPSHOT.jar;
     */
    public static void premain(String featureString, Instrumentation inst) {

        start(featureString, inst);

    }

    private static void start(String featureString, Instrumentation inst) {
        Spy.initialize(
                DebuggerLauncher.class.getClassLoader()
                , VisitorMetadata.getDeclaredMethod(VisitorListenerHandler.class
                        , "preVisit"
                        , long.class                  // traceId
                        , long.class                  // id
                        , int.class                   // line
                        , int.class                   // opCode
                        , int.class                   // type
                        , String.class                // internalClassName
                        , String.class                // internalName
                        , String.class                // internalDesc
                        , int.class                   // access
                        , Object.class                // this
                        , Object[].class)             // arguments
                , VisitorMetadata.getDeclaredMethod(VisitorListenerHandler.class
                        , "postVisit"
                        , long.class                  // traceId
                        , long.class                  // id
                        , int.class                   // line
                        , int.class                   // opCode
                        , int.class                   // type
                        , int.class                   // state
                        , Object.class)               // value
                , VisitorMetadata.getDeclaredMethod(VisitorListenerHandler.class
                        , "visitMethodInvoke"
                        , long.class                  // traceId
                        , long.class                  // id
                        , int.class                   // line
                        , int.class                   // opCode
                        , int.class                   // type
                        , String.class                // internalClassName
                        , String.class                // internalName
                        , String.class                // internalDesc
                        , int.class                   // access
                        , Object.class                // this
                        , Object[].class)             // arguments
                , VisitorMetadata.getDeclaredMethod(VisitorListenerHandler.class
                        , "endVisitMethodInvoke"
                        , long.class                  // traceId
                        , long.class                  // id
                        , int.class                   // line
                        , int.class                   // opCode
                        , int.class                   // type
                        , int.class                   // state
                        , Object.class)               // value
                , null
                , VisitorMetadata.getDeclaredMethod(VisitorListenerHandler.class
                        , "visitLineExecute"
                        , long.class                  // traceId
                        , long.class                  // id
                        , int.class                   // line
                        , int.class                   // slot
                        , int.class                   // opCode
                        , int.class                   // type
                        , String.class                // name
                        , String.class                // descriptor
                        , String.class                // signature
                        , Object.class                // this
                        , Object.class                // value
                ));

        try {

            Thread bindingThread = new Thread() {
                @Override
                public void run() {
                    try {

                        int index = featureString.indexOf(';');
                        String agentJar = featureString.substring(0, index);
                        final String agentArgs = featureString.substring(index, featureString.length());

                        File agentJarFile = new File(agentJar);
                        if (!agentJarFile.exists()) {
                            logger.info("Agent jar file does not exist: " + agentJarFile);
                            return;
                        }

                        File spyJarFile = new File(agentJarFile.getParentFile(), "cloud-debugger-spy-1.0.0-SNAPSHOT.jar");
                        if (!spyJarFile.exists()) {
                            logger.info("Spy jar file does not exist: " + spyJarFile);
                            return;
                        }

                        inst.appendToBootstrapClassLoaderSearch(new JarFile(spyJarFile));

                        inst.addTransformer(new RecorderTransformer(), true);

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            };

            bindingThread.setName("debugger-binding-thread");
            bindingThread.start();
            bindingThread.join();
        } catch (Exception e) {
            logger.error("failed to start debugger.", e);
        }
    }

    public static void agentmain(String featureString, Instrumentation inst) {
        start(featureString, inst);
    }

}
