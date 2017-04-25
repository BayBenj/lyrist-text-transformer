package stanford;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.util.CoreMap;
import main.MultiProgramArgs;
import elements.*;
import elements.Sentence;

import java.util.*;

public class StanfordNlp {

    private static StanfordCoreNLP pipeline;
    private static final String INPUT_TYPE = "annotators";
    private static final String ANNOTATORS = "tokenize, ssplit, pos, lemma, ner";
//    private final String ANNOTATORS = "tokenize, ssplit, pos, lemma, ner, parse, dcoref";
    //private static final MaxentTagger tagger = new MaxentTagger(U.rootPath + "lib/stanford-parser/3.6.0/libexec/models/wsj-0-18-bidirectional-nodistsim.tagger");
    //private final MaxentTagger tagger = new MaxentTagger(U.rootPath + "local-data/models/wordsToPos-tagger/english-left3words/english-bidirectional-distsim.tagger");
    private static DocumentPreprocessor documentPreprocessor;
    private static final TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "untokenizable=noneKeep");
    AbstractSequenceClassifier<CoreLabel> classifier = null;


    public StanfordNlp() {
        this.setupPipeline();
    }

//    public static List<List<CoreLabel>> parseTextCompletelyByPath(String filePath) {
//        setDocumentPreprocessor(filePath);
//        List<List<TaggedWord>> taggedSentences = tokenizeTextFromDocumentPreprocessor();
//        List<List<CoreLabel>> classifiedSentences = new ArrayList<List<CoreLabel>>();
//        for (List<TaggedWord> taggedSentence : taggedSentences)
//            classifiedSentences.add(classifyNamedEntities(taggedSentence));
//        return classifiedSentences;
//    }

//    public static List<List<CoreLabel>> parseTextCompletelyByString(String fullText) {
//        //this.setDocumentPreprocessor(filePath);
//        List<List<HasWord>> taggedSentences = tokenizeTextFromString(fullText);
//        List<List<CoreLabel>> classifiedSentences = new ArrayList<List<CoreLabel>>();
//        for (List<HasWord> taggedSentence : taggedSentences)
//            classifiedSentences.add(classifyNamedEntities(taggedSentence));
//        return classifiedSentences;
//    }

//    public static void setDocumentPreprocessor(String filePath) {
//        documentPreprocessor = new DocumentPreprocessor(filePath);
//        //this.ptbTokenizerFactory.setOptions();
//        documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);
//    }

//    public static List<List<TaggedWord>> tokenizeTextFromDocumentPreprocessor() {
//        List<List<TaggedWord>> taggedSentences = new ArrayList<List<TaggedWord>>();
//        for (List<HasWord> sentence : documentPreprocessor) {
////            List<TaggedWord> taggedSentence = tagger.tagSentence(sentence);
////            taggedSentences.add(taggedSentence);
//        }
//        return taggedSentences;
//    }

//    public static List<List<HasWord>> tokenizeTextFromString(String untokenizedText) {
//        List<List<HasWord>> sentences;
//        StringReader sr = new StringReader(untokenizedText);
//        sentences = MaxentTagger.tokenizeText(new BufferedReader(sr));
//        return sentences;
//    }
//
//    public static List<CoreLabel> classifyNamedEntities(List<? extends HasWord> untaggedSentence) {
//        //classify named entities
//        String serializedClassifier = U.rootPath + "lib/english.all.3class.distsim.crf.ser.gz";
//        AbstractSequenceClassifier<CoreLabel> classifier = null;
//        try {
//            classifier = CRFClassifier.getClassifier(serializedClassifier);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        List<CoreLabel> classifiedSentence = classifier.classifySentence(untaggedSentence);
//        return classifiedSentence;
//    }

    public void setupPipeline() {
        this.buildPipeline();
    }

    private void buildPipeline() {
        Properties props = new Properties();
        props.put(INPUT_TYPE, ANNOTATORS);
        this.pipeline = new StanfordCoreNLP(props);
    }

//    public static List<Sentence> parseWordsToSentences(List<Word> wordInput) {
//        //words to raw text
//        StringBuilder text = new StringBuilder();
//        for (Word word : wordInput) {
//            text.append(word.toString());
//            text.append(" ");
//        }
//        String rawText = text.toString();
//
//        return parseTextToSentences(rawText);
//    }

