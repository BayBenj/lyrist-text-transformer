package filters;

import elements.Pos;
import elements.Word;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UnsafePosForPosTaggingFilter extends WordFilter {
//TODO change this to extend EnumVocabFilter, similar to VocabFilters but holds a list of enums


    private final Pos[] unsafePosArray = new Pos[] {
            Pos.TO,
            Pos.IN,
            Pos.PRP,
            Pos.WRB,
            Pos.CC,
            Pos.DT,
            Pos.EX,
            Pos.PRP$,
            Pos.FW,
            Pos.PUNCTUATION,
            Pos.UNKNOWN
    };
    private final Set<Pos> unsafePosSet = new HashSet<>(Arrays.asList(unsafePosArray));

    public UnsafePosForPosTaggingFilter() {}

    public UnsafePosForPosTaggingFilter(ReturnType returnType) {
        super(returnType);
    }

    @Override
    public Set<Word> doFilter(Collection<Word> w2vSuggestions) {
        Set<Word> filteredIn = new HashSet<>();
        for (Word w : w2vSuggestions) {
            if (super.getReturnType() == ReturnType.MATCHES && unsafePosSet.contains(w.getPos()) ||
                    super.getReturnType() == ReturnType.NON_MATCHES && !unsafePosSet.contains(w.getPos()))
                filteredIn.add(w);
        }
        return filteredIn;
    }
}































































































