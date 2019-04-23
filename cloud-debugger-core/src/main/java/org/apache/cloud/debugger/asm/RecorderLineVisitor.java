package org.apache.cloud.debugger.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.Map;

/**
 * @author yiji@apache.org
 */
public class RecorderLineVisitor extends RecorderSlotVisitor {

    private final Type[] argumentTypes;
    protected boolean isStatic;
    protected boolean isConstructor;
    protected Map<String, RecorderSlotVisitor> slotVisitiors;

    protected RecorderLineVisitor(
            MethodVisitor methodVisitor
            , long traceId
            , int access
            , String name
            , String descriptor
            , String className
            , Map<String, RecorderSlotVisitor> slotVisitiors
            , ClassLoader classLoader
    ) {
        super(traceId, methodVisitor, access, name, descriptor, className, classLoader);

        this.argumentTypes = Type.getArgumentTypes(descriptor);
        this.isStatic = (methodAccess & ACC_STATIC) != 0;
        this.slotVisitiors = slotVisitiors;

        this.isConstructor = "<init>".equals(name);
    }

    public boolean isStatic() {
        return isStatic;
    }

    protected void loadThisOrNull() {
        if (isStatic) {
            pushNull();
        } else {
            loadThis();
        }
    }

    protected void pushNull() {
        push((Type) null);
    }

    protected final void storeArgArray() {
        for (int i = 0; i < argumentTypes.length; i++) {
            dup();
            push(i);
            arrayLoad(ASM_TYPE_OBJECT);
            unbox(argumentTypes[i]);
            storeArg(i);
        }
    }

    /**
     * 装载方法调用参数
     */
    protected void loadInvokeArgArray(String descriptor, int[] slots) {
        Type[] invokeArgumentTypes = Type.getArgumentTypes(descriptor);
        push(invokeArgumentTypes.length);
        newArray(ASM_TYPE_OBJECT);
        for (int i = 0; i < slots.length; i++) {
            dup();
            push(i);
            loadLocal(slots[i]);
            box(invokeArgumentTypes[i]);
            arrayStore(ASM_TYPE_OBJECT);
        }
    }
}
