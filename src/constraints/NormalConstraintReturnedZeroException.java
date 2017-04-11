package constraints;

public class NormalConstraintReturnedZeroException extends ConstraintException {
    public NormalConstraintReturnedZeroException(Constraint constraint) {
        super(constraint);
    }

    @Override
    public void printStackTrace() {
        System.out.println("\t***Exception thrown because 0 results were returned by normal constraint: " + constraint.toString());
        printStackTrace(System.err);
    }

}
