package song;

import edu.stanford.nlp.util.CoreMap;

import java.util.*;

public class SongWrapper {

    //TODO: Right now the idea is that you make a song completely, then make a SongWrapper w/ extra info from it. Its song can never change.

    private Song song;

    private String rawSong;
    private List<ArrayList<char[]>> punctuation;
    private List<ArrayList<ArrayList<String>>> basic_lines;
    private Set<Word> wordSet;
    private List<Sentence> sentences;
    private Map<Pos, HashSet<Word>> posMap;

    private Word sentiment;

    public SongWrapper() {

    }

    public SongWrapper(Song song) {
        this.song = song;
        this.wordSet = new HashSet<Word>();
        this.basic_lines = new ArrayList<ArrayList<ArrayList<String>>>();
        this.setSongStats(this.song);
    }

    public void setSongStats(Song song) {
        ArrayList<ArrayList<ArrayList<String>>> newSong = new ArrayList<ArrayList<ArrayList<String>>>();
        for (int i = 0; i < song.getSize(); i++) {
            Stanza oldStanza = song.getStanzas().get(i);
            ArrayList<ArrayList<String>> newStanza = new ArrayList<ArrayList<String>>();
            for (int j = 0; j < oldStanza.getSize(); j++) {
                Line oldLine = oldStanza.getLines().get(j);
                ArrayList<String> newLine = new ArrayList<String>();
                for (int k = 0; k < oldLine.getSize(); k++) {
                    Word oldWord = oldLine.getWords().get(k);
                    newLine.add(oldWord.getSpelling());
                    this.wordSet.add(oldWord);
                    //this.posMap.put();
                }
                newStanza.add(newLine);
            }
            newSong.add(newStanza);
        }
        this.basic_lines = newSong;
    }

    public List<ArrayList<ArrayList<String>>> getBasic_lines() {
        return basic_lines;
    }

    public void setBasic_lines(ArrayList<ArrayList<ArrayList<String>>> basic_lines) {
        this.basic_lines = basic_lines;
    }

    public Set<Word> getWordSet() {
        return this.wordSet;
    }

    public void setWordSet(HashSet<Word> wordSet) {
        this.wordSet = wordSet;
    }

    public Word getSentiment() {
        return this.sentiment;
    }

    public void setSentiment(Word sentiment) {
        this.sentiment = sentiment;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song s) {
        this.song = s;
    }

    public List<ArrayList<char[]>> getPunctuation() {
        return punctuation;
    }

    public void setPunctuation(ArrayList<ArrayList<char[]>> punctuation) {
        this.punctuation = punctuation;
    }

    public String getRawSong() {
        return rawSong;
    }

    public void setRawSong(String rawSong) {
        this.rawSong = rawSong;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }
}































































































