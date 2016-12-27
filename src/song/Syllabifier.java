//package song;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Scanner;
//
//import song.SequencePair.AlignmentBuilder;
////import song.Aligner;
////import song.SequencePair;
////import song.WordPhonemeAlignment;
////import song.WordPhonemePair;
////import song.Phonetecizer;
////import song.StressedPhone;
//import utils.Pair;
//import utils.Triple;
//import utils.Utils;
//
///*
// * Susan Bartlett, Grzegorz Kondrak and Colin Cherry.
// * On the Syllabification of Phonemes.
// * NAACL-HLT 2009.
// */
//public class Syllabifier {
//
//    public static void main(String[] args) {
//        Phonetecizer.loadCMUDict();
//        String test;
//        Scanner in = new Scanner(System.in);
//        while(true){
//            System.out.print("\nINPUT: ");
//            test = in.nextLine();
//            System.out.println("Pronunciation for \"" + test + "\"");
//            List<StressedPhone[]> phones = Phonetecizer.getPhones(test);
//            for (StressedPhone[] stressedPhones : phones) {
//                System.out.println("\t" + Arrays.toString(Phonetecizer.readable(stressedPhones)));
//                List<Triple<String, StressedPhone[], Integer>> value = syllabify(test,stressedPhones);
//                System.out.println(stringify(value));
//            }
//        }
//    }
//
//    public static String stringify(List<Triple<String, StressedPhone[], Integer>> value) {
//        StringBuilder wStr = new StringBuilder();
//        StringBuilder pStr = new StringBuilder();
//
//        for (int i = 0; i < value.size();i++){
//            Triple<String, StressedPhone[], Integer> triple = value.get(i);
//            if (i!=0){
//                wStr.append('•');
//                pStr.append('•');
//            }
//            wStr.append(triple.getFirst());
//            StressedPhone[] syls = triple.getSecond();
//            for (int j = 0; j < syls.length; j++) {
//                if (j!=0){
//                    pStr.append('-');
//                }
//                pStr.append(Phonetecizer.intToString(syls[j].phone));
//            }
//        }
//
//        return wStr.toString() + " : " + pStr.toString();
//    }
//
//    private static List<String> phoneOnsets = Arrays.asList("P", "T", "K", "B", "D", "G", "F", "V", "TH", "DH", "S", "Z", "SH", "CH", "JH", "M",
//            "N", "R", "L", "HH", "W", "Y", "P R", "T R", "K R", "B R", "D R", "G R", "F R",
//            "TH R", "SH R", "P L", "K L", "B L", "G L", "F L", "S L", "T W", "K W", "D W",
//            "S W", "S P", "S T", "S K", "S F", "S M", "S N", "G W", "SH W", "S P R", "S P L",
//            "S T R", "S K R", "S K W", "S K L", "TH W", "ZH", "P Y", "K Y", "B Y", "F Y",
//            "HH Y", "V Y", "TH Y", "M Y", "S P Y", "S K Y", "G Y", "HH W", "");
//    /**
//     * Given a pronunciation phonemes, syllabify the word word
//     * @param word
//     * @param phonemes
//     * @return
//     */
//    public static List<Triple<String, StressedPhone[], Integer>> syllabify(String word, StressedPhone[] phonemes) {
//        List<Pair<Pair<Integer,Integer>,Integer>> sylRanges = new ArrayList<Pair<Pair<Integer,Integer>, Integer>>();
//        List<String> intermediateConsonants = new ArrayList<String>();
//        Pair<Pair<Integer,Integer>,Integer> currSyllable = null;
//        List<Triple<String, StressedPhone[], Integer>> ret = new ArrayList<Triple<String,StressedPhone[],Integer>>();
//
//        //check if it's a number (which we don't syllabify)
//        if (word.matches(".*\\d+.*")) {
//            int stress = -1;
//            for (StressedPhone p : phonemes) {
//                if (p.stress > stress) {
//                    stress = p.stress;
//                }
//            }
//            ret.add(new Triple<String, StressedPhone[], Integer>(word, phonemes, stress));
//            return ret;
//        }
//
//        //check for literal spelling pronunciation
//        List<Triple<String, StressedPhone[], Integer>> literalPronunciation = spelledOutPronunciation(word);
//        int pIdx = 0;
//        boolean literalPron = true;
//        int sylIdx = 0,sylPhoIdx = 0;
//        StressedPhone[] litPhonemes = null;
//        for (; sylIdx < literalPronunciation.size() && literalPron && pIdx < phonemes.length;sylIdx++) {
//            litPhonemes = literalPronunciation.get(sylIdx).getSecond();
//            for (sylPhoIdx = 0; sylPhoIdx < litPhonemes.length && literalPron && pIdx < phonemes.length; sylPhoIdx++) {
//                if (phonemes[pIdx].phone != litPhonemes[sylPhoIdx].phone) {
//                    literalPron = false;
//                }
//                pIdx++;
//            }
//        }
//
//        // if the pronunciation matches the literal pronunciation at each and every position and they have the same length
//        if(literalPron && sylIdx == literalPronunciation.size() && litPhonemes != null && sylPhoIdx == litPhonemes.length) {
//            return literalPronunciation;
//        }
//
//        //for each phoneme in the pronunciation of "word"
//        for(int i = 0; i < phonemes.length;i++){
//            StressedPhone currPhone = phonemes[i];
//
//            //syllable is marked by start index (in phonemes), end index, and stress of syllable
//            if (Phonetecizer.isVowel(currPhone.phone)) {
//                int split = 0;
//                for(int j = 0; j < intermediateConsonants.size();j++){
//                    if (sylRanges.isEmpty()) {
//                        split = j;
//                        break;
//                    }
//                    if (phoneOnsets.contains(Utils.join(intermediateConsonants.subList(j, intermediateConsonants.size())," "))){
//                        split = j;
//                        break;
//                    }
//                }
//                split = i - intermediateConsonants.size() + split;
//                if(!sylRanges.isEmpty()) {
//                    sylRanges.get(sylRanges.size()-1).getFirst().setSecond(split);
//                }
//
//                currSyllable = new Pair<Pair<Integer,Integer>,Integer>(new Pair<Integer,Integer>(split,-1),currPhone.stress);
//
//                // split intermediate consonants between last syllable and current syllable to favor longest onset for current syllable
//
//                intermediateConsonants = new ArrayList<String>();
//                sylRanges.add(currSyllable);
//            } else {
//                intermediateConsonants.add(Phonetecizer.intToString(currPhone.phone));
//            }
//        }
//        // Set end of last syllable range to end of the phoneme sequence if there's at least one syllable
//        if (sylRanges.size() > 0) {
//            sylRanges.get(sylRanges.size()-1).getFirst().setSecond(phonemes.length);
//            if (sylRanges.size() == 1) { // if there's only one syllable, then we already know the syllibification of word (i.e., no syllibification)
//                ret.add(new Triple<String, StressedPhone[], Integer>(word, phonemes, sylRanges.get(0).getSecond()));
//                return ret;
//            }
//        } else {
//            ret.add(new Triple<String,StressedPhone[],Integer>(word,phonemes,-1));
//            return ret; // no syllables, return empty data structure
//        }
//
//        SequencePair.setCosts(1.0, 0.0, 0, 0);
//        WordPhonemeAlignment aln = (WordPhonemeAlignment) Aligner.alignNW(new WordPhonemePair(word, phonemes));
//        String alnWord = (String) aln.getFirst();
////		System.out.println(aln);
//        StressedPhone[] alnPhones = (StressedPhone[]) aln.getSecond();
//        String[] wordsyls = getWordSyllables(alnWord,alnPhones,sylRanges);
//
//
//        for (int j = 0; j < sylRanges.size();j++) {
//            Pair<Pair<Integer, Integer>, Integer> syl = sylRanges.get(j);
//            Pair<Integer,Integer> range = syl.getFirst();
//            int start = range.getFirst();
//            int end = range.getSecond();
//            StressedPhone[] phones = new StressedPhone[end-start];
//            for (int i = 0; i < end-start; i++) {
//                phones[i] = phonemes[start+i];
//            }
//            ret.add(new Triple<String,StressedPhone[],Integer>(wordsyls[j],phones,syl.getSecond()));
//        }
//
//        return ret;
//    }
//
//    /**
//     * null-delimited letter-by-letter pronunciation of input word
//     */
//    private static List<Triple<String, StressedPhone[], Integer>> spelledOutPronunciation(String word) {
//        List<Triple<String, StressedPhone[], Integer>> spelledOut = new ArrayList<Triple<String, StressedPhone[], Integer>>();
//
//        for (char c : word.toCharArray()) {
//            StressedPhone[] pronunciationForChar = Phonetecizer.getPronunciationForChar(c);
//            int stress = -1;
//            for (StressedPhone stressedPhone : pronunciationForChar) {
//                if (stressedPhone.stress > stress) {
//                    stress = stressedPhone.stress;
//                }
//            }
//            spelledOut.add(new Triple<String,StressedPhone[],Integer>("" + c,pronunciationForChar,stress));
//        }
//
//        return spelledOut;
//    }
//
//    private static String[] getWordSyllables(String alnWord, StressedPhone[] alnPhones,
//                                             List<Pair<Pair<Integer, Integer>, Integer>> sylRanges) {
//        String[] wordSyls = new String[sylRanges.size()];
//
//        StringBuilder sylBldr = new StringBuilder();
//        int currPhIdx = 0;
//        int alnIdx = 0;
//
//        int phEnd;
//        for (int i = 0; i < sylRanges.size(); i++) {
//            phEnd = sylRanges.get(i).getFirst().getSecond();
//
//            // get non-indel letters from aligned that align with phonemes in this word
//            while(currPhIdx < phEnd || alnIdx < alnPhones.length && alnPhones[alnIdx] == null) {
//                char c = alnWord.charAt(alnIdx);
//                if(c != AlignmentBuilder.INDEL_CHAR)
//                    sylBldr.append(c);
//                if (alnPhones[alnIdx] != null)
//                    currPhIdx++;
//                alnIdx++;
//            }
//
//            wordSyls[i] = sylBldr.toString();
//            if (wordSyls[i].length() == 0)
//                System.err.println("Warning: empty syllable for input " + alnWord);
//            sylBldr = new StringBuilder();
//        }
//
//        // Things to fix
//        String currWord;
//        char firstLetter,lastLetter;
//        for (int i = 0; i < wordSyls.length; i++) {
//            currWord = wordSyls[i];
//            if (currWord.length() < 2) continue;
//
//            firstLetter = currWord.charAt(0);
//            // two identical consonants together at the beginning of a syllable (with a neighboring syllable to which they could be split)
//            if (i != 0 && "aeiouy".indexOf(firstLetter) == -1 && firstLetter == currWord.charAt(1)) {
//                wordSyls[i-1] += firstLetter;
//                wordSyls[i] = currWord.substring(1);
//            }
//
//            if (currWord.length() < 2) continue;
//
//            lastLetter = currWord.charAt(currWord.length()-1);
//            // two identical consonants together at the end of a syllable (with a neighboring syllable to which they could be split)
//            if (i != wordSyls.length-1 && "aeiouy".indexOf(lastLetter) == -1 && lastLetter == currWord.charAt(currWord.length()-2)) {
//                wordSyls[i+1] = firstLetter + wordSyls[i+1];
//                wordSyls[i] = currWord.substring(0,currWord.length()-1);
//            }
//        }
//
//        // A syllable without a vowel and a neighboring syllable with two vowels, one that could be shared.
//        boolean hasVowel;
//        String prevWord,nextWord;
//        for (int i = 0; i < wordSyls.length; i++) {
//            currWord = wordSyls[i];
//            hasVowel = false;
//            for (int j = 0; j < currWord.length() && !hasVowel; j++) {
//                hasVowel = ("aeiouy".indexOf(currWord.charAt(j)) != -1);
//            }
//
//            if (!hasVowel) {
//                //check if prev syllable has a vowel to share
//                if (i != 0) {
//                    prevWord = wordSyls[i-1];
//                    if (prevWord.length() <= 1) continue;
//                    lastLetter = prevWord.charAt(prevWord.length()-1);
//                    if ("aeiouy".indexOf(lastLetter) != -1 && "aeiouy".indexOf(prevWord.charAt(prevWord.length()-2)) != -1) {
//                        wordSyls[i] = lastLetter + currWord;
//                        wordSyls[i-1] = prevWord.substring(0,prevWord.length()-1);
//                        hasVowel = true;
//                    }
//                }
//                // prev syllable didn't have a vowel to share or if no prev syllable existed
//                if (!hasVowel) {
//                    //check if next syllable has a vowel to share
//                    if (i != wordSyls.length-1) {
//                        nextWord = wordSyls[i+1];
//                        if (nextWord.length() <= 1) continue;
//                        firstLetter = nextWord.charAt(0);
//                        if ("aeiouy".indexOf(firstLetter) != -1 && "aeiouy".indexOf(nextWord.charAt(1)) != -1) {
//                            wordSyls[i] += firstLetter;
//                            wordSyls[i+1] = nextWord.substring(1);
//                        }
//                    }
//                }
//            }
//
//        }
//
//        return wordSyls;
//    }
//}
//
//
