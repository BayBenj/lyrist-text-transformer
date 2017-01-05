package rhyme;

import java.io.*;
import java.util.*;

import edu.cmu.sphinx.linguist.g2p.G2PConverter;
import song.Punctuation;
import song.Word;
import utils.Utils;

public class Phoneticizer {

//    private static final String cmuFilePath = TabDriver.dataDir + "/pron_dict/cmudict-0.7b.txt";
//    private static final String phonesFilePath = TabDriver.dataDir + "/pron_dict/cmudict-0.7b.phones.reordered.txt";
    private static final String cmuFilePath = Utils.rootPath + "local-data/phonemes/pron-dict/cmudict-0.7b.txt";

    private static Map<String, List<Pronunciation>> cmuDict = loadCMUDict();
    public static Map<List<Phoneme>, Set<String>> lastSylRhymeDict = new HashMap<>();
    public static Map<List<List<Phoneme>>, Set<String>> last2SylRhymeDict = new HashMap<>();
    public static Map<List<List<Phoneme>>, Set<String>> last3SylRhymeDict = new HashMap<>();
    private static Map<String, WordSyllables> syllableDict = loadSyllableDicts();
//    private static Map<String, Pair<Integer, MannerOfArticulation>> phonesDict = loadPhonesDict();
//    private static List<Pair<String, MannerOfArticulation>> reversePhonesDict = loadReversePhonesDict();
    private static G2PConverter converter = new G2PConverter(Utils.rootPath + "local-data/phonemes/pron-dict/model.fst.ser");

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
                StressedPhoneme sPhone;
                int stress, parenIdx;
                List<Pronunciation> newList = null;

                while ((line = bf.readLine()) != null) {
                    if (line.startsWith(";;;"))
                        continue;

                    lineSplit = line.split("  ");
                    phonesSplit = lineSplit[1].split(" ");
                    phones = new Pronunciation();

                    for (String phone : phonesSplit) {
                        if (phone.length() == 3) {// we assume that any phoneme with three chars, the third char is the stress
                            stress = Integer.parseInt(phone.substring(2, 3));
                            phone = phone.substring(0, 2);
                        } else {
                            stress = -1;
                        }
                        sPhone = new StressedPhoneme(Phoneme.valueOf(phone), stress); //TODO does valueOf() here work?
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
                    // Utils.promptEnterKey("");
                }

                bf.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

        return cmuDict;
    }

    public static Map<String, WordSyllables> loadSyllableDicts() {
        Map<String, WordSyllables> result = new HashMap<>();
        for (Map.Entry<String, List<Pronunciation>> entry : cmuDict.entrySet()) {
            if (entry.getKey().equals("SWIMSUIT")) {
                Utils.print("stop for test");
            }
            WordSyllables syllables = SyllableParser.algorithmicallyParse(entry.getValue().get(0));
            result.put(entry.getKey(), syllables);  //TODO it gets the top pronunciation, make sure that's right
            int nSyl = syllables.size();
            if (nSyl > 0) {
                Syllable ultimate = syllables.get(syllables.size() - 1);
                Set<String> oldSet1 = lastSylRhymeDict.get(ultimate.getRhyme());
                if (oldSet1 == null)
                    oldSet1 = new HashSet<>();
                oldSet1.add(entry.getKey());
                lastSylRhymeDict.put(ultimate.getRhyme(), oldSet1);
                if (nSyl > 1) {
                    List<List<Phoneme>> list1 = new ArrayList<>();
                    Syllable penultimate = syllables.get(syllables.size() - 2);
                    list1.add(penultimate.getRhyme());
                    list1.add(ultimate.getPhonemes());
                    Set<String> oldSet2 = lastSylRhymeDict.get(list1);
                    if (oldSet2 == null)
                        oldSet2 = new HashSet<>();
                    oldSet2.add(entry.getKey());
                    last2SylRhymeDict.put(list1, oldSet2);
                    if (nSyl > 2) {
                        List<List<Phoneme>> list2 = new ArrayList<>();
                        Syllable antepenultimate = syllables.get(syllables.size() - 3);
                        list2.add(antepenultimate.getRhyme());
                        list2.add(penultimate.getPhonemes());
                        list2.add(ultimate.getPhonemes());
                        Set<String> oldSet3 = lastSylRhymeDict.get(list2);
                        if (oldSet3 == null)
                            oldSet3 = new HashSet<>();
                        oldSet3.add(entry.getKey());
                        last3SylRhymeDict.put(list2, oldSet3);
                    }
                }
            }
        }
        return result;
    }

