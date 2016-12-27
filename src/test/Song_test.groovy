//package test
//
//import song.Line
//import song.Song
//import song.SongElement
//import song.Stanza
//import song.Word
//
//class Song_test extends GroovyTestCase {
//    void setUp() {
//        super.setUp()
//
//    }
//
//    void tearDown() {
//
//    }
//
//    void testGetSubElements() {
//
//    }
//
//    void testSetSubElements() {
//
//    }
//
//    void testGetStanzas() {
//
//    }
//
//    void testSetStanzas() {
//
//    }
//
//    void testToString() {
//
//    }
//
//    void testAdd() {
//
//    }
//
//    void testAdd1() {
//
//    }
//
//    void testGet() {
//
//    }
//
//    void testGetAllWords() {
//
//    }
//
//    void testGetAllSubElementsOfType() {
//        Song song = new Song();
//        Stanza stanza = new Stanza();
//        Line line = new Line();
//        Word word = new Word("");
//
//        line.add(word);
//        stanza.add(line);
//        song.add(stanza);
//
//        ArrayList<SongElement> wordList = song.getAllSubElementsOfType(word);
//        ArrayList<SongElement> lineList = song.getAllSubElementsOfType(line);
//        ArrayList<SongElement> stanzaList = song.getAllSubElementsOfType(stanza);
//
//        assertEquals(wordList.get(0).class, Word.class);
//        assertEquals(lineList.get(0).class, Line.class);
//        assertEquals(stanzaList.get(0).class, Stanza.class);
//
//        assertEquals(wordList.size(), 1);
//        assertEquals(lineList.size(), 1);
//        assertEquals(stanzaList.size(), 1);
//    }
//
//    void testHasCompleteStructure() {
//
//    }
//
//    void testHasCompleteStructure1() {
//
//    }
//
//    void testHasCompleteSpellingStructure() {
//
//    }
//
//    void testHasCompletePosStructure() {
//
//    }
//
//    void testHasCompletePhonemeStructure() {
//
//    }
//
//    void testHasCompleteSyllableStructure() {
//
//    }
//
//    void testHasCompleteStressStructure() {
//
//    }
//
//    void testGetSize() {
//
//    }
//
//    void testHasInsideSomething() {
//
//    }
//
//    void testHasCarryingSomething() {
//
//    }
//
//    void testGetSuperElement() {
//
//    }
//
//    void testSetSuperElement() {
//
//    }
//
//    void testGetSubElements1() {
//
//    }
//
//    void testSetSubElements1() {
//
//    }
//
//    void testEquals() {
//
//    }
//
//    void testHashCode() {
//
//    }
//}
