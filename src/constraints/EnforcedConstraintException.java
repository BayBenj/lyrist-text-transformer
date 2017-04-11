package constraints;

public class EnforcedConstraintException extends ConstraintException {
    public EnforcedConstraintException(Constraint enforcedConstraint) {
        super(enforcedConstraint);
    }

    @Override
    public void printStackTrace() {
        System.out.println("\t***Exception thrown because of enforced constraint: " + constraint.toString());
        printStackTrace(System.err);
    }

}
