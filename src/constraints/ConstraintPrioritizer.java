package constraints;

import java.util.TreeMap;

public abstract class ConstraintPrioritizer {

    public static TreeMap<Integer, Constraint> removeLastConstraint(TreeMap<Integer,Constraint> constraints) {
        //constraints should be coming in with lowest priority first
        constraints.remove(constraints.firstKey());
        return constraints;
    }

    public static TreeMap<Integer, Constraint> weakenOrRemoveLastConstraint(TreeMap<Integer,Constraint> constraints) {
        //constraints should be coming in with lowest priority first
        if (constraints.firstEntry().getValue() instanceof DoubleConstraint)
            constraints.put(constraints.firstKey(), weakenConstraint((DoubleConstraint)constraints.firstEntry().getValue()));
        else
            constraints = removeLastConstraint(constraints);
        return constraints;
    }

    public static DoubleConstraint weakenConstraint(DoubleConstraint constraint) {
        constraint.weaken();
        return constraint;
    }

}








































































