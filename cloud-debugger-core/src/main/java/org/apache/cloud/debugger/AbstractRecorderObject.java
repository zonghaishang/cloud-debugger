package org.apache.cloud.debugger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yiji@apache.org
 */
public abstract class AbstractRecorderObject implements RecorderObject {

    protected static final ThreadLocal<AtomicLong> SEQUENCE = new ThreadLocal<AtomicLong>() {
        @Override
        protected AtomicLong initialValue() {
            return new AtomicLong(1L);
        }
    };
    protected RecorderObject parent;
    protected Map<String, Object> attributes;
    protected int line;
    protected String label;
    protected int opCode;
    protected long sequence;
    protected long id;
    protected long traceId;

    @Override
    public void accept(RecorderVisitor visitor) {
        if (visitor == null) throw new IllegalArgumentException();

        visitor.preVisit(this);

        doAccept(visitor);

        visitor.postVisit(this);
    }

    protected final void acceptChild(RecorderVisitor visitor, List<? extends RecorderObject> children) {
        if (children == null) {
            return;
        }

        for (RecorderObject child : children) {
            acceptChild(visitor, child);
        }
    }

    protected final void acceptChild(RecorderVisitor visitor, RecorderObject child) {
        if (child == null) {
            return;
        }

        child.accept(visitor);
    }

    @Override
    public RecorderObject clone() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }

    @Override
    public RecorderObject getParent() {
        return parent;
    }

    @Override
    public void setParent(RecorderObject parent) {
        this.parent = parent;
    }

    @Override
    public Map<String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>(1);
        }

        return this.attributes;
    }

    @Override
    public Object getAttribute(String name) {
        if (this.attributes == null) {
            return null;
        }

        return this.attributes.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>(1);
        }

        this.attributes.put(name, value);
    }

    @Override
    public DataType getRuntimeType() {
        return null;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public void setLabel() {
        this.label = label;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public int opCode() {
        return this.opCode;
    }

    @Override
    public void setOpcode(int opCode) {
        this.opCode = opCode;
    }

    @Override
    public long sequence() {
        return this.sequence;
    }

    @Override
    public long nextSequence() {
        this.sequence = SEQUENCE.get().getAndIncrement();
        return sequence;
    }

    @Override
    public void resetSequence() {
        SEQUENCE.remove();
    }

    @Override
    public long id() {
        return this.id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long traceId() {
        return this.traceId;
    }

    @Override
    public void setTraceId(long traceId) {
        this.traceId = traceId;
    }

    protected abstract void doAccept(RecorderVisitor visitor);
}
