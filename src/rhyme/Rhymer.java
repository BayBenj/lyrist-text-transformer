package rhyme;

import java.util.List;
import java.util.Set;
import elements.Word;

public abstract class Rhymer {

    private static final int LOOKAHEAD = 4;
    public static final double MATCHING_LINE_THRESHOLD = .2;

    private static final double PERFECT_RHYME_SCORE = 1.0;
    private static final double FAMILY_RHYME_SCORE = .75;
    private static final double ADDITIVE_RHYME_SCORE = .6;
    private static final double SUBTRACTIVE_RHYME_SCORE = .4;
    private static final double ASSONANCE_RHYME_SCORE = .4;
    private static final double CONSONANCE_RHYME_SCORE = .2;
    private static final boolean DEBUG = false;

//    private static double[][] hMatrix = HirjeeMatrix.load();
//    private static List<Pair<String, MannerOfArticulation>> phoneDict = Phoneticizer.loadReversePhonesDict();

    public static boolean isPerfectRhyme(String s1, String s2, int nSyl) {
        List<PhonemeEnum> rhyme1 = Phoneticizer.getRhymeOfLastXSyllables(s1, nSyl);
        List<PhonemeEnum> rhyme2 = Phoneticizer.getRhymeOfLastXSyllables(s2, nSyl);
        if (rhyme1.equals(rhyme2))
            return true;
        return false;
    }

    public static Set<String> getPerfectRhymes(String s1, int numberOfSyllables) {
        List<PhonemeEnum> rhyme = Phoneticizer.getRhymeOfLastXSyllables(s1, numberOfSyllables);
        if (numberOfSyllables == 1)
            return Phoneticizer.lastSylRhymeDict.get(rhyme);
        else if (numberOfSyllables == 2)
            return Phoneticizer.last2SylRhymeDict.get(rhyme);
        else if (numberOfSyllables == 3)
            return Phoneticizer.last3SylRhymeDict.get(rhyme);
        return null;
    }

    public static double getRhymeScore(Word w1, Word w2) {
        if (areIdentical(w1,w2))
            return 2;
        if (areRhymeIdentical(w1,w2))
            return 1;
        if (areVowelAligned(w1,w2)) {
            double cost = getTweakCost(w1,w2);
            return 1 - cost;
        }
        return 0;
    }

    public static boolean areIdentical(Word w1, Word w2) {
        if (w1.getSyllables().equals(w2.getSyllables()))
            return true;
        return false;
    }

    public static boolean areRhymeIdentical(Word w1, Word w2) {
        if (w1.getFullRhyme().equals(w2.getFullRhyme()))
            return true;
        return false;
    }

    public static boolean areVowelAligned(Word w1, Word w2) {
        WordSyllables s1 = w1.getSyllables();
        WordSyllables s2 = w2.getSyllables();
        if (s1.size() != s2.size())
            return false;
        List<VowelPhoneme> v1 = s1.getVowels();
        List<VowelPhoneme> v2 = s2.getVowels();
        for (int i = 0; i < s1.size(); i++)
            if (!v1.get(i).phonemeEnum.equals(v2.get(i).phonemeEnum))
                return false;
        return true;
    }

    public static double getTweakCost(Word w1, Word w2) {
        return .1;
    }

