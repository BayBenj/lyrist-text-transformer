package rhyme;
import java.io.Serializable;
import java.util.List;

import main.ConstraintCondition;
import main.DelayedConstraintCondition;
import utils.U;

public class ConstraintOld<T> implements Serializable {

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

    // We allow for a constraint to enforce a condition or the negation of the condition
    private boolean desiredConditionState;
    private int position;
    protected ConstraintCondition<T> condition;

    public ConstraintOld(int i, ConstraintCondition<T> condition, boolean desiredConditionState) {
        this.position = i;
        this.condition = condition;
        this.desiredConditionState = desiredConditionState;
    }

    public static <T> void reifyConstraints(List<ConstraintOld<T>> constraintOlds, List<List<T>> tokenLine) {
        for (ConstraintOld<T> constraintOld : constraintOlds) {
            ConstraintCondition<T> condition = constraintOld.getCondition();
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
        str.append(U.getPositionString(position));
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
        if (!(obj instanceof ConstraintOld))
            return false;
        @SuppressWarnings("unchecked")
        ConstraintOld<T> other = (ConstraintOld<T>) obj;
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





































































































































