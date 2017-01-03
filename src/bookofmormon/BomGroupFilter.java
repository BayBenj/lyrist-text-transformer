package bookofmormon;

import filters.Direction;
import filters.VocabListFilter;
import song.VocabList;

import java.util.*;

public class BomGroupFilter extends VocabListFilter {

    private static String[] list = {
            "amalekite",
            "amalickiahite",
            "amlicite",
            "ammonihahite",
            "ammonite",
            "amulonite",
            "anti-nephi-lehite",
            "babylonian",
            "gadianton robber",
            "gentile",
            "ishmaelite",
            "israelite",
            "jacobite",
            "jaredite",
            "jew",
            "josephite",
            "lamanite",
            "lehite",
            "lemuelite",
            "mulekite",
            "nephite",
            "stripling warrior",
            "zoramite"
    };
    private static Set set = new HashSet(Arrays.asList(list));

    public BomGroupFilter() {
        super(new VocabList(set, "book of mormon group"));
    }

    public BomGroupFilter(Direction direction) {
        super(direction, new VocabList(set, "book of mormon group"));
    }

    @Override
    public Set<String> doFilter(Set<String> originalStrings) {
        Set<String> result = new HashSet<>();
        for (String s : originalStrings) {
            if (super.getDirection() == Direction.INCLUDE_MATCH && super.vocabList.contains(s.toLowerCase()) ||
                    super.getDirection() == Direction.EXCLUDE_MATCH && !super.vocabList.contains(s.toLowerCase())) {
                result.add(s.substring(0, 1).toUpperCase() + s.substring(1));
            }
        }
        return result;
    }

    public static String[] getList() {
        return list;
    }
}














































































