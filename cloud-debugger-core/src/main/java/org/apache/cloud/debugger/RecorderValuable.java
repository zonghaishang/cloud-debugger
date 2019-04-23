package org.apache.cloud.debugger;

import java.util.List;

/**
 * @author yiji@apache.org
 */
public interface RecorderValuable extends RecorderObject, Cloneable {

    RecorderValuable clone();

    DataType getRuntimeType();

    List<RecorderObject> getChildren();

    Object getValue();
}
