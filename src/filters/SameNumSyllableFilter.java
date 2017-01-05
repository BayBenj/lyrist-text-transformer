package filters;

import song.Word;

import java.util.HashSet;
import java.util.Set;

public class SameNumSyllableFilter extends ModelWordFilter {

    public SameNumSyllableFilter(Word model) {
        super(model);
    }

    public SameNumSyllableFilter(Direction direction, Word model) {
        super(direction, model);
    }

    @Override
    public Set<Word> doFilter(Set<Word> originalWords) {
        if (this.getModel().getSyllables() != null && !this.getModel().getSyllables().isEmpty()) {
            Set<Word> result = new HashSet<>();
            for (Word w : originalWords) {
                if (    (w.getSyllables() != null && !w.getSyllables().isEmpty()) &&
                        (super.getDirection() == Direction.INCLUDE_MATCH && this.getModel().getSyllables().size() == w.getSyllables().size() ||
                        super.getDirection() == Direction.EXCLUDE_MATCH && this.getModel().getSyllables().size() != w.getSyllables().size()) )
                    result.add(w);
            }
            return result;
        }
        return null;
    }
}





































