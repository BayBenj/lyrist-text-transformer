package filters;

import java.util.HashSet;
import java.util.Set;

public class CommonStringFilter extends VocabListFilter {

    public CommonStringFilter() {
        super(FilterUtils.getCommonWords());
    }

    public CommonStringFilter(ReturnType returnType) {
        super(returnType, FilterUtils.getCommonWords());
    }

    @Override
    public Set<String> doFilter(Set<String> originalStrings) {
        Set<String> result = new HashSet<>();
        for (String s : originalStrings) {
            if (super.getReturnType() == ReturnType.MATCHES && super.vocabList.contains(s) ||
                    super.getReturnType() == ReturnType.NON_MATCHES && !super.vocabList.contains(s))
                result.add(s);
        }
        return result;
    }
}


























































