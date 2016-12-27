package filters;

import song.Word;
import java.util.*;

public class StringFilterEquation extends FilterEquation {

    public Set<String> run() {
        return this.run(new HashSet<String>());
    }

        public Set<String> run(Set<String> unfilteredStrings) {
        Set<String> currentStrings = new HashSet<>(unfilteredStrings);
        for (FilterObject filterObject : this) {
            if (filterObject instanceof Filter) {
                StringFilter stringFilter = (StringFilter) filterObject;
                switch (super.currentOperator) {
                    case INTERSECTION:
                        currentStrings.retainAll(stringFilter.doFilter(unfilteredStrings));
                        break;
                    case UNION:
                        currentStrings.addAll(stringFilter.doFilter(unfilteredStrings));
                        break;
                }
            }
            else if (filterObject instanceof FilterOperator) {
                super.currentOperator = super.getOperatorEnum((FilterOperator)filterObject);
            }
        }
        return currentStrings;
    }


//    public FilterEquation(FilterObject... filterObjects) {
//        this.setFilterEquation(filterObjects);
//    }

//    //TODO > Find better way than passing in a class for this method
//    public void setFilterEquation(List<FilterObject> filterObjects) {
//        for (FilterObject filterObject : filterObjects)
//            this.add(filterObject);
//    }
//
//    //TODO > Find better way than passing in a class for this method
//    public void setFilterEquation(FilterObject... filterObjects) {
//        for (FilterObject filterObject : filterObjects)
//            this.add(filterObject);
////        Set<Filter> filters = new HashSet<>();
////        for (FilterObject filterObject : filterObjects) {
////            if (filterObject instanceof FilterOperator) {
////
////            }
////            else if (filterObject instanceof Filter) {
////
////            }
//
//
//
//
////            if (filterObject instanceof DictionaryFilter)
////                    filters.add(new DictionaryFilter());
////            else if (filterObject instanceof FrequencyFilter)
////                    filters.add(new FrequencyFilter());
////            else if (filterObject instanceof BallparkFilter)
////                    filters.add(new BallparkFilter());
////            else if (filterObject instanceof PosMatchFilter)
////                    filters.add(new PosMatchFilter());
////            else if (filterObject instanceof DistastefulnessFilter)
////                    filters.add(new DistastefulnessFilter());
////            else if (filterObject instanceof RhymeFilter)
////                    filters.add(new RhymeFilter());
////            else if (filterObject instanceof BadStringFilter)
////                    filters.add(new BadStringFilter());
////            else if (filterObject instanceof NeMatchFilter)
////                    filters.add(new NeMatchFilter());
////            else if (filterObject instanceof UnsafePosFilter)
////                    filters.add(new UnsafePosFilter());
////            else {
////                System.out.println("ERROR, DIDN'T RECOGNIZE FILTER NAME!");
////                //TODO throw Filter not found Exception
////            }
////        }
////        this.setFilterObjects(filters);
//    }

//    public FiltrationResults filterWordSuggestions(Set<W2vWordSuggestion> unfilteredWords) {
//        Utils.testPrintln("Entering filterWordSuggestions");
//        ArrayList<HashSet<W2vWordSuggestion>> filteredInMaps = new ArrayList<HashSet<W2vWordSuggestion>>();
//        ArrayList<HashSet<W2vWordSuggestion>> filteredOutMaps = new ArrayList<HashSet<W2vWordSuggestion>>();
//        for (WordFilter wordFilter : this.filterObjects) {
//            FiltrationResults filtrationResults = wordFilter.filter(unfilteredWords);
//            filteredInMaps.add(filtrationResults.getFilteredIn());
//            filteredOutMaps.add(filtrationResults.getFilteredOut());
//        }
//        HashSet<W2vWordSuggestion> intersectionIn = new HashSet<W2vWordSuggestion>();
//        HashSet<W2vWordSuggestion> intersectionOut = new HashSet<W2vWordSuggestion>();
//        intersectionIn.retainAll(filteredInMaps);
//        intersectionOut.retainAll(filteredOutMaps);
//        return new FiltrationResults(intersectionIn, intersectionOut);
//        //TODO: Make sure this map intersection works
//    }
//
//    public FiltrationResults filterWordSuggestionsWithModel(Set<W2vWordSuggestion> unfilteredWords, Word modelWord) {
//        Utils.testPrintln("Entering filterWordSuggestionsWithModel");
//        ArrayList<HashSet<W2vWordSuggestion>> filteredInSets = new ArrayList<HashSet<W2vWordSuggestion>>();
//        ArrayList<HashSet<W2vWordSuggestion>> filteredOutSets = new ArrayList<HashSet<W2vWordSuggestion>>();
//        for (WordFilter wordFilter : this.filterObjects) {
//            FiltrationResults filtrationResults;
//            //TODO FIX THIS AWFUL FILTRATION SYSTEM
//            if (wordFilter instanceof PosMatchFilter) {
//                PosMatchFilter filterWithModel = (PosMatchFilter)wordFilter;
//                filtrationResults =  filterWithModel.filter(unfilteredWords, modelWord);
//
//            }
//            else if (wordFilter instanceof NeMatchFilter) {
//                NeMatchFilter filterWithModel = (NeMatchFilter)wordFilter;
//                filtrationResults = filterWithModel.filter(unfilteredWords, modelWord);
//            }
//            else {
//                filtrationResults = wordFilter.filter(unfilteredWords);
//            }
//            filteredInSets.add(filtrationResults.getFilteredIn());
//            filteredOutSets.add(filtrationResults.getFilteredOut());
//        }
//        HashSet<W2vWordSuggestion> intersectionIn = filteredInSets.get(0);
//        HashSet<W2vWordSuggestion> unionOut = filteredOutSets.get(0);
//        for (int i = 1; i < this.filterObjects.size(); i++) {
//            intersectionIn.retainAll(filteredInSets.get(i));
//            unionOut.addAll(filteredOutSets.get(i));
//        }
//        return new FiltrationResults(intersectionIn, unionOut);
//        //TODO: Make sure this map intersection works
//    }


}




/*
Similar to villain
Original suggestions: bretch, baddie, terrible
POS filter: returns bretch, baddie
Dirty word filter: baddie, terrible

Intersect sets for baddie

Set<String> s1;
Set<String> s2;
Set<String> s3;
Set<String> intersection = new HashSet<String>();
ArrayList<Set<String>> individualFilteredSuggestions = new ArrayList<Set<String>>();
individualFilteredSuggestions.add(s1);
individualFilteredSuggestions.add(s2);
individualFilteredSuggestions.add(s3);

intersection.retainAll(individualFilteredSuggestions);
 */


/*
The way to store uncosined lyrics
Set:
    TaggedWord
 */


/*
The way to store cosined lyrics
Map:
    Key:    cosine distance
    Value:  TaggedWord
 */














































































































