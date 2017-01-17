package filters;

import main.ProgramArgs;
import song.Word;
import utils.Utils;

import java.util.Collection;
import java.util.Set;

public abstract class WordFilter extends Filter {

    public WordFilter() {
        super(ReturnType.MATCHES);
    }

    public WordFilter(ReturnType returnType) {
        super(returnType);
    }

    public Set<Word> filterWords(Collection<Word> originalWords) {
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

    public abstract Set<Word> doFilter(Collection<Word> originalWords);

}

/*
TODO > (maybe) Make sure only filters.FilterEquation can access filters.WordFilter methods
 */























































































