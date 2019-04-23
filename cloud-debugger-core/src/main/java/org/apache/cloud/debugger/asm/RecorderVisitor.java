package org.apache.cloud.debugger.asm;

import org.apache.cloud.debugger.config.VisitorOptions;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.JSRInlinerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.cloud.debugger.util.TypeUtil.convertToClassName;

/**
 * @author yiji@apache.org
 */
public class RecorderVisitor extends ClassVisitor implements Opcodes, VisitorMetadata, VisitorType {

    private static final Logger logger = LoggerFactory.getLogger(RecorderVisitor.class);

    private ClassLoader classLoader;
    private long traceId;

    private String className;
    private String internalClassName;
    private boolean isInterface;
    private boolean isAbstract;
    private VisitorOptions options;

    private Map<String /** field name */, FieldInfo> fields = new LinkedHashMap<>();

    private Map<String /** method signature */, RecorderSlotVisitor> slotVisitiors;

    public RecorderVisitor(
            long traceId
            , ClassVisitor classVisitor
            , ClassLoader classLoader
            , VisitorOptions options
            , Map<String, RecorderSlotVisitor> slotVisitiors) {
        super(ASM7, classVisitor);

        this.traceId = traceId;
        this.classLoader = classLoader;
        this.options = options;
        this.slotVisitiors = slotVisitiors;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.internalClassName = name;
        this.className = convertToClassName(name);
        this.isInterface = ((access & ACC_INTERFACE) != 0);
        this.isAbstract = ((access & ACC_ABSTRACT) != 0);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        String methodSign = getFullMethodSignature(className, name, descriptor);

        // todo 方便调试，直接写死增强类
        if (!className.contains("HelloWorld")) {
            return mv;
        }

        logger.info("Visiting method '{}'", methodSign);

        // todo 校验是否匹配增强方法
        return new RecorderLineVisitor(
                new JSRInlinerAdapter(mv, access, name, descriptor, signature, exceptions)
                , traceId
                , access
                , name
                , descriptor
                , className
                , slotVisitiors
                , classLoader) {

            int line = -1;
            boolean shouldSkip = false;

            @Override
            protected void onMethodEnter() {

                RecorderSlotVisitor slotVisitor = null;
                if (slotVisitiors == null
                        || (slotVisitor = slotVisitiors.get(methodSign)) == null) {
                    shouldSkip = true;
                    return;
                }

                StringBuilder buf = new StringBuilder();

                stdout0(buf
                        .append("debug enter ")
                        .append(methodSignature)
                        .append(" method").toString()
                );

                {
                    push(traceId);              // traceId
                    push(100L);           // id , long
                    push(line);                 // line
                    push(-1);             // opCode
                    push(ACCESS_PRE_METHOD);    // invoke type: method
                    push(internalClassName);    // internalClassName
                    push(name);                 // internalName
                    push(descriptor);           // internalDesc
                    push(access);               // access
                    loadThisOrNull();           // this or null (static method)
                    loadArgArray();             // argument Object[]

                    invokeStatic(ASM_TYPE_SPY, ASM_SPY_METHOD_PREVISIT);
                }

            }

            @Override
            public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface) {

                boolean isConstructor = (opcodeAndSource == INVOKESPECIAL) && name.equals("<init>");
                boolean isObjectConstructor = (isConstructor && owner.equals("java/lang/Object"));
                if (shouldSkip || isObjectConstructor) {
                    super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
                    return;
                }

                StringBuilder buf = new StringBuilder();
                stdout0(buf
                        .append("debug methodInsn ")
                        .append(methodSignature)
                        .append(" method").toString()
                );

                /**
                 * prepare invoking method slots.
                 */
                int[] slots = getInvokeMethodArgumentSlots(descriptor);

                int slotOfThis = getInvkeMethodSlotOfThis(opcodeAndSource, isConstructor);

                {
                    push(traceId);                         // traceId
                    push(100L);                      // id , long
                    push(line);                            // line
                    push(opcodeAndSource);                 // opCode
                    push(ACCESS_METHOD_INVOKING);          // invoke type: method
                    push(owner);                           // internalClassName
                    push(name);                            // internalName
                    push(descriptor);                      // internalDesc
                    push(access);                          // access
                    loadTargetOrNull(slotOfThis);          // this or null (static method)
                    loadInvokeArgArray(descriptor, slots); // argument Object[]

                    invokeStatic(ASM_TYPE_SPY, ASM_SPY_METHOD_VISITMETHODINVOKE);
                    recoveryOperateStack(slots, slotOfThis);
                }

                super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);

                {
                    Type returnType = Type.getReturnType(descriptor);
                    int slotOfReturn = loadReturn(returnType);

                    push(traceId);                         // traceId
                    push(100L);                      // id , long
                    push(line);                            // line
                    push(opcodeAndSource);                 // opCode
                    push(ACCESS_METHOD_END_INVOKING);      // type

                    int state = (0 << 16) | returnType.getSort();
                    push(state);                           // state

                    pushReturn(slotOfReturn);              // return

                    invokeStatic(ASM_TYPE_SPY, ASM_SPY_METHOD_ENDVISITMETHODINVOKE);
                }

            }

