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
//    private static final double RHYME_SCORE_THRESHOLD = .6;
//
//    public Rhyme(int line, int wordsToPos) {
//        super(line,wordsToPos);
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
//        List<Syllable> tSyllables = getLastXSyllables(t, 1);
//        List<Syllable> sSyllables = getLastXSyllables(s, 1);
//
////        for(VowelPhoneme[] line1Phone:tPhones) {
////            for(VowelPhoneme[] line2Phone: sPhones) {
////                if (Rhymer.scoreRhymeByPatsRules(line1Phone, line2Phone) > RHYME_SCORE_THRESHOLD) {
////                    return true;
////                }
////            }
////        }
//
//        return false;
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
