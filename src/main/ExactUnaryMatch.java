//package main;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class ExactUnaryMatch<T> extends ConstraintCondition<T> {
//
//    private Set<T> acceptableStates;
//
//    public ExactUnaryMatch(T[] acceptableStates) {
//        this.acceptableStates = new HashSet<T>();
//        for (int j = 0; j < acceptableStates.length; j++) {
//            this.acceptableStates.add(acceptableStates[j]);
//        }
//    }
//
//    @Override
//    public boolean isSatisfiedBy(T t) {
//        return acceptableStates.contains(t);
//    }
//
//    @Override
//    protected String asString() {
//        StringBuilder str = new StringBuilder();
//
//        str.append(" with token in [");
//        boolean first = true;
//        for (T t : acceptableStates) {
//            if(!first)
//            {
//                str.append(',');
//            }
//            else
//            {
//                first = false;
//            }
//            str.append(t);
//        }
//
//        str.append("]");
//
//        return str.toString();
//    }
//
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((acceptableStates == null) ? 0 : acceptableStates.hashCode()); // TODO: sets should be equal if they doesContain the oldWordSpecific elements
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (!(obj instanceof ExactUnaryMatch))
//            return false;
//        ExactUnaryMatch other = (ExactUnaryMatch) obj;
//        if (acceptableStates == null) {
//            if (other.acceptableStates != null)
//                return false;
//        } else if (!acceptableStates.equals(other.acceptableStates))
//            return false;
//        return true;
//    }
//}
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
