//package word2vec;
//
//import edu.stanford.nlp.ling.TaggedWord;
//import external.CommandlineExecutor;
//import misc.LyricSegment;
//import song.Word;
//import stanford_nlp.StanfordPosTagger;
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
//    public static ArrayList<TaggedWord> getAnalogousWords(TaggedWord original1, TaggedWord switch1, TaggedWord original2, int resultSize) {
//        String output = getOutputFileName(original1.value() + "-" + switch1.value(), original2.value());
//        File f = new File(output);
//        if(!f.exists()) {
//            String command = getCommand("word-analogy-new", original1.value(), switch1.value(), original2.value());
//            long startTime = System.nanoTime();
//            CommandlineExecutor.execute(command, output);
//            long endTime = System.nanoTime();
//            System.out.println("external.CommandlineExecutor time for " + original2.value() + ": " + ((endTime - startTime) / 1000000) + " milliseconds (" + ((endTime - startTime) / 1000000000) + " seconds).");
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
//        String pos = modelWord.tag();
//        for (int i = 0; i < unclean.size(); i++) {
//            TaggedWord uncleanWord = unclean.get(i);
//            if (uncleanWord.tag().equals(pos))
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
////        stanford_nlp.StanfordPosTagger spt = new stanford_nlp.StanfordPosTagger(ls);
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
///*
//Where are my replacement lyrics coming from?
//Pop song lyrics
//    Would be best, dataset may be too small. Maybe train model on bigger data but favor word found here?
//Wikipedia
//Google news
// */
//
//
///*
//TODO > Write a method that scans my model and finds every word that is not in a good dictionary. Clean these words out if
//they are indeed low-quality or gibberish.
//TODO add a clean parent method for more specific analogous results that gets more specific parts of speech and categories (eating=present progressive, yellow=color, America=country)
//
// */
//
//
///*
//TODO > Find song themes by averaging all words within. Does doc2vec do this?
// */
//
// /*
//TODO > Write c scripts for every word2vec operation I want
//  */
//
///*
//TODO > In analogies, experiment with:
//a is to b as c is to...
//b is to a as d is to...
//c is to d as a is to...
//d id to c as b is to...
//Are different results yielded? When can I expect the same result?
// */
//
//
///*
//TODO >  Optimize paths and file stuff so that Paul can use it
//*/
//
///*
//TODO > Tell Paul I learned how to decompile third-party tools, it helps a lot.
// */
//
///*
//TODO > Use some sort of DocumentPreprocessor on my spelling corpora.
// */
//
///*
//TODO > Eventually streamline C scripts.
// */
//
///*
//TODO > Eventually document all my code nicely.
// */
//
///*
//TODO > Do lyric switch by averaging the song's vectors to find the meaning, then by replacing the 'meaning'
//word with the 'new meaning word'. Then I can use meaning is to new meaning as current word is to ____.
// */
//
///*
//> Speed up running time if possible w/ Stanford parser
// */
//
///*
//TODO > Somehow ensure that my vector bins have every word that the pop-star database has
// */
//
///*
//TODO > Install better part of speech tagger
// */
//
///*
//TODO > Make original lyrics readable, not from within program
// */
//
///*
//TODO > Why would it show bad results the first runOnWords through, then good results on the second?
// */
//
///*
//TODO > Consider weakening theme, multiply it by a theme-weakening factor.
// */
//
///*
//TODO > Clean and lowercase all my spelling data
// */
//
///*
//TODO > If cosine distance is low enough, use different replacement rather than an analogous replacement.
// */
//
///*
//TODO > Use the Hrjee matrix
// */
//
///*
//TODO > Go through DL4J or Gensim decompiled code to find their c scripts w/ word2vec
// */
//
///*
//TODO > Look into paying for COCA
// */
//
///*
//TODO > Understand "extract rhyme scheme"
// */
//
///*
//TODO > Hava a java class extract data from the vector, then do all word2vec functions for the whole song at once. The main.LyricPack saves needed analogies and send them all at once.
// */
//
///*
//TODO > Consider allowing for multiple analogies to affect one stanza; this word get analogy A, this other word gets analogy B, etc.
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
