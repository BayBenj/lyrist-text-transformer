package bookofmormon;

import filters.ReturnType;
import filters.VocabListFilter;
import filters.VocabList;

import java.util.*;

public class BomGroupsFilter extends VocabListFilter {

    private static String[] list = {
            "amalekites",
            "amalickiahites",
            "amlicites",
            "ammonihahites",
            "ammonites",
            "amulonites",
            "anti-nephi-lehies",
            "babylonians",
            "gadianton robbers",
            "gentiles",
            "ishmaelites",
            "israelites",
            "jacobites",
            "jaredites",
            "jews",
            "josephites",
            "lamanites",
            "lehites",
            "lemuelites",
            "mulekites",
            "nephites",
            "stripling warriors",
            "zoramites"
    };
    private static Set set = new HashSet(Arrays.asList(list));

    public BomGroupsFilter() {
        super(new VocabList(set, "book of mormon groups"));
    }

    public BomGroupsFilter(ReturnType returnType) {
        super(returnType, new VocabList(set, "book of mormon groups"));
    }

    @Override
    public Set<String> doFilter(Collection<String> originalStrings) {
        Set<String> result = new HashSet<>();
        for (String s : originalStrings) {
            if (super.getReturnType() == ReturnType.MATCHES && super.vocabList.contains(s.toLowerCase()) ||
                    super.getReturnType() == ReturnType.NON_MATCHES && !super.vocabList.contains(s.toLowerCase()))
                result.add(s.substring(0, 1).toUpperCase() + s.substring(1));
        }
        return result;
    }

    public static String[] getList() {
        return list;
    }
}















































































