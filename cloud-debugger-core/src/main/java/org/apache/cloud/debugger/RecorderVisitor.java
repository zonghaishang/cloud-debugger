package org.apache.cloud.debugger;

/**
 * @author yiji@apache.org
 */
public interface RecorderVisitor {

    void preVisit(RecorderObject object);

    void postVisit(RecorderObject object);


//    boolean visit(IntObject object);
//
//    void endVisit(IntObject object);
//
//
//    boolean visit(DataType object);
//
//    void endVisit(DataType object);

    /**
     * 处理方法调用
     */
    boolean visit(MethodInvoker object);

    void endVisit(MethodInvoker object);

    /**
     * 处理字段调用
     */
//    boolean visit(FieldInvoker object);
//
//    void endVisit(FieldInvoker object);

    /**
     * 处理行指令调用
     */
    boolean visit(LineInvoker object);

//    void endVisit(LineInvoker object);
//
//
//    boolean visit(ClassObject object);
//
//    void endVisit(ClassObject object);
}
