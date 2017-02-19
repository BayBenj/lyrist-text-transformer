//package filters;
//
//import main.MultiProgramArgs;
//import elements.Word;
//import utils.U;
//
//import java.util.Collection;
//import java.util.Set;
//
//public abstract class WordFilter extends Filter {
//
//    public WordFilter() {
//        super(ReturnType.MATCHES);
//    }
//
//    public WordFilter(ReturnType returnType) {
//        super(returnType);
//    }
//
//    public Set<Word> filterWords(Collection<Word> originalWords) {
//        if (MultiProgramArgs.isDebugMode()) {
//            FilterUtils.startTimer();
//            Set<Word> result = this.doFilter(originalWords);
//            FilterUtils.stopTimer();
//            U.testPrint(this.getClass().toString() + " time: " + FilterUtils.getTotalTime());
//            return result;
//        }
//        else
//            return this.doFilter(originalWords);
//    }
//
//    public abstract Set<Word> doFilter(Collection<Word> originalWords);
//
//}
//
///*
//TODO > (maybe) Make sure only filters.FilterEquation can access filters.WordFilter methods
// */
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
