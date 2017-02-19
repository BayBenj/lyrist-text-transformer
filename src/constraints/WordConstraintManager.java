package constraints;

import elements.Pos;
import filters.ReturnType;
import main.VocabManager;

import java.util.*;

public abstract class WordConstraintManager {

    //provides complete packages of constraints for replacement operations

    private static Set<String> commonWords = VocabManager.readIn("common-words.txt");
    private static Set<String> dirtyWords = VocabManager.readIn("dirty-words.txt");
    private static Set<String> unsafeWordsForMarking = VocabManager.readIn("unsafe-marking-words.txt");
    private static Set<String> alreadyUsedWords = new HashSet<>();
    private static Set<String> alreadyUsedBases = new HashSet<>();
    private static Set<String> cmuWords = VocabManager.readIn("cmu-vocab.txt");
    private static Set<Pos> goodPosForMarking = null;

    public static List<WordConstraint> getNormal() {
        /*
        1. not the original word
        2. not one of these restricted words
        3. must be one of these common words
        4. pos == instanceSpecific
        5. ne == instanceSpecific
        6. Highest cosine distance
        */
        List<WordConstraint> result = new ArrayList<>();
        result.add(new BaseConstraint(ReturnType.NON_MATCHES));//(not the same base as the old word) instance-specific, enforced
        result.get(0).enforce();
        result.add(new StringConstraint(alreadyUsedWords, ReturnType.NON_MATCHES));//(not the same as any other new word) enforced
        result.get(1).enforce();
        result.add(new BaseConstraint(alreadyUsedBases, ReturnType.NON_MATCHES));//(not the same as any other new word) enforced
        result.get(2).enforce();
        result.add(new StringConstraint(dirtyWords, ReturnType.NON_MATCHES));// enforced
        result.get(3).enforce();
        result.add(new StringConstraint(commonWords, ReturnType.MATCHES));
        result.add(new PosConstraint(ReturnType.MATCHES));//instance-specific
        result.add(new NeConstraint(ReturnType.MATCHES));//instance-specific
//        result.add(new RhymeSyllableNConstraint(ModelNum.EQUAL));//instance-specific
        result.add(new CosineDistanceConstraint(ModelNum.HIGHEST));
        return result;
    }

    public static List<WordConstraint> getNormalCmuMulti() {
        /*
        1. not the original word
        2. not one of these restricted words
        3. must be one of these common words
        4. pos == instanceSpecific
        5. ne == instanceSpecific
        6. Highest cosine distance
        */
        List<WordConstraint> result = new ArrayList<>();
        result.add(new BaseConstraint(ReturnType.NON_MATCHES));//(not the same base as the old word) instance-specific, enforced
        result.get(0).enforce();
        result.add(new StringConstraint(alreadyUsedWords, ReturnType.NON_MATCHES));//(not the same as any other new word) enforced
        result.get(1).enforce();
        result.add(new BaseConstraint(alreadyUsedBases, ReturnType.NON_MATCHES));//(not the same as any other new word) enforced
        result.get(2).enforce();
        result.add(new StringConstraint(cmuWords, ReturnType.MATCHES));//in the cmu rhyming dictionary, enforced
        result.get(3).enforce();
        result.add(new StringConstraint(dirtyWords, ReturnType.NON_MATCHES));// enforced
        result.get(4).enforce();
        result.add(new StringConstraint(commonWords, ReturnType.MATCHES));
        result.add(new PosConstraint(ReturnType.MATCHES));//instance-specific
        result.add(new NeConstraint(ReturnType.MATCHES));//instance-specific
//        result.add(new RhymeSyllableNConstraint(ModelNum.EQUAL));//instance-specific
        return result;
    }

