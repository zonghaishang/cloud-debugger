package org.apache.cloud.debugger;

import java.util.Objects;

/**
 * @author yiji@apache.org
 */
public class LineInvoker extends RecorderObjectImpl implements RecorderValuable {

    private long traceId;
    /**
     * classloader << 14 | className << 18 | methodName << 16 | methodDesc << 16
     */
    private long id;

    private int line;

    private int slot;
    private int opCode;

    private int type;

    private String name;
    private String descriptor;
    private String signature;

    private Object self;
    private Object value;

    public LineInvoker(long traceId
            , long id
            , int line
            , int slot
            , int opCode
            , int type
            , String name
            , String descriptor
            , String signature
            , Object self
            , Object value) {
        this.traceId = traceId;
        this.id = id;
        this.line = line;
        this.slot = slot;
        this.opCode = opCode;
        this.type = type;
        this.name = name;
        this.descriptor = descriptor;
        this.signature = signature;
        this.self = self;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineInvoker that = (LineInvoker) o;
        return traceId == that.traceId &&
                id == that.id &&
                line == that.line &&
                slot == that.slot &&
                opCode == that.opCode &&
                type == that.type &&
                Objects.equals(name, that.name) &&
                Objects.equals(descriptor, that.descriptor) &&
                Objects.equals(signature, that.signature) &&
                Objects.equals(self, that.self) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traceId, id, line, slot, opCode, type, name, descriptor, signature, self, value);
    }

    @Override
    protected void doAccept(RecorderVisitor visitor) {

    }

    @Override
    public Object getValue() {
        return this.value;
    }
}
