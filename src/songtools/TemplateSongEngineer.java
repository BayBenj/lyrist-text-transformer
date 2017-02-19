package songtools;

import elements.Song;
import elements.Stanza;
import intentions.CompleteIntentions;
import rhyme.LineRhymeScheme;
import utils.U;

public final class TemplateSongEngineer extends SongEngineer {

    private boolean providedStructuralIntentions = false;
    private boolean providedEmotionalIntentions = false;
    private boolean providedCulturalIntentions = false;
    private boolean providedRhymeScheme = false;
    private boolean providedMeter = false;

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
//            InfoSong generatedInfoSong = LyristReplacer.normalReplace(templateInfoSong, NormalReplacementInfo.getExample(oldTheme, newTheme));
//
////            InfoSong generatedInfoSong = LyristReplacer.rhymeReplace(templateInfoSong, RhymeReplacementInfo.getExample(oldTheme, newTheme, rhymeScheme));
//        }
//    }

    @Override
    public InfoSong generateSong(CompleteIntentions intentions, InfoSong templateInfoSong) {
        U.startTimer();

        this.setIntentionBools(intentions);

        String oldTheme = intentions.getOldTheme();
        String newTheme = intentions.getEmotionalIntentions().get(0).getEmotionKeyword();
        LineRhymeScheme rhymeScheme = intentions.getStructuralIntentions().getRhymeScheme();

//        InfoSong generatedInfoSong = LyristReplacer.normalReplace(templateInfoSong, NormalReplacementInfo.getExample(oldTheme, newTheme));

        InfoSong generatedInfoSong = LyristReplacer.rhymeReplace(templateInfoSong, RhymeReplacementInfo.getExample(oldTheme, newTheme, rhymeScheme));
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
        this.manageSongText(generatedInfoSong);

        //Print both songtools out
//        U.printSideBySide(templateInfoSong, generatedInfoSong);

        U.stopTimer();
        U.print("TOTAL RUNNING TIME: " + U.getTotalTime() + "\n");
        return generatedInfoSong;
    }

    private void setIntentionBools(CompleteIntentions intentions) {
        if (intentions.getStructuralIntentions() != null && !intentions.getStructuralIntentions().hasNothing()) {
            providedStructuralIntentions = true;
            if (intentions.getStructuralIntentions().getRhymeScheme() != null)
                providedRhymeScheme = true;

            if (intentions.getStructuralIntentions().getMeter() != null)
                providedMeter = true;
        }

        if (intentions.getEmotionalIntentions() != null && intentions.getEmotionalIntentions().size() > 0)
            providedEmotionalIntentions = true;

        if (intentions.getCulturalIntentions() != null && intentions.getCulturalIntentions().size() > 0)
            providedCulturalIntentions = true;
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
//        //oldTheme = ((TreeMap<Double,String>)U.getW2VInterface().findSentiment(sentimentWords, 1)).firstEntry().getValue(); TODO try looking at 100 results and choosing one from a good POS and NE (the instanceSpecific as the old theme?)
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
//        LyristReplacer replacementManager = new LyristReplacer();
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

    private void manageSongText(Song generatedInfoSong) {
        // Fix indefinite articles
        SongMutator.fixAllIndefiniteArticles(generatedInfoSong);

        // Capitalize first word of every line
        SongMutator.capitalizeFirstWordsInLines(generatedInfoSong);

        //Lowercase every word
//        SongMutator.lowercaseAllWords(generatedInfoSong);

        //Hide all punctuation
//        SongMutator.hideAllPunctuation(generatedInfoSong);

        //Reveal all punctuation
//        SongMutator.revealAllPunctuation(generatedInfoSong);

        //Soften all punctuation
//        SongMutator.softenAllPunctuation(generatedInfoSong);
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



























































