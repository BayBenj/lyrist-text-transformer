package constraints;

public class EnforcedConstraintReturnedZeroException extends EnforcedConstraintException {
    public EnforcedConstraintReturnedZeroException(Constraint enforcedConstraint) {
        super(enforcedConstraint);
    }

    @Override
    public void printStackTrace() {
        System.out.println("\t***Exception thrown because 0 results were returned by enforced constraint: " + constraint.toString());
        printStackTrace(System.err);
    }

}
