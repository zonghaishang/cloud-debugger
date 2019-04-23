package org.apache.cloud.debugger;

import java.util.Map;

/**
 * @author yiji@apache.org
 */
public interface RecorderObject extends LineNumberObject {

    void accept(RecorderVisitor visitor);

    RecorderObject clone();

    RecorderObject getParent();

    void setParent(RecorderObject parent);

    Map<String, Object> getAttributes();

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    DataType getRuntimeType();

    long sequence();

    long nextSequence();

    void resetSequence();

    long id();

    void setId(long id);

    long traceId();

    void setTraceId(long traceId);
}
