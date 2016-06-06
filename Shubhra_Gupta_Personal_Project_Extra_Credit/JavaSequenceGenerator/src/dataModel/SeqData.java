package dataModel;

public class SeqData {
    private final SeqType seqType;
    private final String sourceName;
    private final String targetName;
    private final String signatureName;
    private final Object[] args;

    public SeqData(SeqType traceType, String source, String target, String signature, Object... args) {
        this.seqType = traceType;
        this.sourceName = source;
        this.targetName = target;
        this.signatureName = signature;
        this.args = args;
    }

    public SeqType getSeqType() {
        return seqType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public Object[] getArgs() {
        return args;
    }
}
