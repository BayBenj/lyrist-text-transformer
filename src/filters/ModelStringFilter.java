package filters;

public abstract class ModelStringFilter extends Filter {

    private String model;

    public ModelStringFilter(String model) {
        this.setModel(model);
    }

    protected String getModel() {
        return model;
    }

    private void setModel(String model) {
        this.model = model;
    }
}
