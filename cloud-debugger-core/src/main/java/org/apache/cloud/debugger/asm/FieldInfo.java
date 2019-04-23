package org.apache.cloud.debugger.asm;

/**
 * @author yiji@apache.org
 */
public class FieldInfo {

    private String name;
    private String desc;
    private String signature;
    private Object value;

    public FieldInfo(String name, String desc, String signature, Object value) {
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.value = value;
    }
}
