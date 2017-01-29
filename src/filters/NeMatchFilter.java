//package filters;
//
//import elements.Ne;
//import elements.Word;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//public class NeMatchFilter extends ModelWordFilter {
//
//    public NeMatchFilter(Word model) {
//        super(model);
//    }
//
//    public NeMatchFilter(ReturnType returnType, Word model) {
//        super(returnType, model);
//    }
//
//    //    private Word neWord;
////
////    public FiltrationResults filterWords(HashSet<W2vWordSuggestion> w2vWordSuggestions, Word neWord) {
////        this.setNeWord(neWord);
////        return this.filterWords(w2vWordSuggestions);
////    }
////
////    public HashSet<Word> filterWords(HashSet<Word> w2vSuggestions, Word neWord, boolean b) {
////        this.setNeWord(neWord);
////        return this.filterWords(w2vSuggestions, b);
////    }
////
////    @Override
////    public FiltrationResults filterWords(Set<W2vWordSuggestion> w2vWordSuggestions) {
////        HashSet<W2vWordSuggestion> filteredIn = new HashSet<W2vWordSuggestion>();
////        HashSet<W2vWordSuggestion> filteredOut = new HashSet<W2vWordSuggestion>();
////        Ne filterNe = neWord.getNe();
////        for (W2vWordSuggestion wordSuggestion : w2vWordSuggestions) {
////            W2vWordSuggestion temp = new W2vWordSuggestion(wordSuggestion.getWord(), wordSuggestion.getCosineDistance());
////            if (filterNe == wordSuggestion.getWord().getNe()
//////                    &&
//////                    (wordsToPos.equals(Pos.NN) ||
//////                    wordsToPos.equals(Pos.NNS) ||
//////                    wordsToPos.equals(Pos.VB) ||
//////                    wordsToPos.equals(Pos.VBG) ||
//////                    wordsToPos.equals(Pos.JJ) ||
//////                    wordsToPos.equals(Pos.RB))
////                    )  //TODO: remove this when I'm better at Pos
////                filteredIn.add(temp);
////            else
////                filteredOut.add(temp);
////        }
////        return new FiltrationResults(filteredIn, filteredOut);
////    }
//
//
////    public Word getNeWord() {
////        return neWord;
////    }
////
////    private void setNeWord(Word neWord) {
////        this.neWord = neWord;
////    }
//
//    @Override
//    public Set doFilter(Collection<Word> w2vSuggestions) {
//        Set<Word> filteredIn = new HashSet<>();
//        Ne modelEnum = super.getModel().getNe();
//        for (Word w : w2vSuggestions) {
//            if (super.getReturnType() == ReturnType.MATCHES && modelEnum == w.getNe() ||
//                    super.getReturnType() == ReturnType.NON_MATCHES && modelEnum != w.getNe())
//                filteredIn.add(w);
//        }
//        return filteredIn;
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
//
//
//
//
//
//
//
//
