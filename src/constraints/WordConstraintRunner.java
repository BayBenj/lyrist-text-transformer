package constraints;

import elements.Word;
import songtools.WordsToSuggestions;
import songtools.WordReplacements;
import utils.U;

import java.util.*;

public abstract class WordConstraintRunner {

    public static WordReplacements useConstraintsTo1ByWeakening(List<WordConstraint> constraints, WordsToSuggestions candidates) throws EnforcedConstraintReturnedZeroException, AttemptedDisableOfEnforcedConstraintException {
        WordReplacements result = new WordReplacements();
        for (Map.Entry<Word,Set<Word>> entry : candidates.entrySet())
            result.put(entry.getKey(), useConstraintsTo1ByWeakening(constraints, entry.getKey(), entry.getValue()));
        return result;
    }

    public static WordsToSuggestions useConstraintsByWeakening(List<WordConstraint> constraints, WordsToSuggestions candidates) throws EnforcedConstraintReturnedZeroException, AttemptedDisableOfEnforcedConstraintException {
        WordsToSuggestions result = new WordsToSuggestions();
        for (Map.Entry<Word,Set<Word>> entry : candidates.entrySet()) {
            Set<Word> temp = useConstraintsByWeakening(constraints, entry.getKey(), entry.getValue());
            result.put(entry.getKey(), temp);
        }
        return result;
    }

    public static Set<Word> useConstraintsByWeakening(List<WordConstraint> constraints, Collection<Word> originals) throws EnforcedConstraintReturnedZeroException {
        Set<Word> result = new HashSet<>(originals);
        for (int c = 0; c < constraints.size(); c++) {
            WordConstraint wordConstraint = constraints.get(c);
            if (!wordConstraint.isEnabled()) continue;
            if (!wordConstraint.isOldWordSpecific())
                result.retainAll(wordConstraint.useWithPresetFields(originals));
            else {
                for (Word w : originals)
                    result.retainAll(wordConstraint.useOldWordSpecific(originals, w));
            }
            if (result.isEmpty()) {
                //weaken or disable constraint
                if (wordConstraint.isEnforced())
                    throw new EnforcedConstraintReturnedZeroException(wordConstraint);
                if (!wordConstraint.weaken()) {
                    wordConstraint.disable();
                }

                //recurse, start filtering process over with weaker filters
                return useConstraintsByWeakening(constraints, originals);
            }
        }
        return result;
    }

    public static Word useConstraintsTo1ByWeakening(List<WordConstraint> constraints, Word oldWord, Collection<Word> candidates) throws EnforcedConstraintReturnedZeroException, AttemptedDisableOfEnforcedConstraintException {
        Set<Word> results = useConstraintsByWeakening(constraints, oldWord, candidates);
        if (results == null)
            return null;
        TreeSet<Word> result = new TreeSet<>(results);
        if (result != null && !result.isEmpty())
            return result.first();
        return null;
    }

    public static Set<Word> useConstraintsByWeakening(List<WordConstraint> constraints, Word oldWord, Collection<Word> candidates) throws EnforcedConstraintReturnedZeroException, AttemptedDisableOfEnforcedConstraintException {
        if (candidates == null || candidates.isEmpty() || constraints == null || constraints.isEmpty()) return null;
        Set<Word> remaining = new HashSet(candidates);
        for (int c = 0; c < constraints.size(); c++) {
            WordConstraint constraint = constraints.get(c);
            if (constraint.isEnabled()) {
                try {
                    remaining.retainAll(useConstraint(constraint, oldWord, remaining));
                } catch (NormalConstraintReturnedZeroException e) {
                    U.testPrint(constraint.toString() + " returned 0 results.");
                    constraints.set(c, weakenOrDisableConstraint(constraint));
                    remaining = new HashSet(candidates);
                    c = 0;
                }
            }
        }
        if (U.notNullnotEmpty(remaining)) {
            return remaining;
        }
        return null;
    }

    public static Set<Word> useConstraint(WordConstraint constraint, Word oldWord, Collection<Word> candidates) throws EnforcedConstraintReturnedZeroException, NormalConstraintReturnedZeroException {
        if (candidates == null || candidates.isEmpty() || constraint == null) return null;
        Set<Word> remaining = new HashSet(candidates);
        if (!constraint.isEnabled())
            return new HashSet<>(candidates);
        while (constraint.isEnabled()) {

            if (!constraint.isOldWordSpecific())
                remaining.retainAll(constraint.useWithPresetFields(remaining));
            else
                remaining.retainAll(constraint.useOldWordSpecific(remaining, oldWord));


            if (remaining.isEmpty()) {
                if (constraint.isEnforced())
                    throw new EnforcedConstraintReturnedZeroException(constraint);
                else
                    throw new NormalConstraintReturnedZeroException(constraint);
            }

            else
                return remaining;
        }


        if (U.notNullnotEmpty(remaining))
            return remaining;
        return null;
    }

    public static WordConstraint weakenOrDisableConstraint(WordConstraint constraint) throws AttemptedDisableOfEnforcedConstraintException {
        if (constraint.isEnforced())
            throw new AttemptedDisableOfEnforcedConstraintException(constraint);
        boolean wasWeakened = constraint.weaken();
        if (!wasWeakened)
            constraint.disable();
        return constraint;
    }

    public static void enableAllConstraints(List<WordConstraint> constraints) {
        for (WordConstraint c : constraints)
            c.enable();
    }

    public static void disableRhymeConstraints(List<WordConstraint> constraints) {
        for (WordConstraint c : constraints)
            if (c instanceof RhymeScoreConstraint)
                c.disable();
    }

    public static void disableUnenforcedConstraints(List<WordConstraint> constraints) {
        for (WordConstraint c : constraints)
            if (!c.isEnforced())
                c.disable();
    }

    public static void disableIntanceSpecificConstraints(List<WordConstraint> constraints) {
        for (WordConstraint c : constraints)
            if (c.isOldWordSpecific())
                c.disable();
    }

    public static void disableNonRhymeConstraints(List<WordConstraint> constraints) {
        for (WordConstraint c : constraints)
            if (!(c instanceof RhymeScoreConstraint))
                c.disable();
    }

//    public static Set<Word> useConstraints(List<WordConstraint> constraints, Set<Word> candidates, Word original) {
//        if (candidates == null || candidates.isEmpty() || constraints == null || constraints.isEmpty()) return null;
//        Set<Word> remaining = new HashSet<>();
//        for (int c = 0; c < constraints.size(); c++) {
//            WordConstraint wordConstraint = constraints.get(c);
//            if (!wordConstraint.isEnabled()) continue;
//            if (wordConstraint.isOldWordSpecific())
//                remaining.retainAll(wordConstraint.useOldWordSpecific(candidates, original));
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