    public static List<Phoneme> getRhymeOfLastXSyllables(String s, int x) {
        if (x < 1 || x > s.length())
            return null;
        else {
            List<Syllable> syllables = getSyllables(s);
            List<Syllable> shavedSyllables = getLastXSyllables(syllables, x);
            List<Phoneme> rhymeOfLastXSyllables = new ArrayList<>();
            rhymeOfLastXSyllables.addAll(shavedSyllables.get(0).getRhyme());
            for (int i = 1; i < shavedSyllables.size(); i++)
                rhymeOfLastXSyllables.addAll(shavedSyllables.get(i).getPhonemes());
            return rhymeOfLastXSyllables;
        }
    }

    public static List<Syllable> getLastXSyllables(String s, int x) {
        if (x < 1 || x > s.length())
            return null;
        else
            return getLastXSyllables(Phoneticizer.getSyllables(s), x);
    }

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
//            String line, phoneme;
//            MannerOfArticulation category;
//            int lineNum = 0;
//            while ((line = bf.readLine()) != null) {
//                lineSplit = line.split("\t");
//                phoneme = lineSplit[0];
//                category = MannerOfArticulation.valueOf(lineSplit[1]);
//                phonesDict.put(phoneme, new Pair<Integer, MannerOfArticulation>(lineNum++, category));
//                reversePhonesDict.add(new Pair<String, MannerOfArticulation>(phoneme, category));
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
//                List<StressedPhoneme[]> phones = getPhonesForXLastSyllables(tests[k], j);
//                for (StressedPhoneme[] stressedPhones : phones) {
//                    System.out.println("\t" + Arrays.toString(readable(stressedPhones)));
//                }
//            }
//        }

        //print entire cmu dictionary
//        for (String string : cmuDict.keySet()) {
//            List<StressedPhoneme[]> list = cmuDict.get(string);
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
            System.out.println("Pronunciation for \"" + test + "\"");
            List<Pronunciation> phones = getPronunciations(test);
            for (List<StressedPhoneme> stressedPhonemes : phones) {
                System.out.println("\t" + Arrays.toString(readable(stressedPhonemes)));
            }
        }
    }

//    /**
//     * Returns datastruct mapping phonemes to phoneme category
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
//                pronunciationChoices = new ArrayList<StressedPhoneme[]>();
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
//            for (List<StressedPhoneme> prevPhone : prevPhones) {
//                for (List<StressedPhoneme> pronunciationChoice : pronunciationChoices) {
//                    nextPhones.addAll(prevPhone, pronunciationChoice);
//                }
//            }
//        }
//
//        prevPhones = nextPhones;
    }

    public static WordSyllables getSyllables(String s) {
        return syllableDict.get(s.toUpperCase());
    }

        /**
         * Returns a list of ways the input string could be pronounced. If word is in cmu dictionary, then CMU entry is returned, otherwise G2Pconverter is used to guess.
         * @param string
         * @return
         */
    public static List<Pronunciation> getPronunciationsMultipleWords(String string) {
        List<Pronunciation> prevPhones = null, nextPhones, pronunciationChoices;

        for (String s : string.toUpperCase().trim().split("[^A-Z0-9']+")) {
            prevPhones.add(getTopPronunciation(s));
        }

        return prevPhones == null? new ArrayList<>(): prevPhones;
    }


//    private static StressedPhoneme[] parse(Path path) {
//        if (path == null) return null;
//        ArrayList<String> pathPhones = path.getPath();
//        StressedPhoneme[] phones = new StressedPhoneme[pathPhones.size()];
//        StressedPhoneme sPhone;
//        String phoneme;
//
//        for (int i = 0; i < pathPhones.size(); i++) {
//            phoneme = pathPhones.get(i);
//            sPhone = new StressedPhoneme(phonesDict.get(phoneme).getFirst(), -1);
//            phones[i] = sPhone;
//        }
//
//        return phones;
//    }

//    public static String intToString(int phoneInt) {
//        return reversePhonesDict.get(phoneInt).getFirst();
//    }

    public static String[] readable(List<StressedPhoneme> word1sPs) {
        String[] returnVal = new String[word1sPs.size()];

        for (int i = 0; i < returnVal.length; i++) {
            returnVal[i] = word1sPs.get(i).phoneme.toString() + ":" + word1sPs.get(i).stress;
        }

        return returnVal;
    }

