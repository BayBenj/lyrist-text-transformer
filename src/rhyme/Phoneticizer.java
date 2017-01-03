package rhyme;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import edu.cmu.sphinx.linguist.g2p.G2PConverter;
import edu.cmu.sphinx.linguist.g2p.Path;
import utils.EnglishNumberToWords;
import utils.Pair;
import utils.Utils;

public class Phoneticizer {

//    private static final String cmuFilePath = TabDriver.dataDir + "/pron_dict/cmudict-0.7b.txt";
//    private static final String phonesFilePath = TabDriver.dataDir + "/pron_dict/cmudict-0.7b.phones.reordered.txt";
    private static final String cmuFilePath = Utils.rootPath + "local-data/phonemes/pron-dict/cmudict-0.7b.txt";

    private static Map<String, List<Pronunciation>> cmuDict = loadCMUDict();
    private static Map<String, List<Syllable>> syllableDict = loadSyllableDict();
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
                StressedPhone sPhone;
                int stress, parenIdx;
                List<Pronunciation> newList = null;

                while ((line = bf.readLine()) != null) {
                    if (line.startsWith(";;;"))
                        continue;

                    lineSplit = line.split("  ");
                    phonesSplit = lineSplit[1].split(" ");
                    phones = new Pronunciation();

                    for (String phone : phonesSplit) {
                        if (phone.length() == 3) {// we assume that any phone with three chars, the third char is the stress
                            stress = Integer.parseInt(phone.substring(2, 3));
                            phone = phone.substring(0, 2);
                        } else {
                            stress = -1;
                        }
                        sPhone = new StressedPhone(Phoneme.valueOf(phone), stress); //TODO does valueOf() here work?
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

    public static Map<String, List<Syllable>> loadSyllableDict() {
        Map<String, List<Syllable>> result = new HashMap<>();
        for (Map.Entry<String, List<Pronunciation>> entry : cmuDict.entrySet())
            result.put(entry.getKey(), SyllableParser.parse(entry.getValue().get(0)));  //TODO it gets the top pronunciation, make sure that's right
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
//            String line, phone;
//            MannerOfArticulation category;
//            int lineNum = 0;
//            while ((line = bf.readLine()) != null) {
//                lineSplit = line.split("\t");
//                phone = lineSplit[0];
//                category = MannerOfArticulation.valueOf(lineSplit[1]);
//                phonesDict.put(phone, new Pair<Integer, MannerOfArticulation>(lineNum++, category));
//                reversePhonesDict.add(new Pair<String, MannerOfArticulation>(phone, category));
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
//                List<StressedPhone[]> phones = getPhonesForXLastSyllables(tests[k], j);
//                for (StressedPhone[] stressedPhones : phones) {
//                    System.out.println("\t" + Arrays.toString(readable(stressedPhones)));
//                }
//            }
//        }

        //print entire cmu dictionary
//        for (String string : cmuDict.keySet()) {
//            List<StressedPhone[]> list = cmuDict.get(string);
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
            for (List<StressedPhone> stressedPhones : phones) {
                System.out.println("\t" + Arrays.toString(readable(stressedPhones)));
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
            return getPronunciations(s).get(0);
        //In the event of an out-of-dictinary-word:
//            if (pronunciationChoices == null) {
//                pronunciationChoices = new ArrayList<StressedPhone[]>();
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
//            for (List<StressedPhone> prevPhone : prevPhones) {
//                for (List<StressedPhone> pronunciationChoice : pronunciationChoices) {
//                    nextPhones.addAll(prevPhone, pronunciationChoice);
//                }
//            }
//        }
//
//        prevPhones = nextPhones;
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


//    private static StressedPhone[] parse(Path path) {
//        if (path == null) return null;
//        ArrayList<String> pathPhones = path.getPath();
//        StressedPhone[] phones = new StressedPhone[pathPhones.size()];
//        StressedPhone sPhone;
//        String phone;
//
//        for (int i = 0; i < pathPhones.size(); i++) {
//            phone = pathPhones.get(i);
//            sPhone = new StressedPhone(phonesDict.get(phone).getFirst(), -1);
//            phones[i] = sPhone;
//        }
//
//        return phones;
//    }

//    public static String intToString(int phoneInt) {
//        return reversePhonesDict.get(phoneInt).getFirst();
//    }

    public static String[] readable(List<StressedPhone> word1sPs) {
        String[] returnVal = new String[word1sPs.size()];

        for (int i = 0; i < returnVal.length; i++) {
            returnVal[i] = word1sPs.get(i).phone.toString() + ":" + word1sPs.get(i).stress;
        }

        return returnVal;
    }

//    public static List<StressedPhone[]> getPhonesForXLastSyllables(String string, int num) {
//        if (num == 0 || string.length() == 0)
//            return new ArrayList<StressedPhone[]>();
//
//        List<StressedPhone[]> returnList = new ArrayList<StressedPhone[]>(), pronunciationChoices;
//        Map<StressedPhone[],Integer> prevPhones = null, nextPhones = null;
//        String[] words = string.toUpperCase().trim().split("[^A-Z0-9']+");
//        int vowelCount, vowelCountForPrevPhone, start;
//        String s;
//        for (int i = words.length-1; i >= 0; i--) {
//            s = words[i];
//            if (s.length() == 0 || stopRhymes.contains(s)) continue;
//            pronunciationChoices = cmuDict.get(s);
//
//            if (pronunciationChoices == null) {
//                pronunciationChoices = new ArrayList<StressedPhone[]>();
//                for(Path path: converter.phoneticize(s.replaceAll("[^A-Z0-9]", " "),3)) {
//                    pronunciationChoices.add(parse(path));
//                }
//            }
//
//            if (prevPhones == null) { // if this is the first word
//                nextPhones = new HashMap<StressedPhone[],Integer>();
//                // go through each pronunciation and
//                for (StressedPhone[] phones: pronunciationChoices) {
//                    vowelCount = 0;
//                    start = phones.length;
//                    while (vowelCount < num && start > 0) {
//                        if (reversePhonesDict.get(phones[--start].phone).getSecond() == MannerOfArticulation.VOWEL) {
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
//                nextPhones = new HashMap<StressedPhone[], Integer>();
//                for (Entry<StressedPhone[],Integer> prevPhone : prevPhones.entrySet()) {
//                    vowelCountForPrevPhone = prevPhone.getValue();
//                    for (StressedPhone[] pronunciationChoice: pronunciationChoices) {
//                        vowelCount = 0;
//                        start = pronunciationChoice.length;
//                        while (vowelCount < num && start > 0) {
//                            if (reversePhonesDict.get(pronunciationChoice[--start].phone).getSecond() == MannerOfArticulation.VOWEL) {
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

//    public static StressedPhone[] getPhonesForLastSyllables(StressedPhone[] phones, int num) {
//        int start = phones.length;
//
//        if (num == 0 || start == 0) {
//            return new StressedPhone[0];
//        }
//
//        int syllableCount = 0;
//
//        while (syllableCount < num && start > 0) {
//            if (reversePhonesDict.get(phones[--start].phone).getSecond() == MannerOfArticulation.VOWEL) {
//                syllableCount++;
//            }
//        }
//
//        return Arrays.copyOfRange(phones, start, phones.length);
//    }

    public static StressedPhone[] getLastSyllable(StressedPhone[] phones, int offsetFromEnd) {
        int start = phones.length;

        if (start == 0) {
            return new StressedPhone[0];
        }

        int syllableCount = 0;
        int lastVowelPosition = start;

        while (syllableCount <= offsetFromEnd && start > 0) {
            if (phones[--start].phone.isVowel()) {
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

//    public static boolean isVowel(int phone) {
//        return reversePhonesDict.get(phone).getSecond() == MannerOfArticulation.VOWEL;
//    }

//    public static MannerOfArticulation getCategory(int phone) {
//        return reversePhonesDict.get(phone).getSecond();
//    }

//    public static MannerOfArticulation getGeneralCategory(int phone) {
//        MannerOfArticulation cat = getCategory(phone);
//        if(cat == MannerOfArticulation.AFFRICATE)
//            return MannerOfArticulation.FRICATIVE;
//        else
//            return cat;
//    }

//    public static StressedPhone[] getPronunciationForChar(char c) {
//        List<StressedPhone[]> pronuns = cmuDict.get("" + Character.toUpperCase(c));
//        if (pronuns == null) {
//            return parse(converter.phoneticize("" + c, 1).get(0));
//        }
//        return pronuns.get(pronuns.size()-1);
//    }

    public static boolean cmuDictContains(String word) {
        return cmuDict.containsKey(word.toUpperCase());
    }

}
















































































