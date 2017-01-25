package filters;

import filters.FilterUtils;
import filters.ReturnType;
import main.ProgramArgs;
import elements.Word;
import utils.U;
import java.util.Map;

public abstract class CosineWordFilter extends Filter {

    public CosineWordFilter() {
        super(ReturnType.MATCHES);
    }

    public CosineWordFilter(ReturnType returnType) {
        super(returnType);
    }

    public Map<Double, Word> filterWords(Map<Double, Word> originalWords) {
        if (ProgramArgs.isTesting()) {
            FilterUtils.startTimer();
            Map<Double, Word> result = this.doFilter(originalWords);
            FilterUtils.stopTimer();
            U.testPrint(this.getClass().toString() + " time: " + FilterUtils.getTotalTime());
            return result;
        }
        else
            return this.doFilter(originalWords);
    }

    public abstract Map<Double, Word> doFilter(Map<Double, Word> originalWords);

}

/*
TODO > (maybe) Make sure only filters.FilterEquation can access filters.WordFilter methods
 */



















































































