package filters;

import elements.Song;
import main.ProgramArgs;
import elements.Word;
import utils.U;

import java.util.Collection;
import java.util.Set;

public abstract class SongFilter extends Filter {

    public SongFilter() {
        super(ReturnType.MATCHES);
    }

    public SongFilter(ReturnType returnType) {
        super(returnType);
    }

    public Set<Word> filterWords(Song song) {
        if (ProgramArgs.isTesting()) {
            FilterUtils.startTimer();
            Set<Word> result = this.doFilter(song);
            FilterUtils.stopTimer();
            U.testPrint(this.getClass().toString() + " time: " + FilterUtils.getTotalTime());
            return result;
        }
        else
            return this.doFilter(song);
    }

    public abstract Set<Word> doFilter(Song song);

}

/*
TODO > (maybe) Make sure only filters.FilterEquation can access filters.WordFilter methods
 */


























































































