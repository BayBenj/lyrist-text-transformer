package constraints;

import elements.Pos;
import filters.ReturnType;
import main.VocabManager;
import java.util.*;

public abstract class WordConstraintMaker {

	//provides complete packages of constraints for replacement operations

	//sets
	private static Set<String> commonWords = VocabManager.readIn("common-words.txt");
	private static Set<String> wikiWords = VocabManager.readIn("wiki-titles.txt");
	private static Set<String> dirtyWords = VocabManager.readIn("dirty-words.txt");
	private static Set<String> unsafeWordsForMarking = VocabManager.readIn("unsafe-marking-words.txt");
	private static Set<String> alreadyUsedWords = new HashSet<>();
	private static Set<String> alreadyUsedBases = new HashSet<>();
	private static Set<String> cmuWords = VocabManager.readIn("cmu-vocab.txt");
	private static Set<Pos> goodPosForMarking = null;

	//custom constraints
	private final static BaseConstraint differentBase = new BaseConstraint(ReturnType.NON_MATCHES);
	private final static StringConstraint differentSpelling = new StringConstraint(ReturnType.NON_MATCHES);
	private final static BaseConstraint unusedBase = new BaseConstraint(alreadyUsedBases, ReturnType.NON_MATCHES);
	private final static StringConstraint unusedSpelling = new StringConstraint(alreadyUsedWords, ReturnType.NON_MATCHES);
	private final static StringConstraint notInDirty = new StringConstraint(dirtyWords, ReturnType.NON_MATCHES);
	private final static PosConstraint samePos = new PosConstraint(ReturnType.MATCHES);
//    private final static NeConstraint sameNe = new NeConstraint(ReturnType.MATCHES);
	private final static StringConstraint inCommonDict = new StringConstraint(commonWords, ReturnType.MATCHES);
	private final static StringConstraint inWikiDict = new StringConstraint(wikiWords, ReturnType.MATCHES);
	private final static StringConstraint inCmuDict = new StringConstraint(cmuWords, ReturnType.MATCHES);
	private final static RhymeScoreConstraint highestRhymeScore = new RhymeScoreConstraint(NonModelNum.HIGHEST);
	private final static RhymeScoreConstraint reasonableRhymeScore = new RhymeScoreConstraint(ModelNum.GREATER_OR_EQUAL, 0.75, ReturnType.MATCHES);
	private final static CosineSimilarityConstraint highestCosine = new CosineSimilarityConstraint(NonModelNum.HIGHEST);
	private final static StringConstraint safeSpellingsForMarking = new StringConstraint(unsafeWordsForMarking, ReturnType.NON_MATCHES);
	private static PosConstraint safePosForMarking;
	private static SyllableNConstraint sameNSyllables = new SyllableNConstraint(ModelNum.EQUAL);

	public static void initializeFields() {
		initializeMarkingPos();
		safePosForMarking = new PosConstraint(goodPosForMarking, ReturnType.MATCHES);
		differentBase.enforce();
		differentSpelling.enforce();
		unusedBase.enforce();
		unusedSpelling.enforce();
		notInDirty.enforce();
		highestRhymeScore.enforce();
		reasonableRhymeScore.enforce();
		highestCosine.enforce();
		inCmuDict.enforce();//until I can guess phonemes or if there's another phoneme source
		safeSpellingsForMarking.enforce();
		safePosForMarking.enforce();
	}

	public static void initializeMarkingPos() {
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
	}

