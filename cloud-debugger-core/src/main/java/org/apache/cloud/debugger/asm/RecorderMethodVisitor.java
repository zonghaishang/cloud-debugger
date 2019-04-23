package org.apache.cloud.debugger.asm;

import org.apache.cloud.debugger.runtime.IdGenerator;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.LinkedHashMap;

import static org.apache.cloud.debugger.util.TypeUtil.convertToClassName;

/**
 * @author yiji@apache.org
 */
public class RecorderMethodVisitor extends AdviceAdapter implements Opcodes, VisitorMetadata {

    public String methodSignature;
    public long id;
    protected String className;
    protected LinkedHashMap<Label, String> labelNames;
    protected LinkedHashMap<String, SlotFieldInfo> slotFields;
    protected LinkedHashMap<String, Integer> labelNumbers;
    protected long traceId;
    protected ClassLoader classLoader;

    protected RecorderMethodVisitor(
            long traceId
            , MethodVisitor methodVisitor
            , int access
            , String name
            , String descriptor
            , String className
            , ClassLoader classLoader) {
        super(ASM7, methodVisitor, access, name, descriptor);
        this.className = convertToClassName(className);
        this.methodSignature = getFullMethodSignature(className, name, descriptor);
        this.traceId = traceId;
        this.classLoader = classLoader;
        this.id = IdGenerator.newId(classLoader, className, getSimpleMethodSignature(name, descriptor));
    }

    protected String appendLabel(final Label label) {
        if (labelNames == null) {
            labelNames = new LinkedHashMap<>();
        }
        String name = labelNames.get(label);
        if (name == null) {
            name = "L" + labelNames.size();
            labelNames.put(label, name);
        }

        return name;
    }

    protected void appendLabelNumber(int line, Label start) {
        if (labelNumbers == null) {
            labelNumbers = new LinkedHashMap<>();
        }

        labelNumbers.put(appendLabel(start), line);
    }

    protected void appendSlot(String name, String descriptor, String signature, Label start, Label end, int index) {
        if (slotFields == null) {
            slotFields = new LinkedHashMap<>();
        }

        SlotFieldInfo slot = slotFields.get(name);
        if (slot == null) {
            slot = new SlotFieldInfo(name
                    , descriptor
                    , signature
                    , null
                    , index
                    , appendLabel(start)
                    , appendLabel(end));

            slotFields.put(name, slot);
            return;
        }

        SlotFieldInfo newSlot = new SlotFieldInfo(name
                , descriptor
                , signature
                , null
                , index
                , appendLabel(start)
                , appendLabel(end));

        newSlot.next = slot;

        slotFields.put(name, newSlot);
    }


    protected void updateLine() {
        if (slotFields != null) {
            slotFields.forEach((name, field) -> {
                SlotFieldInfo slot = field;

                while (slot != null) {
                    slot.line = findLine(slot.lineLabel);
                    slot.endLine = findLine(slot.endLineLabel);

                    slot = slot.next;
                }

            });
        }
    }

    protected int findLine(String label) {

        /**
         * found line number
         */
        if (labelNumbers != null) {
            Integer line = labelNumbers.get(label);
            if (line != null) return line.intValue();
        }

        if (labelNames != null) {

            String name = null;
            Integer line = null;
            boolean matched = false;

            String[] names = labelNames.values().toArray(new String[0]);
            for (int i = names.length - 1; i >= 0; --i) {

                if (label.equals(name = names[i])) {
                    matched = true;
                    if ((line = labelNumbers.get(name)) != null) {
                        return line.intValue();
                    }
                }

                if (matched) {
                    if ((line = labelNumbers.get(name)) != null) {
                        return line.intValue();
                    }
                }
            }
        }

        return -1;
    }
}
