package org.apache.cloud.debugger.asm;

import org.apache.cloud.debugger.config.VisitorOptions;
import org.apache.cloud.debugger.util.TypeUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.JSRInlinerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yiji@apache.org
 */
public class RecorderClassVisitor extends ClassVisitor implements Opcodes, VisitorMetadata, VisitorType {

    private static final Logger logger = LoggerFactory.getLogger(RecorderVisitor.class);
    Map<String /** method signature */, RecorderSlotVisitor> slotVisitiors = new LinkedHashMap<>();
    private ClassLoader classLoader;
    private long traceId;
    private String className;
    private String internalClassName;
    private boolean isInterface;
    private boolean isAbstract;
    private VisitorOptions options;
    private Map<String /** field name */, FieldInfo> fields = new LinkedHashMap<>();

    public RecorderClassVisitor(
            long traceId
            , String className
            , ClassVisitor classVisitor
            , ClassLoader classLoader
            , VisitorOptions options) {
        super(ASM7, classVisitor);
        this.className = TypeUtil.convertToClassName(className);
        this.traceId = traceId;
        this.classLoader = classLoader;
        this.options = options;
        this.slotVisitiors = slotVisitiors;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        String methodSign = getFullMethodSignature(className, name, descriptor);
        RecorderSlotVisitor slotVisitor = new RecorderSlotVisitor(traceId
                , new JSRInlinerAdapter(mv, access, name, descriptor, signature, exceptions)
                , access
                , name
                , descriptor
                , className
                , classLoader);

        slotVisitiors.put(methodSign, slotVisitor);
        return slotVisitor;
    }

}
