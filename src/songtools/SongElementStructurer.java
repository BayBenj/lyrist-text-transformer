//package song;
//
//import edu.stanford.nlp.ling.TaggedWord;
//import utils.U;
//
//import java.util.*;
//
//import static utils.U.getStanfordNlp;
//
//public class SongElementStructurer //extends SongSegmentTool
//{
//    //TODO: eventually make a final static list holding Structure enum types.
//
//    public void structureSongElement(SongElement element, Structure... structures) {
//        if (structures[0] == Structure.POS) {
//            ArrayList<? extends SongElement> wholeSong = element.getAllSubElementsOfType(new Song());
//            for (SongElement elements : wholeSong) {
//                this.structureSong((Song)elements, Structure.POS);
//            }
//        }
//    }
//
//    public void structureSongElement(SongElement element, String rawSong, Structure... structures) {
//        if (structures[0] == Structure.POS) {
//            List<TaggedWord> taggedWords = U.getStanfordNlp().tagPosForRawSong(rawSong);
//            List<Word> allWords = element.getAllWords();
//            for (int i = 0; i < allWords.size(); i++) {
//                allWords.get(i).setPos(Pos.valueOf(taggedWords.get(i).tag()));
//            }
//        }
//    }
//
//    public SongElement structureAll(SongElement element) {
//        SongElement s1 = this.structure(element, Structure.POS);
////        SongElement s2 = this.structure(s1, Structure.GRAMMAR);
////        SongElement s3 = this.structure(s2, Structure.PHONEME);
////        SongElement s4 = this.structure(s3, Structure.SYLLABLE);
////        SongElement s5 = this.structure(s4, Structure.STRESS);
//        return s1;
//    }
//
//    public HashMap<Word, Double> structureAll(HashMap<String, Double> map) {
//        HashMap<Word, Double> result = new HashMap<Word, Double>();
//        for (Map.Entry<String, Double> pair : map.entrySet()) {
//            SongElement s1 = this.structure(new Word(pair.getKey()), Structure.POS);
////        Word s2 = this.structure(s1, Structure.GRAMMAR);
////        Word s3 = this.structure(s2, Structure.PHONEME);
////        Word s4 = this.structure(s3, Structure.SYLLABLE);
////        Word s5 = this.structure(s4, Structure.STRESS);
//            result.put((Word)s1, pair.getValue());
//        }
//        return result;
//    }
//
//
//    public SongElement structure(SongElement element, Structure structure) {
//        if (element instanceof Song)
//            return this.structureSong((Song)element, structure);
//
//        else if (element instanceof Stanza)
//            return this.structureStanza((Stanza)element, structure);
//
//        else if (element instanceof Line)
//            return this.structureLine((Line)element, structure);
//
//        else if (element instanceof Word)
//            return this.structureWord((Word)element, structure);
//
//        else return null;
//    }
//
//    public Song structureSong(Song elements, Structure structure) {
//        switch (structure) {
//            case POS:
//                getStanfordNlp().tagPoSForSong(elements);
//                break;
//        }
//            Song posSong = new Song();
//        for (int i = 0; i < elements.getSize(); i ++) {
//            Stanza tempStanza = structureStanza(elements.getStanzas().get(i), structure);
//            posSong.add(tempStanza);
//        }
//        return posSong;
//    }
//
//    public Stanza structureStanza(Stanza stanza, Structure structure) {
//        Stanza posStanza = new Stanza();
//        for (int i = 0; i < stanza.getSize(); i ++) {
//            Line tempLine = structureLine(stanza.getLines().get(i), structure);
//            posStanza.add(tempLine);
//        }
//        return posStanza;
//    }
//
//    public Line structureLine(Line line, Structure structure) {
//        switch (structure) {
//            case POS:
//                getStanfordNlp().tagPoSForLine(line);
//                break;
//            case GRAMMAR:
//                //TODO: implement grammar scanning on Line level
//                return null;
//        }
//        Line posLine = new Line();
//        for (Word w : line.getWords()) {
//            Word tempWord = structureWord(w, structure);
//            posLine.add(tempWord);
//        }
//        return posLine;
//    }
//
//    public Word structureWord(Word word, Structure structure) {
//        switch (structure) {
//            case PHONEME:
//                break;
//            case SYLLABLE:
//                break;
//            case STRESS:
//                break;
//            case GRAMMAR:
//                break;
//            case POS:
//                getStanfordNlp().tagPoSForWord(word);
//                break;
//        }
//        return word;
//    }
//
//}
//
//
///*
//TODO: tool that turns raw text into elements, stanzas, lines, and words.
// */
//
///*
//TODO: tool that turns elements, stanzas, lines, and words into raw text.
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
