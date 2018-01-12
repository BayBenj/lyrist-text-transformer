package songtools;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import elements.Word;
import intentions.SourceEnum;
import rhyme.NoRhymeFoundException;
import rhyme.Rhymer;
import rhyme.SyllableGroup;
import utils.U;
import word2vec.BadW2vInputException;
import word2vec.W2vInterface;

import java.util.*;

public class WordSource {

    private SourceEnum source;

    public WordSource(SourceEnum source) {
        this.source = source;
    }

    public static Map<Double, String> w2vAnalogy(W2vInterface w2v, String oldTheme, String newTheme, String oldWord, int nOfSuggestions) throws BadW2vInputException {
        Map<Double, String> stringSuggestionMap = w2v.findAnalogy(
                oldTheme.toLowerCase(),
                newTheme.toLowerCase(),
                oldWord.toLowerCase(),
                nOfSuggestions);
        Map<Double, String> noBadStrings = filterBadStrings(stringSuggestionMap);
        U.testPrintln("Found " + noBadStrings.size() + " analogous suggestions\n");
        return noBadStrings;
    }

    public static Map<Double, String> w2vSimilar(W2vInterface w2v, String oldWord, int nOfSuggestions) throws BadW2vInputException {
        Map<Double, String> stringSuggestionMap = w2v.findSimilars(
                oldWord.toLowerCase(),
                nOfSuggestions);
        Map<Double, String> noBadStrings = filterBadStrings(stringSuggestionMap);
        U.testPrintln("Found " + noBadStrings.size() + " similar suggestions\n");
        return noBadStrings;
    }

    public static Map<Double, String> filterBadStrings(Map<Double, String> stringSuggestionMap) {
        Map<Double, String> noBadStrings = new TreeMap<>(stringSuggestionMap);
        for (Map.Entry<Double, String> entry : stringSuggestionMap.entrySet()) {
            if (!entry.getValue().matches("\\w+") && noBadStrings.containsKey(entry.getKey())) {
                noBadStrings.remove(entry.getKey());//TODO maybe removes all non \w characters instead
            }
        }
        return noBadStrings;
    }


//    public static Map<Double, String> perfectCmuRhymes(String oldWord) {
//        //Adds all this word's perfect rhymes to the stringSuggestionMap. TODO use the actual distance from the rhyming word to the point of analogy.
//        Set<String> allRhymes = Rhymer.getPerfectRhymes(oldWord.toUpperCase(), 1);
//        Map<Double, String> cmuRhymes = new HashMap<>();
//        double extra = 0.0001;
//        for (String rhyme : allRhymes) {
//            cmuRhymes.put(1.0 + extra, rhyme.toLowerCase());
//            extra += 0.0001;
//        }
//        return cmuRhymes;
//    }

    public static Map<Double, String> imperfectCmuRhymes(SyllableGroup rhymeModel, int limit, int thresh) throws NoRhymeFoundException {
        //Adds all this word's perfect rhymes to the stringSuggestionMap. TODO use the actual distance from the rhyming word to the point of analogy.
        Set<String> allRhymes = new HashSet<>();
        for (Set<String> set : Rhymer.getAllRhymesByThreshold(rhymeModel, thresh).values()) {
            allRhymes.addAll(set);
        }

        Map<Double, String> cmuRhymes = new HashMap<>();
        //TODO: fix this, find actual distance
        double extra = 0.0001;
        for (String rhyme : allRhymes) {
            cmuRhymes.put(-1.0 - extra, rhyme.toLowerCase());
            extra += 0.0001;
            if (cmuRhymes.size() > limit)
                return cmuRhymes;
        }
        return cmuRhymes;
    }

    public static Map<Double, String> perfectCmuRhymes(SyllableGroup rhymeModel, int limit) throws NoRhymeFoundException {
        //Adds all this word's perfect rhymes to the stringSuggestionMap. TODO use the actual distance from the rhyming word to the point of analogy.
//        Set<String> allRhymes = new HashSet<>(Rhymer.perfectRhymes.get(rhymeModel));
        Set<String> allRhymes = new HashSet<>();
        for (Set<String> set : Rhymer.getAllRhymesByThreshold(rhymeModel, 1.0).values()) {
            allRhymes.addAll(set);
        }
        Map<Double, String> cmuRhymes = new HashMap<>();
        //TODO: fix this, find actual distance
        double extra = 0.00001;
        for (String rhyme : allRhymes) {
            cmuRhymes.put(-1.0 - extra, rhyme.toLowerCase());
            extra += 0.00001;
            if (cmuRhymes.size() > limit)
                return cmuRhymes;
        }
        return cmuRhymes;
    }

//    public static boolean thingToThing(Object o1, Object o2) {
//        if (o1.getClass() == String.class && o2.getClass() == Word.class ||
//                o2.getClass() == String.class && o1.getClass() == Word.class) {
//            return true;
//        }
//
//    }

}
































































