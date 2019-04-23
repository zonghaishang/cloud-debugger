package org.apache.cloud.debugger;

/**
 * @author yiji@apache.org
 */
public class ClassObject extends RecorderObjectImpl implements RecorderValuable {
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
        return null;
    }
}
