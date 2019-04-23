package org.apache.cloud.debugger.asm;

/**
 * @author yiji@apache.org
 */
public class SlotFieldInfo extends FieldInfo {

    public int slot;
    public int line;
    public int endLine;

    public String lineLabel;
    public String endLineLabel;

    public SlotFieldInfo next;

    public SlotFieldInfo(String name
            , String desc
            , String signature
            , Object value
            , int slot
            , int line
            , int endLine) {
        super(name, desc, signature, value);
        this.slot = slot;
        this.line = line;
        this.endLine = endLine;
    }

    public SlotFieldInfo(String name
            , String desc
            , String signature
            , Object value
            , int slot
            , String lineLabel
            , String endLineLabel) {
        super(name, desc, signature, value);
        this.slot = slot;
        this.line = -1;
        this.endLine = -1;
        this.lineLabel = lineLabel;
        this.endLineLabel = endLineLabel;
    }
}
