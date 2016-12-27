//package song;
//
//import stanford_nlp.StanfordPosTagger;
//
//public class SongSegmentTool {
//    //TODO: Use recursion?
//    //TODO: Decide whether to move this functionality to the actual SongElement class
//
//    protected SongElement identifySegment(SongElement segment) {
//        if (segment instanceof Song)
//            return this.structureSong((Song)segment, structure);
//
//        else if (segment instanceof Stanza)
//            return this.structureStanza((Stanza)segment, structure);
//
//        else if (segment instanceof Line)
//            return this.structureLine((Line)segment, structure);
//
//        else if (segment instanceof Word)
//            return this.structureWord((Word)segment, structure);
//
//        else return null;
//    }
//
//
//    protected Song structureSong(Song song, Structure structure) {
//        Song posSong = new Song();
//        for (int i = 0; i < song.getSize(); i ++) {
//            Stanza tempStanza = structureStanza(song.getStanzas().get(i), structure);
//            posSong.add(tempStanza);
//        }
//        return posSong;
//    }
//
//    protected Stanza structureStanza(Stanza stanza, Structure structure) {
//        Stanza posStanza = new Stanza();
//        for (int i = 0; i < stanza.getSize(); i ++) {
//            Line tempLine = structureLine(stanza.getLines().get(i), structure);
//            posStanza.add(tempLine);
//        }
//        return posStanza;
//    }
//
//    protected Line structureLine(Line line, Structure structure) {
//        switch (structure) {
//            case POS:
//                StanfordPosTagger posTagger = new StanfordPosTagger();
//                return posTagger.tagPoSForLine(line);
//            case GRAMMAR:
//                //TODO: implement grammar scanning on Line level
//                return null;
//        }
//        Line posLine = new Line();
//        for (int i = 0; i < line.getSize(); i ++) {
//            Word tempWord = structureWord(line.getWords().get(i), structure);
//            posLine.add(tempWord);
//        }
//        return posLine;
//    }
//
//    protected Word structureWord(Word word, Structure structure) {
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
//                StanfordPosTagger posTagger = new StanfordPosTagger();
//                return posTagger.tagPoSForWord(word);
//        }
//        return word;
//    }
//
//}
//
//
//
///*
//Recursion plan:
//    There should be 1 recursive method for structuring and for attribute scanning.
//    Each type of structure request needs a base case (Word, Line, Stanza). For most it is word, but for Grammar it would stop at the line (or even the stanza).
//    A method takes in a SongElement and recurses down to the case base.
//    When the base case is reached, data is extracted and returned up. It is stored.
//
//
//
//
// */