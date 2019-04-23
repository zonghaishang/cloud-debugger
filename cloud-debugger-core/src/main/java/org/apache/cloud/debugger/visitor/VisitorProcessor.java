package org.apache.cloud.debugger.visitor;

import org.apache.cloud.debugger.RecorderVisitor;
import org.apache.cloud.debugger.config.VisitorOptions;

/**
 * @author yiji@apache.org
 */
public class VisitorProcessor {

    private long traceId;

    private RecorderVisitor visitor;

    private VisitorOptions options;

    public VisitorProcessor(long traceId, RecorderVisitor visitor, VisitorOptions options) {
        this.traceId = traceId;
        this.visitor = visitor;
        this.options = options;
    }

    public long getTraceId() {
        return traceId;
    }

    public void setTraceId(long traceId) {
        this.traceId = traceId;
    }

    public RecorderVisitor getVisitor() {
        return visitor;
    }

    public void setVisitor(RecorderVisitor visitor) {
        this.visitor = visitor;
    }

    public VisitorOptions getOptions() {
        return options;
    }

    public void setOptions(VisitorOptions options) {
        this.options = options;
    }
}
