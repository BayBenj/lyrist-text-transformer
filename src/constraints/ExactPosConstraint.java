package constraints;

import elements.Pos;
import filters.Filter;
import filters.PosMatchFilter;

public class ExactPosConstraint extends Constraint {

    public Filter getFilter(Pos pos) {
        return new PosMatchFilter();
    }
}
