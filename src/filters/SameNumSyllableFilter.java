//package filters;
//
//import elements.Word;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//public class SameNumSyllableFilter extends ModelWordFilter {
//
//    public SameNumSyllableFilter(Word model) {
//        super(model);
//    }
//
//    public SameNumSyllableFilter(ReturnType returnType, Word model) {
//        super(returnType, model);
//    }
//
//    @Override
//    public Set<Word> doFilter(Collection<Word> originalWords) {
//        if (this.getModel().getPronunciations() != null && !this.getModel().getPronunciations().isEmpty()) {
//            Set<Word> result = new HashSet<>();
//            for (Word w : originalWords) {
//                if (    (w.getPronunciations() != null && !w.getPronunciations().isEmpty()) &&
//                        (super.getReturnType() == ReturnType.MATCHES && this.getModel().getPronunciations().size() == w.getPronunciations().size() ||
//                        super.getReturnType() == ReturnType.NON_MATCHES && this.getModel().getPronunciations().size() != w.getPronunciations().size()) )
//                    result.add(w);
//            }
//            return result;
//        }
//        return null;
//    }
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
