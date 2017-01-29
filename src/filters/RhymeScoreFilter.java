//package filters;
//
//import elements.Word;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//public class RhymeScoreFilter extends ModelScoreFilter {
//
//    public RhymeScoreFilter(double score) {
//        super(score);
//    }
//
//    public RhymeScoreFilter(ReturnType returnType, double score) {
//        super(returnType, score);
//    }
//
//    @Override
//    public Set<Word> doFilter(Collection<Word> w2vSuggestions) {
//        Set<Word> result = new HashSet<>();
//        double score = super.getModel();
//        for (Word w : w2vSuggestions)
//            if (super.getReturnType() == ReturnType.MATCHES && score <= w.getRhymeScore() ||
//                    super.getReturnType() == ReturnType.NON_MATCHES && score > w.getRhymeScore())
//                result.add(w);
//        return result;
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
//
