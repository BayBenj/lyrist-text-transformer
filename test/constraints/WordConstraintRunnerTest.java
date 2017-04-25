package constraints;

import elements.Word;
import filters.ReturnType;
import junit.framework.TestCase;
import main.LyristDriver;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordConstraintRunnerTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();
        LyristDriver.setupRootPath();
        LyristDriver.setupConstraints();
    }

    public void tearDown() throws Exception {
    }

    public void testUseConstraintsTo1ByWeakening() throws Exception {
//        List<WordConstraint> constraints;
//        Word oldWord;
//        Collection<Word> candidates;
//        WordConstraintRunner.useConstraintsTo1ByWeakening(constraints, oldWord, candidates);
    }

    public void testUseConstraintsByWeakening() throws Exception {
    }

    public void testUseConstraintsByWeakening1() throws Exception {
    }

    public void testUseConstraintsTo1ByWeakening1() throws Exception {
    }

    public void testUseConstraintsByWeakening2() throws Exception {
    }

    public void testUseConstraint() throws Exception {
        Set<String> dirty = new HashSet<>();
        dirty.add("bretch");
        dirty.add("idiot");
        dirty.add("imbecile");
        WordConstraint constraint = new StringConstraint(dirty, ReturnType.NON_MATCHES);
        Word w1 = new Word("bretch");
        Word w2 = new Word("idiot");
        Word w3 = new Word("imbecile");
        Word w4 = new Word("lovely");

        Set<Word> candidates = new HashSet<>();
        candidates.add(w1);
        candidates.add(w2);
        candidates.add(w3);
        candidates.add(w4);
        Set<Word> r = WordConstraintRunner.useConstraint(constraint, null, candidates);
        assertTrue(r.contains(w4));
        assertFalse(r.contains(w1));
        assertFalse(r.contains(w2));
        assertFalse(r.contains(w3));

        constraint = new StringConstraint(dirty, ReturnType.MATCHES);
        r = WordConstraintRunner.useConstraint(constraint, null, candidates);
        assertTrue(r.contains(w1));
        assertTrue(r.contains(w2));
        assertTrue(r.contains(w3));
        assertFalse(r.contains(w4));
    }

    public void testWeakenOrDisableConstraint() throws Exception {
    }

    public void testEnableAllConstraints() throws Exception {
        List<WordConstraint> l = WordConstraintMaker.getNormal();
        WordConstraintRunner.enableAllConstraints(l);
        for (WordConstraint wc : l) {
            assertTrue(wc.isEnabled());
        }
        for (WordConstraint wc : l) {
            wc.disable();
        }
        for (WordConstraint wc : l) {
            assertFalse(wc.isEnabled());
        }
        WordConstraintRunner.enableAllConstraints(l);
        for (WordConstraint wc : l) {
            assertTrue(wc.isEnabled());
        }
    }

    public void testDisableRhymeConstraints() throws Exception {
    }

    public void testDisableUnenforcedConstraints() throws Exception {
    }

    public void testDisableInstanceSpecificConstraints() throws Exception {
    }

    public void testDisableNonRhymeConstraints() throws Exception {
    }

}






