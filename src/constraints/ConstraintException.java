package constraints;

public class ConstraintException extends Exception {

    protected Constraint constraint;

    public ConstraintException(Constraint constraint) {
        this.constraint = constraint;
    }

    @Override
    public void printStackTrace() {
        System.out.println("\t***Exception thrown because of constraint: " + constraint.toString());
        printStackTrace(System.err);
    }

}
