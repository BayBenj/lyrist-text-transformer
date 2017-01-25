package bookofmormon;

import filters.ReturnType;
import filters.FilterUtils;
import filters.VocabListFilter;

import java.util.*;

public class BibleWordsFilter extends VocabListFilter {

    public BibleWordsFilter() {
        super(FilterUtils.getBibleWords());
    }

    public BibleWordsFilter(ReturnType returnType) {
        super(returnType, FilterUtils.getBibleWords());
    }

    @Override
    public Set<String> doFilter(Collection<String> originalStrings) {
        Set<String> result = new HashSet<>();
        for (String s : originalStrings) {
            if (super.getReturnType() == ReturnType.MATCHES && super.vocabList.contains(s.toLowerCase()) ||
                    super.getReturnType() == ReturnType.NON_MATCHES && !super.vocabList.contains(s.toLowerCase()))
                result.add(s);
        }
        return result;
    }

}















































































