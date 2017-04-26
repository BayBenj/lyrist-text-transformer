package rhyme;

import java.io.*;
import java.util.*;

import edu.cmu.sphinx.linguist.g2p.G2PConverter;
import elements.Punctuation;
import elements.Word;
import utils.U;

public class Phoneticizer {

//    private static final String cmuFilePath = TabDriver.dataDir + "/pron_dict/cmudict-0.7b.txt";
//    private static final String phonesFilePath = TabDriver.dataDir + "/pron_dict/cmudict-0.7b.phones.reordered.txt";
    private static final String cmuFilePath = U.rootPath + "data/phonemes/pron-dict/cmudict-0.7b.txt";

    public static Map<String, List<Pronunciation>> cmuDict = loadCMUDict();
    public static Map<List<PhonemeEnum>, Set<String>> lastSylRhymeDict = new HashMap<>();
    public static Map<List<List<PhonemeEnum>>, Set<String>> last2SylRhymeDict = new HashMap<>();
    public static Map<List<List<PhonemeEnum>>, Set<String>> last3SylRhymeDict = new HashMap<>();
    public static Map<String, List<WordSyllables>> syllableDict = loadSyllableDicts();
//    private static Map<String, Pair<Integer, MannerOfArticulation>> phonesDict = loadPhonesDict();
//    private static List<Pair<String, MannerOfArticulation>> reversePhonesDict = loadReversePhonesDict();
    private static G2PConverter converter = new G2PConverter(U.rootPath + "data/phonemes/pron-dict/model.fst.ser");

