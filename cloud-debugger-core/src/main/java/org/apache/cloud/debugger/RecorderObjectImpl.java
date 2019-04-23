package org.apache.cloud.debugger;

import java.util.Collections;
import java.util.List;

/**
 * @author yiji@apache.org
 */
public abstract class RecorderObjectImpl extends AbstractRecorderObject implements RecorderValuable {

    public RecorderObjectImpl clone() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataType getRuntimeType() {
        return null;
    }

    @Override
    public List<RecorderObject> getChildren() {
        return Collections.emptyList();
    }

    public abstract boolean equals(Object o);

    public abstract int hashCode();
}
