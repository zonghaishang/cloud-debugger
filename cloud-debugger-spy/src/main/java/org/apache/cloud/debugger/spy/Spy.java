package org.apache.cloud.debugger.spy;

import java.lang.reflect.Method;

/**
 * @author yiji@apache.org
 */
public class Spy {

    public static volatile ClassLoader CLASSLOADER;
    /**
     * 方法进入时触发
     */
    private static volatile Method PRE_VISIT_METHOD;
    /**
     * 方法结束时触发
     */
    private static volatile Method POST_VISIT_METHOD;
    /**
     * 正在执行的方法内部，发起另外一个调用触发
     */
    private static volatile Method PRE_VISIT_INVOKING_METHOD;
    /**
     * 正在执行的方法内部，发起另外一个调用结束触发
     */
    private static volatile Method POST_VISIT_INVOKING_METHOD;
    /**
     * 执行行指令触发
     */
    private static volatile Method PRE_VISIT_LINE_METHOD;
    /**
     * 执行行指令后触发
     */
    private static volatile Method POST_VISIT_LINE_METHOD;

    public static void initialize(ClassLoader classLoader
            , Method preVisitMethod
            , Method postVisitMethod
            , Method preVisitInvokingMethod
            , Method postVisitInvokingMethod
            , Method preVisitLineMethod
            , Method postVisitLineMethod) {
        CLASSLOADER = classLoader;
        PRE_VISIT_METHOD = preVisitMethod;
        POST_VISIT_METHOD = postVisitMethod;
        PRE_VISIT_INVOKING_METHOD = preVisitInvokingMethod;
        POST_VISIT_INVOKING_METHOD = postVisitInvokingMethod;
        PRE_VISIT_LINE_METHOD = preVisitLineMethod;
        POST_VISIT_LINE_METHOD = postVisitLineMethod;
    }


    public static void destroy() {
        CLASSLOADER = null;
        PRE_VISIT_METHOD = null;
        POST_VISIT_METHOD = null;
        PRE_VISIT_INVOKING_METHOD = null;
        POST_VISIT_INVOKING_METHOD = null;
        PRE_VISIT_LINE_METHOD = null;
        POST_VISIT_LINE_METHOD = null;
    }

    public static void preVisit(
            final long traceId
            /**
             * classloader << 14 | className << 18 | methodSign << 32
             */
            , final long id
            , final int line
            , final int opCode
            , final int type

            , final String internalClassName
            , final String internalName
            , final String internalDesc

            , final int access
            , final Object self
            , final Object[] value) {
        if (PRE_VISIT_METHOD != null) {

            try {
                PRE_VISIT_METHOD.invoke(null
                        , traceId
                        , id
                        , line
                        , opCode
                        , type
                        , internalClassName
                        , internalName
                        , internalDesc
                        , access
                        , self
                        , value);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void postVisit(
            final long traceId
            /**
             * classloader << 14 | className << 18 | methodSign << 32
             */
            , final long id
            , final int line
            , final int opCode
            , final int type
            , final int state
            , final Object value) {
        if (POST_VISIT_METHOD != null) {

            try {
                POST_VISIT_METHOD.invoke(null
                        , traceId
                        , id
                        , line
                        , opCode
                        , type
                        , state
                        , value);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void visitLineExecute(
            final long traceId
            /**
             * classloader << 14 | className << 18 | methodSign << 32
             */
            , final long id
            , final int line
            , final int slot
            , final int opCode

            , final int type

            , final String name
            , final String descriptor
            , final String signature

            , final Object self
            , final Object value) {
        if (POST_VISIT_LINE_METHOD != null) {

            try {
                POST_VISIT_LINE_METHOD.invoke(null
                        , traceId
                        , id
                        , line
                        , slot
                        , opCode
                        , type
                        , name
                        , descriptor
                        , signature
                        , self
                        , value);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void visitMethodInvoke(
            final long traceId
            /**
             * classloader << 14 | className << 18 | methodSign << 32
             */
            , final long id
            , final int line
            , final int opCode
            , final int type

            , final String internalClassName
            , final String internalName
            , final String internalDesc

            , final int access
            , final Object self
            , final Object[] value) {
        if (PRE_VISIT_INVOKING_METHOD != null) {

            try {
                PRE_VISIT_INVOKING_METHOD.invoke(null
                        , traceId
                        , id
                        , line
                        , opCode
                        , type
                        , internalClassName
                        , internalName
                        , internalDesc
                        , access
                        , self
                        , value);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void endVisitMethodInvoke(
            final long traceId
            /**
             * classloader << 14 | className << 18 | methodSign << 32
             */
            , final long id
            , final int line
            , final int opCode
            , final int type
            , final int state
            , final Object value) {
        if (POST_VISIT_INVOKING_METHOD != null) {

            try {
                POST_VISIT_INVOKING_METHOD.invoke(null
                        , traceId
                        , id
                        , line
                        , opCode
                        , type
                        , state
                        , value);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

//    public static class Return {
//
//        public static final int RETURN_VOID = 0;
//
//        public static final int RETURN_NORMAL = 1;
//
//        public static final int RETURN_THROWABLE = 2;
//        public static Return NOOP = new Return(RETURN_VOID, null);
//        private Object value;
//        private int state = RETURN_VOID;
//
//        private Return(int state, Object value) {
//            if (state < RETURN_VOID || state > RETURN_THROWABLE) {
//                throw new IllegalArgumentException("state expect '0、1、2' , actual '" + state + "'");
//            }
//            this.state = state;
//            this.value = value;
//        }
//
//        public static Return valueOf(Object value) {
//            return new Return(RETURN_NORMAL, value);
//        }
//
//        public static Return throwableOf(Throwable value) {
//            return new Return(RETURN_THROWABLE, value);
//        }
//    }
}
