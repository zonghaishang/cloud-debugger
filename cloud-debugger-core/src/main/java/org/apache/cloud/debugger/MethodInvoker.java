package org.apache.cloud.debugger;

/**
 * @author yiji@apache.org
 */
public class MethodInvoker extends RecorderObjectImpl implements RecorderValuable {

    private boolean isConstructor;

    private String className;
    private String methodName;

    private String methodDesc;

    private int access;

    private Object self;

    private Object arguments;

    private Object value;

    private int state;

    public MethodInvoker(
            final long traceId
            /**
             * classloader << 14 | className << 18 | methodName << 16 | methodDesc << 16
             */
            , final long id
            , final int line
            , final int opCode

            , final String internalClassName
            , final String internalName
            , final String internalDesc

            , final int access
            , final Object self
            , final Object value) {
        this.traceId = traceId;
        this.id = id;
        this.line = line;
        this.opCode = opCode;
        this.access = access;
        this.className = internalClassName;
        this.methodName = internalName;
        this.methodDesc = internalDesc;
        this.self = self;
        this.arguments = value;

        nextSequence();
    }

    public MethodInvoker(
            final long traceId
            /**
             * classloader << 14 | className << 18 | methodName << 16 | methodDesc << 16
             */
            , final long id
            , final int line
            , final int opCode

            , final Object value
            , final int state) {
        this.traceId = traceId;
        this.id = id;
        this.line = line;
        this.opCode = opCode;

        this.value = value;
        this.state = state;

        nextSequence();
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    protected void doAccept(RecorderVisitor visitor) {

    }

    @Override
    public Object getValue() {
        if (arguments != null) return arguments;

        return this.value;
    }


}
