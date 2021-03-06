package stanford_nlp;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import main.ProgramArgs;
import song.NamedEntity;
import song.Pos;
import song.Sentence;
import song.Word;
import utils.Utils;

import java.io.*;
import java.util.*;

public class StanfordNlp {

    private static StanfordCoreNLP pipeline;
    private static final String ANNOTATOR = "annotators";
    private static final String ANNOTATORS = "tokenize, ssplit, pos, lemma, ner";
//    private final String ANNOTATORS = "tokenize, ssplit, pos, lemma, ner, parse, dcoref";
    //private static final MaxentTagger tagger = new MaxentTagger(Utils.rootPath + "lib/stanford-parser/3.6.0/libexec/models/wsj-0-18-bidirectional-nodistsim.tagger");
    //private final MaxentTagger tagger = new MaxentTagger(Utils.rootPath + "local-data/models/pos-tagger/english-left3words/english-bidirectional-distsim.tagger");
    private static DocumentPreprocessor documentPreprocessor;
    private static final TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "untokenizable=noneKeep");
    AbstractSequenceClassifier<CoreLabel> classifier = null;


    public StanfordNlp() {
        this.setupPipeline();
    }

    public static List<List<CoreLabel>> parseTextCompletelyByPath(String filePath) {
        setDocumentPreprocessor(filePath);
        List<List<TaggedWord>> taggedSentences = tokenizeTextFromDocumentPreprocessor();
        List<List<CoreLabel>> classifiedSentences = new ArrayList<List<CoreLabel>>();
        for (List<TaggedWord> taggedSentence : taggedSentences)
            classifiedSentences.add(classifyNamedEntities(taggedSentence));
        return classifiedSentences;
    }

    public static List<List<CoreLabel>> parseTextCompletelyByString(String fullText) {
        //this.setDocumentPreprocessor(filePath);
        List<List<HasWord>> taggedSentences = tokenizeTextFromString(fullText);
        List<List<CoreLabel>> classifiedSentences = new ArrayList<List<CoreLabel>>();
        for (List<HasWord> taggedSentence : taggedSentences)
            classifiedSentences.add(classifyNamedEntities(taggedSentence));
        return classifiedSentences;
    }

    public static void setDocumentPreprocessor(String filePath) {
        documentPreprocessor = new DocumentPreprocessor(filePath);
        //this.ptbTokenizerFactory.setOptions();
        documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);
    }

    public static List<List<TaggedWord>> tokenizeTextFromDocumentPreprocessor() {
        List<List<TaggedWord>> taggedSentences = new ArrayList<List<TaggedWord>>();
        for (List<HasWord> sentence : documentPreprocessor) {
            //List<TaggedWord> taggedSentence = tagger.tagSentence(sentence);
            //taggedSentences.add(taggedSentence);
        }
        return taggedSentences;
    }

    public static List<List<HasWord>> tokenizeTextFromString(String untokenizedText) {
        List<List<HasWord>> sentences;
        StringReader sr = new StringReader(untokenizedText);
        sentences = MaxentTagger.tokenizeText(new BufferedReader(sr));
        return sentences;
    }

    public static List<CoreLabel> classifyNamedEntities(List<? extends HasWord> untaggedSentence) {
        //classify named entities
        String serializedClassifier = Utils.rootPath + "lib/english.all.3class.distsim.crf.ser.gz";
        AbstractSequenceClassifier<CoreLabel> classifier = null;
        try {
            classifier = CRFClassifier.getClassifier(serializedClassifier);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<CoreLabel> classifiedSentence = classifier.classifySentence(untaggedSentence);
        return classifiedSentence;
    }

    public void setupPipeline() {
//        File f = new File(PATH);
//        if(f.exists() && !f.isDirectory())
//            this.deserializePipeline();
//        else
//            this.buildPipeline();
        this.buildPipeline();
    }

//    private void deserializePipeline() {
//        System.out.println("Deserializing StanfordCoreNLP object");
//        try {
//            FileInputStream fileIn = new FileInputStream(PATH);
//            ObjectInputStream in = new ObjectInputStream(fileIn);
//            this.pipeline = (StanfordCoreNLP) in.readObject();
//            in.close();
//            fileIn.close();
//        }
//        catch(IOException i) {
//            i.printStackTrace();
//        }
//        catch(ClassNotFoundException c) {
//            System.out.println("StanfordCoreNLP class not found");
//            c.printStackTrace();
//        }
//    }
//
//    private void serializePipeline() {
//        try {
//            FileOutputStream fileOut = new FileOutputStream(PATH);
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(this.pipeline);
//            out.close();
//            fileOut.close();
//            System.out.println("Serialized StanfordCoreNLP object is saved in " + PATH);
//        }
//        catch(IOException i) {
//            i.printStackTrace();
//        }
//    }

    private void buildPipeline() {
        Properties props = new Properties();
        props.put(ANNOTATOR, ANNOTATORS);
        this.pipeline = new StanfordCoreNLP(props);
        //this.serializePipeline();
    }

    public static ArrayList<Sentence> parseTextToSentences(String rawText) {
        //TODO: eventually preserve punctuation in Sentence object

        //make the result object
        ArrayList<Sentence> mySentences = new ArrayList<Sentence>();


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
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : tempCoreMap.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String spelling = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                try {
                    Pos.valueOf(pos);
                    NamedEntity.valueOf(ne);
                }
                catch (IllegalArgumentException e) {
                    //System.out.println("BAD POS OR NE TOKEN: " + pos + " or " + ne + " FOR THE WORD: " + spelling);
                    //e.printStackTrace();
                    spelling = "";
                }

                if (spelling.length() < 1 || (spelling.length() == 1 && !Character.isAlphabetic(spelling.charAt(0)))) {
                    //it's empty or lonely punctuation, do nothing
                }
                else if (spelling.contains("'")) {
                    Word lastWord = tempSentence.get(tempSentence.size() - 1);
                    lastWord.setSpelling(lastWord.getSpelling() + spelling);
                    lastWord.setPos(Pos.CONTRACTION_WORD);
                }
                else {
                    //Make a new word object
                    Word tempWord = new Word(spelling);
                    tempWord.setPos(Pos.valueOf(pos));
                    tempWord.setNe(NamedEntity.valueOf(ne));

                    //Add it to the sentence object
                    tempSentence.add(tempWord);
                }

                //System.out.println("token: " + spelling + " pos: " + pos + " ne:" + ne);
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

    public static Set<Word> tagWordsWithSentenceContext(Collection<String> suggestions,
                                                        Sentence contextSentence,
                                                        Word oldWord,
                                                        int oldWordIndex,
                                                        boolean oldWordIsCapital) {
        Set<Word> result = new HashSet<>();
//        int corelabelIndex = contextSentence.getWordIndexIncludingPunctuation(oldWordIndex);
        int corelabelIndex = oldWordIndex;

        //Contextually tag every input string
        for (String suggestion : suggestions) {
            Word word = sentenceContext(contextSentence, oldWord, suggestion, corelabelIndex);
//            Word contextualSuggestedWord = new Word(suggestion);
//            String contextSentenceString = contextSentence.toString();
//            contextSentenceString = contextSentenceString.replace(oldString, suggestion);
//
//            Annotation annotation = new Annotation(contextSentenceString);
//            pipeline.annotate(annotation);
//            List<CoreMap> stanfordSentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
////            List<List<CoreLabel>> stanfordSentences = parseTextCompletelyByString(contextSentenceString);
//
//            //TODO does this get the correct CoreLabel?
////            for (List<CoreLabel> stanfordSentence : stanfordSentences) {
//            for (CoreMap stanfordSentence : stanfordSentences) {
////                CoreLabel parsedToken = stanfordSentence.get(corelabelIndex);
//                List<CoreLabel> listOfCoreLabels = stanfordSentence.get(CoreAnnotations.TokensAnnotation.class);
//                CoreLabel parsedToken = null;
//                try {
//                    parsedToken = stanfordSentence.get(CoreAnnotations.TokensAnnotation.class).get(corelabelIndex);
//                } catch (IndexOutOfBoundsException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    contextualSuggestedWord.setPos(Pos.valueOf(parsedToken.get(CoreAnnotations.PartOfSpeechAnnotation.class)));
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                    System.out.println(parsedToken.get(CoreAnnotations.TextAnnotation.class));
//                    System.out.println(parsedToken.get(CoreAnnotations.PartOfSpeechAnnotation.class));
//                    contextualSuggestedWord.setPos(Pos.UNKNOWN);
//                }
//                try {
//                    contextualSuggestedWord.setNe(NamedEntity.valueOf(parsedToken.get(CoreAnnotations.NamedEntityTagAnnotation.class)));
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                    System.out.println(parsedToken.get(CoreAnnotations.TextAnnotation.class));
//                    System.out.println(parsedToken.get(CoreAnnotations.NamedEntityTagAnnotation.class));
//                    contextualSuggestedWord.setNe(NamedEntity.UNKNOWN);
//                }
//            }
            result.add(word);
        }
        return result;
    }

    public static Map<Double, Word> tagWordsWithSentenceContextWithDoubles(TreeMap<Double, String> suggestions,
                                                                           Sentence contextSentence,
                                                                           Word oldWord,
                                                                           int oldWordIndex) {
        Map<Double, Word> result = new HashMap<>();
        int corelabelIndex = contextSentence.getWordIndexIncludingPunctuation(oldWordIndex);
//        int corelabelIndex = oldWordIndex;

        //Contextually tag every input string all at once
        String oldString = oldWord.toString();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Double, String> entry : suggestions.entrySet()) {
            String suggestion = entry.getValue();
            if (oldWord.getCapitalized())
                suggestion = suggestion.substring(0, 1).toUpperCase() + suggestion.substring(1);
            String contextSentenceString = contextSentence.toString().replace(oldString, suggestion);
            sb.append(contextSentenceString);
            sb.append(".\n");
        }
        Annotation annotation = new Annotation(sb.toString());
        pipeline.annotate(annotation);
        List<CoreMap> stanfordSentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap stanfordSentence : stanfordSentences) {
            CoreLabel parsedToken = null;
            Word contextualSuggestedWord = null;
            try {
                parsedToken = stanfordSentence.get(CoreAnnotations.TokensAnnotation.class).get(corelabelIndex);
                contextualSuggestedWord = new Word(parsedToken.get(CoreAnnotations.TextAnnotation.class));
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            try {
                contextualSuggestedWord.setPos(Pos.valueOf(parsedToken.get(CoreAnnotations.PartOfSpeechAnnotation.class)));
            } catch (IllegalArgumentException e) {
                if (ProgramArgs.isTesting()) {
                    e.printStackTrace();
                    System.out.println(parsedToken.get(CoreAnnotations.TextAnnotation.class));
                    System.out.println(parsedToken.get(CoreAnnotations.PartOfSpeechAnnotation.class));
                }
                contextualSuggestedWord.setPos(Pos.UNKNOWN);
            }
            try {
                contextualSuggestedWord.setNe(NamedEntity.valueOf(parsedToken.get(CoreAnnotations.NamedEntityTagAnnotation.class)));
            } catch (IllegalArgumentException e) {
                if (ProgramArgs.isTesting()) {
                    e.printStackTrace();
                    System.out.println(parsedToken.get(CoreAnnotations.TextAnnotation.class));
                    System.out.println(parsedToken.get(CoreAnnotations.NamedEntityTagAnnotation.class));
                }
                contextualSuggestedWord.setNe(NamedEntity.UNKNOWN);
            }
            if (Character.isUpperCase(oldString.charAt(0)))
                contextualSuggestedWord.setCapitalized(oldWord.getCapitalized());
            result.put(suggestions.firstKey(), contextualSuggestedWord);
            suggestions.remove(suggestions.firstKey());
        }


        //Contextually tag every input string
//        for (Map.Entry<Double, String> entry : suggestions.entrySet()) {
//            String suggestion = entry.getValue();
//            Word word = sentenceContext(contextSentence, oldWord, suggestion, corelabelIndex);
//            result.put(entry.getKey(), word);
//        }
        return result;
    }

    private static Word sentenceContext(//Map<Double, String> suggestions,
                                       Sentence contextSentence,
                                       Word oldWord,
                                       String suggestion,
                                       int corelabelIndex
    ) {
        String oldString = oldWord.toString();
        if (oldWord.getCapitalized())
            suggestion = suggestion.substring(0, 1).toUpperCase() + suggestion.substring(1);
        Word contextualSuggestedWord = new Word(suggestion);
        String contextSentenceString = contextSentence.toString();
        contextSentenceString = contextSentenceString.replace(oldString, suggestion);

        Annotation annotation = new Annotation(contextSentenceString);
        pipeline.annotate(annotation);
        List<CoreMap> stanfordSentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
//            List<List<CoreLabel>> stanfordSentences = parseTextCompletelyByString(contextSentenceString);

        //TODO does this get the correct CoreLabel?
//            for (List<CoreLabel> stanfordSentence : stanfordSentences) {
        for (CoreMap stanfordSentence : stanfordSentences) {
//                CoreLabel parsedToken = stanfordSentence.get(corelabelIndex);
            //List<CoreLabel> listOfCoreLabels = stanfordSentence.get(CoreAnnotations.TokensAnnotation.class);
            CoreLabel parsedToken = null;
            try {
                parsedToken = stanfordSentence.get(CoreAnnotations.TokensAnnotation.class).get(corelabelIndex);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            try {
                contextualSuggestedWord.setPos(Pos.valueOf(parsedToken.get(CoreAnnotations.PartOfSpeechAnnotation.class)));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.out.println(parsedToken.get(CoreAnnotations.TextAnnotation.class));
                System.out.println(parsedToken.get(CoreAnnotations.PartOfSpeechAnnotation.class));
                contextualSuggestedWord.setPos(Pos.UNKNOWN);
            }
            try {
                contextualSuggestedWord.setNe(NamedEntity.valueOf(parsedToken.get(CoreAnnotations.NamedEntityTagAnnotation.class)));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.out.println(parsedToken.get(CoreAnnotations.TextAnnotation.class));
                System.out.println(parsedToken.get(CoreAnnotations.NamedEntityTagAnnotation.class));
                contextualSuggestedWord.setNe(NamedEntity.UNKNOWN);
            }
        }
        if (Character.isUpperCase(oldString.charAt(0)))
        contextualSuggestedWord.setCapitalized(oldWord.getCapitalized());
        return contextualSuggestedWord;
    }
}

/*
Decide how to hold data on words and their corresponding sentences:
ArrayList<CoreMap> sentences
HashMap<sentenceIndex, Word>
 */






























































































































































































