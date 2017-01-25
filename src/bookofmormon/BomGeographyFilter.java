package bookofmormon;

import filters.ReturnType;
import filters.VocabListFilter;
import filters.VocabList;

import java.util.*;

public class BomGeographyFilter extends VocabListFilter {

    private static String[] list = {
            "city",
            "plains",
            "wilderness",
            "hill",
            "land",
            "mount",
            "garden",
            "waters",
            "place",
            "forest",
            "river",
            "valley"

    };
    private static Set set = new HashSet(Arrays.asList(list));

    public BomGeographyFilter() {
        super(new VocabList(set, "book of mormon geography"));
    }

    public BomGeographyFilter(ReturnType returnType) {
        super(returnType, new VocabList(set, "book of mormon geography"));
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

    public static String[] getList() {
        return list;
    }
}















































