//    public static List<Sentence> tagSong(Song song) {
//        String rawText = song.toString();
//        List<Sentence> sentences = parseTextToSentences(rawText);
////        List<Word> words = song.getAllWords();
//        int i = 0;
//        int s = 0;
//        int w = 0;
//        List<Stanza> stanzas = song.getStanzas();
//        for (Stanza stanza : stanzas) {
//            List<Line> lines = stanza.getLines();
//            for (Line line : lines) {
//                List<Word> words = line.getWords();
//                for (Word word : words) {
//                    word = sentences.get(s).get(w);
//                }
//            }
//
//        }
//        while (s < sentences.size()) {
//            while (w >= sentences.get(s).size()) {
//                w = 0;
//                s++;
//            }
//            Word w
//            words.get(i) = sentences.get(s).get(w);
//            w++;
//            i++;
//        }
//    }

    public static List<Sentence> parseTextToSentences(String rawText) {
        //TODO: eventually preserve punctuation in Sentence object

        //make the result object
        ArrayList<Sentence> mySentences = new ArrayList<>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(rawText);

        // runOnWords all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap tempCoreMap : sentences) {
            Sentence tempSentence = new Sentence();
            tempSentence.setCoreMap(tempCoreMap);
            int sentenceIndex = 0;
            // traversing the filterWords in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : tempCoreMap.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String spelling = token.get(CoreAnnotations.TextAnnotation.class);

                if (spelling.length() == 1 && !Character.isLetterOrDigit(spelling.charAt(0))) {
                    //it's punctuation
                    Punctuation punct = new Punctuation(spelling);
                    punct.setSentence(tempSentence);
                    punct.setSentenceIndex(sentenceIndex);
                    tempSentence.add(punct);
                }
                else {
                    // this is the POS tag of the token
                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    // this is the NER label of the token
                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                    try {
                        Pos.valueOf(pos);
                        Ne.valueOf(ne);
                    }
                    catch (IllegalArgumentException e) {
                        //System.out.println("BAD POS OR NE TOKEN: " + wordsToPos + " or " + filterNe + " FOR THE WORD: " + spelling);
                        //e.printStackTrace();
                        spelling = "";
                    }
                    if (spelling.length() < 1) {
                        //it's empty, do nothing
                    }
//                    else if (spelling.contains("'")) {
//                        Word lastWord = tempSentence.get(tempSentence.size() - 1);
//                        lastWord.setSpelling(lastWord.getLowerSpelling() + spelling);
//                        lastWord.setPos(Pos.CONTRACTION_WORD);
//                        String[] fullStrings = ContractionManager.getExpansion(lastWord.getLowerSpelling()+spelling);
//                        List<Word> words = new ArrayList<>();
//                        if (fullStrings != null && fullStrings.length >= 1) {
//                            Word word1 = new Word(fullStrings[0]);
//                            words.add(word1);//TODO get other attributes besides spelling!
//                            if (fullStrings != null && fullStrings.length == 2) {
//                                Word word2 = new Word(fullStrings[1]);//TODO get other attributes besides spelling!
//                                words.add(word2);
//                            }
//                        }
//                        Word contraction = new ContractionWord(lastWord.getLowerSpelling()+spelling, words);
//                    }
                    else {
                        //Make a new word object
                        Word tempWord = new Word(spelling);
                        tempWord.setBase(token.get(CoreAnnotations.LemmaAnnotation.class));
                        tempWord.setPos(Pos.valueOf(pos));
                        tempWord.setNe(Ne.valueOf(ne));

                        //Add it to the sentence object
                        tempWord.setSentence(tempSentence);
                        tempWord.setSentenceIndex(sentenceIndex);
                        tempSentence.add(tempWord);
                    }
                }
                sentenceIndex++;
                //System.out.println("token: " + spelling + " wordsToPos: " + wordsToPos + " filterNe:" + filterNe);
            }

            mySentences.add(tempSentence);

//            // this is the parse tree of the current sentence
//            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//            System.out.println("parse tree:\n" + tree);
//
//            // this is the Stanford dependency graph of the current sentence
//            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
//            System.out.println("dependency graph:\n" + dependencies);
        }

        // This is the coreference link graph
        // Each chain stores a set of mentions that link to each other,
        // along with a method for getting the most representative mention
        // Both sentence and token offsets start at 1!
