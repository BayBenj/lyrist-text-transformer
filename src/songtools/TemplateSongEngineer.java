package songtools;

import elements.Song;
import intentions.CompleteIntentions;
import intentions.IntentionManager;
import main.IntentionSelectionMode;
import main.LyristDriver;
import main.SingleProgramArgs;
import main.ThemeManager;
import rhyme.*;
import utils.Pair;
import utils.U;
import java.io.File;
import java.io.IOException;

public abstract class TemplateSongEngineer extends SongEngineer {

//    public Stanza generateStanza(CompleteIntentions intentions, InfoSong templateInfoSong) {
//
//    }

//    public InfoSong generateSongByStanza(CompleteIntentions intentions, InfoSong templateInfoSong) {
//        this.setIntentionBools(intentions);
//        InfoSong templateInfoSong = templateInfoSong.getInfoSong();
//
//        String oldTheme = intentions.getOldTheme();
//        String newTheme = intentions.getEmotionalIntentions().get(0).getEmotionKeyword();
//        LineRhymeScheme rhymeScheme = intentions.getStructuralIntentions().getRhymeScheme();
//
//        for (Stanza stanza : templateInfoSong.getStanzas()) {
//
//            InfoSong generatedInfoSong = LyristNormalReplacer.transform(templateInfoSong, NormalReplacementInfo.getExample(oldTheme, newTheme));
//
////            InfoSong generatedInfoSong = LyristNormalReplacer.rhymeReplace(templateInfoSong, RhymeReplacementInfo.getExample(oldTheme, newTheme, rhymeScheme));
//        }
//    }

    public static void main(String[] args) throws IOException {
        //Setup
        LyristDriver.standardSetup();

        //Load arguments
        SingleProgramArgs.loadExtraProgramArgs(args[5], args[6]);
        SingleProgramArgs.loadSingleProgramArgs(args);
        InfoSong templateSong = SongScanner.getInfoSong(SingleProgramArgs.textInFormat, SingleProgramArgs.templateName);
        TextComposition composition = generateSongWithArgs(templateSong);
        U.print(composition.toString());
    }

