package org.apache.cloud.debugger;

/**
 * @author yiji@apache.org
 */
public class SortType {

    public static final SortType BYTE = new SortType(byte.class.getName(), 0);
    public static final SortType _BYTE = new SortType(Byte.class.getName(), BYTE.sort + 1);
    public static final SortType SHORT = new SortType(short.class.getName(), 2);
    public static final SortType _SHORT = new SortType(Short.class.getName(), SHORT.sort + 1);
    public static final SortType INT = new SortType(int.class.getName(), 4);
    public static final SortType _INT = new SortType(Integer.class.getName(), INT.sort + 1);
    public static final SortType LONG = new SortType(long.class.getName(), 6);
    public static final SortType _LONG = new SortType(Long.class.getName(), LONG.sort + 1);
    public static final SortType FLOAT = new SortType(float.class.getName(), 8);
    public static final SortType _FLOAT = new SortType(Float.class.getName(), FLOAT.sort + 1);
    public static final SortType DOUBLE = new SortType(double.class.getName(), 10);
    public static final SortType _DOUBLE = new SortType(Double.class.getName(), DOUBLE.sort + 1);
    public static final SortType STRING = new SortType(String.class.getName(), 12);
    private String name;
    private int sort;

    private SortType(String name, int sort) {
        this.name = name;
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "SortType{" +
                "name='" + name + '\'' +
                ", sort=" + sort +
                '}';
    }
}