//        Map<Integer, CoreNLPProtos.CorefChain> graph =
//                document.get(CorefChainAnnotation.class);
        return mySentences;
    }

    public static Map<Double, Word> tagWordsWithSentenceContextWithDoubles(TreeMap<Double, String> suggestions,
                                                                           Word oldWord) {
        Sentence contextSentence = oldWord.getSentence();
        int originalOldWordIndex = oldWord.getSentenceIndex();


        Map<Double, Word> result = new HashMap<>();
        int oldWordIndex = originalOldWordIndex;

        //Add 1 sentence per suggestion to sb
        String oldString = oldWord.toString();
        StringBuilder sb = new StringBuilder();
        boolean capitalized = oldWord.getCapitalized();
        for (Map.Entry<Double, String> entry : suggestions.entrySet()) {
            String suggestion = entry.getValue();
            if (capitalized)
                suggestion = suggestion.substring(0, 1).toUpperCase() + suggestion.substring(1);
            if (Character.isLetterOrDigit(contextSentence.toString().charAt(contextSentence.toString().length() - 1)))
                sb.append(contextSentence.toString().replaceAll(oldString, suggestion) + ".\n");
            else
                sb.append(contextSentence.toString().replaceAll(oldString, suggestion) + "\n");
        }

        //Parse all sentences
        Annotation annotation = new Annotation(sb.toString());
        pipeline.annotate(annotation);
        List<CoreMap> stanfordSentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

        //Go through parsed sentences to find tags on the suggested word
        for (CoreMap stanfordSentence : stanfordSentences) {
            CoreLabel parsedToken = null;
            Word contextualSuggestedWord = null;

            //Get parsed token, set text
            try {
                if (stanfordSentence.get(CoreAnnotations.TokensAnnotation.class).size() == contextSentence.size())
                    parsedToken = stanfordSentence.get(CoreAnnotations.TokensAnnotation.class).get(oldWordIndex);
                else {
                    oldWordIndex = originalOldWordIndex;
                    for (int i = 0; i < stanfordSentence.get(CoreAnnotations.TokensAnnotation.class).size(); i++) {
                        if (i == oldWordIndex)
                            break;

                        String string = stanfordSentence.get(CoreAnnotations.TokensAnnotation.class).get(i).toString();
                        int dashIndex = string.lastIndexOf('-');
                        int subtract = string.length() - dashIndex;
                        String shortened = string.substring(0, string.length() - subtract);

                        if (shortened.matches(".*[^\\w\\s].*")) {
                            oldWordIndex++;
                        }
                    }
                    parsedToken = stanfordSentence.get(CoreAnnotations.TokensAnnotation.class).get(oldWordIndex);
                }
                contextualSuggestedWord = new Word(parsedToken.get(CoreAnnotations.TextAnnotation.class));
            }
            catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            //Set Part of Speech
            try {
                contextualSuggestedWord.setPos(Pos.valueOf(parsedToken.get(CoreAnnotations.PartOfSpeechAnnotation.class)));
            }
            catch (IllegalArgumentException e) {
                if (MultiProgramArgs.isDebugMode()) {
                    System.out.println("Bad Pos " + parsedToken.get(CoreAnnotations.TextAnnotation.class) + " in StandfordNlp!");
                    System.out.println(parsedToken.get(CoreAnnotations.TextAnnotation.class));
                    System.out.println(parsedToken.get(CoreAnnotations.PartOfSpeechAnnotation.class));
                }
                contextualSuggestedWord.setPos(Pos.UNKNOWN);
            }

            //Set Named Entity
            try {
                contextualSuggestedWord.setNe(Ne.valueOf(parsedToken.get(CoreAnnotations.NamedEntityTagAnnotation.class)));
            }
            catch (IllegalArgumentException e) {
                if (MultiProgramArgs.isDebugMode()) {
                    System.out.println("Bad Ne " + parsedToken.get(CoreAnnotations.TextAnnotation.class) + " in StandfordNlp!");
                    System.out.println(parsedToken.get(CoreAnnotations.TextAnnotation.class));
                    System.out.println(parsedToken.get(CoreAnnotations.NamedEntityTagAnnotation.class));
                }
                contextualSuggestedWord.setNe(Ne.UNKNOWN);
            }

            //Set Lemma Base
            try {
                contextualSuggestedWord.setBase(parsedToken.get(CoreAnnotations.LemmaAnnotation.class));
            }
            catch (IllegalArgumentException e) {
                if (MultiProgramArgs.isDebugMode()) {
                    System.out.println("Bad lemma " + parsedToken.get(CoreAnnotations.TextAnnotation.class) + " in StandfordNlp!");
                    System.out.println(parsedToken.get(CoreAnnotations.TextAnnotation.class));
                    System.out.println(parsedToken.get(CoreAnnotations.LemmaAnnotation.class));
                }
                contextualSuggestedWord.setNe(Ne.UNKNOWN);
            }

            //Manage capitalization
            contextualSuggestedWord.setCapitalized(oldWord.getCapitalized());

            if (suggestions.size() > 0) {
                result.put(suggestions.firstKey(), contextualSuggestedWord);
                suggestions.remove(suggestions.firstKey());
            }
        }
        return result;
    }

}

/*
Decide how to hold data on filterWords and their corresponding sentences:
ArrayList<CoreMap> sentences
HashMap<sentenceIndex, Word>
 */




































































































































