    public static TextComposition generateSongWithArgs(InfoSong templateSong) {
        U.startSingleTimer();

        //Get rhyme scheme
        LineRhymeScheme rhymeScheme = new LineRhymeScheme(SingleProgramArgs.rhymeScheme.split("-"));

        //Get elements intentions from programmer input
        final CompleteIntentions completeIntentions = IntentionManager.getSongIntentions(
                rhymeScheme, SingleProgramArgs.oldTheme, SingleProgramArgs.newTheme, SingleProgramArgs.culture);

        //Generate a new song
        final InfoSong newSong = TemplateSongEngineer.generateSong(completeIntentions, templateSong);
        manageSongText(newSong);

        //Make and print out composition
        final TextComposition composition = new TextComposition(templateSong, newSong, completeIntentions);

//
//        //Filter out filterWords w/ unsafe wordsToPos so they can't be marked
//        List<Word> allMarkableWordsList = this.getMarkableWords(templateInfoSong);
//
//        //Get word indexes
//        Set<Integer> allWordIndexes = this.getWordIndexes(allMarkableWordsList);
//
//        //Mark all rhyme scheme filterWords for rhyme replacement
//        Map<Rhyme,Set<Word>> rhymeWordsToReplace = this.markRhymeWordsToReplace(templateInfoSong, (LineRhymeScheme)intentions.getStructuralIntentions().getRhymeScheme());
//
//        //Mark all normal filterWords for normal replacement
//        Set<Word> normalWordsToReplace = this.markNormalWordsToReplace(allWordIndexes, allMarkableWordsList);
//
//        //Find/decide the old theme of this elements
//        String oldTheme = this.decideOldTheme();
//
//        //Decide the new w2v analogy theme
//        String newTheme = this.decideNewTheme(intentions);
//
//        //Replace marked filterWords in template w/ word2vec
//        Song generatedInfoSong = this.w2vReplace(normalWordsToReplace, rhymeWordsToReplace, templateInfoSong, oldTheme, newTheme, templateInfoSong);

        //Manage generated text

        //Print both songtools out
//        U.printSideBySide(templateInfoSong, generatedInfoSong);

        SingleProgramArgs.clearSingleProgramArgs();
        U.stopSingleTimer();
        U.print("TOTAL RUNNING TIME FOR THIS SONG: " + U.getTotalSingleTime() + "\n");
        return composition;
    }


//    private Set<Integer> getWordIndexes(List<Word> allMarkableWordsList) {
//        Set<Integer> allWordIndexes = new HashSet<>();
//        for (int i = 0; i < allMarkableWordsList.size(); i++)
//            allWordIndexes.add(i);
//        return allWordIndexes;
//    }
//
//    private List<Word> getMarkableWords(Song templateSong) {
//        WordFilterEquation wordFilterEquation = FilterManager.wEq(FilterManager.getTaggingSafetyFilters());
//        List<Word> allMarkableWordsList = new ArrayList<>(templateSong.getAllWords());
//        allMarkableWordsList.retainAll(wordFilterEquation.removeMatches(new HashSet(templateSong.getAllWords())));
//        return allMarkableWordsList;
//    }
//
//    private Set<Word> markNormalWordsToReplace(Set<Integer> allWordIndexes, List<Word> allMarkableWordsList) {
//        Set<Integer> normalIndexesToReplace = SongScanner.getRandomIndexes(allWordIndexes, 1);
//        Set<Word> normalWordsToReplace = new HashSet<>();
//        Set<String> sentimentWords = new HashSet<>();
//        for (int index : normalIndexesToReplace) {
//            normalWordsToReplace.add(allMarkableWordsList.get(index));
//            sentimentWords.add(allMarkableWordsList.get(index).toString().toLowerCase());
//        }
//        return normalWordsToReplace;
//    }
//
//    private Map<Rhyme,Set<Word>> markRhymeWordsToReplace(Song song, LineRhymeScheme rhymeScheme) {
//        List<Stanza> stanzas = song.getStanzas();
//        List<Line> lines = new ArrayList<>();
//        for (Stanza stanza : stanzas) {
//            lines.addAll(stanza.getLines());
//        }
//        //TODO check if # of lines = length of rhyme scheme?
//        Map<Rhyme,Set<Word>> rhymeWordsToReplace = new HashMap<>();
//
//        for (Map.Entry<Rhyme, Set<Integer>> entry : rhymeScheme.entrySet()) {
//            for (Integer i : entry.getValue()) {
//                Line line = lines.get(i);
//                if (rhymeWordsToReplace.containsKey(entry.getKey())) {
//                    Set<Word> words = rhymeWordsToReplace.get(entry.getKey());
//                    words.add(line.getAllWords().get(line.getSize() - 1));
//                }
//                else {
//                    Set<Word> words = new HashSet<>();
//                    words.add(line.getAllWords().get(line.getSize() - 1));
//                    rhymeWordsToReplace.put(entry.getKey(), words);
//                }
//            }
//        }
//        return rhymeWordsToReplace;
//    }
//
//    private String decideOldTheme() {
//        String oldTheme = "sorrow";//default
//        //oldTheme = ((TreeMap<Double,String>)U.getW2VInterface().findSentiment(sentimentWords, 1)).firstEntry().getValue(); TODO try looking at 100 results and choosing one from a good POS and NE (the oldWordSpecific as the old theme?)
//        return oldTheme;
//    }
//
//    private String decideNewTheme(CompleteIntentions intentions) {
//        String newTheme = "happiness";//default
//        if (providedEmotionalIntentions)
//            newTheme = intentions.getEmotionalIntentions().get(0).getEmotionKeyword();
//        return newTheme;
//    }

//    private Song w2vReplace(Set<Word> normalWordsToReplace, Map<Rhyme,Set<Word>> rhymeWordsToReplace, InfoSong templateInfoSong, String oldTheme, String newTheme, Song templateSong) {
//        LyristNormalReplacer replacementManager = new LyristNormalReplacer();
//        replacementManager.setnSuggestionsToPrint(100);
//        WordReplacements wordReplacements = replacementManager.getWordSuggestions(
//                normalWordsToReplace,
//                rhymeWordsToReplace,
//                templateInfoSong.getSentences(),
//                FilterManager.sEq(FilterManager.getSafetyStringFilters()),
//                100,
//                U.getW2VInterface(),
//                oldTheme,
//                newTheme,
//                null);
//        return SongMutator.replaceWords(templateSong, wordReplacements);
//    }

