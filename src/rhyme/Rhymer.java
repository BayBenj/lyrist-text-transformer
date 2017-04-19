package rhyme;

import java.io.*;
import java.util.*;
import elements.Word;
import main.LyristDriver;
import main.MultiProgramArgs;
import utils.U;

public abstract class Rhymer {

//    private static final int LOOKAHEAD = 4;
//    public static final double MATCHING_LINE_THRESHOLD = .2;
//
//    private static final double PERFECT_RHYME_SCORE = 1.0;
//    private static final double FAMILY_RHYME_SCORE = .75;
//    private static final double ADDITIVE_RHYME_SCORE = .6;
//    private static final double SUBTRACTIVE_RHYME_SCORE = .4;
//    private static final double ASSONANCE_RHYME_SCORE = .4;
//    private static final double CONSONANCE_RHYME_SCORE = .2;
//    private static final boolean DEBUG = false;

//    private static double[][] hMatrix = HirjeeMatrix.load();
//    private static List<Pair<String, MannerOfArticulation>> phoneDict = Phoneticizer.loadReversePhonesDict();

    public static Map<SyllableGroup, Set<String>> perfectRhymes;
    public static boolean mainDebug = false;

    public static void main(String[] args) throws IOException {
        mainDebug = true;
        MultiProgramArgs.debugMode = true;

        LyristDriver.setupRootPath();
        LyristDriver.setupCmuDict();

        Word w = new Word("wine");
        w.setSyllables(Phoneticizer.getSyllables(w.getLowerSpelling()));

        Set<String> perfects = perfectRhymes.get(w.getRhymeTail());
        System.out.println("PERFECT RHYMES:");
        for (String s : perfects)
            System.out.println(s + "\t1.0");

        try {
            System.out.println("\n\nALL RHYMES:");
            getAllRhymesByThreshold(w, 0.95);
        } catch (NoRhymeFoundException e) {
            System.out.println("No rhyme found.");
            e.printStackTrace();
        }
//        Map<SyllableGroup, Set<String>> perf = new HashMap<>();
//        Set<String> cmudict = Phoneticizer.getCmuDict().keySet();
//        int count = 0;
//
//        for (String s : cmudict) {
//            count++;
//            Word word = new Word(s.toLowerCase());
//            word.setSyllables(Phoneticizer.getSyllablesForWord(word.getUpperSpelling()));
//            if (U.isNullOrEmpty(word.getSyllables()) || U.isNullOrEmpty(word.getRhymeTail()))
//                continue;
//
//            if (!perf.containsKey(word.getRhymeTail())) {
//                Set<String> perfectRhymes;
//                try {
//                    perfectRhymes = getAllRhymesByThreshold(word, 1.0);
//                    perf.put(word.getRhymeTail(), perfectRhymes);
//                } catch (NoRhymeFoundException e) {
//                    perf.put(word.getRhymeTail(), new HashSet<>());
//                }
//            }
//
//            if (count % 1000 == 0)
//                System.out.println(count + "/130");
//        }
//
//        serializePerfRhymes(perf);
//        //deserializePerfRhymes();
    }

