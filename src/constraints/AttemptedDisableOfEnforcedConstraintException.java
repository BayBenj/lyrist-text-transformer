package constraints;

public class AttemptedDisableOfEnforcedConstraintException extends EnforcedConstraintException {

    public AttemptedDisableOfEnforcedConstraintException(Constraint enforcedConstraint) {
        super(enforcedConstraint);
    }

    @Override
    public void printStackTrace() {
        System.out.println("\t***Exception thrown because of attempt to disable enforced constraint: " + constraint.toString());
        printStackTrace(System.err);
    }
}
