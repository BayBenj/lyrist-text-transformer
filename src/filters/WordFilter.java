package filters;

import main.ProgramArgs;
import song.Word;
import utils.Utils;

import java.util.Set;

public abstract class WordFilter extends Filter {

    public WordFilter() {
        super(Direction.INCLUDE_MATCH);
    }

    public WordFilter(Direction direction) {
        super(direction);
    }

    public Set<Word> filterWords(Set<Word> originalWords) {
        if (ProgramArgs.isTesting()) {
            FilterUtils.startTimer();
            Set<Word> result = this.doFilter(originalWords);
            FilterUtils.stopTimer();
            Utils.testPrint(this.getClass().toString() + " time: " + FilterUtils.getTotalTime());
            return result;
        }
        else
            return this.doFilter(originalWords);
    }

    public abstract Set<Word> doFilter(Set<Word> originalWords);

}

/*
TODO > (maybe) Make sure only filters.FilterEquation can access filters.WordFilter methods
 */























































































