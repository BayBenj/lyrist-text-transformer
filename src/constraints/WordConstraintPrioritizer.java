package constraints;

import elements.Word;
import utils.U;

import java.util.*;

public abstract class WordConstraintPrioritizer {

    public static Word useConstraintsTo1(Map<Integer, WordConstraint> constraints, Set<Word> candidates, Word original) {
        TreeSet<Word> results = (TreeSet<Word>)useConstraints(constraints, candidates, original);
        if (U.notNullnotEmpty(results))
            return results.first();
        return null;
    }

    public static Word useConstraintsTo1ByWeakening(Map<Integer, WordConstraint> constraints, Set<Word> candidates, Word original) {
        TreeSet<Word> results = (TreeSet<Word>)useConstraintsByWeakening(constraints, candidates, original);
        if (U.notNullnotEmpty(results))
            return results.first();
        return null;
    }

    public static Set<Word> useConstraintsByWeakening(Map<Integer, WordConstraint> constraints, Set<Word> candidates, Word original) {
        if (candidates == null || candidates.isEmpty() || constraints == null || constraints.isEmpty()) return null;
        Set<Word> remaining = new HashSet<>();
        for (int c = 0; c < constraints.size(); c++) {
            WordConstraint wordConstraint = constraints.get(c);
            if (!wordConstraint.isEnabled()) continue;
            if (wordConstraint.isInstanceSpecific())
                remaining.retainAll(wordConstraint.useInstanceSpecific(candidates, original));
            else
                remaining.retainAll(wordConstraint.useWithPresetFields(candidates));
            if (remaining.isEmpty()) {
                if (wordConstraint.isEnforced())
                    return null;
                //weaken or disable constraint
                if (!wordConstraint.weaken())
                    wordConstraint.disable();
                //recurse, start filtering process over with weaker filters
                return useConstraintsByWeakening(constraints, candidates, original);
            }
        }
        if (!remaining.isEmpty())
            return remaining;
        return null;
    }

    public static Set<Word> useConstraints(Map<Integer, WordConstraint> constraints, Set<Word> candidates, Word original) {
        if (candidates == null || candidates.isEmpty() || constraints == null || constraints.isEmpty()) return null;
        Set<Word> remaining = new HashSet<>();
        for (int c = 0; c < constraints.size(); c++) {
            WordConstraint wordConstraint = constraints.get(c);
            if (!wordConstraint.isEnabled()) continue;
            if (wordConstraint.isInstanceSpecific())
                remaining.retainAll(wordConstraint.useInstanceSpecific(candidates, original));
            else
                remaining.retainAll(wordConstraint.useWithPresetFields(candidates));
            if (remaining.isEmpty())
                return remaining;
        }
        return remaining;
    }

//    private static TreeMap<Integer, WordConstraint> weakenOrRemoveLastConstraint(TreeMap<Integer, WordConstraint> constraints) {
//        //constraints should be coming in with lowest priority first
//        if (constraints.firstEntry().getValue() instanceof DoubleConstraint)
//            constraints.lastEntry().getValue().weaken();
//        else
//            constraints.remove(constraints.firstKey());;
//        return constraints;
//    }

}

/*
Ideally, constraints filterByMultiple results until they are done and only 1 result is left.
Bad case 1: 0 results remain.
    Solution: if constraint is enforced, don't do a replacement. Else weaken constraint that returned 0 and start over.
Bad case 2: all constraints are done and multiple results remain.
    Solution: Take the first result (or any result) in the list.
 */


/*
Non-continuous constraints are used or not used.
Continuous constraints may be weakened or strengthened.
 */

/*
1. not one of these restricted words
2. rhyme dbl >= .75
3. pos == NN
4. Highest rhyme dbl
5. Highest cosine distance
 */




























