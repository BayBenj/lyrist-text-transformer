package constraints;

import elements.Word;
import songtools.WordsToSuggestions;
import songtools.WordReplacements;
import java.util.*;

public abstract class WordConstraintPrioritizer {

    public static WordReplacements useConstraintsTo1ByWeakening(List<WordConstraint> constraints, WordsToSuggestions candidates) {
        WordReplacements result = new WordReplacements();
        for (Map.Entry<Word,Set<Word>> entry : candidates.entrySet())
            result.put(entry.getKey(), useConstraintsTo1ByWeakening(constraints, entry.getKey(), entry.getValue()));
        return result;
    }

    public static Word useConstraintsTo1ByWeakening(List<WordConstraint> constraints, Collection<Word> candidates) {
        Word result = useConstraintsTo1ByWeakening(constraints, null, candidates);
        return result;
    }

    public static WordsToSuggestions useConstraintsByWeakening(List<WordConstraint> constraints, WordsToSuggestions candidates) {
        WordsToSuggestions result = new WordsToSuggestions();
        for (Map.Entry<Word,Set<Word>> entry : candidates.entrySet()) {
            Set<Word> temp = useConstraintsByWeakening(constraints, entry.getKey(), entry.getValue());
            result.put(entry.getKey(), temp);
        }
        return result;
    }

    public static Set<Word> useConstraintsByWeakening(List<WordConstraint> constraints, Collection<Word> originals) {
        Set<Word> result = new HashSet<>(originals);
        for (int c = 0; c < constraints.size(); c++) {
            WordConstraint wordConstraint = constraints.get(c);
            if (!wordConstraint.isEnabled()) continue;
            if (!wordConstraint.isInstanceSpecific())
                result.retainAll(wordConstraint.useWithPresetFields(originals));
            else {
                for (Word w : originals)
                    result.retainAll(wordConstraint.useInstanceSpecific(originals, w));
            }
            if (result.isEmpty()) {
                if (wordConstraint.isEnforced())
                    return null;
                //weaken or disable constraint
                if (!wordConstraint.weaken())
                    wordConstraint.disable();
                //recurse, start filtering process over with weaker filters
                return useConstraintsByWeakening(constraints, originals);
            }
        }
        return result;
    }

    public static Word useConstraintsTo1ByWeakening(List<WordConstraint> constraints, Word original, Collection<Word> candidates) {
        TreeSet<Word> results = new TreeSet<>(useConstraintsByWeakening(constraints, original, candidates));
        if (results != null && !results.isEmpty())
            return results.first();
        return null;
    }

    public static Set<Word> useConstraintsByWeakening(List<WordConstraint> constraints, Word original, Collection<Word> candidates) {
        if (candidates == null || candidates.isEmpty() || constraints == null || constraints.isEmpty()) return null;
        Set<Word> remaining = new HashSet(candidates);
        for (int c = 0; c < constraints.size(); c++) {
            WordConstraint wordConstraint = constraints.get(c);
            if (!wordConstraint.isEnabled()) continue;
            if (!wordConstraint.isInstanceSpecific())
                remaining.retainAll(wordConstraint.useWithPresetFields(remaining));
            else
                remaining.retainAll(wordConstraint.useInstanceSpecific(remaining, original));
            if (remaining.isEmpty()) {
                if (wordConstraint.isEnforced())
                    return null;
                //weaken or disable constraint
                if (!wordConstraint.weaken())
                    wordConstraint.disable();
                //recurse, start filtering process over with weaker filters
                return useConstraintsByWeakening(constraints, original, candidates);
            }
        }
        if (!remaining.isEmpty())
            return remaining;
        return null;
    }

    public static void enableAllConstraints(List<WordConstraint> constraints) {
        for (WordConstraint c : constraints)
            c.enable();
    }

//    public static Set<Word> useConstraints(List<WordConstraint> constraints, Set<Word> candidates, Word original) {
//        if (candidates == null || candidates.isEmpty() || constraints == null || constraints.isEmpty()) return null;
//        Set<Word> remaining = new HashSet<>();
//        for (int c = 0; c < constraints.size(); c++) {
//            WordConstraint wordConstraint = constraints.get(c);
//            if (!wordConstraint.isEnabled()) continue;
//            if (wordConstraint.isInstanceSpecific())
//                remaining.retainAll(wordConstraint.useInstanceSpecific(candidates, original));
//            else
//                remaining.retainAll(wordConstraint.useWithPresetFields(candidates));
//            if (remaining.isEmpty())
//                return remaining;
//        }
//        return remaining;
//    }

//    private static TreeList<WordConstraint> weakenOrRemoveLastConstraint(TreeList<WordConstraint> constraints) {
//        //constraints should be coming in with lowest priority first
//        if (constraints.firstEntry().getValue() instanceof DoubleConstraint)
//            constraints.lastEntry().getValue().weaken();
//        else
//            constraints.remove(constraints.firstKey());;
//        return constraints;
//    }

//    public static Word useConstraintsTo1(List<WordConstraint> constraints, Set<Word> candidates, Word original) {
//        TreeSet<Word> results = (TreeSet<Word>)useConstraints(constraints, candidates, original);
//        if (U.notNullnotEmpty(results))
//            return results.first();
//        return null;
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























