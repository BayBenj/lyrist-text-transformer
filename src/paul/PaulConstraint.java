package paul;
import java.io.Serializable;
import constraints.Constraint;

public class PaulConstraint<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final int FINAL_POSITION = -1;
    public static final double ALL_POSITIONS = -2.0;
    public static final String CONSTRAINT_ANNOTATIONS_DIR = "wikifonia_rhyme_annotations";

    public boolean getDesiredConditionState() {
        return desiredConditionState;
    }

    public PaulConstraintCondition<T> getCondition() {
        return condition;
    }

    // We allow for a constraint to enforce a condition or the negation of the condition
    private boolean desiredConditionState;
    protected PaulConstraintCondition<T> condition;

    public PaulConstraint(PaulConstraintCondition<T> condition, boolean desiredConditionState) {
        this.condition = condition;
        this.desiredConditionState = desiredConditionState;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("token must ");
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
        PaulConstraint<T> other = (PaulConstraint<T>) obj;
        if (condition == null) {
            if (other.condition != null)
                return false;
        } else if (!condition.equals(other.condition))
            return false;
        if (desiredConditionState != other.desiredConditionState)
            return false;
        return true;
    }
}
