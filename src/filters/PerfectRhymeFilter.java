package filters;

import rhyme.Phoneme;
import rhyme.Pronunciation;
import rhyme.StressedPhoneme;
import song.Word;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PerfectRhymeFilter extends ModelWordFilter {

    public PerfectRhymeFilter(Word model) {
        super(model);
    }

    public PerfectRhymeFilter(Direction direction, Word model) {
        super(direction, model);
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
                    if (    super.getDirection() == Direction.INCLUDE_MATCH && modelRhyme.equals(wordRhyme) ||
                            super.getDirection() == Direction.EXCLUDE_MATCH && !modelRhyme.equals(wordRhyme) )
                    result.add(w);
                }
                //Removes words with no syllables
                else if (super.getDirection() == Direction.EXCLUDE_MATCH) {
                    result.add(w);
                }
            }
            return result;
        }
        if (super.getDirection() == Direction.EXCLUDE_MATCH)
            return originalWords;
        else
            return new HashSet<>();
    }
}

















































































