//package filters;
//
//import java.util.*;
//
//public class StringFilterEquation extends FilterEquation {
//
//    public Set<String> run() {
//        return this.run(new HashSet<>());
//    }
//
//    public Set<String> intersectMatches(Collection<String> unfilteredStrings) {
//        if (unfilteredStrings == null)
//            return null;
//        super.currentOperator = Operator.INTERSECTION;
//        Set<String> currentStrings = new HashSet<>(unfilteredStrings);
//        for (FilterObject filterObject : this) {
//            if (filterObject instanceof Filter) {
//                StringFilter stringFilter = (StringFilter) filterObject;
//                currentStrings.retainAll(stringFilter.doFilter(unfilteredStrings));
//            }
//        }
//        return currentStrings;
//    }
//
//    public Set<String> unionMatches(Collection<String> unfilteredStrings) {
//        if (unfilteredStrings == null)
//            return null;
//        super.currentOperator = Operator.UNION;
//        Set<String> currentStrings = new HashSet<>();
//        for (FilterObject filterObject : this) {
//            if (filterObject instanceof Filter) {
//                StringFilter stringFilter = (StringFilter) filterObject;
//                currentStrings.addAll(stringFilter.doFilter(currentStrings));
//            }
//        }
//        return currentStrings;
//    }
//
//    public Set<String> removeMatches(Collection<String> unfilteredStrings) {
//        if (unfilteredStrings == null)
//            return null;
//        super.currentOperator = Operator.DIFFERENCE;
//        Set<String> currentStrings = new HashSet<>(unfilteredStrings);
//        for (FilterObject filterObject : this) {
//            if (filterObject instanceof Filter) {
//                StringFilter stringFilter = (StringFilter) filterObject;
//                currentStrings.removeAll(stringFilter.doFilter(unfilteredStrings));
//            }
//        }
//        return currentStrings;
//    }
//
//    public Set<String> run(Collection<String> unfilteredStrings) {
//        if (unfilteredStrings == null)
//            return null;
//        Set<String> currentStrings = new HashSet<>(unfilteredStrings);
//        for (FilterObject filterObject : this) {
//            if (filterObject instanceof Filter) {
//                StringFilter stringFilter = (StringFilter) filterObject;
//                switch (super.currentOperator) {
//                    case INTERSECTION:
//                        currentStrings.retainAll(stringFilter.doFilter(unfilteredStrings));
//                        break;
//                    case UNION:
//                        try {
//                            currentStrings.addAll(stringFilter.doFilter(unfilteredStrings));
//                        }
//                        catch (NullPointerException e) {
//                            System.out.println("stop for testing");
//                        }
//                        break;
//                    case DIFFERENCE:
//                        currentStrings.removeAll(stringFilter.doFilter(unfilteredStrings));
//                        break;
//                }
//            }
//            else if (filterObject instanceof FilterOperator) {
//                super.currentOperator = super.getOperatorEnum((FilterOperator)filterObject);
//            }
//        }
//        return currentStrings;
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
