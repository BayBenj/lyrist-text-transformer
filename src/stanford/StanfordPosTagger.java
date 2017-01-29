//package stanford_nlp;
//
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//
//import edu.stanford.nlp.ling.*;
//import edu.stanford.nlp.process.DocumentPreprocessor;
//import edu.stanford.nlp.tagger.maxent.MaxentTagger;
//import edu.stanford.nlp.util.CoreMap;
//import elements.Pos;
//import elements.*;
//import elements.Word;
//import word2vec.W2vSuggestion;
//import word2vec.W2vWordSuggestion;
//
//public final class StanfordPosTagger {
//
//    //private final String taggerPath = "/Applications/Cellar/stanford-parser/3.6.0/libexec/models/wsj-0-18-bidirectional-nodistsim.tagger";
//    //private final MaxentTagger tagger = new MaxentTagger(taggerPath);
//
//    //TODO: eventually make interface so StandfordPosTagger isn't dependent upon Lyrist classes.
//    //TODO: Understand taggerpath
//    //TODO: Understand grammatical structure
//    //ArrayList<GrammaticalStructure> gs = new ArrayList<GrammaticalStructure>();
//
//    public void tagPoSForSongSegment(SongElement segment) {
//        if (segment instanceof Song)
//            this.tagPoSForSong((Song)segment);
//
//        else if (segment instanceof Stanza)
//            this.tagPoSForStanza((Stanza)segment);
//
//        else if (segment instanceof Line)
//            this.tagPoSForLine((Line)segment);
//
//        else if (segment instanceof Word)
//            this.tagPoSForWord((Word)segment);
//    }
//
//    public List<TaggedWord> tagPosForRawSong(String rawSong) {
//        String[] words = rawSong.split("\\s");
//        List<HasWord> sentence = new ArrayList<HasWord>();
//        for (String s : words)
//            sentence.add(new edu.stanford.nlp.ling.Word(s.toLowerCase()));
//        return tagger.tagSentence(sentence);
//    }
//
//    public void tagPoSForSong(Song elements) {
//        for (int i = 0; i < elements.getSize(); i ++) {
//            Stanza tempStanza = elements.getStanzas().get(i);
////            posSong.add(tagPoSForStanza(tempStanza));
//            this.tagPoSForStanza(tempStanza);
//        }
////        return elements;
//    }
//
//    public void tagPoSForStanza(Stanza stanza) {
//        for (int i = 0; i < stanza.getSize(); i ++) {
//            Line tempLine = stanza.getLines().get(i);
////            posStanza.add(tagPoSForLine(tempLine));
//            this.tagPoSForLine(tempLine);
//        }
////        return stanza;
//    }
//
//    public void tagPoSForLine(Line line) {
//        List<HasWord> sentence = new ArrayList<HasWord>();
//        for (Word w : line.getWords())
//            sentence.add(new edu.stanford.nlp.ling.Word(w.getLowerSpelling().toLowerCase()));
//        List<TaggedWord> tagged = tagger.tagSentence(sentence);
//        for (int i = 0; i < line.getSize(); i++) {
//            line.getWords().get(i).setParts(Pos.valueOf(tagged.get(i).tag()));
//        }
////        return line;
//    }
//
//    public void tagPoSForWord(Word word) {
//        if (word.getLowerSpelling().equals("")) {
//            word.setParts(Pos.UNKNOWN);
//            return;
//        }
//        String taggedString = tagger.tagString(word.getLowerSpelling().toLowerCase());
//        taggedString = taggedString.replaceAll("\\s+","");
//        String[] splitString = taggedString.split("_");
//        try {
//            word.setParts(Pos.valueOf(splitString[splitString.length - 1].toUpperCase()));
//        }
//        catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            System.out.println("Word: " + word.toString());
//            System.out.println("taggedString: " + taggedString);
//            System.out.println("splitString: " + splitString);
//            word.setParts(Pos.UNKNOWN);
//        }
////        return word;
//    }
//
//    public HashSet<W2vWordSuggestion> tagPoSForW2vSuggestions(HashSet<W2vSuggestion> suggestions, Map.Entry<Word, CoreMap> oldWordAndSentence) {
//        HashSet<W2vWordSuggestion> result = new HashSet<W2vWordSuggestion>();
//        for (W2vSuggestion suggestion : suggestions) {
//            Word word = new Word(suggestion.getString());
//            String spelling = word.getLowerSpelling();
//            if (    spelling.equals("") ||
//                    spelling.contains("_") ||
//                    spelling.contains(",") ||
//                    spelling.contains("'") ){
//                word.setParts(Pos.UNKNOWN);
//            }
//            else {
//                String taggedString = tagger.tagString(spelling).toLowerCase();
//                taggedString = taggedString.replaceAll("\\s+","");
//                String[] splitString = taggedString.split("_");
//                try {
//                    word.setParts(Pos.valueOf(splitString[splitString.length - 1].toUpperCase()));
//                }
//                catch (IllegalArgumentException e) {
//                    System.out.println("COULDN'T FIND THE POS FOR A WORD!");
//                    e.printStackTrace();
//                    System.out.println("Word: " + spelling);
//                    System.out.println("taggedString: " + taggedString);
//                    System.out.println("splitString: " + splitString);
//                    word.setParts(Pos.UNKNOWN);
//                }
//            }
//            result.add(new W2vWordSuggestion(word, suggestion.getCosineDistance()));
//        }
//        return result;
//    }
//
//
//    public ArrayList<ArrayList<TaggedWord>> tagPoSForStanza(ArrayList<ArrayList<Word>> stanza) {
//        ArrayList<ArrayList<TaggedWord>> result = new ArrayList<ArrayList<TaggedWord>>();
//        //String modelPath = DependencyParser.DEFAULT_MODEL;
//	    String taggerPath = "/Applications/Cellar/stanford-parser/3.6.0/libexec/models/wsj-0-18-bidirectional-nodistsim.tagger";
//
////	    for (int argIndex = 0; argIndex < args.length; ) {
////	      switch (args[argIndex]) {
////	        case "-tagger":
////	          taggerPath = args[argIndex + 1];
////	          argIndex += 2;
////	          break;
////	        case "-model":
////	          modelPath = args[argIndex + 1];
////	          argIndex += 2;
////	          break;
////	        default:
////	          throw new RuntimeException("Unknown argument " + args[argIndex]);
////	      }
////	    }
//
//	    //String spelling = "I can almost always tell when movies use fake dinosaurs.";
//
//
//        //THIS CLASS IS THE ROOT OF NO-ANALOGY ERRORS
//
//
////          MUCH SLOWER THAN BELOW VERSION
////        for (int i = 0; i < lyrics.getLines().size(); i++) {
////            String spelling = "";
////            for (int j = 0; j < lyrics.getLines().get(i).size(); j++) {
////                spelling += lyrics.getLines().get(i).get(j) + " ";
////            }
////            spelling += ".";
////            DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(spelling));
////            MaxentTagger tagger = new MaxentTagger(taggerPath);
////            for (List<HasWord> sentence : tokenizer) {
////                List<TaggedWord> tagged = tagger.tagSentence(sentence);
////                this.taggedWords.add((ArrayList<TaggedWord>) tagged);
////            }
////        }
//
//            String text = "";
//
//            for (int i = 0; i < stanza.size(); i++) {
//                for (int j = 0; j < stanza.get(i).size(); j++) {
//                    text += stanza.get(i).get(j).getLowerSpelling() + " ";
//                }
//                text += ".";
//            }
//
//            MaxentTagger tagger = new MaxentTagger(taggerPath);
//            //DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);
//
//            DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
//            for (List<HasWord> sentence : tokenizer) {
//                List<TaggedWord> tagged = tagger.tagSentence(sentence);
//                result.add((ArrayList<TaggedWord>) tagged);
//                //GrammaticalStructure gs1 = parser.predict(tagged);
////                ArrayList<TaggedWord> periodlessTaggedSentence = new ArrayList();
////                for (int i = 0; i < tagged.size(); i++) {
////                    if (!tagged.get(i).value().equals(".")) {
////                        periodlessTaggedSentence.add(tagged.get(i));
////                    }
////                }
////                this.taggedWords.add(periodlessTaggedSentence);
//
//                //this.gs.add(gs1);
//
//                // Print typed dependencies
//                // System.err.println(gs);
//            }
//
//            return result;
//	    }
//}
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
//