            @Override
            public void visitInsn(int opcode) {
                super.visitInsn(opcode);
            }

            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, var);
            }

            @Override
            public void visitIntInsn(int opcode, int operand) {
                super.visitIntInsn(opcode, operand);
            }

            @Override
            public void visitTypeInsn(int opcode, String type) {
                super.visitTypeInsn(opcode, type);
            }

            @Override
            protected void onMethodExit(int opcode) {

                if (shouldSkip) return;

                StringBuilder buf = new StringBuilder();
                stdout0(buf
                        .append("method exit: ")
                        .append(methodSignature)
                        .toString()
                );

                Type returnType = getReturnType();
                int slotOfReturn = loadReturn(returnType);

                {
                    push(traceId);                         // traceId
                    push(100L);                      // id , long
                    push(line);                            // line
                    push(opcode);                          // opCode
                    push(ACCESS_POST_METHOD);              // invoke type: method

                    int state = (((opcode == ATHROW) ? 1 : 0) << 16) | returnType.getSort();
                    push(state);                           // state

                    pushReturn(slotOfReturn);              // return

                    invokeStatic(ASM_TYPE_SPY, ASM_SPY_METHOD_POSTVISIT);
                }
            }

            @Override
            public void visitLineNumber(int line, Label start) {
                super.visitLineNumber(line, start);
                this.line = line;
            }

            @Override
            public void visitLocalVariable(String name
                    , String descriptor
                    , String signature
                    , Label start
                    , Label end
                    , int index) {
                super.visitLocalVariable(name, descriptor, signature, start, end, index);
            }

            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                StringBuilder buf = new StringBuilder();

                stdout0(buf
                        .append("visit method max: ")
                        .append(methodSignature)
                        .toString()
                );

                super.visitMaxs(maxStack, maxLocals);
            }

            @Override
            public void visitEnd() {
                super.visitEnd();
            }

            private void stdout0(final String msg) {

                // getStatic(ASM_TYPE_System, "out", ASM_TYPE_PrintStream);
                // push(msg);
                // invokeVirtual(Type.getType(PrintStream.class), ASM_METHOD_PRINTLN_INVOKE);

            }

            private int[] getInvokeMethodArgumentSlots(String descriptor) {
                Type[] argumentTypes = Type.getArgumentTypes(descriptor);
                int[] slots = new int[argumentTypes.length];

                for (int i = argumentTypes.length - 1; i >= 0; --i) {
                    slots[i] = newLocal(argumentTypes[i]);
                    storeLocal(slots[i]);
                }
                return slots;
            }

            private void recoveryOperateStack(int[] slots, int slotOfThis) {

                if (slotOfThis != -1) {
                    loadLocal(slotOfThis);
                }

                for (int slot : slots) {
                    loadLocal(slot);
                }
            }

            private int getInvkeMethodSlotOfThis(int opcodeAndSource, boolean isConstructor) {

                /**
                 * 构造函数不允许this逃逸
                 */
                if (isConstructor) return -1;

                switch (opcodeAndSource) {
                    case INVOKEVIRTUAL:
                    case INVOKESPECIAL:
                    case INVOKEINTERFACE: {
                        int slotOfThis = newLocal(ASM_TYPE_OBJECT);
                        storeLocal(slotOfThis);
                        return slotOfThis;
                    }
                    default:
                        return -1;
                }
            }

            private void loadTargetOrNull(int slotOfThis) {
                if (slotOfThis != -1) {
                    loadLocal(slotOfThis);
                } else {
                    pushNull();
                }
            }

            private int loadReturn(Type returnType) {

                if (!returnType.equals(Type.VOID_TYPE)) {

                    if (returnType.getSize() == 2) {
                        dup2();
                    } else {
                        dup();
                    }

                    box(returnType);

                    int slotOfReturn = newLocal(ASM_TYPE_OBJECT);
                    storeLocal(slotOfReturn);
                    return slotOfReturn;
                }

                return -1;
            }

            private void pushReturn(int slotOfReturn) {
                if (slotOfReturn == -1) {
                    pushNull();
                } else {
                    loadLocal(slotOfReturn);
                }
            }

        };

    }


}