    /**
     * In the returned structure the ith element, j, is an array where
     * of the LOOKAHEAD lines following line i, j[0] was the highest matching rhyme
     * above THRESHOLD and j[1] syllables matched
     *
     * return scheme in which a number n at line i means that line i rhymes with line i-n
     */
//    public static int[] extractRhymeScheme(List<String> words) {
//        // For each line we want to return the number of syllables in the line
//        // and any rhyming information
//        int lineCount = words.size();
//        List<List<VowelPhoneme[]>> wordsPhones = new ArrayList<List<VowelPhoneme[]>>();
//
//        // for each line, store the syllables for the last few words
//        for (int i = 0; i < lineCount; i++) {
////            wordsPhones.add(Phoneticizer.getPhonesForXLastSyllables(words.get(i), 1));
//        }
//
//        int[] scheme = new int[lineCount];
//
//        List<VowelPhoneme[]> line1Phones, line2Phones;
//        double rhymeScore, maxRhymeScoreForLine, maxRhymeScoreForLines;
//        int maxJ = -1;
//        for (int i = 0; i < lineCount; i++) {
//            line1Phones = wordsPhones.get(i);
//            if (line1Phones == null){
//                continue;
//            }
////			System.out.println("COMPUTING RHYME SCORES FOR LINE:");
//            if (DEBUG) {
//                System.out.print(i + "\t");
//                System.out.print(scheme[i] + "\t");
//                System.out.print(words.get(i) + "\t");
//            }
//            maxRhymeScoreForLines = -1.0;
//            for (int j = i+1; j < Math.min(i+LOOKAHEAD+1, lineCount); j++) {
//                line2Phones = wordsPhones.get(j);
//                if (line2Phones == null){
//                    continue;
//                }
//                maxRhymeScoreForLine = -1.0;
//                for(VowelPhoneme[] line1Phone:line1Phones) {
//                    for(VowelPhoneme[] line2Phone: line2Phones) {
////                        rhymeScore = scoreRhymeByPatsRules(line1Phone, line2Phone);
//                        if (rhymeScore > maxRhymeScoreForLine) {
//                            maxRhymeScoreForLine = rhymeScore;
//                        }
//                    }
//                }
////				System.out.println("\n\t"+ maxRhymeScoreForLine + "\t" + words.get(j));
//                if ( maxRhymeScoreForLine > maxRhymeScoreForLines){
//                    maxRhymeScoreForLines = maxRhymeScoreForLine;
//                    maxJ = j;
//                }
//            }
//
//            if(maxRhymeScoreForLines >= MATCHING_LINE_THRESHOLD) {
//                scheme[maxJ] = maxJ-i;
//                if (DEBUG) System.out.print(maxRhymeScoreForLines + "\t" + maxJ);
//            }
//            if (DEBUG) System.out.println();
//        }
//
//        return scheme;
//    }

//    public static double scoreRhymeByPatsRules(VowelPhoneme[] line1Phones, VowelPhoneme[] line2Phones) {
////		VowelPhoneme[] line1LastSyl = Phoneticizer.getLastSyllable(line1Phones,0);
////		VowelPhoneme[] line1PenultimateSyl = Phoneticizer.getLastSyllable(line1Phones,1);
////		VowelPhoneme[] line2LastSyl = Phoneticizer.getLastSyllable(line2Phones,0);
////		VowelPhoneme[] line2PenultimateSyl = Phoneticizer.getLastSyllable(line2Phones,1);
//
//        if (line2Phones == null || line1Phones.length == 0 || line2Phones.length == 0)
//            return 0.;
//
//        if (isPerfectRhyme(line1Phones, line2Phones)) {
//            return PERFECT_RHYME_SCORE;
//        } else if (isFamilyRhyme(line1Phones, line2Phones)) {
//            return FAMILY_RHYME_SCORE;
//        } else if (isAdditiveRhyme(line1Phones, line2Phones)) {
//            return ADDITIVE_RHYME_SCORE;
//        } else if (isAdditiveRhyme(line2Phones, line1Phones)) {
//            return SUBTRACTIVE_RHYME_SCORE;
//        } else if (isAssonanceRhyme(line1Phones, line2Phones)) {
//            return ASSONANCE_RHYME_SCORE;
//        } else if (isConsonanceRhyme(line1Phones, line2Phones)) {
//            return CONSONANCE_RHYME_SCORE;
//        }
//
//        return 0.0;
//    }

    /**
     * Are the final consonants the same (and is the vowel somewhat similar?)
     * @param line1Phones
     * @param line2Phones
     * @return
     */
//    private static boolean isConsonanceRhyme(VowelPhoneme[] line1Phones, VowelPhoneme[] line2Phones) {
//        int line1Len = line1Phones.length;
//        int line2Len = line2Phones.length;
//
//        int phone1VowelIdx = getLastVowelIdx(line1Phones);
//        int phone2VowelIdx = getLastVowelIdx(line2Phones);
//
//        int line1Remander = line1Len - phone1VowelIdx;
//        if(phone1VowelIdx == -1 || phone2VowelIdx == -1 || (line1Remander >= line2Len - phone2VowelIdx)) {
//            return false;
//        }
//
////        int phone1 = line1Phones[phone1VowelIdx].phonemeEnum;
////        int phone2 = line2Phones[phone2VowelIdx].phonemeEnum;
////        if (HirjeeMatrix.score(phone1, phone2) < 0.){
////            return false;
////        }
//
//        for (int i = 1; i < line1Remander; i++) {
////            phone1 = line1Phones[phone1VowelIdx + i].phonemeEnum;
////            phone2 = line2Phones[phone2VowelIdx + i].phonemeEnum;
////            if (phone1 != phone2) {
////                return false;
////            }
//        }
//
//        return true;
//    }