    public static void deserializePerfRhymes() {
        U.testPrint("Deserializing perfect rhyme Model");
        try {
            FileInputStream fileIn = new FileInputStream(U.rootPath + "data/phonemes/rhyme/sers/perf.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            perfectRhymes = null;
            perfectRhymes = (Map<SyllableGroup, Set<String>>) in.readObject();
            in.close();
            fileIn.close();
        }
        catch(IOException i) {
            i.printStackTrace();
        }
        catch(ClassNotFoundException c) {
            System.out.println("perfect rhyme class not found");
            c.printStackTrace();
        }
    }

    private static void serializePerfRhymes(Map<SyllableGroup, Set<String>> perfRhymes) {
        try {
            FileOutputStream fileOut = new FileOutputStream(U.rootPath + "data/phonemes/rhyme/sers/perf.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(perfRhymes);
            out.close();
            fileOut.close();
            System.out.println("Serialized perfect rhyme map is saved in lyrist/data/phonemes/rhyme/sers/perf.ser");
        }
        catch(IOException i) {
            i.printStackTrace();
        }
    }


    public static Set<String> getAllRhymesByThreshold(Word w, double threshold, int limit) throws NoRhymeFoundException {
        Set<String> rhymes = getAllRhymesByThreshold(w,threshold);
        Set<String> result = new HashSet<>();
        int i = 0;
        for (String s : rhymes) {
            if (i > limit)
                break;
            result.add(s);
            i++;
        }
        return result;
    }

    public static Set<String> getAllRhymesByThreshold(Word w, double threshold) throws NoRhymeFoundException {
        if (w == null || U.isNullOrEmpty(w.getRhymeTail())) throw new NoRhymeFoundException();
        return getAllRhymesByThreshold(w.getRhymeTail(), threshold);
    }

    public static Set<String> getAllRhymesByThreshold(SyllableGroup w, double threshold) throws NoRhymeFoundException {
        if (U.isNullOrEmpty(w)) throw new NoRhymeFoundException();

        if (threshold > 1.0)
            threshold = 1.0;
        else if (threshold < 0.0)
            throw new NoRhymeFoundException();

        if (threshold == 1.0) {
            Set<String> rhymes = perfectRhymes.get(w);
            if (mainDebug)
                for (String s : rhymes)
                    U.testPrint(s.toLowerCase() + ": 1.0");
            return rhymes;
        }

        Set<String> result = new HashSet<>();
        for (Map.Entry<String, List<Pronunciation>> entry : Phoneticizer.cmuDict.entrySet()) {
            if (!Phoneticizer.syllableDict.keySet().contains(entry.getKey().toUpperCase()) || !entry.getKey().matches("\\w+")) continue;
            if (entry.getKey().equalsIgnoreCase("weeping")) {
                System.out.println("stop for weeping test");
            }
            Word temp = new Word(entry.getKey());
            temp.setSyllables(Phoneticizer.getSyllablesForWord(entry.getKey()));
            double score = score2Rhymes(w, temp.getRhymeTail());
            if (score >= threshold) {
                result.add(entry.getKey().toLowerCase());
                if (mainDebug)
                    System.out.println(entry.getKey().toLowerCase() + ": " + score);
            }
        }
        return result;
    }

    public static double score2Rhymes(SyllableGroup s1, SyllableGroup s2) {

        if (U.isNullOrEmpty(s1) || U.isNullOrEmpty(s2))
            return 0;

        SyllableGroup shorter = s1;
        SyllableGroup longer = s2;
        if (s1.size() > s2.size()) {
            shorter = s2;
            longer = s1;
        }

        double d = 0.0;
        double n = shorter.size();
//        double m = .75;
        for (int s = shorter.size() - 1; s >= 0; s--) {
//            if (s == 0)
//                m = 1.25;
            d += (score2Syllables(s1.get(s), s2.get(s)) );
        }

        double average = d / n;

        //penalize words with rhymes of differing syllables
//        double difference = Math.abs((double)shorter.size() - (double)longer.size());
//
//        if (difference == 0)
//            return average;
//
//        double ratio = (((difference / 2.0) + ((double)shorter.size())) / ((double)longer.size()));
//        average *= ratio;

        return average;
    }

    public static double score2Syllables(Syllable s1, Syllable s2) {
        int n = 3;

        double onsetWeight = 1;
        double nucleusWeight = 6;
        double codaWeight = 1;

        ConsonantPronunciation o1 = s1.getOnset();
        ConsonantPronunciation o2 = s2.getOnset();
        double onsetScore;
        if (U.isNullOrEmpty(o1) && U.isNullOrEmpty(o2)) {
            n--;
            onsetScore = 0;
            onsetWeight = 0;
        }
        else
            onsetScore = scoreConsonantPronunciations(o1,o2);

        VowelPhoneme n1 = s1.getNucleus();
        VowelPhoneme n2 = s2.getNucleus();
        double nucleusScore;
        if (n1 == null && n2 == null) {
            n--;
            nucleusScore = 0;
            nucleusWeight = 0;
        }
        else
             nucleusScore = score2Vowels(n1,n2);

        ConsonantPronunciation c1 = s1.getCoda();
        ConsonantPronunciation c2 = s2.getCoda();
        double codaScore;
        if (U.isNullOrEmpty(c1) && U.isNullOrEmpty(c2)) {
            n--;
            codaScore = 0;
            codaWeight = 0;
        }
        else
            codaScore = scoreConsonantPronunciations(c1,c2);


        double total = (onsetWeight + nucleusWeight + codaWeight) / n;

        double onsetMult = onsetWeight / total;
        double nucleusMult = nucleusWeight / total;
        double codaMult = codaWeight / total;

        double syllableAlignmentScore = ((onsetMult * onsetScore) + (nucleusMult * nucleusScore) + (codaMult * codaScore)) / n;
        return syllableAlignmentScore;
    }

    public static double scoreConsonantPronunciations(ConsonantPronunciation o1, ConsonantPronunciation o2) {
        if (!U.notNullnotEmpty(o1) && !U.notNullnotEmpty(o2))
            return 1;
        if (!U.notNullnotEmpty(o1) || !U.notNullnotEmpty(o2))
            return 0;
        //TODO ACTUALLY ALIGN consonants here
        double alignmentScore = 0;
        ConsonantPronunciation shortest = o1;
        ConsonantPronunciation longest = o2;
        if (o1.size() > o2.size()) {
            shortest = o2;
            longest = o1;
        }
        for (int cp = shortest.size() - 1; cp >= 0; cp--) {
            ConsonantPhoneme cp1 = o1.get(cp);
            ConsonantPhoneme cp2 = o2.get(cp);
            alignmentScore += score2Consonants(cp1, cp2);
        }
        int n = longest.size();
        double average = alignmentScore / n;
        return average;
    }

    public static double score2Vowels(VowelPhoneme n1, VowelPhoneme n2) {
        if (n1 == null || n2 == null)
            return 0;
        double[] coord1 = PhonemeEnum.getCoord(n1.phonemeEnum);
        double[] coord2 = PhonemeEnum.getCoord(n2.phonemeEnum);
        if (coord1 == null || coord2 == null)
            return 0;
        double frontnessDiff = Math.abs(coord1[0] - coord2[0]);
        double hightDiff = Math.abs(coord1[1] - coord2[1]);
        double frontScore = Math.abs((frontnessDiff / 12.72792) - 1) * 1;
        double heightScore = Math.abs((hightDiff / 12.72792) - 1) * 1;
        return (frontScore + heightScore) / 2;//TODO weight frontness and height
        //        Frontness f1 = ph1.getFrontness();
//        Frontness f2 = ph2.getFrontness();
//        Height h1 = ph1.getHeight();
//        Height h2 = ph2.getHeight();
//        boolean r1 = PhonemeEnum.isRounded(ph1);
//        boolean r2 = PhonemeEnum.isRounded(ph2);
//        double score = 0;
//        if (f1 == f2)
//            score += .3; //TODO: score individual types of frontness, etc.
//
//        if (h1 == h2)
//            if (h1 == Height.LOW)
//                score += .4;
//            else
//                score += .35;
//
//        if (r1 == r2)
//            score += .2;
//
//        return score;

    }

    private static double getDistance(double[] p1, double[] p2) {
        double d = Math.sqrt(Math.pow(p2[0] - p1[0], 2) + Math.pow(p2[1] - p1[1], 2));
        return d;
    }

    private static double score2Consonants(ConsonantPhoneme ph1, ConsonantPhoneme ph2) {
        if (ph1 == null || ph2 == null)
            return 0;
        MannerOfArticulation m1 = PhonemeEnum.getManner(ph1.phonemeEnum);
        MannerOfArticulation m2 = PhonemeEnum.getManner(ph2.phonemeEnum);
        PlaceOfArticulation p1 = PhonemeEnum.getPlace(ph1.phonemeEnum);
        PlaceOfArticulation p2 = PhonemeEnum.getPlace(ph2.phonemeEnum);
        boolean v1 = ph1.isVoiced();
        boolean v2 = ph2.isVoiced();
        double score = 0;
        if (v1 == v2)
            score += .1;
        if (m1 == m2)
            score += .45;
        if (p1 == p2)
            score += .45;
        return score;
    }

        /**
         * In the returned structure the ith element, j, is an array where
         * of the LOOKAHEAD lines following line i, j[0] was the filterToHighest matching rhyme
         * above THRESHOLD and j[1] syllables matched
         *
         * return scheme in which a number n at line i means that line i rhymes with line i-n
         */
//    public static int[] extractRhymeScheme(List<String> filterWords) {
//        // For each line we want to return the number of syllables in the line
//        // and any rhyming information
//        int lineCount = filterWords.size();
//        List<List<VowelPhoneme[]>> wordsPhones = new ArrayList<List<VowelPhoneme[]>>();
//
//        // for each line, store the syllables for the last few filterWords
//        for (int i = 0; i < lineCount; i++) {
////            wordsPhones.add(Phoneticizer.getPhonesForXLastSyllables(filterWords.get(i), 1));
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
//                System.out.print(filterWords.get(i) + "\t");
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
////				System.out.println("\n\t"+ maxRhymeScoreForLine + "\t" + filterWords.get(j));
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
     * Are the final consonants the oldWordSpecific (and is the vowel somewhat similar?)
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
////        if (HirjeeMatrix.dbl(phone1, phone2) < 0.){
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
//        for (int wordsToPos = 1; wordsToPos <= Math.min(line1Len, line2Len); wordsToPos++) {
//            phone1 = line1Phones[line1Len-wordsToPos].phonemeEnum;
//            phone2 = line2Phones[line2Len-wordsToPos].phonemeEnum;
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
////		return (HirjeeMatrix.dbl(phone1, phone2) > 0.0);
////		return Phoneticizer.getCategory(phone1) == Phoneticizer.getCategory(phone2);
//        return Phoneticizer.getGeneralCategory(phone1) == Phoneticizer.getGeneralCategory(phone2); // Pat's definition
//    }

//    private static boolean isPerfectRhyme(VowelPhoneme[] line1Phones, VowelPhoneme[] line2Phones) {
//        int line1Len = line1Phones.length;
//        int line2Len = line2Phones.length;
//
//        int phone1, phone2;
//        for (int wordsToPos = 1; wordsToPos <= Math.min(line1Len, line2Len); wordsToPos++) {
//            phone1 = line1Phones[line1Len-wordsToPos].phonemeEnum;
//            phone2 = line2Phones[line2Len-wordsToPos].phonemeEnum;
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
//        // 2. find the one with the filterToHighest dbl
//
//        // TODO: 3. penalize for greater distance and for not aligning last syllable
////		return Math.max(lastVLastScore, Math.max(lastVPenultimateScore, Math.max(penultimateVLastScore, penultimateVPenultimateScore)));// * (1.05 - (.05 * distanceAhead));
////        return lastVLastScore;// * (1.05 - (.05 * distanceAhead));
//    }

//    public static void main(String[] args) throws IOException {
//
//        String[] filterWords = new String[]{"Megaphone","Xylophone","Acetone","place called home", "bland", "hoax","jokes","folks"};
//        List<List<VowelPhoneme[]>> stressedPhones = new ArrayList<List<VowelPhoneme[]>>();
//        for (int i = 0; i < filterWords.length; i++) {
//            //TODO: get last 3-4 syllables (i.e., not phones) for each word, not whole thing
////			stressedPhones[i] = CMULoader.getPhones(filterWords[i]);
////            stressedPhones.add(Phoneticizer.getPhonesForXLastSyllables(filterWords[i],1));
//        }
//
////		double[][] scores = new double[filterWords.length][filterWords.length];
//
//        //TODO: set Aligner costs
////        Aligner.setMinPercOverlap(.7);
//        SequencePair.setCosts(1,-1,-20,0);
//
//        VowelPhoneme[] word1SPs, word2SPs;
//        if (DEBUG) System.out.println("Simple\tAlign\tWord1\tWord2\tWord1Syls\tWord2Syls");
//        double dbl;
//        for (int i = 0; i < filterWords.length; i++) {
//            word1SPs = stressedPhones.get(i).get(0);
//            for (int j = i+1; j < filterWords.length; j++) {
//                word2SPs = stressedPhones.get(j).get(0);
//                dbl = simpleRhymeScore(word1SPs, word2SPs);
//                if (DEBUG) System.out.print(""+dbl);
//                dbl = alignedRhymeScore(word1SPs, word2SPs);
//                if (DEBUG) System.out.println("\t" + filterWords[i] + "\t" + filterWords[j] + "\t" + Arrays.toString(Phoneticizer.readable(word1SPs)) + "\t" + Arrays.toString(Phoneticizer.readable(word2SPs)));
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
     * @param filterWords
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
//        double dbl, totScore = 0.;
//        int wrd1SPsLen = word1SPs.length;
//        int wrd2SPsLen = word2SPs.length;
//        int k;
//        for (k = 0; k < Math.min(wrd1SPsLen, wrd2SPsLen); k++) {
//            word1SP = word1SPs[wrd1SPsLen-k-1];
//            word2SP = word2SPs[wrd2SPsLen-k-1];
//            dbl = hMatrix[word1SP.phonemeEnum][word2SP.phonemeEnum];
////			System.out.println("\tScore for " + phoneDict.get(word1SP.phonemeEnum).getFirst() + " & " + phoneDict.get(word2SP.phonemeEnum).getFirst() + " = " + dbl);
//            if (k<1 || dbl >= 0) {
//                totScore += dbl;
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


