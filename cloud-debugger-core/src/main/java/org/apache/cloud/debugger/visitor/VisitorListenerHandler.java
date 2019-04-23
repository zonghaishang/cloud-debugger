package org.apache.cloud.debugger.visitor;

import org.apache.cloud.debugger.LineInvoker;
import org.apache.cloud.debugger.MethodInvoker;
import org.apache.cloud.debugger.RecorderObject;
import org.apache.cloud.debugger.RecorderVisitor;
import org.apache.cloud.debugger.asm.VisitorType;
import org.apache.cloud.debugger.config.VisitorOptions;
import org.apache.cloud.debugger.util.TypeUtil;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yiji@apache.org
 */
public class VisitorListenerHandler implements Opcodes {

    private static final Logger logger = LoggerFactory.getLogger(VisitorListenerHandler.class);
    private static final VisitorListenerHandler Instance = new VisitorListenerHandler();
    private static final Map<Long/*Trace Id*/, VisitorProcessor> visitorProcessor
            = new ConcurrentHashMap<Long, VisitorProcessor>();

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
        final VisitorProcessor processor = visitorProcessor.get(traceId);
        if (processor == null) {
            logger.debug("ignored to pre visit {}#{} because of visitor not be registered, traceId: {}", TypeUtil.convertToClassName(internalClassName), internalName, traceId);
            return;
        }

        RecorderObject object = null;

        switch (type) {
            case VisitorType.ACCESS_PRE_METHOD: {
                object = new MethodInvoker(
                        traceId
                        , id
                        , line
                        , opCode
                        , internalClassName
                        , internalName
                        , internalDesc
                        , access
                        , self
                        , value
                );
                break;
            }
            default:
                break;
        }

        if (object != null) {
            processor.getVisitor().preVisit(object);
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
            /**
             * isException << 16 | returnType
             */
            , final int state
            , final Object value) {

        final VisitorProcessor processor = visitorProcessor.get(traceId);
        if (processor == null) {
            logger.debug("ignored to post visitor because of visitor not be registered, traceId: {}", traceId);
            return;
        }

        RecorderObject object = null;

        switch (type) {
            case VisitorType.ACCESS_POST_METHOD: {
                object = new MethodInvoker(
                        traceId
                        , id
                        , line
                        , opCode
                        , value
                        , state
                );
                break;
            }
            default:
                break;
        }

        if (object != null) {
            processor.getVisitor().postVisit(object);
        }
    }

    public static void visitLineExecute(
            final long traceId
            /**
             * classloader << 14 | className << 18 | methodSign << 16
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
        final VisitorProcessor processor = visitorProcessor.get(traceId);
        if (processor == null) {
            logger.debug("ignored to pre visitor because of visitor not be registered, traceId: {}", traceId);
            return;
        }

        LineInvoker object = null;

        switch (type) {
            case VisitorType.ACCESS_PRE_METHOD: {
                object = new LineInvoker(
                        traceId
                        , id
                        , line
                        , slot
                        , opCode
                        , type
                        , name
                        , descriptor
                        , signature
                        , self
                        , value
                );
                break;
            }
            default:
                break;
        }

        if (object != null) {
            processor.getVisitor().visit(object);
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
        final VisitorProcessor processor = visitorProcessor.get(traceId);
        if (processor == null) {
            logger.debug("ignored to visit invoke {}#{} because of visitor not be registered, traceId: {}", TypeUtil.convertToClassName(internalClassName), internalName, traceId);
            return;
        }

        RecorderObject object = null;

        switch (type) {
            case VisitorType.ACCESS_PRE_METHOD: {
                object = new MethodInvoker(
                        traceId
                        , id
                        , line
                        , opCode
                        , internalClassName
                        , internalName
                        , internalDesc
                        , access
                        , self
                        , value
                );
                break;
            }
            default:
                break;
        }

        if (object != null) {
            processor.getVisitor().preVisit(object);
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

        final VisitorProcessor processor = visitorProcessor.get(traceId);
        if (processor == null) {
            logger.debug("ignored to end visit method invoker because of visitor not be registered, traceId: {}", traceId);
            return;
        }

        RecorderObject object = null;

        switch (type) {
            case VisitorType.ACCESS_PRE_METHOD: {
                object = new MethodInvoker(
                        traceId
                        , id
                        , line
                        , opCode
                        , value
                        , state
                );
                break;
            }
            default:
                break;
        }

        if (object != null) {
            processor.getVisitor().postVisit(object);
        }
    }

    public void register(final long traceId, RecorderVisitor visitor, VisitorOptions options) {
        logger.info("register visitor: id = {}, ref = {}, options = {}", traceId, visitor, options);
        visitorProcessor.put(traceId, new VisitorProcessor(traceId, visitor, options));
    }

    public void unRegister(final long traceId) {
        VisitorProcessor processor = visitorProcessor.remove(traceId);
        if (processor != null) {
            logger.info("cancel register visitor: id = {}", traceId);
            return;
        }
    }

}
