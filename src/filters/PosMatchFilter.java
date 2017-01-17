package filters;

import song.Pos;
import song.Word;
import java.util.HashSet;
import java.util.Set;

public class PosMatchFilter extends ModelWordFilter {

    public PosMatchFilter(Word model) {
        super(model);
    }

    public PosMatchFilter(ReturnType returnType, Word model) {
        super(returnType, model);
    }


//    private Word posWord;
//
//    public FiltrationResults filterWords(HashSet<W2vWordSuggestion> w2vWordSuggestions, Word posWord) {
//        this.setPosWord(posWord);
//        return this.filterWords(w2vWordSuggestions);
//    }
//
//    public HashSet<Word> filterWords(HashSet<Word> w2vSuggestions, Word posWord, boolean b) {
//        this.setPosWord(posWord);
//        return this.filterWords(w2vSuggestions, b);
//    }
//
//    @Override
//    public FiltrationResults filterWords(Set<W2vWordSuggestion> w2vWordSuggestions) {
//        HashSet<W2vWordSuggestion> filteredIn = new HashSet<W2vWordSuggestion>();
//        HashSet<W2vWordSuggestion> filteredOut = new HashSet<W2vWordSuggestion>();
//        Pos pos = posWord.getPos();
//        for (W2vWordSuggestion wordSuggestion : w2vWordSuggestions) {
//            W2vWordSuggestion temp = new W2vWordSuggestion(wordSuggestion.getWord(), wordSuggestion.getCosineDistance());
//            if (pos == wordSuggestion.getWord().getPos())  //TODO: modify this when I'm better at Pos
//                filteredIn.add(temp);
//            else
//                filteredOut.add(temp);
//        }
//        return new FiltrationResults(filteredIn, filteredOut);
//    }

    @Override
    public Set<Word> doFilter(Set<Word> w2vSuggestions) {
        Set<Word> filteredIn = new HashSet<>();
        Pos pos = super.getModel().getPos();
        for (Word w : w2vSuggestions) {
            if (super.getReturnType() == ReturnType.MATCHES && pos == w.getPos() ||
                    super.getReturnType() == ReturnType.NON_MATCHES && pos != w.getPos())
                filteredIn.add(w);
        }
        return filteredIn;
    }

//    public Word getPosWord() {
//        return posWord;
//    }
//
//    private void setPosWord(Word posWord) {
//        this.posWord = posWord;
//    }

}



















































































