package org.apache.cloud.debugger;

/**
 * @author yiji@apache.org
 */
//public class RecorderDataTypeImpl extends RecorderObjectImpl implements DataType {
//
//    private String name;
//
//    private int sort;
//
//    public RecorderDataTypeImpl(String name, int sort) {
//        this.name = name;
//        this.sort = sort;
//    }
//
//    @Override
//    public String getName() {
//        return this.name;
//    }
//
//    @Override
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        RecorderDataTypeImpl that = (RecorderDataTypeImpl) o;
//        return sort == that.sort &&
//                Objects.equals(name, that.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name, sort);
//    }
//
//    @Override
//    protected void doAccept(RecorderVisitor visitor) {
//        visitor.visit(this);
//        visitor.endVisit(this);
//    }
//
//    @Override
//    public int getSort() {
//        return this.sort;
//    }
//
//    @Override
//    public void setSort(int sort) {
//        this.sort = sort;
//    }
//
//    @Override
//    public Object getValue() {
//        return null;
//    }
//
//    @Override
//    public RecorderDataTypeImpl clone() {
//        RecorderDataTypeImpl dataType = new RecorderDataTypeImpl(name, sort);
//        return dataType;
//    }
//}
