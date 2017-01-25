package constraints;

public class CosineConstraint extends DoubleConstraint {

    public CosineConstraint(double score) {
        super(score);
    }

    @Override
    public boolean weaken() {
        this.weaken(.1);
        return true;
    }

    public boolean weaken(double d) {
        this.score -= d;
        return true;
    }

}
