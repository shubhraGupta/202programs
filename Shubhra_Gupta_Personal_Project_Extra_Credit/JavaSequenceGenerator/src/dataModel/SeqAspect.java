package dataModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class SeqAspect {

    private static List<SeqData> seqDataList = new ArrayList<>();

    @Before("within(app.*) && call(* app.*.*(..))")
    public void before(JoinPoint thisJoinPoint) {
        beforeData(getThisName(thisJoinPoint).getName(), getTargetName(thisJoinPoint).getName(), thisJoinPoint.getSignature(), thisJoinPoint.getArgs());
        
    }

    public void beforeData(String source, String target, Signature signature, Object[] args) {
        if(source != null && target != null) {
            String seqData = source + " -> " + target + " : " + signature.getName() + "(" + Arrays.deepToString(args) + ")";
            seqDataList.add(new SeqData(SeqType.CALL, source, target, signature.getName(), args));
            System.out.println(seqData);
        }
    }

    @AfterReturning(pointcut = "within(app.*) && call(* app.*.*(..))", returning = "o")
    public void after(final JoinPoint thisJoinPoint, Object o) {
        afterData(getThisName(thisJoinPoint).getName(), getTargetName(thisJoinPoint).getName(), thisJoinPoint.getSignature(), o);
    }

    public void afterData(String source, String target, Signature signature, Object... returnValue) {
        if(source != null && target != null) {
            String seqData = target + " -> " + source + " : return" + "(" + Arrays.deepToString(returnValue) + ")";
            seqDataList.add(new SeqData(SeqType.EXIT, source, target, signature.getName(), returnValue));
            System.out.println(seqData);
        }
    }

    @AfterThrowing(pointcut = "within(app.*) && call(* app.*.*(..))", throwing = "t")
    public void afterThrowing(final JoinPoint thisJoinPoint, Throwable t) {
        afterThrowingData(getThisName(thisJoinPoint).getName(), getTargetName(thisJoinPoint).getName(), thisJoinPoint.getSignature(), t);
    }

    public void afterThrowingData(String source, String target, Signature signature, Throwable throwable) {
        if(source != null && target != null) {
            String seqData = target + " -> " + source + " : throws" + "(" + throwable + ")";
            seqDataList.add(new SeqData(SeqType.EXCEPTION, source, target, signature.getName(), throwable));
            System.out.println(seqData);
        }
    }

    public Class<? extends Object> getTargetName(JoinPoint thisJoinPoint) {
        if(thisJoinPoint == null || thisJoinPoint.getTarget() == null) return null;
        return thisJoinPoint.getTarget().getClass();
    }

    public Class<? extends Object> getThisName(JoinPoint thisJoinPoint) {
        if(thisJoinPoint == null || thisJoinPoint.getThis() == null) return null;
        return thisJoinPoint.getThis().getClass();
    }

    public static List<SeqData> getSeqDataList() {
        return seqDataList;
    }
}
