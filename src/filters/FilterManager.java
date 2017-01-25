package filters;

import elements.Word;

public abstract class FilterManager {

    public static WordFilterEquation wEq(FilterObject... filterObjects) {
        WordFilterEquation eq = new WordFilterEquation();
        eq.add(getIntersection());
        int i = 0;
        for (FilterObject filterObject : filterObjects) {
            if (i == 1)
                eq.add(getUnion());
            eq.add(filterObject);
            i++;
        }
        return eq;
    }

    public static StringFilterEquation sEq(FilterObject... filterObjects) {
        StringFilterEquation eq = new StringFilterEquation();
        eq.add(getIntersection());
        int i = 0;
        for (FilterObject filterObject : filterObjects) {
            if (i == 1)
                eq.add(getUnion());
            eq.add(filterObject);
            i++;
        }
        return eq;
    }

    public static FilterObject[] getMirrorPosNeFilters(Word oldWord) {
        FilterObject[] filters = new FilterObject[3];
        filters[0] = (getPosWordFilter(oldWord));
        filters[1] = (getNeWordFilter(oldWord));
        return filters;
    }

    public static FilterObject[] getMirrorRhymeFilters(Word oldWord) {
        FilterObject[] filters = new FilterObject[2];
        filters[0] = (getPerfectRhymeFilter(oldWord));
        filters[1] = (getSameNumSyllableFilter(oldWord));
        return filters;
    }

    public static FilterObject[] getSafetyStringFilters() {
        FilterObject[] filters = new FilterObject[4];
        filters[0] = getDistatefulnessFilter();
        filters[1] = getBadStringFilter();
        filters[2] = getCommonStringFilter();
        filters[3] = getDistatefulnessFilter();
        return filters;
    }

    public static FilterObject[] getTaggingSafetyFilters() {
        FilterObject[] filters = new FilterObject[2];
        filters[0] = getUnsafePosForPosTaggingFilter();
        filters[1] = getUnsafeWordFilter();
        return filters;
    }

    public static FilterObject getPosWordFilter(Word oldWord) {
        return new PosMatchFilter(ReturnType.NON_MATCHES, oldWord);
    }

    public static FilterObject getNeWordFilter(Word oldWord) {
        return new NeMatchFilter(ReturnType.NON_MATCHES, oldWord);
    }

    public static FilterObject getDistatefulnessFilter() {
        return new DistastefulnessFilter(ReturnType.MATCHES);
    }

    public static FilterObject getBadStringFilter() {
        return new BadStringFilter(ReturnType.MATCHES);
    }

    public static FilterObject getUnsafeWordFilter() {
        return new UnsafeWordFilter(ReturnType.MATCHES);
    }

    public static FilterObject getCommonStringFilter() {
        return new CommonStringFilter(ReturnType.NON_MATCHES);
    }

    public static FilterObject getSameNumSyllableFilter(Word oldWord) {
        return new SameNumSyllableFilter(ReturnType.NON_MATCHES, oldWord);
    }

    public static FilterObject getPerfectRhymeFilter(Word oldWord) {
        return new PerfectRhymeFilter(ReturnType.NON_MATCHES, oldWord);
    }

    public static FilterObject getFirstLetterFilter(CharList oldString) {
        return new FirstLetterFilter(ReturnType.NON_MATCHES, oldString);
    }

    public static FilterObject getUnsafePosForPosTaggingFilter() {
        return new UnsafePosForPosTaggingFilter(ReturnType.MATCHES);
    }

    public static FilterObject getUnion() {
        return new FilterUNION();
    }

    public static FilterObject getIntersection() {
        return new FilterINTERSECTION();
    }

}
























































































