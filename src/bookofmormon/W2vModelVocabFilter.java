package bookofmormon;

import filters.Direction;
import filters.FilterUtils;
import filters.VocabListFilter;
import song.VocabList;

import java.util.*;

public class W2vModelVocabFilter extends VocabListFilter {

    public W2vModelVocabFilter() {
        super(FilterUtils.getModelWords());
    }

    public W2vModelVocabFilter(Direction direction) {
        super(direction, FilterUtils.getModelWords());
    }

    @Override
    public Set<String> doFilter(Set<String> originalStrings) {
        Set<String> result = new HashSet<>();
        for (String s : originalStrings) {
            if (super.getDirection() == Direction.INCLUDE_MATCH && super.vocabList.contains(s.toLowerCase()) ||
                    super.getDirection() == Direction.EXCLUDE_MATCH && !super.vocabList.contains(s.toLowerCase()))
                result.add(s);
        }
        return result;
    }

}














































































