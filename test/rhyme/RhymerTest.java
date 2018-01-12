package rhyme;

import elements.Word;
import junit.framework.TestCase;
import main.LyristDriver;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RhymerTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();
        LyristDriver.setupRootPath();
        LyristDriver.setupCmuDict();
    }

    public void tearDown() throws Exception {
    }

    public void testGetAllRhymesByThreshold1() throws Exception {
        testPerfectRhymes();
        testImperfectRhymes();
    }

    public void testPerfectRhymes() throws Exception {
        Word w1 = new Word("bat");
        w1.setPronunciations(Phoneticizer.getSyllables("bat"));
        Map<Double, Set<String>> r1 = Rhymer.getAllRhymesByThreshold(w1, 1.0);

        Set<String> allRhymes = new HashSet<>();
        for (Map.Entry<Double, Set<String>> entry : r1.entrySet()) {
            allRhymes.addAll(entry.getValue());
        }
        
        // corner cases
        assertTrue(allRhymes.contains("bat"));
        assertFalse(allRhymes.contains(""));

        // + 0 syllables
        assertTrue(allRhymes.contains("cat"));
        assertTrue(allRhymes.contains("rat"));
        assertTrue(allRhymes.contains("scat"));
        assertTrue(allRhymes.contains("chat"));
        assertTrue(allRhymes.contains("fat"));
        assertTrue(allRhymes.contains("sat"));
        assertTrue(allRhymes.contains("spat"));
        assertFalse(allRhymes.contains("cats"));
        assertFalse(allRhymes.contains("rats"));
        assertFalse(allRhymes.contains("scats"));
        assertFalse(allRhymes.contains("chats"));
        assertFalse(allRhymes.contains("fats"));
        assertFalse(allRhymes.contains("ban"));
        assertFalse(allRhymes.contains("bad"));
        assertFalse(allRhymes.contains("batch"));
        assertFalse(allRhymes.contains("back"));
        assertFalse(allRhymes.contains("bet"));
        assertFalse(allRhymes.contains("bent"));
        assertFalse(allRhymes.contains("bought"));
        assertFalse(allRhymes.contains("bit"));
        assertFalse(allRhymes.contains("boot"));
        assertFalse(allRhymes.contains("beat"));

        // + 1 syllable
//        assertFalse(allRhymes.contains("wombat"));
        assertFalse(allRhymes.contains("combat"));
    }

    public void testImperfectRhymes() throws Exception {

    }

    public void testGetAllRhymesByThreshold2() throws Exception {
    }

    public void testScore2Rhymes() throws Exception {
        Word w1 = new Word("bat");
        w1.setPronunciations(Phoneticizer.getSyllables("bat"));
        Word w2 = new Word("cat");
        w2.setPronunciations(Phoneticizer.getSyllables("cat"));
        Word w3 = new Word("bait");
        w3.setPronunciations(Phoneticizer.getSyllables("bait"));
        Word w4 = new Word("ban");
        w4.setPronunciations(Phoneticizer.getSyllables("ban"));

        assertEquals(Rhymer.score2Rhymes(w1.getRhymeTail(), w1.getRhymeTail()), 1.0);
        assertEquals(Rhymer.score2Rhymes(w1.getRhymeTail(), w2.getRhymeTail()), 1.0);
//        assertTrue(Rhymer.score2Rhymes(w1.getRhymeTail(), w3.getRhymeTail()) > 0.10);
//        assertTrue(Rhymer.score2Rhymes(w1.getRhymeTail(), w4.getRhymeTail()) > 0.95);
        testRndMatchingSingleSyllables();
    }

    public void testRndMatchingSingleSyllables() throws Exception {
        //matching short single syllables have score of 1.0
        SyllableGroup sg1 = new SyllableGroup();
        Syllable s1 = Syllable.rnd(1, 1);
        sg1.add(s1);

        SyllableGroup sg2 = new SyllableGroup();
        Syllable s2 = s1;
        sg2.add(s2);

        assertEquals(Rhymer.score2Rhymes(sg1, sg2),1.0);

        //matching shortish single syllables have score of 1.0
        sg1 = new SyllableGroup();
        s1 = Syllable.rnd(2, 2);
        sg1.add(s1);

        sg2 = new SyllableGroup();
        s2 = s1;
        sg2.add(s2);

        assertEquals(Rhymer.score2Rhymes(sg1, sg2),1.0);

        //matching long single syllables have score of 1.0
        sg1 = new SyllableGroup();
        s1 = Syllable.rnd(10, 10);
        sg1.add(s1);

        sg2 = new SyllableGroup();
        s2 = s1;
        sg2.add(s2);

        assertEquals(Rhymer.score2Rhymes(sg1, sg2),1.0);

        //matching gigantic single syllables have score of 1.0
        sg1 = new SyllableGroup();
        s1 = Syllable.rnd(100, 100);
        sg1.add(s1);

        sg2 = new SyllableGroup();
        s2 = s1;
        sg2.add(s2);

        assertEquals(Rhymer.score2Rhymes(sg1, sg2),1.0);
    }

}





