    private static void manageSongText(Song generatedInfoSong) {
        // Fix indefinite articles
        SongMutator.fixAllIndefiniteArticles(generatedInfoSong);

        // Capitalize first word of every line
//        SongMutator.capitalizeFirstWordsInLines(generatedInfoSong);

        //Lowercase every word
//        SongMutator.lowercaseAllWords(generatedInfoSong);

        //Hide all punctuation
//        SongMutator.hideAllPunctuation(generatedInfoSong);

        //Reveal all punctuation
//        SongMutator.revealAllPunctuation(generatedInfoSong);

        //Soften all punctuation
//        SongMutator.softenAllPunctuation(generatedInfoSong);
    }

    public static String[] generateArgs(InfoSong song, File template) {
        String oldTheme = null;
        String newTheme = null;
        String culture = null;
        String rhymeScheme = null;

        if (SingleProgramArgs.oldThemeSelectionMode == IntentionSelectionMode.RND &&
                SingleProgramArgs.newThemeSelectionMode == IntentionSelectionMode.RND) {
            Pair<String,String> themes = ThemeManager.getRndThemePair();
            oldTheme = themes.getFirst();
            newTheme = themes.getSecond();
        }
        else if (SingleProgramArgs.oldThemeSelectionMode == IntentionSelectionMode.RND) {
            Pair<String,String> themes = ThemeManager.getRndThemePair();
            oldTheme = themes.getFirst();
        }
        else if (SingleProgramArgs.newThemeSelectionMode == IntentionSelectionMode.RND) {
            Pair<String,String> themes = ThemeManager.getRndThemePair();
            newTheme = themes.getSecond();
        }

        if (SingleProgramArgs.cultureSelectionMode == IntentionSelectionMode.DEFAULT) {
            culture = "English";
        }

        if (SingleProgramArgs.rhymeSchemeSelectionMode == IntentionSelectionMode.RND) {
            RhymeScheme scheme = RhymeSchemeManager.getRndAlternatingScheme(song.lines().size());
            rhymeScheme = scheme.toString();
        }
        else {
            LineRhymeScheme scheme = new LineRhymeScheme(SingleProgramArgs.rhymeScheme.split("-"));
            rhymeScheme = scheme.toString();
        }

        return new String[]{
                oldTheme,
                newTheme,
                culture,
                rhymeScheme,
                template.getName()
        };
    }

    public static InfoSong generateSong(CompleteIntentions intentions, InfoSong templateSong) {
        InfoSong generatedInfoSong = LyristTransformer.transform(templateSong, NormalReplacementInfo.getExample(SingleProgramArgs.oldTheme, SingleProgramArgs.newTheme), false);

//        InfoSong generatedInfoSong = LyristRhymeReplacer.rhymeReplace(templateSong, RhymeReplacementInfo.getExample(SingleProgramArgs.oldTheme, SingleProgramArgs.newTheme, intentions.getStructuralIntentions().getRhymeScheme()));
        return generatedInfoSong;
    }
}













































































































































































































/*
Stanford
TODO > use coreferences to get gender right
TODO > Recognize compound nouns, replace them with nouns or compound nouns (using n-grams?)

Professionalism
TODO >  Optimize paths and file stuff so that Paul can use it
TODO > Document all my code nicely.

Research
TODO > Install better part of speech tagger (Parsey McParseface?).
TODO > Build a noun pluralizer! Same with comparative and superlative adverbs! Also get an abstract / concrete noun dictionary!
TODO > Figure out verbs: reflexive, transitive, person, etc. Get a verb dictionary!
TODO > get more specific parts of speech and categories (eating=present progressive, yellow=color, America=country)
TODO > Inspect doc2vec

Word2vec
TODO: make a naming code for w2v models, like l for lower, u for upper, lu for both, p for punctuation, etc.
TODO > Use some sort of DocumentPreprocessor on my spelling corpora.
TODO > Standardize all w2v data (lowercase, only certain punctuation types). Also ensure at W2vInterface that no such 'bad strings' are queried.
TODO > Consider weakening theme, multiply it by a theme-weakening factor.
TODO > Ensure that my vector bins have every word that the pop-star database has with vocab lists
TODO > Write distance() script for word2vec models

LyristReplacementInfo
TODO > If cosine distance is low enough, use different replacement rather than an analogous replacement.
TODO > Consider allowing for multiple analogies to affect one stanza; this word get analogy A, this other word gets analogy B, etc.

Rhyme
TODO > Use the Hirjee matrix
TODO > extract rhyme scheme



/*
Safe parts of speech:
CD
JJ
JJR
JJS
NN
NNS
NNP
NNPS
RB
RBR
RBS
UH
VB
VBD
VBG
VBN
VBP
VBZ
 */



























































