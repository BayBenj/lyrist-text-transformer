package main;

import java.util.List;

import rhyme.ConstraintOld;
import utils.U;

public abstract class DelayedConstraintCondition<T> extends ConstraintCondition<T> {
    protected T prevT = null;
    protected int prevLineNumber;
    protected int prevPos;

    public DelayedConstraintCondition(int line, int pos) {
        this.prevLineNumber = line;
        this.prevPos = pos;
    }

    /**
     * A DelayedMatchConstraint is a constraint that cannot be fully defined until a previous
     * line has been generated (i.e., the constraint depends on the value of a generated
     * element)
     * @param prevLineNumber the previous line from which to extract the constraint
     * @param prevPos the position in the previous line from which to extract the match
     * @param pos the position to constrain in the constrained line
     * @param condition
     */

    public void setPrevT(T prevT)
    {
        this.prevT = prevT;
    }

    public void reify(List<List<T>> tokenLines) {
        List<T> prevLine = tokenLines.get(prevLineNumber);
        if (prevPos == ConstraintOld.FINAL_POSITION){
            setPrevT(prevLine.get(prevLine.size()-1));
        }
        else{
            setPrevT(prevLine.get(prevPos));
        }
    }

    public String asString() {
        return " with the token at line " + (prevLineNumber+1) + ", " + U.getPositionString(prevPos) + " (" + (prevT == null? "" : prevT) + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + prevLineNumber;
        result = prime * result + prevPos;
        result = prime * result + ((prevT == null) ? 0 : prevT.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof DelayedConstraintCondition))
            return false;
        DelayedConstraintCondition<T> other = (DelayedConstraintCondition<T>) obj;
        if (prevLineNumber != other.prevLineNumber)
            return false;
        if (prevPos != other.prevPos)
            return false;
        if (prevT == null) {
            if (other.prevT != null)
                return false;
        } else if (!prevT.equals(other.prevT))
            return false;
        return true;
    }
}
