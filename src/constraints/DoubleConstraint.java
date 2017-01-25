package constraints;

import filters.Filter;

public abstract class DoubleConstraint extends Constraint {

    protected double score;

    public DoubleConstraint(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public abstract boolean weaken();

    @Override
    public Filter getFilter() {
        return null;
    }

}

















































