    /**
     * Do the vowels match?
     * @param line1Phones
     * @param line2Phones
     * @return
     */
//    private static boolean isAssonanceRhyme(VowelPhoneme[] line1Phones, VowelPhoneme[] line2Phones) {
//        int phone1VowelIdx = getLastVowelIdx(line1Phones);
//        int phone2VowelIdx = getLastVowelIdx(line2Phones);
//
//        if(phone1VowelIdx == -1 || phone2VowelIdx == -1) {
//            return false;
//        }
//
////        int phone1 = line1Phones[phone1VowelIdx].phonemeEnum;
////        int phone2 = line2Phones[phone2VowelIdx].phonemeEnum;
////        return (phone1 == phone2);
//    }

//    private static boolean isAdditiveRhyme(VowelPhoneme[] line1Phones, VowelPhoneme[] line2Phones) {
//        int line1Len = line1Phones.length;
//        int line2Len = line2Phones.length;
//
//        int phone1VowelIdx = getLastVowelIdx(line1Phones);
//        int phone2VowelIdx = getLastVowelIdx(line2Phones);
//
//        int line1Remander = line1Len - phone1VowelIdx;
//        if(phone1VowelIdx == -1 || phone2VowelIdx == -1 || (line1Remander >= line2Len - phone2VowelIdx)) {
//            return false;
//        }
//
//        int phone1, phone2;
//        for (int i = 0; i < line1Remander; i++) {
//            phone1 = line1Phones[phone1VowelIdx + i].phonemeEnum;
//            phone2 = line2Phones[phone2VowelIdx + i].phonemeEnum;
//            if (phone1 != phone2) {
//                return false;
//            }
//        }
//
//        return true;
//    }

//    private static int getLastVowelIdx(VowelPhoneme[] line1Phones) {
//        int idx;
//        for (int i = 1; i <= line1Phones.length; i++) {
//            idx = line1Phones.length-i;
//            if (Phoneticizer.isVowel(line1Phones[idx].phonemeEnum))
//                return idx;
//        }
//        return -1;
//    }

//    private static boolean isFamilyRhyme(VowelPhoneme[] line1Phones, VowelPhoneme[] line2Phones) {
//        int line1Len = line1Phones.length;
//        int line2Len = line2Phones.length;
//
//        int phone1, phone2;
//        for (int pos = 1; pos <= Math.min(line1Len, line2Len); pos++) {
//            phone1 = line1Phones[line1Len-pos].phonemeEnum;
//            phone2 = line2Phones[line2Len-pos].phonemeEnum;
//
//            //vowels (by sound) are identical
//            if (Phoneticizer.isVowel(phone1)){
//                return phone1 == phone2;
//            }
//
//            //consonants after the vowel are phonetically related (e.g., mud, rut, truck, cup)
//            if (!arePhoneticallyRelated(phone1,phone2)) {
//                return false;
//            }
//
//        }
//        return false; // never found a vowel
//    }

//    private static boolean arePhoneticallyRelated(int phone1, int phone2) {
////		return (HirjeeMatrix.score(phone1, phone2) > 0.0);
////		return Phoneticizer.getCategory(phone1) == Phoneticizer.getCategory(phone2);
//        return Phoneticizer.getGeneralCategory(phone1) == Phoneticizer.getGeneralCategory(phone2); // Pat's definition
//    }

//    private static boolean isPerfectRhyme(VowelPhoneme[] line1Phones, VowelPhoneme[] line2Phones) {
//        int line1Len = line1Phones.length;
//        int line2Len = line2Phones.length;
//
//        int phone1, phone2;
//        for (int pos = 1; pos <= Math.min(line1Len, line2Len); pos++) {
//            phone1 = line1Phones[line1Len-pos].phonemeEnum;
//            phone2 = line2Phones[line2Len-pos].phonemeEnum;
//
//            //vowels (by sound) are identical
//            if (Phoneticizer.isVowel(phone1)){
//                return phone1 == phone2;
//            }
//
//            //consonants after the vowel are identical
//            if (phone1 != phone2) {
//                return false;
//            }
//
//        }
//        //rhyming syllables begin differently (required to create some minimal tension)
//
//        return true;
//    }

//    private static double scoreRhymingLinesByAlignment(VowelPhoneme[] line1Phones, VowelPhoneme[] line2Phones) {
//        if (line2Phones == null)
//            return 0.;
//
////		return (alignedRhymeScore(CMULoader.getPhonesForLastSyllables(line1Phones,2), CMULoader.getPhonesForLastSyllables(line2Phones,2)) / Math.max(1, distanceAhead-1));
//
//        // 1. do 4 syllable v syllable alignments (just 3? 5?)
//        VowelPhoneme[] line1Last = Phoneticizer.getLastSyllable(line1Phones,0);
////		VowelPhoneme[] line1Penultimate = Phoneticizer.getLastSyllable(line1Phones,1);
//        VowelPhoneme[] line2Last = Phoneticizer.getLastSyllable(line2Phones,0);
////		VowelPhoneme[] line2Penultimate = Phoneticizer.getLastSyllable(line2Phones,1);
//
////        double lastVLastScore = alignedRhymeScore(line1Last, line2Last) / Math.max(line1Last.length, line2Last.length);
////		double lastVPenultimateScore = 0.0;//alignedRhymeScore(line1Last, line2Penultimate);
////		double penultimateVLastScore = 0.0;//alignedRhymeScore(line1Penultimate, line2Last);
////		double penultimateVPenultimateScore = 0.0;//alignedRhymeScore(line1Penultimate, line2Penultimate);
//
//        // 2. find the one with the highest score
//
//        // TODO: 3. penalize for greater distance and for not aligning last syllable
////		return Math.max(lastVLastScore, Math.max(lastVPenultimateScore, Math.max(penultimateVLastScore, penultimateVPenultimateScore)));// * (1.05 - (.05 * distanceAhead));
////        return lastVLastScore;// * (1.05 - (.05 * distanceAhead));
//    }

//    public static void main(String[] args) throws IOException {
//
//        String[] words = new String[]{"Megaphone","Xylophone","Acetone","place called home", "bland", "hoax","jokes","folks"};
//        List<List<VowelPhoneme[]>> stressedPhones = new ArrayList<List<VowelPhoneme[]>>();
//        for (int i = 0; i < words.length; i++) {
//            //TODO: get last 3-4 syllables (i.e., not phones) for each word, not whole thing
////			stressedPhones[i] = CMULoader.getPhones(words[i]);
////            stressedPhones.add(Phoneticizer.getPhonesForXLastSyllables(words[i],1));
//        }
//
////		double[][] scores = new double[words.length][words.length];
//
//        //TODO: set Aligner costs
////        Aligner.setMinPercOverlap(.7);
//        SequencePair.setCosts(1,-1,-20,0);
//
//        VowelPhoneme[] word1SPs, word2SPs;
//        if (DEBUG) System.out.println("Simple\tAlign\tWord1\tWord2\tWord1Syls\tWord2Syls");
//        double score;
//        for (int i = 0; i < words.length; i++) {
//            word1SPs = stressedPhones.get(i).get(0);
//            for (int j = i+1; j < words.length; j++) {
//                word2SPs = stressedPhones.get(j).get(0);
//                score = simpleRhymeScore(word1SPs, word2SPs);
//                if (DEBUG) System.out.print(""+score);
//                score = alignedRhymeScore(word1SPs, word2SPs);
//                if (DEBUG) System.out.println("\t" + words[i] + "\t" + words[j] + "\t" + Arrays.toString(Phoneticizer.readable(word1SPs)) + "\t" + Arrays.toString(Phoneticizer.readable(word2SPs)));
////                Utils.promptEnterKey("");
//            }
//        }
//
//    }

