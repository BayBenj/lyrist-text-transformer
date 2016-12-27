package filters;

import song.Word;
import java.util.Set;

public class RhymeFilter extends ModelWordFilter {

    public RhymeFilter(Word model) {
        super(model);
    }

    @Override
    public Set<Word> doFilter(Set<Word> w2vSuggestions) {
        return null;
    }

}


































































