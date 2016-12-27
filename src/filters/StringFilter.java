package filters;

import main.ProgramArgs;
import song.Word;
import utils.Utils;

import java.util.Set;

public abstract class StringFilter extends Filter {

    public StringFilter() {
        super(Direction.EXCLUDE_MATCH);
    }

    public StringFilter(Direction direction) {
        super(direction);
    }

    public Set<String> filter(Set<String> originalStrings) {
        if (ProgramArgs.isTesting()) {
            FilterUtils.startTimer();
            Set<String> result = this.doFilter(originalStrings);
            FilterUtils.stopTimer();
            Utils.testPrint(this.getClass().toString() + " time: " + FilterUtils.getTotalTime());
            return result;
        }
        else
            return this.doFilter(originalStrings);
    }

    public abstract Set<String> doFilter(Set<String> originalWords);

}

