    public static List<WordConstraint> getMarking() {

        goodPosForMarking = new HashSet<>();
        goodPosForMarking.add(Pos.NN);
        goodPosForMarking.add(Pos.NNS);
        goodPosForMarking.add(Pos.NNP);
        goodPosForMarking.add(Pos.NNPS);
        goodPosForMarking.add(Pos.JJ);
        goodPosForMarking.add(Pos.JJR);
        goodPosForMarking.add(Pos.JJS);
        goodPosForMarking.add(Pos.RB);
        goodPosForMarking.add(Pos.RBR);
        goodPosForMarking.add(Pos.RBS);
        goodPosForMarking.add(Pos.VB);
        goodPosForMarking.add(Pos.VBD);
        goodPosForMarking.add(Pos.VBG);
        goodPosForMarking.add(Pos.VBN);
        goodPosForMarking.add(Pos.VBP);
        goodPosForMarking.add(Pos.VBZ);

        /*
        1. not the original word
        2. not one of these restricted words
        3. must be one of these common words
        4. pos == instanceSpecific
        5. ne == instanceSpecific
        6. Highest cosine distance
        */
        List<WordConstraint> result = new ArrayList<>();
        result.add(new StringConstraint(unsafeWordsForMarking, ReturnType.NON_MATCHES));// enforced
        result.get(0).enforce();
        result.add(new PosConstraint(goodPosForMarking, ReturnType.MATCHES));// enforced
        result.get(1).enforce();
        return result;
    }

    public static List<WordConstraint> getNormalSameSyllables() {
        /*
        1. not one of these restricted words
        2. must be one of these common words
        3. pos == instanceSpecific
        4. ne == instanceSpecific
        5. Highest cosine distance
        */
        return null;
    }

    public static List<WordConstraint> getRhyme() {
        /*
        1. not one of these restricted words (should this be here or should rhyme-specific stuff only be here?)
        2. must be one of these common words
        3. rhyme dbl >= .75
        4. pos == instanceSpecific
        5. ne == instanceSpecific
        6. Highest rhyme dbl
        7. Highest cosine distance
        */

        List<WordConstraint> result = new ArrayList<>();
        result.add(new BaseConstraint(ReturnType.NON_MATCHES));//(not the same base as the old word) instance-specific, enforced, instance-specific
        result.get(0).enforce();
        result.add(new StringConstraint(alreadyUsedWords, ReturnType.NON_MATCHES));//(not the same as any other new word) enforced
        result.get(1).enforce();
        result.add(new BaseConstraint(alreadyUsedBases, ReturnType.NON_MATCHES));//(not the same as any other new word) enforced
        result.get(2).enforce();
        result.add(new StringConstraint(cmuWords, ReturnType.MATCHES));//in the cmu rhyming dictionary, enforced
        result.get(3).enforce();
        result.add(new StringConstraint(dirtyWords, ReturnType.NON_MATCHES));// enforced
        result.get(4).enforce();
        result.add(new StringConstraint(commonWords, ReturnType.MATCHES));
        result.add(new RhymeScoreConstraint(ModelNum.GREATER_OR_EQUAL, .95, ReturnType.MATCHES));// enforced
        result.get(6).enforce();
        result.add(new PosConstraint(ReturnType.MATCHES));//instance-specific
        result.add(new NeConstraint(ReturnType.MATCHES));//instance-specific
//        result.add(new RhymeSyllableNConstraint(ModelNum.EQUAL));//instance-specific
        result.add(new RhymeScoreConstraint(NonModelNum.HIGHEST));
        result.add(new CosineDistanceConstraint(NonModelNum.HIGHEST));
        return result;
    }

    public static List<WordConstraint> getRhymeSameSyllables() {
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

    public static Set<String> getCommonWords() {
        return commonWords;
    }

    public static void setCommonWords(HashSet<String> commonWords) {
        WordConstraintManager.commonWords = commonWords;
    }

    public static Set<String> getDirtyWords() {
        return dirtyWords;
    }

    public static void setDirtyWords(HashSet<String> dirtyWords) {
        WordConstraintManager.dirtyWords = dirtyWords;
    }

    public static Set<Pos> getGoodPosForMarking() {
        return goodPosForMarking;
    }

    public static void setGoodPosForMarking(HashSet<Pos> goodPosForMarking) {
        WordConstraintManager.goodPosForMarking = goodPosForMarking;
    }

}

























































