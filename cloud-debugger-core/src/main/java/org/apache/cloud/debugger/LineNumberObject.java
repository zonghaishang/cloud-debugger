package org.apache.cloud.debugger;

/**
 * @author yiji@apache.org
 */
public interface LineNumberObject {

    String getLabel();

    void setLabel();

    int getLine();

    void setLine(int line);

    int opCode();

    void setOpcode(int opCode);

}
