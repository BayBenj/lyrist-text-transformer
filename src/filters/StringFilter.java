package filters;

import main.ProgramArgs;
import utils.Utils;

import java.util.Collection;
import java.util.Set;

public abstract class StringFilter extends Filter {

    public StringFilter() {
        super(ReturnType.NON_MATCHES);
    }

    public StringFilter(ReturnType returnType) {
        super(returnType);
    }

    public Set<String> filter(Collection<String> originalStrings) {
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

    public abstract Set<String> doFilter(Collection<String> originalStrings);

}






























