//package word2vec;
//
//import edu.stanford.nlp.ling.TaggedWord;
//import main.CommandlineExecutor;
//import misc.LyricSegment;
//import elements.Word;
//import stanford.StanfordPosTagger;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Word2VecInterface {
//
//    /*
//    This is the only class that uses Word2Vec.
//    Sjould be broken up further.
//    Should pass each result through a category/POS test to make sure it makes sense.
//
//    Each method should accept the parsed word.
//     */
//
//    //TODO: update these terminal word2vec commands
//
//    private static String vector_address = "models/vectors-phrase.bin";
//
//    public static ArrayList<TaggedWord> getSimilarWords(TaggedWord original, int resultSize) {
//        String output = getOutputFileName("similar-to", original.value());
//        String command = getCommand("nearby-words", original.value());
//        ArrayList<TaggedWord> similar_words = new ArrayList<TaggedWord>();
//
//        CommandlineExecutor.execute(command, output);
//
//        ArrayList<String> w2v_results = readW2vOutput(output);
//        ArrayList<TaggedWord> close_words = new ArrayList<TaggedWord>();
//        cleanPos(close_words, original);
//        return close_words;
//    }
//
//    public static ArrayList<TaggedWord> getAnalogousWordSuggestions(TaggedWord original1, TaggedWord switch1, TaggedWord original2, int resultSize) {
//        String output = getOutputFileName(original1.value() + "-" + switch1.value(), original2.value());
//        File f = new File(output);
//        if(!f.exists()) {
//            String command = getCommand("word-analogy-new", original1.value(), switch1.value(), original2.value());
//            long startTime = System.nanoTime();
//            CommandlineExecutor.execute(command, output);
//            long endTime = System.nanoTime();
//            System.out.println("main.CommandlineExecutor time for " + original2.value() + ": " + ((endTime - startTime) / 1000000) + " milliseconds (" + ((endTime - startTime) / 1000000000) + " seconds).");
//        }
//
//        ArrayList<String> w2v_results = readW2vOutput(output);
//        long startTime = System.nanoTime();
//        ArrayList<TaggedWord> tagged_w2v_results = stringsToSmartLyrics(w2v_results);
//        long endTime = System.nanoTime();
//        System.out.println("stringsToSmartLyrics_Stanford time for " + original2.value() + ": " + ((endTime - startTime) / 1000000) + " milliseconds (" + ((endTime - startTime) / 1000000000) + " seconds).");
//        ArrayList<TaggedWord> analogous_words = cleanPos(tagged_w2v_results, original2);
//        if (analogous_words.size() == 0) {
//            System.out.println("NO ANALOGOUS WORDS!");
//        }
//        return analogous_words;
//    }
//
//    public static ArrayList<TaggedWord> getWordsSubracting(TaggedWord original, String subtracting, int resultSize) {
//        String output = getOutputFileName("subtracting-" + subtracting, original.value());
//        String command = getCommand("subtract", original.value(), subtracting);
//        ArrayList<TaggedWord> subtracted_words = new ArrayList<TaggedWord>();
//        cleanPos(subtracted_words, original);
//        CommandlineExecutor.execute(command, output);
//
//        ArrayList<String> w2v_results = readW2vOutput(output);
//
//        return subtracted_words;
//    }
//
//    public static ArrayList<TaggedWord> getWordsAdding(TaggedWord original, String adding, int resultSize) {
//        String output = getOutputFileName("adding-" + adding, original.value());
//        String command = getCommand("add", original.value(), adding);
//        ArrayList<TaggedWord> added_words = new ArrayList<TaggedWord>();
//        cleanPos(added_words, original);
//        CommandlineExecutor.execute(command, output);
//
//        ArrayList<String> w2v_results = readW2vOutput(output);
//        return added_words;
//    }
//
//    //TODO: change to private
//    public static ArrayList<TaggedWord> cleanPos(ArrayList<TaggedWord> unclean, TaggedWord modelWord) {
//        ArrayList<TaggedWord> clean = new ArrayList<TaggedWord>();
//        String wordsToPos = modelWord.tag();
//        for (int i = 0; i < unclean.size(); i++) {
//            TaggedWord uncleanWord = unclean.get(i);
//            if (uncleanWord.tag().equals(wordsToPos))
//                clean.add(uncleanWord);
//        }
//        return clean;
//    }
//
//    private static String getOutputFileName(String subAddress, String unique) {
//        StringBuilder modified_address = new StringBuilder(vector_address);
//        String new_address = modified_address.toString();
//        new_address = new_address.replace(".bin", "");
//        new_address = new_address.replace(".gz", "");
//        new_address = new_address.replace("models/", "w2v_results/");
//        File dir = new File("w2v_results");
//        if (!dir.exists()) {
//            try{
//                dir.mkdir();
//            }
//            catch(SecurityException se) {
//                se.printStackTrace();
//            }
//        }
//        dir = new File(new_address);
//        if (!dir.exists()) {
//            try{
//                dir.mkdir();
//            }
//            catch(SecurityException se) {
//                se.printStackTrace();
//            }
//        }
//        dir = new File(new_address + '/' + subAddress);
//        if (!dir.exists()) {
//            try{
//                dir.mkdir();
//            }
//            catch(SecurityException se) {
//                se.printStackTrace();
//            }
//        }
//
//
//        return new_address + "/" + subAddress + "/" + unique + ".txt";
//    }
//
//    private static String getCommand(String command, String... inputs) {
//        String spacedInputs = "";
//        for (int i = 0; i < inputs.length; i++) {
//            if (i == inputs.length - 1)
//                spacedInputs += inputs[i];
//            else
//                spacedInputs += inputs[i] + " ";
//        }
//        return "./" + command + " " + vector_address + " " + spacedInputs;
//    }
//
//    private static ArrayList<String> readW2vOutput(String filename) {
//        ArrayList<String> result = new ArrayList<String>();
//        try {
//            BufferedReader br;
//            br = new BufferedReader(new FileReader(filename));
//
//            String line = br.readLine();
//
//            while (line != null) {
//                result = detabOutputLine(line, result);
//                line = br.readLine();
//            }
//            br.close();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
////    //TODO: change to private
////    public static ArrayList<TaggedWord> stringsToSmartLyrics(ArrayList<String> array) {
////        // TODO: Do something useful with cosine distance instead of trashing it here
////        // TODO: FIX!! THIS IS SLOPPY. Uses Stanford parser inefficiently to tag words. Change Song class.
////        // TODO: is it best to return each word2vec result as its own line of a stanza as it's done here?
////            // It removes any grammatical context, which is good.
////
////
//////        // 2 seconds on normal-already written
//////        // 43 seconds on normal-not yet written (had errors)
//////        // 47 seconds on alternate-already written
////          // 91 seconds on alternate-not yet written (NO ERRORS)
////        List<List<Word>> lll = new ArrayList<List<Word>>();
////        List<Word> ll ;
////        Word l;
////        for (int i = 0; i < array.size(); i += 2) {
////            l = new Word(array.get(i));
////            ll = new ArrayList<Word>();
////            ll.add(l);
////            lll.add(ll);
////        }
////        LyricSegment ls = new LyricSegment(lll);
////        StanfordPosTagger spt = new StanfordPosTagger(ls);
////        ArrayList<TaggedWord> tagged = spt.getPosEz();
////        return tagged;
//        // 6 seconds on normal-already written
//        // 43 seconds on normal-not yet written (had errors)
//        // 7 seconds on alternate-already written
//        // 45 seconds on alternate-not yet written (had errors)
////        List<List<Lyric>> lll = new ArrayList<List<Lyric>>();
////        List<Lyric> ll = new ArrayList<Lyric>();
////        Lyric l;
////        for (int i = 0; i < array.size(); i += 2) {
////            l = new Lyric(array.get(i));
////            ll.add(l);
////        }
////        lll.add(ll);
////        Song ls = new Song(lll);
////        stanford.StanfordPosTagger spt = new stanford.StanfordPosTagger(ls);
////        ArrayList<TaggedWord> tagged = spt.getPosEz();
////        return tagged;
//
//    private static ArrayList<String> detabOutputLine(String line, ArrayList<String> result) {
//        if (line == null || line.startsWith("#")) {
//            //add nothing
//        }
//        else {
//            String[] splitLine = line.split("\\t");
//            for (String strg : splitLine) {
//                result.add(strg);
//            }
//        }
//        return result;
//    }
//
//
//}
//
//
//
// */
//
//
//
//
//
//
//
//
//
//