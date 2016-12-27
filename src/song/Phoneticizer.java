//package song;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.StringUtils;
//
//import edu.cmu.sphinx.linguist.g2p.G2PConverter;
//import edu.cmu.sphinx.linguist.g2p.Path;
//import song.TabDriver;
//import utils.EnglishNumberToWords;
//import utils.Pair;
//
//public class Phonetecizer {
//
//    private static final String cmuFilePath = TabDriver.dataDir + "/pron_dict/cmudict-0.7b.txt";
//    private static final String phonesFilePath = TabDriver.dataDir + "/pron_dict/cmudict-0.7b.phones.reordered.txt";
//
//    private static Map<String, List<StressedPhone[]>> cmuDict = loadCMUDict();
//    private static Map<String, Pair<Integer, PhoneCategory>> phonesDict = loadPhonesDict();
//    private static List<Pair<String, PhoneCategory>> reversePhonesDict = loadReversePhonesDict();
//    private static G2PConverter converter = new G2PConverter(TabDriver.dataDir + "/pron_dict/model.fst.ser");
//
//    /**
//     * Loads CMU dictionary from file into a datastructure
//     */
//    public static Map<String, List<StressedPhone[]>> loadCMUDict() {
//        loadPhonesDict();
//
//        if (cmuDict == null) {
//            cmuDict = new HashMap<String, List<StressedPhone[]>>();
//
//            try {
//                BufferedReader bf = new BufferedReader(new FileReader(cmuFilePath));
//
//                String[] lineSplit, phonesSplit;
//                StressedPhone[] phones;
//                String line, phone, key;
//                StressedPhone sPhone;
//                int phonesLen, stress, parenIdx;
//                ArrayList<StressedPhone[]> newList = null;
//
//                while ((line = bf.readLine()) != null) {
//                    if (line.startsWith(";;;"))
//                        continue;
//
//                    lineSplit = line.split("  ");
//                    phonesSplit = lineSplit[1].split(" ");
//                    phones = new StressedPhone[phonesSplit.length];
//                    phonesLen = phones.length;
//
//                    for (int i = 0; i < phonesLen; i++) {
//                        phone = phonesSplit[i];
//                        if (phone.length() == 3) {// we assume that any phone with three chars, the third char is the stress
//                            stress = Integer.parseInt(phone.substring(2, 3));
//                            phone = phone.substring(0, 2);
//                        } else {
//                            stress = -1;
//                        }
//                        sPhone = new StressedPhone(phonesDict.get(phone).getFirst(), stress);
//                        phones[i] = sPhone;
//                    }
//                    // System.out.println(lineSplit[0] + ":" + Arrays.toString(phones));
//
//                    key = lineSplit[0];
//
//                    parenIdx = key.indexOf('(', 1);
//                    if (parenIdx == -1) {
//                        newList = new ArrayList<StressedPhone[]>();
//                        cmuDict.put(key, newList);
//                        newList.add(phones);
//                    } else {
//                        assert(cmuDict.containsKey(key.substring(0, parenIdx)));
//                        newList.add(phones);
//                    }
//                    // Utils.promptEnterKey("");
//                }
//
//                bf.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new RuntimeException();
//            }
//        }
//
//        return cmuDict;
//    }
//
//    /**
//     * returns loaded dict if not null, otherwise loads dict from file and returns dict
//     * @return
//     */
//    public static Map<String, Pair<Integer, PhoneCategory>> loadPhonesDict() {
//        if (phonesDict == null) {
//            loadPhonesDicts();
//        }
//
//        return phonesDict;
//    }
//
//    /**
//     * @throws FileNotFoundException
//     * @throws IOException
//     */
//    private static void loadPhonesDicts() {
//        phonesDict = new HashMap<String, Pair<Integer, PhoneCategory>>();
//        reversePhonesDict = new ArrayList<Pair<String, PhoneCategory>>();
//
//        try {
//
//            BufferedReader bf = new BufferedReader(new FileReader(phonesFilePath));
//
//            String[] lineSplit;
//            String line, phone;
//            PhoneCategory category;
//            int lineNum = 0;
//            while ((line = bf.readLine()) != null) {
//                lineSplit = line.split("\t");
//                phone = lineSplit[0];
//                category = PhoneCategory.valueOf(lineSplit[1]);
//                phonesDict.put(phone, new Pair<Integer, PhoneCategory>(lineNum++, category));
//                reversePhonesDict.add(new Pair<String, PhoneCategory>(phone, category));
//            }
//
//            bf.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        System.out.println("Loading CMU...");
//        Phonetecizer.loadCMUDict();
//
//        String alphabet = "abcdeghijklmnopqrstuvwyz1234567890";
//        for (int i = 0; i < alphabet.length(); i++) {
//            getPronunciationForChar(alphabet.charAt(i));
//        }
//
//        String[] tests = new String[]{"Hey, wind, oh","Hey, wind,", "aw","","aw, aw, aw,"};
//
//        for (int k = 0; k < tests.length; k++) {
//            for (int j = 0; j < 5; j++) {
//                System.out.println("Getting last " + j + " syllables of " + tests[k]);
//                List<StressedPhone[]> phones = getPhonesForXLastSyllables(tests[k], j);
//                for (StressedPhone[] stressedPhones : phones) {
//                    System.out.println("\t" + Arrays.toString(readable(stressedPhones)));
//                }
//            }
//        }
//
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
//
//
//        tests = new String[]{"Potatoes, tomatoes, windy","hey-you, i.o.u. nu__in","namaste","schtoikandikes","lichtenstein","avadacadabrax"};
//        for (String test : tests) {
//            System.out.println("Pronunciation for \"" + test + "\"");
//            List<StressedPhone[]> phones = getPhones(test);
//            for (StressedPhone[] stressedPhones : phones) {
//                System.out.println("\t" + Arrays.toString(readable(stressedPhones)));
//            }
//        }
//    }
//
//    /**
//     * Returns datastruct mapping phonemes to phoneme category
//     * @return
//     */
//    public static List<Pair<String, PhoneCategory>> loadReversePhonesDict() {
//        if (reversePhonesDict == null) {
//            loadPhonesDicts();
//        }
//
//        return reversePhonesDict;
//    }
//
//    /**
//     * phonemes that should be ignored when computing rhymes
//     */
//    private static Set<String> stopRhymes = new HashSet<String>(Arrays.asList("AW","OH"));
//
//    /**
//     * Returns a list of ways the input string could be pronounced. If word is in cmu dictionary, then CMU entry is returned, otherwise G2Pconverter is used to guess.
//     * @param string
//     * @return
//     */
//    public static List<StressedPhone[]> getPhones(String string) {
//        List<StressedPhone[]> prevPhones = null, nextPhones, pronunciationChoices;
//
//        for (String s : string.toUpperCase().trim().split("[^A-Z0-9']+")) {
//            if (s.length() == 0) continue; // || stopRhymes.contains(s)) continue; This second condition should be handled elsewhere.
//            if (StringUtils.isNumeric(s)) {
//                pronunciationChoices = getPhones(EnglishNumberToWords.convert(Integer.parseInt(s)));
//            } else {
//                pronunciationChoices = cmuDict.get(s);
//            }
//
//            if (pronunciationChoices == null) {
//                pronunciationChoices = new ArrayList<StressedPhone[]>();
//                s = s.replaceAll("[^A-Z0-9]", " ");
//
//                for(Path path: converter.phoneticize(s,3)) {
//                    pronunciationChoices.add(parse(path));
//                }
//            }
//
//            if (prevPhones == null) {
//                nextPhones = pronunciationChoices;
//            } else {
//                nextPhones = new ArrayList<StressedPhone[]>();
//                for (StressedPhone[] prevPhone : prevPhones) {
//                    for (StressedPhone[] pronunciationChoice : pronunciationChoices) {
//                        nextPhones.add(ArrayUtils.addAll(prevPhone, pronunciationChoice));
//                    }
//                }
//            }
//
//            prevPhones = nextPhones;
//        }
//
//        return prevPhones == null? new ArrayList<StressedPhone[]>(): prevPhones;
//    }
//
//
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
//
//    public static String intToString(int phoneInt) {
//        return reversePhonesDict.get(phoneInt).getFirst();
//    }
//
//    public static String[] readable(StressedPhone[] word1sPs) {
//        String[] returnVal = new String[word1sPs.length];
//
//        for (int i = 0; i < returnVal.length; i++) {
//            returnVal[i] = intToString(word1sPs[i].phone) + ":" + word1sPs[i].stress;
//        }
//
//        return returnVal;
//    }
//
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
//                        if (reversePhonesDict.get(phones[--start].phone).getSecond() == PhoneCategory.vowel) {
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
//                            if (reversePhonesDict.get(pronunciationChoice[--start].phone).getSecond() == PhoneCategory.vowel) {
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
//
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
//            if (reversePhonesDict.get(phones[--start].phone).getSecond() == PhoneCategory.vowel) {
//                syllableCount++;
//            }
//        }
//
//        return Arrays.copyOfRange(phones, start, phones.length);
//    }
//
//
//    public static StressedPhone[] getLastSyllable(StressedPhone[] phones, int offsetFromEnd) {
//        int start = phones.length;
//
//        if (start == 0) {
//            return new StressedPhone[0];
//        }
//
//        int syllableCount = 0;
//        int lastVowelPosition = start;
//
//        while (syllableCount <= offsetFromEnd && start > 0) {
//            if (isVowel(phones[--start].phone)) {
//                if (syllableCount == offsetFromEnd)
//                {
//                    return Arrays.copyOfRange(phones, start, lastVowelPosition);
//                }
//                else {
//                    lastVowelPosition = start;
//                    syllableCount++;
//                }
//            }
//        }
//
//        return Arrays.copyOfRange(phones, 0, lastVowelPosition);
//    }
//
//    public static boolean isVowel(int phone) {
//        return reversePhonesDict.get(phone).getSecond() == PhoneCategory.vowel;
//    }
//
//    public static PhoneCategory getCategory(int phone) {
//        return reversePhonesDict.get(phone).getSecond();
//    }
//
//    public static PhoneCategory getGeneralCategory(int phone) {
//        PhoneCategory cat = getCategory(phone);
//        if(cat == PhoneCategory.affricate)
//            return PhoneCategory.fricative;
//        else
//            return cat;
//    }
//
//    public static StressedPhone[] getPronunciationForChar(char c) {
//        List<StressedPhone[]> pronuns = cmuDict.get("" + Character.toUpperCase(c));
//        if (pronuns == null) {
//            return parse(converter.phoneticize("" + c, 1).get(0));
//        }
//        return pronuns.get(pronuns.size()-1);
//    }
//
//}