    /*
     * naive SW alignment
     */
//    private static double alignedRhymeScore(VowelPhoneme[] word1sPs, VowelPhoneme[] word2sPs) {
////		System.out.println(Arrays.toString(Phoneticizer.readable(word1sPs)));
////		System.out.println(Arrays.toString(Phoneticizer.readable(word2sPs)));
//
//        //align
//        StressedPhonePairAlignment aln = (StressedPhonePairAlignment) Aligner.alignNW(new StressedPhonePair(word1sPs, word2sPs));
//
////		System.out.println(aln.getFinalScore());
//
//        return aln.getFinalScore();
//    }

    /**
     * @param phoneDict
     * @param hMatrix
     * @param words
     * @param scores
     * @param word1SPs
     * @param word2SPs
     * @param wrd1SPsLen
     * @param i
     * @param j
     * @return
     */
//    private static double simpleRhymeScore(VowelPhoneme[] word1SPs, VowelPhoneme[] word2SPs) {
//        VowelPhoneme word1SP;
//        VowelPhoneme word2SP;
//        double score, totScore = 0.;
//        int wrd1SPsLen = word1SPs.length;
//        int wrd2SPsLen = word2SPs.length;
//        int k;
//        for (k = 0; k < Math.min(wrd1SPsLen, wrd2SPsLen); k++) {
//            word1SP = word1SPs[wrd1SPsLen-k-1];
//            word2SP = word2SPs[wrd2SPsLen-k-1];
//            score = hMatrix[word1SP.phonemeEnum][word2SP.phonemeEnum];
////			System.out.println("\tScore for " + phoneDict.get(word1SP.phonemeEnum).getFirst() + " & " + phoneDict.get(word2SP.phonemeEnum).getFirst() + " = " + score);
//            if (k<1 || score >= 0) {
//                totScore += score;
//            }
//            else {
//                break;
//            }
//        }
////		System.out.println("\tscore: " + totScore);
////		System.out.println("\tsyllables: " + k);
//
//        return totScore;
//    }

}



