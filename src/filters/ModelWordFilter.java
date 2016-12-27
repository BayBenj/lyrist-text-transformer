package filters;

import song.Word;

public abstract class ModelWordFilter extends WordFilter {

    private Word model;

    public ModelWordFilter(Word model) {
        this.setModel(model);
    }

    public ModelWordFilter(Direction direction, Word model) {
        super(direction);
        this.model = model;
    }

    protected Word getModel() {
        return model;
    }

    private void setModel(Word model) {
        this.model = model;
    }
}
