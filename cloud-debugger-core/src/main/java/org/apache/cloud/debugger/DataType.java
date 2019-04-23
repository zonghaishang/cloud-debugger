package org.apache.cloud.debugger;

/**
 * @author yiji@apache.org
 */
public interface DataType extends RecorderObject {

    String getName();

    void setName(String name);

    int getSort();

    void setSort(int sort);

    DataType clone();
}
