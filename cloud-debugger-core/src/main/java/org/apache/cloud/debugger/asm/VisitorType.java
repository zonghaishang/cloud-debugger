package org.apache.cloud.debugger.asm;

/**
 * @author yiji@apache.org
 */
public interface VisitorType {

    public static final int ACCESS_PRE_METHOD = 0;

    public static final int ACCESS_POST_METHOD = 1;

    public static final int ACCESS_METHOD_INVOKING = 2;

    public static final int ACCESS_METHOD_END_INVOKING = 3;

    public static final int ACCESS_FIELD = 4;
}
