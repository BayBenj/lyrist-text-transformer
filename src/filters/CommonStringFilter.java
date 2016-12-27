package filters;

import java.util.HashSet;
import java.util.Set;

public class CommonStringFilter extends VocabListFilter {

    public CommonStringFilter() {
        super(FilterUtils.getCommonWords());
    }

    public CommonStringFilter(Direction direction) {
        super(direction, FilterUtils.getCommonWords());
    }

    @Override
    public Set<String> doFilter(Set<String> originalStrings) {
        Set<String> result = new HashSet<>();
        for (String s : originalStrings) {
            if (super.getDirection() == Direction.INCLUDE_MATCH && super.vocabList.contains(s) ||
                    super.getDirection() == Direction.EXCLUDE_MATCH && !super.vocabList.contains(s))
                result.add(s);
        }
        return result;
    }
}


























































