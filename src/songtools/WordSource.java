package songtools;

import elements.Word;
import intentions.SourceEnum;
import rhyme.Rhymer;
import utils.U;
import word2vec.W2vCommander;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WordSource {

    private SourceEnum source;

    public WordSource(SourceEnum source) {
        this.source = source;
    }

    public static Map<Double, String> w2vAnalogy(W2vCommander w2v, String oldTheme, String newTheme, String oldWord, int nOfSuggestions) {
        Map<Double, String> stringSuggestionMap = w2v.findAnalogy(
                oldTheme.toLowerCase(),
                newTheme.toLowerCase(),
                oldWord.toLowerCase(),
                nOfSuggestions);
        U.testPrintln("After word2vec operation: " + stringSuggestionMap.size() + " suggestions");
        return stringSuggestionMap;
    }

    public static Map<Double, String> cmuRhymes(String oldWord) {
        //Adds all this word's perfect rhymes to the stringSuggestionMap. TODO use the actual distance from the rhyming word to the point of analogy.
        Set<String> allRhymes = Rhymer.getPerfectRhymes(oldWord.toUpperCase(), 1);
        Map<Double, String> cmuRhymes = new HashMap<>();
        double extra = 0.0001;
        for (String rhyme : allRhymes) {
            cmuRhymes.put(1.0 + extra, rhyme.toLowerCase());
            extra += 0.0001;
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




























