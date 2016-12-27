//package song;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//
//import harmony.Chord;
//import tabcomplete.rawsheet.ChordSheet;
//import tabcomplete.rawsheet.LyricSheet;
//import tabcomplete.rawsheet.RawDataLoader;
//import tabcomplete.tab.CompletedTab;
//import tabcomplete.utils.Serializer;
//import tabcomplete.validate.TabValidator;
//
//public class TabDriver {
//
//    private static boolean deserializeLyrics = true;
//    private static boolean serializeLyrics = false;
//    private static boolean deserializeChords = true;
//    private static boolean serializeChords = false;
//    private static boolean deserializeValidatedTabs = false;
//    private static boolean serializeValidatedTabs = true;
//
//    public static boolean mini_data_set = false;
//    private static boolean test_accuracy = false;
//
//    // Filtered run
////	public static String[] filtersArray = new String[]{"billy joel", "elton john","mika","chicago","boston","ben folds","doobie brothers","eagles","beatles","bruno mars","josh groban","earth wind and fire"}; // remember, no "^the "
////	public static String filtersName = "80s"; // what should be appended to the file when saving results with the above filters
//    public static String[] filtersArray = new String[]{}; // remember, no "^the "
//    public static String filtersName = ""; // what should be appended to the file when saving results with the above filters
//
//    public static final Set<String> filters = new HashSet<String>(Arrays.asList(filtersArray));
//
//    public final static String dataDir = "../../data";
//    public final static String serializedDataDir = dataDir + "/ser";
//    private static String serializedLyricsPath = serializedDataDir + "/" + (mini_data_set?"":"new_") +"lyrics" + (filters.size()==0?"":"." + filtersName) + ".ser";
//    private static String serializedTabsPath = serializedDataDir + "/" + (mini_data_set?"":"new_") +"tabs" + (filters.size()==0?"":"." + filtersName) + ".ser";
//    private static String serializedCompleteTabsPath = serializedDataDir + "/" + (mini_data_set?"":"new_") +"complete_tabs" + (filters.size()==0?"":"." + filtersName) + ".ser";
//    private static String correctTabsPath = dataDir + "/complete_tabs";
//    private static boolean loadTabsWithLyricsOnly = true;
//
//    public static void main(String[] args) throws IOException {
//        loadValidatedTabs();
//    }
//
//    /**
//     * @return
//     * @throws FileNotFoundException
//     * @throws IOException
//     */
//    @SuppressWarnings("unchecked")
//    public static List<CompletedTab> loadValidatedTabs() {
//        Map<String, Map<String, List<LyricSheet>>> lyricSheets = null;
//        Map<String, Map<String, List<ChordSheet>>> chordSheets = null;
//        List<CompletedTab> validatedTabs = null;
//        try {
//            if (!deserializeValidatedTabs) {
//                lyricSheets = (deserializeLyrics? (Map<String, Map<String, List<LyricSheet>>>) Serializer.load(serializedLyricsPath): RawDataLoader.loadLyricSheets(null));
//                if (lyricSheets == null) return null;
//                if(!deserializeLyrics && serializeLyrics) {
//                    Serializer.serialize(lyricSheets, serializedLyricsPath);
//                }
//
//                int count = 0;
//                for(Map<String, List<LyricSheet>> songsByArtist:lyricSheets.values()) {
//                    count+= songsByArtist.size();
//                }
//                System.out.println("Loaded " + count + " lyric sheet(s) for " + lyricSheets.size() + " artist(s)");
//                System.out.println(LyricSheet.parseSummary());
//
//
//                if (!deserializeChords && loadTabsWithLyricsOnly)  RawDataLoader.setLyricKeys(lyricSheets);
//                chordSheets = (deserializeChords? (Map<String, Map<String, List<ChordSheet>>>) Serializer.load(serializedTabsPath): RawDataLoader.loadChordSheets());
//                if (chordSheets == null) return null;
//                if(!deserializeChords && serializeChords) {
//                    Serializer.serialize(chordSheets, serializedTabsPath);
//                }
//
//
//                PrintWriter pw = new PrintWriter(new File("chordseqs.txt"));
//                for (Map<String, List<ChordSheet>> chordSheetsByArtist : chordSheets.values()) {
//                    for (List<ChordSheet> css : chordSheetsByArtist.values()) {
//                        for (ChordSheet cs : css) {
//                            for(List<SortedMap<Integer, Chord>> chords : cs.getChords()){
//                                if (chords == null) continue;
//                                for(SortedMap<Integer, Chord> chords2: chords) {
//                                    if (chords2 == null) continue;
//                                    for (Chord chord : chords2.values()) {
//                                        if (chord == null) continue;
//                                        pw.write(chord.toString());
//                                        pw.write(" ");
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    pw.write("\n");
//                }
//                pw.close();
//
//                count = 0;
//                for(Map<String, List<ChordSheet>> songsByArtist:chordSheets.values()) {
//                    count+= songsByArtist.size();
//                }
//                System.out.println("Loaded " + count + " chord sheet(s) for " + chordSheets.size() + " artist(s)");
//                System.out.println(ChordSheet.parseSummary());
//            }
//
//            validatedTabs = (deserializeValidatedTabs? (List<CompletedTab>) Serializer.load(serializedCompleteTabsPath) : TabValidator.validateTabs(lyricSheets, chordSheets));
//            if(!deserializeValidatedTabs && serializeValidatedTabs) {
//                Serializer.serialize(validatedTabs, serializedCompleteTabsPath);
//            }
//            System.out.println("Found " + validatedTabs.size() + " completed tab(s)");
//
//
//            if (test_accuracy) {
//                int sampleSize = Math.min(1000, validatedTabs.size());
//                System.out.println("Saving " + sampleSize + " completed tabs to file");
//                List<CompletedTab> correctTabs = new ArrayList<CompletedTab>(sampleSize);
//
//                List<Integer> list = new ArrayList<Integer>(validatedTabs.size());
//                for (int i = 0; i < validatedTabs.size(); i++) {
//                    list.add(new Integer(i));
//                }
//                Collections.shuffle(list);
//                for (int i = 0; i < sampleSize; i++) {
//                    correctTabs.add(validatedTabs.get(list.get(i)));
//                }
//
//                int i = 1;
//                for (CompletedTab completedTab : correctTabs) {
//                    PrintWriter writer  = new PrintWriter(correctTabsPath + "/complete_tab" + i++ + ".txt");
//
//                    writer.println(completedTab);
//
//                    writer.close();
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(-1);
//        }
//
//        return validatedTabs;
//    }
//}
