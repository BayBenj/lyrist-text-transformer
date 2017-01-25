package constraints;

public class RhymeScoreConstraint extends DoubleConstraint {

    public RhymeScoreConstraint(double score) {
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
