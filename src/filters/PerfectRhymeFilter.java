package filters;

import rhyme.Phoneme;
import song.Word;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PerfectRhymeFilter extends ModelWordFilter {

    public PerfectRhymeFilter(Word model) {
        super(model);
    }

    public PerfectRhymeFilter(ReturnType returnType, Word model) {
        super(returnType, model);
    }

    @Override
    public Set<Word> doFilter(Set<Word> originalWords) {
        if (this.getModel().getSyllables() != null && !this.getModel().getSyllables().isEmpty()) {
            int nModelSyl = this.getModel().getSyllables().size();
            Set<Word> result = new HashSet<>();
            for (Word w : originalWords) {
                if (w.getSyllables() != null && !w.getSyllables().isEmpty()) {
                    int nWordSyl = w.getSyllables().size();
                    List<Phoneme> modelRhyme = this.getModel().getSyllables().get(nModelSyl - 1).getRhyme();
                    List<Phoneme> wordRhyme = w.getSyllables().get(nWordSyl - 1).getRhyme();
                    if (    super.getReturnType() == ReturnType.MATCHES && modelRhyme.equals(wordRhyme) ||
                            super.getReturnType() == ReturnType.NON_MATCHES && !modelRhyme.equals(wordRhyme) )
                    result.add(w);
                }
                //Removes words with no syllables
                else if (super.getReturnType() == ReturnType.NON_MATCHES) {
                    result.add(w);
                }
            }
            return result;
        }
        if (super.getReturnType() == ReturnType.NON_MATCHES)
            return originalWords;
        else
            return new HashSet<>();
    }
}

















































