//    public static List<StressedPhoneme[]> getPhonesForXLastSyllables(String string, int num) {
//        if (num == 0 || string.length() == 0)
//            return new ArrayList<StressedPhoneme[]>();
//
//        List<StressedPhoneme[]> returnList = new ArrayList<StressedPhoneme[]>(), pronunciationChoices;
//        Map<StressedPhoneme[],Integer> prevPhones = null, nextPhones = null;
//        String[] words = string.toUpperCase().trim().split("[^A-Z0-9']+");
//        int vowelCount, vowelCountForPrevPhone, start;
//        String s;
//        for (int i = words.length-1; i >= 0; i--) {
//            s = words[i];
//            if (s.length() == 0 || stopRhymes.contains(s)) continue;
//            pronunciationChoices = cmuDict.get(s);
//
//            if (pronunciationChoices == null) {
//                pronunciationChoices = new ArrayList<StressedPhoneme[]>();
//                for(Path path: converter.phoneticize(s.replaceAll("[^A-Z0-9]", " "),3)) {
//                    pronunciationChoices.add(parse(path));
//                }
//            }
//
//            if (prevPhones == null) { // if this is the first word
//                nextPhones = new HashMap<StressedPhoneme[],Integer>();
//                // go through each pronunciation and
//                for (StressedPhoneme[] phones: pronunciationChoices) {
//                    vowelCount = 0;
//                    start = phones.length;
//                    while (vowelCount < num && start > 0) {
//                        if (reversePhonesDict.get(phones[--start].phoneme).getSecond() == MannerOfArticulation.VOWEL) {
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
//                nextPhones = new HashMap<StressedPhoneme[], Integer>();
//                for (Entry<StressedPhoneme[],Integer> prevPhone : prevPhones.entrySet()) {
//                    vowelCountForPrevPhone = prevPhone.getValue();
//                    for (StressedPhoneme[] pronunciationChoice: pronunciationChoices) {
//                        vowelCount = 0;
//                        start = pronunciationChoice.length;
//                        while (vowelCount < num && start > 0) {
//                            if (reversePhonesDict.get(pronunciationChoice[--start].phoneme).getSecond() == MannerOfArticulation.VOWEL) {
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

//    public static StressedPhoneme[] getPhonesForLastSyllables(StressedPhoneme[] phones, int num) {
//        int start = phones.length;
//
//        if (num == 0 || start == 0) {
//            return new StressedPhoneme[0];
//        }
//
//        int syllableCount = 0;
//
//        while (syllableCount < num && start > 0) {
//            if (reversePhonesDict.get(phones[--start].phoneme).getSecond() == MannerOfArticulation.VOWEL) {
//                syllableCount++;
//            }
//        }
//
//        return Arrays.copyOfRange(phones, start, phones.length);
//    }

    public static StressedPhoneme[] getLastSyllable(StressedPhoneme[] phones, int offsetFromEnd) {
        int start = phones.length;

        if (start == 0) {
            return new StressedPhoneme[0];
        }

        int syllableCount = 0;
        int lastVowelPosition = start;

        while (syllableCount <= offsetFromEnd && start > 0) {
            if (phones[--start].phoneme.isVowel()) {
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

//    public static boolean isVowel(int phoneme) {
//        return reversePhonesDict.get(phoneme).getSecond() == MannerOfArticulation.VOWEL;
//    }

//    public static MannerOfArticulation getCategory(int phoneme) {
//        return reversePhonesDict.get(phoneme).getSecond();
//    }

//    public static MannerOfArticulation getGeneralCategory(int phoneme) {
//        MannerOfArticulation cat = getCategory(phoneme);
//        if(cat == MannerOfArticulation.AFFRICATE)
//            return MannerOfArticulation.FRICATIVE;
//        else
//            return cat;
//    }

//    public static StressedPhoneme[] getPronunciationForChar(char c) {
//        List<StressedPhoneme[]> pronuns = cmuDict.get("" + Character.toUpperCase(c));
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
            return getPronunciationForWord(word.getSpelling());
        }
        return null;
    }

    public static WordSyllables getSyllablesForWord(String string) {
        if (string != null && string.length() > 0 && string.matches("\\w+")) {
            return getSyllables(string.toUpperCase());
        }
        return null;
    }

    public static WordSyllables getSyllablesForWord(Word word) {
        if (word.getClass() != Punctuation.class) {
            return getSyllablesForWord(word.getSpelling());
        }
        return null;
    }

}





















































