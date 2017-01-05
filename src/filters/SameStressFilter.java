package filters;

import song.Word;

import java.util.HashSet;
import java.util.Set;

public class SameStressFilter extends ModelWordFilter {

    public SameStressFilter(Word model) {
        super(model);
    }

    public SameStressFilter(Direction direction, Word model) {
        super(direction, model);
    }

    @Override
    public Set<Word> doFilter(Set<Word> originalWords) {
        Set<Word> result = new HashSet<>();
        for (Word w : originalWords) {
            if (super.getDirection() == Direction.INCLUDE_MATCH && this.getModel().getStresses().equals(w.getStresses()) ||
                    super.getDirection() == Direction.EXCLUDE_MATCH && !this.getModel().getStresses().equals(w.getStresses()))
                result.add(w);
        }
        return result;
    }
}









































