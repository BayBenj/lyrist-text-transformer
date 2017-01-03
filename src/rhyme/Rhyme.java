//package rhyme;
//
//import java.util.List;
//import condition.DelayedConstraintCondition;
//
//public class Rhyme<T> extends DelayedConstraintCondition<String> {
//
//    /**
//     *
//     */
//    private static final long serialVersionUID = 1L;
//    private static final double MATCHING_LINE_THRESHOLD = .6;
//
//    public Rhyme(int line, int pos) {
//        super(line,pos);
//    }
//
//    public boolean isSatisfiedBy(String t) {
//        return rhyme(t,prevT);
//    }
//
//    private boolean rhyme(String t, String s) {
//        if (t.equals(s)) {
//            return false;
//        }
//        List<StressedPhone[]> tPhones = Phoneticizer.getPhonesForXLastSyllables(t.toString(), 1);
//        List<StressedPhone[]> sPhones = Phoneticizer.getPhonesForXLastSyllables(s.toString(), 1);
//
//        for(StressedPhone[] line1Phone:tPhones) {
//            for(StressedPhone[] line2Phone: sPhones) {
//                if (RhymeStructureAnalyzer.scoreRhymeByPatsRules(line1Phone, line2Phone) > MATCHING_LINE_THRESHOLD) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }
//}
