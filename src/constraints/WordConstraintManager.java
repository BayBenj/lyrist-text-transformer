package constraints;

import filters.ReturnType;

import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public abstract class WordConstraintManager {

    //provides complete packages of constraints for replacement operations

    private static HashSet<String> commonWords = null;
    private static HashSet<String> restrictedWords = null;

    public static Map<Integer,WordConstraint> getNormal() {
        /*
        1. not the original word
        2. not one of these restricted words
        3. must be one of these common words
        4. pos == instanceSpecific
        5. ne == instanceSpecific
        6. Highest cosine distance
        */
        Map<Integer,WordConstraint> result = new TreeMap<>();
        result.put(1, new StringConstraint(ReturnType.NON_MATCHES));//instance-specific, enforced
        result.get(1).enforce();
        result.put(2, new StringConstraint(restrictedWords, ReturnType.NON_MATCHES));// enforced
        result.get(2).enforce();
        result.put(3, new StringConstraint(commonWords, ReturnType.MATCHES));
        result.put(4, new PosConstraint(ReturnType.MATCHES));//instance-specific
        result.put(5, new NeConstraint(ReturnType.MATCHES));//instance-specific
        result.put(6, new CosineConstraint(NonModelNum.HIGHEST));
        return result;
    }

    public static Map<Integer,WordConstraint> getNormalSameSyllables() {
        /*
        1. not one of these restricted words
        2. must be one of these common words
        3. pos == instanceSpecific
        4. ne == instanceSpecific
        5. Highest cosine distance
        */
        return null;
    }

    public static Map<Integer,WordConstraint> getRhyme() {
        /*
        1. not one of these restricted words (should this be here or should rhyme-specific stuff only be here?)
        2. must be one of these common words
        3. rhyme dbl >= .75
        4. pos == instanceSpecific
        5. ne == instanceSpecific
        6. Highest rhyme dbl
        7. Highest cosine distance
        */

        Map<Integer,WordConstraint> result = new TreeMap<>();
        result.put(1, new StringConstraint(ReturnType.NON_MATCHES));//instance-specific, enforced
        result.get(1).enforce();
        result.put(2, new StringConstraint(restrictedWords, ReturnType.NON_MATCHES));// enforced
        result.get(2).enforce();
        result.put(3, new StringConstraint(commonWords, ReturnType.MATCHES));
        result.put(4, new RhymeScoreConstraint(ModelNum.GREATER_OR_EQUAL, .75, ReturnType.MATCHES));// enforced
        result.get(4).enforce();
        result.put(5, new PosConstraint(ReturnType.MATCHES));//instance-specific
        result.put(6, new NeConstraint(ReturnType.MATCHES));//instance-specific
        result.put(7, new RhymeScoreConstraint(NonModelNum.HIGHEST));
        result.put(8, new CosineConstraint(NonModelNum.HIGHEST));
        return result;
    }

    public static Map<Integer,WordConstraint> getRhymeSameSyllables() {
        /*
        1. not one of these restricted words
        2. must be one of these common words
        3. rhyme dbl >= .75
        4. pos == instanceSpecific
        5. ne == instanceSpecific
        3. n of syllables == instanceSpecific
        6. Highest rhyme dbl
        7. Highest cosine distance
        */
        return null;
    }



}





































































