package filters;

public abstract class Filter extends FilterObject {

    private Direction direction;

    public Filter() {
        this.setDirection(Direction.INCLUDE_MATCH);
    }

    public Filter(Direction direction) {
        this.setDirection(direction);
    }

    protected Direction getDirection() {
        return direction;
    }

    private void setDirection(Direction direction) {
        this.direction = direction;
    }
}



































































