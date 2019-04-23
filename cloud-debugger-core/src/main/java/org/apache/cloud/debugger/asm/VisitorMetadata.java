package org.apache.cloud.debugger.asm;

import org.apache.cloud.debugger.spy.Spy;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import static org.apache.cloud.debugger.util.TypeUtil.join;


/**
 * @author yiji@apache.org
 */
public interface VisitorMetadata {

    Type ASM_TYPE_SPY = Type.getType(Spy.class);

    Type ASM_TYPE_OBJECT = Type.getType(Object.class);

    Type ASM_TYPE_THROWABLE = Type.getType(Throwable.class);

    Type ASM_TYPE_CLASS = Type.getType(Class.class);

    Type ASM_TYPE_INT = Type.getType(int.class);

    Type ASM_TYPE_LONG = Type.getType(long.class);

    Type ASM_TYPE_STRING = Type.getType(String.class);

    Type ASM_TYPE_REFLECT_METHOD = Type.getType(java.lang.reflect.Method.class);

    Type ASM_TYPE_PrintStream = Type.getType(PrintStream.class);

    Type ASM_TYPE_System = Type.getType(System.class);

    Method ASM_METHOD_PRINTLN_INVOKE = Method.getMethod("void println (String)");

    Method ASM_SPY_METHOD_PREVISIT = getASMMethod(Spy.class
            , "preVisit"
            , long.class  // traceId
            , long.class                  // id
            , int.class                   // line
            , int.class                   // opCode
            , int.class                   // type
            , String.class                // internalClassName
            , String.class                // internalName
            , String.class                // internalDesc
            , int.class                   // access
            , Object.class                // this
            , Object[].class              // arguments
    );

    Method ASM_SPY_METHOD_POSTVISIT = getASMMethod(Spy.class
            , "postVisit"
            , long.class                  // traceId
            , long.class                  // id
            , int.class                   // line
            , int.class                   // opCode
            , int.class                   // type
            , int.class                   // state
            , Object.class                // value
    );

    Method ASM_SPY_METHOD_VISITLINEEXECUTE = getASMMethod(Spy.class
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
    );

    Method ASM_SPY_METHOD_VISITMETHODINVOKE = getASMMethod(Spy.class
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
            , Object[].class              // arguments
    );

    Method ASM_SPY_METHOD_ENDVISITMETHODINVOKE = getASMMethod(Spy.class
            , "endVisitMethodInvoke"
            , long.class                  // traceId
            , long.class                  // id
            , int.class                   // line
            , int.class                   // opCode
            , int.class                   // type
            , int.class                   // state
            , Object.class                // value
    );

    static java.lang.reflect.Method getDeclaredMethod(final Class<?> clazz,
                                                      final String name,
                                                      final Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    static Method getASMMethod(final Class<?> clazz,
                               final String name,
                               final Class<?>... parameterTypes) {
        java.lang.reflect.Method method = getDeclaredMethod(clazz, name, parameterTypes);
        return method != null ? Method.getMethod(method) : null;
    }

    default String getFullMethodSignature(final String className,
                                          final String name,
                                          final String desc) {

        Type methodType = Type.getMethodType(desc);
        Collection<String> parameterTypes = new ArrayList<String>();
        if (methodType.getArgumentTypes() != null) {
            for (final Type parameterType : methodType.getArgumentTypes()) {
                parameterTypes.add(parameterType.getClassName());
            }
        }
        return String.format("%s#%s(%s)"
                , className
                , name
                , join(parameterTypes, ",")
        );
    }

    default String getSimpleMethodSignature(
            final String name,
            final String desc) {

        Type methodType = Type.getMethodType(desc);
        Collection<String> parameterTypes = new ArrayList<String>();
        if (methodType.getArgumentTypes() != null) {
            for (final Type parameterType : methodType.getArgumentTypes()) {
                parameterTypes.add(parameterType.getClassName());
            }
        }
        return String.format("%s(%s)"
                , name
                , join(parameterTypes, ",")
        );
    }
}