	public static List<WordConstraint> getNormal() {
		List<WordConstraint> result = new ArrayList<>();
		result.add(differentBase);
		result.add(differentSpelling);
		result.add(unusedBase);
		result.add(unusedSpelling);
		result.add(notInDirty);
		result.add(samePos);
//        result.add(sameNSyllables);
//        result.add(sameNe);
		result.add(inWikiDict);
//        result.add(highestCosine);
//        result.add(new BaseConstraint(ReturnType.NON_MATCHES));//(not the same base as the old word) oldWord-specific, enforced
//        result.get(0).enforce();
//        result.add(new StringConstraint(alreadyUsedWords, ReturnType.NON_MATCHES));//(not the same as any other new word) enforced
//        result.get(1).enforce();
//        result.add(new BaseConstraint(alreadyUsedBases, ReturnType.NON_MATCHES));//(not the same as any other new word) enforced
//        result.get(2).enforce();
//        result.add(new StringConstraint(dirtyWords, ReturnType.NON_MATCHES));// enforced
//        result.get(3).enforce();
//        result.add(new PosConstraint(ReturnType.MATCHES));//oldWord-specific
//        result.add(new NeConstraint(ReturnType.MATCHES));//oldWord-specific
//        result.add(new StringConstraint(commonWords, ReturnType.MATCHES));
////        result.add(new RhymeSyllableNConstraint(ModelNum.EQUAL));//oldWord-specific
//        result.add(new CosineDistanceConstraint(ModelNum.HIGHEST));
		return result;
	}

	public static List<WordConstraint> getNormalCmuMulti() {
		/*
		1. not the original word
		2. not one of these restricted words
		3. must be one of these common words
		4. pos == oldWordSpecific
		5. ne == oldWordSpecific
		6. Highest cosine distance
		*/


		List<WordConstraint> result = new ArrayList<>();
		result.add(inCmuDict);
//        result.add(reasonableRhymeScore);
		result.add(differentBase);
		result.add(differentSpelling);
//        result.add(unusedBase);
//        result.add(unusedSpelling);
		result.add(notInDirty);
		result.add(samePos);
		//loop until 1 left:
			//cut 2 suggestions with lowest rhyme
			//cut suggestion with lowest cosine similarity




//        result.add(highestRhymeScore);
//        result.add(highestCosine);


//        List<WordConstraint> result = new ArrayList<>();
//        result.add(new BaseConstraint(ReturnType.NON_MATCHES));//(not the same base as the old word) oldWord-specific, enforced
//        result.get(0).enforce();
////        result.add(new StringConstraint(alreadyUsedWords, ReturnType.NON_MATCHES));//(not the same as any other new word) enforced
////        result.get(1).enforce();
////        result.add(new BaseConstraint(alreadyUsedBases, ReturnType.NON_MATCHES));//(not the same as any other new word) enforced
////        result.get(2).enforce();
//        result.add(new StringConstraint(cmuWords, ReturnType.MATCHES));//in the cmu rhyming dictionary, enforced
//        result.get(1).enforce();
//        result.add(new StringConstraint(dirtyWords, ReturnType.NON_MATCHES));// enforced
//        result.get(2).enforce();
////        result.add(new StringConstraint(commonWords, ReturnType.MATCHES));
//        result.add(new PosConstraint(ReturnType.MATCHES));//oldWord-specific
////        result.add(new NeConstraint(ReturnType.MATCHES));//oldWord-specific
////        result.add(new RhymeSyllableNConstraint(ModelNum.EQUAL));//oldWord-specific
		return result;
	}

	public static List<WordConstraint> getMarking() {
		/*
		1. not the original word
		2. not one of these restricted words
		3. must be one of these common words
		4. pos == oldWordSpecific
		5. ne == oldWordSpecific
		6. Highest cosine distance
		*/
		List<WordConstraint> result = new ArrayList<>();
		result.add(safeSpellingsForMarking);
		result.add(safePosForMarking);
//        result.add(new StringConstraint(unsafeWordsForMarking, ReturnType.NON_MATCHES));// enforced
//        result.get(0).enforce();
//        result.add(new PosConstraint(goodPosForMarking, ReturnType.MATCHES));// enforced
//        result.get(1).enforce();
		return result;
	}

	public static List<WordConstraint> getNormalSameSyllables() {
		/*
		1. not one of these restricted words
		2. must be one of these common words
		3. pos == oldWordSpecific
		4. ne == oldWordSpecific
		5. Highest cosine distance
		*/
		return null;
	}

