package org.apache.cloud.debugger.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * @author yiji@apache.org
 */
public class RecorderSlotVisitor extends RecorderMethodVisitor {

    protected RecorderSlotVisitor(
            long traceId
            , MethodVisitor methodVisitor
            , int access
            , String name
            , String descriptor
            , String className
            , ClassLoader classLoader) {
        super(traceId, methodVisitor, access, name, descriptor, className, classLoader);
    }

    @Override
    public void visitLabel(Label label) {
        super.visitLabel(label);
        appendLabel(label);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        super.visitLineNumber(line, start);
        appendLabelNumber(line, start);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
        appendSlot(name, descriptor, signature, start, end, index);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
        updateLine();
    }
}
