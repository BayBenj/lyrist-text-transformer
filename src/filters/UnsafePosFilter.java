package filters;

import song.Pos;
import song.Word;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UnsafePosFilter extends WordFilter {
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
            Pos.UNKNOWN
    };
    private final Set<Pos> unsafePosSet = new HashSet<>(Arrays.asList(unsafePosArray));

    public UnsafePosFilter() {}

    public UnsafePosFilter(Direction direction) {
        super(direction);
    }

    @Override
    public Set<Word> doFilter(Set<Word> w2vSuggestions) {
        Set<Word> filteredIn = new HashSet<Word>();
        for (Word w : w2vSuggestions) {
            if (super.getDirection() == Direction.INCLUDE_MATCH && unsafePosSet.contains(w.getPos()) ||
                    super.getDirection() == Direction.EXCLUDE_MATCH && !unsafePosSet.contains(w.getPos()))
                filteredIn.add(w);
        }
        return filteredIn;
    }
}


























































































