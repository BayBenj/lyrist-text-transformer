package constraintCHECK;
import java.io.Serializable;
import java.util.List;

import conditionCHECK.ConstraintCondition;
import conditionCHECK.DelayedConstraintCondition;
import utils.Utils;

public class Constraint<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final int FINAL_POSITION = -1;

    public boolean getDesiredConditionState() {
        return desiredConditionState;
    }

    public int getPosition() {
        return position;
    }

    public ConstraintCondition<T> getCondition() {
        return condition;
    }

    // We allow for a constraintCHECK to enforce a conditionCHECK or the negation of the conditionCHECK
    private boolean desiredConditionState;
    private int position;
    protected ConstraintCondition<T> condition;

    public Constraint(int i, ConstraintCondition<T> condition, boolean desiredConditionState) {
        this.position = i;
        this.condition = condition;
        this.desiredConditionState = desiredConditionState;
    }

    public static <T> void reifyConstraints(List<Constraint<T>> constraints, List<List<T>> tokenLine) {
        for (Constraint<T> constraint : constraints) {
            ConstraintCondition<T> condition = constraint.getCondition();
            if(condition instanceof DelayedConstraintCondition)
            {
                ((DelayedConstraintCondition<T>) condition).reify(tokenLine);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("token in ");
        str.append(Utils.getPositionString(position));
        str.append(" must ");
        if (!desiredConditionState)
            str.append("not ");
        str.append("be a ");
        str.append(condition);

        return str.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((condition == null) ? 0 : condition.hashCode());
        result = prime * result + (desiredConditionState ? 1231 : 1237);
        result = prime * result + position;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Constraint))
            return false;
        @SuppressWarnings("unchecked")
        Constraint<T> other = (Constraint<T>) obj;
        if (condition == null) {
            if (other.condition != null)
                return false;
        } else if (!condition.equals(other.condition))
            return false;
        if (desiredConditionState != other.desiredConditionState)
            return false;
        if (position != other.position)
            return false;
        return true;
    }
}








































































































