    /**
     * Loads CMU dictionary from file into a datastructure
     */
    public static Map<String, List<Pronunciation>> loadCMUDict() {
//        loadPhonesDict();

        if (cmuDict == null) {
            cmuDict = new HashMap<>();

            try {
                BufferedReader bf = new BufferedReader(new FileReader(cmuFilePath));

                String[] lineSplit, phonesSplit;
                Pronunciation phones;
                String line, key;
                Phoneme sPhone;
                int stress, parenIdx;
                List<Pronunciation> newList = null;

                while ((line = bf.readLine()) != null) {
                    if (line.startsWith(";;;"))
                        continue;

                    lineSplit = line.split("  ");
                    phonesSplit = lineSplit[1].split(" ");
                    phones = new Pronunciation();

                    for (String phone : phonesSplit) {
                        if (phone.length() == 3) {// we assume that any phonemeEnum with three chars, the third char is the stress
                            stress = Integer.parseInt(phone.substring(2, 3));
                            phone = phone.substring(0, 2);
                        }
                        else
                            stress = -1;
                        if (PhonemeEnum.valueOf(phone).isVowel())
                            sPhone = new VowelPhoneme(PhonemeEnum.valueOf(phone), stress);
                        else
                            sPhone = new ConsonantPhoneme(PhonemeEnum.valueOf(phone));
                        phones.add(sPhone);
                    }
                    // System.out.println(lineSplit[0] + ":" + Arrays.toString(phones));

                    key = lineSplit[0];

                    parenIdx = key.indexOf('(', 1);
                    if (parenIdx == -1) {
                        newList = new ArrayList<>();
                        cmuDict.put(key, newList);
                        newList.add(phones);
                    } else {
                        assert(cmuDict.containsKey(key.substring(0, parenIdx)));
                        newList.add(phones);
                    }
                    // U.promptEnterKey("");
                }

                bf.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

        return cmuDict;
    }

    public static Map<String, List<WordSyllables>> loadSyllableDicts() {
        Map<String, List<WordSyllables>> result = new HashMap<>();
        for (Map.Entry<String, List<Pronunciation>> entry : cmuDict.entrySet()) {
            if (entry.getKey().equals("SWIMSUIT")) {
//                U.print("stop for test");
            }
            List<WordSyllables> pronunciationSyls = new ArrayList<>();
            for (Pronunciation pronunciation : entry.getValue()) {
                WordSyllables syllables = SyllableParser.algorithmicallyParse(pronunciation);
                pronunciationSyls.add(syllables);
                int nSyl = syllables.size();
                if (nSyl > 0) {
                    Syllable ultimate = syllables.get(syllables.size() - 1);
                    Set<String> oldSet1 = lastSylRhymeDict.get(ultimate.getRhyme());
                    if (oldSet1 == null)
                        oldSet1 = new HashSet<>();
                    oldSet1.add(entry.getKey());
                    lastSylRhymeDict.put(ultimate.getRhyme().getPhonemeEnums(), oldSet1);
                    if (nSyl > 1) {
                        List<List<PhonemeEnum>> list1 = new ArrayList<>();
                        Syllable penultimate = syllables.get(syllables.size() - 2);
                        list1.add(penultimate.getRhyme().getPhonemeEnums());
                        list1.add(ultimate.getPhonemes().getPhonemeEnums());
                        Set<String> oldSet2 = lastSylRhymeDict.get(list1);
                        if (oldSet2 == null)
                            oldSet2 = new HashSet<>();
                        oldSet2.add(entry.getKey());
                        last2SylRhymeDict.put(list1, oldSet2);
                        if (nSyl > 2) {
                            List<List<PhonemeEnum>> list2 = new ArrayList<>();
                            Syllable antepenultimate = syllables.get(syllables.size() - 3);
                            list2.add(antepenultimate.getRhyme().getPhonemeEnums());
                            list2.add(penultimate.getPhonemes().getPhonemeEnums());
                            list2.add(ultimate.getPhonemes().getPhonemeEnums());
                            Set<String> oldSet3 = lastSylRhymeDict.get(list2);
                            if (oldSet3 == null)
                                oldSet3 = new HashSet<>();
                            oldSet3.add(entry.getKey());
                            last3SylRhymeDict.put(list2, oldSet3);
                        }
                    }
                }
            }
            result.put(entry.getKey(), pronunciationSyls);
        }
        return result;
    }

//    public static List<PhonemeEnum> getRhymeOfLastXSyllables(String s, int x) {
//        if (x < 1 || x > s.length())
//            return null;
//        else {
//            List<Syllable> syllables = getPronunciations(s);
//            List<Syllable> shavedSyllables = getLastXSyllables(syllables, x);
//            List<PhonemeEnum> rhymeOfLastXSyllables = new ArrayList<>();
//            rhymeOfLastXSyllables.addAll(shavedSyllables.get(0).getRhyme().getPhonemeEnums());
//            for (int i = 1; i < shavedSyllables.size(); i++)
//                rhymeOfLastXSyllables.addAll(shavedSyllables.get(i).getPhonemes().getPhonemeEnums());
//            return rhymeOfLastXSyllables;
//        }
//    }
//
//    public static List<Syllable> getLastXSyllables(String s, int x) {
//        if (x < 1 || x > s.length())
//            return null;
//        else
//            return getLastXSyllables(Phoneticizer.getPronunciations(s), x);
//    }

    private static List<Syllable> getLastXSyllables(List<Syllable> syllables, int x) {
        if (syllables == null || x > syllables.size())
            return null;

        List<Syllable> result = new WordSyllables();
        for (int i = 0; i < x; i++)
            result.add(syllables.get(syllables.size() - x + i));
        return result;
    }



//    /**
//     * returns loaded dict if not null, otherwise loads dict from file and returns dict
//     * @return
//     */
//    public static Map<String, Pair<Integer, MannerOfArticulation>> loadPhonesDict() {
//        if (phonesDict == null) {
//            loadPhonesDicts();
//        }
//
//        return phonesDict;
//    }

    /**
     * @throws FileNotFoundException
     * @throws IOException
     */
//    private static void loadPhonesDicts() {
//        phonesDict = new HashMap<String, Pair<Integer, MannerOfArticulation>>();
//        reversePhonesDict = new ArrayList<Pair<String, MannerOfArticulation>>();
//
//        try {
//
//            BufferedReader bf = new BufferedReader(new FileReader(phonesFilePath));
//
//            String[] lineSplit;
//            String line, phonemeEnum;
//            MannerOfArticulation category;
//            int lineNum = 0;
//            while ((line = bf.readLine()) != null) {
//                lineSplit = line.split("\t");
//                phonemeEnum = lineSplit[0];
//                category = MannerOfArticulation.valueOf(lineSplit[1]);
//                phonesDict.put(phonemeEnum, new Pair<Integer, MannerOfArticulation>(lineNum++, category));
//                reversePhonesDict.add(new Pair<String, MannerOfArticulation>(phonemeEnum, category));
//            }
//
//            bf.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//    }

    public static void main(String[] args) throws IOException {
        System.out.println("Loading CMU...");
        Phoneticizer.loadCMUDict();

//        String alphabet = "abcdefghijklmnopqrstuvwyz1234567890";
//        for (int i = 0; i < alphabet.length(); i++) {
//            getPronunciationForChar(alphabet.charAt(i));
//        }

        String[] tests = new String[]{"Hey, wind, oh","Hey, wind,", "aw","","aw, aw, aw,"};

//        for (int k = 0; k < tests.length; k++) {
//            for (int j = 0; j < 5; j++) {
//                System.out.println("Getting last " + j + " syllables of " + tests[k]);
//                List<VowelPhoneme[]> phones = getPhonesForXLastSyllables(tests[k], j);
//                for (VowelPhoneme[] stressedPhones : phones) {
//                    System.out.println("\t" + Arrays.toString(readable(stressedPhones)));
//                }
//            }
//        }

        //print entire cmu dictionary
//        for (String string : cmuDict.keySet()) {
//            List<VowelPhoneme[]> list = cmuDict.get(string);
//            System.out.println(string + " has " + list.size() + " pronunciations:");
//            ArrayList<Path> paths = converter.phoneticize(string,3);
//            for (int i = 0; i < list.size(); i++) {
//                System.out.println("\t\t" + Arrays.toString(readable(list.get(i))));
//            }
//            System.out.println("\tCOMPARED WITH:");
//            for(Path path: paths){
//                System.out.println("\t\t"+ path.getPath());
//            }
//        }


        tests = new String[]{"Potatoes, tomatoes, windy","hey-you, i.o.u. nu__in","namaste","schtoikandikes","lichtenstein","avadacadabrax"};
        for (String test : tests) {
            System.out.println("VowelPronunciation for \"" + test + "\"");
            List<Pronunciation> phones = getPronunciations(test);
            for (List<Phoneme> phonemes : phones) {
                System.out.println("\t" + Arrays.toString(readable(phonemes)));
            }
        }
    }

//    /**
//     * Returns datastruct mapping phonemes to phonemeEnum category
//     * @return
//     */
//    public static List<Pair<String, MannerOfArticulation>> loadReversePhonesDict() {
//        if (reversePhonesDict == null) {
//            loadPhonesDicts();
//        }
//
//        return reversePhonesDict;
//    }

    /**
     * phonemes that should be ignored when computing rhymes
     */
//    private static Set<String> stopRhymes = new HashSet<String>(Arrays.asList("AW","OH"));

    public static List<Pronunciation> getPronunciations(String s) {
        boolean contains = cmuDict.containsKey(s);
        //TOMATO    T AH0-M EY1-T OW2
        //CREATION  K R IY0-EY1-SH AH0 N
        List<Pronunciation> pronunciations = cmuDict.get(s);
        return pronunciations;
    }

    public static Pronunciation getTopPronunciation(String s) {
        List<Pronunciation> result = getPronunciations(s);
        if (result != null && !result.isEmpty())
            return getPronunciations(s).get(0);
        return null;
        //In the event of an out-of-dictinary-word:
//            if (pronunciationChoices == null) {
//                pronunciationChoices = new ArrayList<VowelPhoneme[]>();
//                s = s.replaceAll("[^A-Z0-9]", " ");
//
//                for (Path path: converter.phoneticize(s,3)) {
//                    pronunciationChoices.add(parse(path));
//                }
//            }

//        if (prevPhones == null) {
//            nextPhones = pronunciationChoices;
//        } else {
//            nextPhones = new ArrayList<>();
//            for (List<VowelPhoneme> prevPhone : prevPhones) {
//                for (List<VowelPhoneme> pronunciationChoice : pronunciationChoices) {
//                    nextPhones.addAll(prevPhone, pronunciationChoice);
//                }
//            }
//        }
//
//        prevPhones = nextPhones;
    }

    public static List<WordSyllables> getSyllables(String s) {
        return syllableDict.get(s.toUpperCase());
    }

        /**
         * Returns a list of ways the input string could be pronounced. If word is in cmu dictionary, then CMU entry is returned, otherwise G2Pconverter is used to guess.
         * @param string
         * @return
         */
    public static List<Pronunciation> getPronunciationsMultipleWords(String string) {
        List<Pronunciation> prevPhones = null, nextPhones, vowelPronunciationChoices;

        for (String s : string.toUpperCase().trim().split("[^A-Z0-9']+")) {
            prevPhones.add(getTopPronunciation(s));
        }

        return prevPhones == null? new ArrayList<>(): prevPhones;
    }


//    private static VowelPhoneme[] parse(Path path) {
//        if (path == null) return null;
//        ArrayList<String> pathPhones = path.getPath();
//        VowelPhoneme[] phones = new VowelPhoneme[pathPhones.size()];
//        VowelPhoneme sPhone;
//        String phonemeEnum;
//
//        for (int i = 0; i < pathPhones.size(); i++) {
//            phonemeEnum = pathPhones.get(i);
//            sPhone = new VowelPhoneme(phonesDict.get(phonemeEnum).getFirst(), -1);
//            phones[i] = sPhone;
//        }
//
//        return phones;
//    }

//    public static String intToString(int phoneInt) {
//        return reversePhonesDict.get(phoneInt).getFirst();
//    }

    public static String[] readable(List<Phoneme> word1sPs) {
        String[] returnVal = new String[word1sPs.size()];

        for (int i = 0; i < returnVal.length; i++) {
            if (word1sPs.get(i) instanceof VowelPhoneme)
                returnVal[i] = word1sPs.get(i).phonemeEnum.toString() + ":" + ((VowelPhoneme)word1sPs.get(i)).stress;
            else
                returnVal[i] = word1sPs.get(i).phonemeEnum.toString();
        }

        return returnVal;
    }

//    public static List<VowelPhoneme[]> getPhonesForXLastSyllables(String string, int num) {
//        if (num == 0 || string.length() == 0)
//            return new ArrayList<VowelPhoneme[]>();
//
//        List<VowelPhoneme[]> returnList = new ArrayList<VowelPhoneme[]>(), pronunciationChoices;
//        Map<VowelPhoneme[],Integer> prevPhones = null, nextPhones = null;
//        String[] filterWords = string.toUpperCase().trim().split("[^A-Z0-9']+");
//        int vowelCount, vowelCountForPrevPhone, start;
//        String s;
//        for (int i = filterWords.length-1; i >= 0; i--) {
//            s = filterWords[i];
//            if (s.length() == 0 || stopRhymes.contains(s)) continue;
//            pronunciationChoices = cmuDict.get(s);
//
//            if (pronunciationChoices == null) {
//                pronunciationChoices = new ArrayList<VowelPhoneme[]>();
//                for(Path path: converter.phoneticize(s.replaceAll("[^A-Z0-9]", " "),3)) {
//                    pronunciationChoices.add(parse(path));
//                }
//            }
//
//            if (prevPhones == null) { // if this is the first word
//                nextPhones = new HashMap<VowelPhoneme[],Integer>();
//                // go through each pronunciation and
//                for (VowelPhoneme[] phones: pronunciationChoices) {
//                    vowelCount = 0;
//                    start = phones.length;
//                    while (vowelCount < num && start > 0) {
//                        if (reversePhonesDict.get(phones[--start].phonemeEnum).getSecond() == MannerOfArticulation.VOWEL) {
//                            vowelCount++;
//                            if (vowelCount == num) {
//                                break;
//                            }
//                        }
//                    }
//                    //if it has sufficient syllables,
//                    if (vowelCount == num) {
//                        // add it to the return list
//                        returnList.add(Arrays.copyOfRange(phones, start, phones.length));
//                    } else {
//                        // otherwise it will be extended
//                        nextPhones.put(phones,vowelCount);
//                    }
//                }
//            } else {
//                nextPhones = new HashMap<VowelPhoneme[], Integer>();
//                for (Entry<VowelPhoneme[],Integer> prevPhone : prevPhones.entrySet()) {
//                    vowelCountForPrevPhone = prevPhone.getValue();
//                    for (VowelPhoneme[] pronunciationChoice: pronunciationChoices) {
//                        vowelCount = 0;
//                        start = pronunciationChoice.length;
//                        while (vowelCount < num && start > 0) {
//                            if (reversePhonesDict.get(pronunciationChoice[--start].phonemeEnum).getSecond() == MannerOfArticulation.VOWEL) {
//                                vowelCount++;
//                                if (vowelCount + vowelCountForPrevPhone == num) {
//                                    break;
//                                }
//                            }
//                        }
//                        //if it has sufficient syllables,
//                        if (vowelCount + vowelCountForPrevPhone == num) {
//                            // add it to the return list
//                            returnList.add(ArrayUtils.addAll(Arrays.copyOfRange(pronunciationChoice, start, pronunciationChoice.length),prevPhone.getKey()));
//                        } else {
//                            // otherwise it will be extended
//                            nextPhones.put(ArrayUtils.addAll(pronunciationChoice,prevPhone.getKey()),vowelCount + vowelCountForPrevPhone);
//                        }
//                    }
//                }
//            }
//
//            if (nextPhones.size() == 0) {
//                break;
//            } else {
//                prevPhones = nextPhones;
//            }
//        }
//
//        if (nextPhones != null && nextPhones.size() > 0) {
//            returnList.addAll(nextPhones.keySet());
//        }
//
//        return returnList;
//    }

//    public static VowelPhoneme[] getPhonesForLastSyllables(VowelPhoneme[] phones, int num) {
//        int start = phones.length;
//
//        if (num == 0 || start == 0) {
//            return new VowelPhoneme[0];
//        }
//
//        int syllableCount = 0;
//
//        while (syllableCount < num && start > 0) {
//            if (reversePhonesDict.get(phones[--start].phonemeEnum).getSecond() == MannerOfArticulation.VOWEL) {
//                syllableCount++;
//            }
//        }
//
//        return Arrays.copyOfRange(phones, start, phones.length);
//    }

    public static VowelPhoneme[] getLastSyllable(VowelPhoneme[] phones, int offsetFromEnd) {
        int start = phones.length;

        if (start == 0) {
            return new VowelPhoneme[0];
        }

        int syllableCount = 0;
        int lastVowelPosition = start;

        while (syllableCount <= offsetFromEnd && start > 0) {
            if (phones[--start].phonemeEnum.isVowel()) {
                if (syllableCount == offsetFromEnd)
                {
                    return Arrays.copyOfRange(phones, start, lastVowelPosition);
                }
                else {
                    lastVowelPosition = start;
                    syllableCount++;
                }
            }
        }

        return Arrays.copyOfRange(phones, 0, lastVowelPosition);
    }

//    public static boolean isVowel(int phonemeEnum) {
//        return reversePhonesDict.get(phonemeEnum).getSecond() == MannerOfArticulation.VOWEL;
//    }

//    public static MannerOfArticulation getCategory(int phonemeEnum) {
//        return reversePhonesDict.get(phonemeEnum).getSecond();
//    }

//    public static MannerOfArticulation getGeneralCategory(int phonemeEnum) {
//        MannerOfArticulation cat = getCategory(phonemeEnum);
//        if(cat == MannerOfArticulation.AFFRICATE)
//            return MannerOfArticulation.FRICATIVE;
//        else
//            return cat;
//    }

//    public static VowelPhoneme[] getPronunciationForChar(char c) {
//        List<VowelPhoneme[]> pronuns = cmuDict.get("" + Character.toUpperCase(c));
//        if (pronuns == null) {
//            return parse(converter.phoneticize("" + c, 1).get(0));
//        }
//        return pronuns.get(pronuns.size()-1);
//    }

    public static boolean cmuDictContains(String word) {
        return cmuDict.containsKey(word.toUpperCase());
    }

    public static Pronunciation getPronunciationForWord(String string) {
        if (string != null && string.length() > 0 && string.matches("\\w+")) {
            return getTopPronunciation(string.toUpperCase());
        }
        return null;
    }

    public static Pronunciation getPronunciationForWord(Word word) {
        if (word.getClass() != Punctuation.class) {
            return getPronunciationForWord(word.getLowerSpelling());
        }
        return null;
    }

    public static List<WordSyllables> getSyllablesForWord(String string) {
        if (string != null && string.length() > 0 && string.matches("\\w+")) {
            return getSyllables(string.toUpperCase());
        }
        return null;
    }

    public static List<WordSyllables> getSyllablesForWord(Word word) {
        if (word.getClass() != Punctuation.class) {
            return getSyllablesForWord(word.getLowerSpelling());
        }
        return null;
    }

    public static Map<String, List<Pronunciation>> getCmuDict() {
        return cmuDict;
    }
}














































