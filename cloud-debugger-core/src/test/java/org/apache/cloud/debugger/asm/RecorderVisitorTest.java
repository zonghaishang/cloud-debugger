package org.apache.cloud.debugger.asm;

import org.apache.cloud.debugger.demo.HelloWorld;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

/**
 * @author yiji@apache.org
 */
public class RecorderVisitorTest {

    public static void main(String[] args) throws Exception {
        // Textifier.main(new String[]{HelloWorld.class.getName()});

        ClassReader cr2 = new ClassReader(HelloWorld.class.getName());
        RecorderClassVisitor classVisitor = new RecorderClassVisitor(0L
                , HelloWorld.class.getName()
                , null
                , RecorderVisitorTest.class.getClassLoader()
                , null);
        cr2.accept(classVisitor, EXPAND_FRAMES);


        ClassReader classReader = new ClassReader(HelloWorld.class.getName());


        ClassWriter cw = new ClassWriter(/*COMPUTE_FRAMES | */COMPUTE_MAXS);

        RecorderVisitor recoder = new RecorderVisitor(
                0L
                , cw/*new CheckClassAdapter(cw)*/
                , null
                , null
                , classVisitor.slotVisitiors);

        classReader.accept(recoder, ClassReader.EXPAND_FRAMES);

        byte[] byteCodes = cw.toByteArray();

        InputStream inputStream =
                new ByteArrayInputStream(byteCodes);


        new ClassReader(inputStream)
                .accept(new CheckClassAdapter(new TraceClassVisitor(null
                        , new Textifier()
//                        , new ASMifier()
                        , new PrintWriter(System.out, true))), 0);

    }

}
