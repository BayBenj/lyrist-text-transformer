package constraints;

import filters.Filter;

public class TimeConstraint extends Constraint {

    private int milliseconds;

    public TimeConstraint(int milliseconds) {
        this.setMilliseconds(milliseconds);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }
}
