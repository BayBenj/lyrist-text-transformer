package filters;

import elements.Word;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WordFilterEquation extends FilterEquation {

    public Set<Word> run() {
        return this.run(new HashSet<>());
    }

    public Set<Word> intersectMatches(Collection<Word> unfilteredWords) {
        if (unfilteredWords == null)
            return null;
        super.currentOperator = Operator.INTERSECTION;
        Set<Word> currentWords = new HashSet<>(unfilteredWords);
        for (FilterObject filterObject : this) {
            if (filterObject instanceof Filter) {
                WordFilter wordFilter = (WordFilter) filterObject;
                currentWords.retainAll(wordFilter.filterWords(unfilteredWords));
            }
        }
        return currentWords;
    }

    public Set<Word> unionMatches(Collection<Word> unfilteredWords) {
        if (unfilteredWords == null)
            return null;
        super.currentOperator = Operator.UNION;
        Set<Word> currentWords = new HashSet<>();
        for (FilterObject filterObject : this) {
            if (filterObject instanceof Filter) {
                WordFilter wordFilter = (WordFilter) filterObject;
                currentWords.addAll(wordFilter.filterWords(unfilteredWords));
            }
        }
        return currentWords;
    }

    public Set<Word> removeMatches(Collection<Word> unfilteredWords) {
        if (unfilteredWords == null)
            return null;
        super.currentOperator = Operator.DIFFERENCE;
        Set<Word> currentWords = new HashSet<>(unfilteredWords);
        for (FilterObject filterObject : this) {
            if (filterObject instanceof Filter) {
                WordFilter wordFilter = (WordFilter) filterObject;
                currentWords.removeAll(wordFilter.filterWords(unfilteredWords));
            }
        }
        return currentWords;
    }

    public Set<Word> run(Collection<Word> unfilteredWords) {
        if (unfilteredWords == null)
            return null;
        Set<Word> currentWords = new HashSet<>(unfilteredWords);
        for (FilterObject filterObject : this) {
            if (filterObject instanceof Filter) {
                WordFilter wordFilter = (WordFilter) filterObject;
                switch (super.currentOperator) {
                    case INTERSECTION:
                        currentWords.retainAll(wordFilter.filterWords(unfilteredWords));
                        break;
                    case UNION:
                        try {
                            currentWords.addAll(wordFilter.filterWords(unfilteredWords));
                        }
                        catch (NullPointerException e) {
                            System.out.println("stop for testing");
                        }
                        break;
                    case DIFFERENCE:
                        currentWords.removeAll(wordFilter.filterWords(unfilteredWords));
                        break;
                }
            }
            else if (filterObject instanceof FilterOperator) {
                super.currentOperator = super.getOperatorEnum((FilterOperator)filterObject);
            }
        }
        return currentWords;
    }
}




































