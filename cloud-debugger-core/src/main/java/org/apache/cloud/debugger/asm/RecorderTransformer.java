package org.apache.cloud.debugger.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;

/**
 * @author yiji@apache.org
 */
public class RecorderTransformer implements ClassFileTransformer {

    private static final Logger logger = LoggerFactory.getLogger(RecorderTransformer.class);

    private long traceId;

    @Override
    public byte[] transform(ClassLoader loader
            , String className
            , Class<?> classBeingRedefined
            , ProtectionDomain protectionDomain
            , byte[] classfileBuffer) throws IllegalClassFormatException {

        try {

            ClassReader cr = new ClassReader(classfileBuffer);

            // 字节码增强
            final ClassWriter cw = new ClassWriter(cr, COMPUTE_FRAMES/* | COMPUTE_MAXS*/) {

                @Override
                protected String getCommonSuperClass(String type1, String type2) {
                    Class<?> c, d;
                    final ClassLoader classLoader = loader;
                    try {
                        c = Class.forName(type1.replace('/', '.'), false, classLoader);
                        d = Class.forName(type2.replace('/', '.'), false, classLoader);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (c.isAssignableFrom(d)) {
                        return type1;
                    }
                    if (d.isAssignableFrom(c)) {
                        return type2;
                    }
                    if (c.isInterface() || d.isInterface()) {
                        return "java/lang/Object";
                    } else {
                        do {
                            c = c.getSuperclass();
                        } while (!c.isAssignableFrom(d));
                        return c.getName().replace('.', '/');
                    }
                }

            };

            // todo Need to be refactored.
            RecorderClassVisitor classVisitor = new RecorderClassVisitor(traceId
                    , className
                    , null
                    , loader
                    , null);

            ClassReader cr2 = new ClassReader(classfileBuffer);
            cr2.accept(classVisitor, EXPAND_FRAMES);

            cr.accept(new RecorderVisitor(
                    0L
                    , cw
                    , loader
                    , null
                    , classVisitor.slotVisitiors), EXPAND_FRAMES);

            return cw.toByteArray();

        } catch (Exception e) {

            logger.warn("failed to transformer class {}, cause:{}", className, e);

            return null;
        }
    }
}