	public static List<WordConstraint> getRhyme() {
		/*
		1. not one of these restricted words (should this be here or should rhyme-specific stuff only be here?)
		2. must be one of these common words
		3. rhyme dbl >= .75
		4. pos == oldWordSpecific
		5. ne == oldWordSpecific
		6. Highest rhyme dbl
		7. Highest cosine distance
		*/

		List<WordConstraint> result = new ArrayList<>();
		result.add(inCmuDict);
		result.add(reasonableRhymeScore);
		result.add(differentBase);
		result.add(differentSpelling);
//        result.add(unusedBase);
//        result.add(unusedSpelling);
		result.add(notInDirty);
		result.add(samePos);
//        result.add(sameNSyllables);
//        result.add(highestRhymeScore);
//        result.add(highestCosine);

		// BEST idea - have equation like:
		//      maximize: A = xyz
		//      constraint: x + y + z = 3
		//      constraint: x, y, z > 0
		//      constraint: x, y, z <= 1.0
		// Then filter by word with the highest of that score

		//delete the 2 above, loop until 1 left:
		//  cut 2 suggestions with lowest rhyme
		//  cut suggestion with lowest cosine similarity


//        result.add(new BaseConstraint(ReturnType.NON_MATCHES));//(not the same base as the old word) oldWord-specific, enforced, oldWord-specific
//        result.get(0).enforce();
//        result.add(new StringConstraint(alreadyUsedWords, ReturnType.NON_MATCHES));//(not the same as any other new word) enforced
//        result.get(1).enforce();
//        result.add(new BaseConstraint(alreadyUsedBases, ReturnType.NON_MATCHES));//(not the same as any other new word) enforced
//        result.get(2).enforce();
//        result.add(new StringConstraint(cmuWords, ReturnType.MATCHES));//in the cmu rhyming dictionary, enforced
//        result.get(3).enforce();
//        result.add(new StringConstraint(dirtyWords, ReturnType.NON_MATCHES));// enforced
//        result.get(4).enforce();
////        result.add(new StringConstraint(commonWords, ReturnType.MATCHES));
//        result.add(new RhymeScoreConstraint(ModelNum.GREATER_OR_EQUAL, 1.0, ReturnType.MATCHES));// enforced
////        result.get(5).enforce();
//        result.add(new PosConstraint(ReturnType.MATCHES));//oldWord-specific
//        result.add(new NeConstraint(ReturnType.MATCHES));//oldWord-specific
////        result.add(new RhymeSyllableNConstraint(ModelNum.EQUAL));//oldWord-specific
//        result.add(new RhymeScoreConstraint(NonModelNum.HIGHEST));
//        result.add(new CosineDistanceConstraint(NonModelNum.HIGHEST));
		return result;
	}

	public static List<WordConstraint> getRhymeSameSyllables() {
		/*
		1. not one of these restricted words
		2. must be one of these common words
		3. rhyme dbl >= .75
		4. pos == oldWordSpecific
		5. ne == oldWordSpecific
		3. n of syllables == oldWordSpecific
		6. Highest rhyme dbl
		7. Highest cosine distance
		*/
		return null;
	}

	public static Set<String> getCommonWords() {
		return commonWords;
	}

	public static void setCommonWords(HashSet<String> commonWords) {
		WordConstraintMaker.commonWords = commonWords;
	}

	public static Set<String> getDirtyWords() {
		return dirtyWords;
	}

	public static void setDirtyWords(HashSet<String> dirtyWords) {
		WordConstraintMaker.dirtyWords = dirtyWords;
	}

	public static Set<Pos> getGoodPosForMarking() {
		return goodPosForMarking;
	}

	public static void setGoodPosForMarking(HashSet<Pos> goodPosForMarking) {
		WordConstraintMaker.goodPosForMarking = goodPosForMarking;
	}

}





















