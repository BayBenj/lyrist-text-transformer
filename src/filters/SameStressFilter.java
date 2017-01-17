package filters;

import song.Word;

import java.util.HashSet;
import java.util.Set;

public class SameStressFilter extends ModelWordFilter {

    public SameStressFilter(Word model) {
        super(model);
    }

    public SameStressFilter(ReturnType returnType, Word model) {
        super(returnType, model);
    }

    @Override
    public Set<Word> doFilter(Set<Word> originalWords) {
        Set<Word> result = new HashSet<>();
        for (Word w : originalWords) {
            if (super.getReturnType() == ReturnType.MATCHES && this.getModel().getStresses().equals(w.getStresses()) ||
                    super.getReturnType() == ReturnType.NON_MATCHES && !this.getModel().getStresses().equals(w.getStresses()))
                result.add(w);
        }
        return result;
    }
}









































