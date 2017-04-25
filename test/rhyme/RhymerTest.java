package rhyme;

import elements.Word;
import junit.framework.TestCase;
import main.LyristDriver;

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
        w1.setSyllables(Phoneticizer.getSyllables("bat"));
        Set<String> r1 = Rhymer.getAllRhymesByThreshold(w1, 1.0);

        // corner cases
        assertTrue(r1.contains("bat"));
        assertFalse(r1.contains(""));

        // + 0 syllables
        assertTrue(r1.contains("cat"));
        assertTrue(r1.contains("rat"));
        assertTrue(r1.contains("scat"));
        assertTrue(r1.contains("chat"));
        assertTrue(r1.contains("fat"));
        assertTrue(r1.contains("sat"));
        assertTrue(r1.contains("spat"));
        assertFalse(r1.contains("cats"));
        assertFalse(r1.contains("rats"));
        assertFalse(r1.contains("scats"));
        assertFalse(r1.contains("chats"));
        assertFalse(r1.contains("fats"));
        assertFalse(r1.contains("ban"));
        assertFalse(r1.contains("bad"));
        assertFalse(r1.contains("batch"));
        assertFalse(r1.contains("back"));
        assertFalse(r1.contains("bet"));
        assertFalse(r1.contains("bent"));
        assertFalse(r1.contains("bought"));
        assertFalse(r1.contains("bit"));
        assertFalse(r1.contains("boot"));
        assertFalse(r1.contains("beat"));

        // + 1 syllable
//        assertFalse(r1.contains("wombat"));
        assertFalse(r1.contains("combat"));
    }

    public void testImperfectRhymes() throws Exception {

    }

    public void testGetAllRhymesByThreshold2() throws Exception {
    }

    public void testScore2Rhymes() throws Exception {
        Word w1 = new Word("bat");
        w1.setSyllables(Phoneticizer.getSyllables("bat"));
        Word w2 = new Word("cat");
        w2.setSyllables(Phoneticizer.getSyllables("cat"));
        Word w3 = new Word("bait");
        w3.setSyllables(Phoneticizer.getSyllables("bait"));
        Word w4 = new Word("ban");
        w4.setSyllables(Phoneticizer.getSyllables("ban"));

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





































