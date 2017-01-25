package songtools;

import java.util.List;

public abstract class SuperOperation extends Operation {

    private List<Operation> suboperations;

    public SuperOperation(List<Operation> suboperations) {
        this.suboperations = suboperations;
    }

    @Override
    public Object run(List<Object> input) {
        return null;
    }

    public List<Operation> getSuboperations() {
        return suboperations;
    }

    public void setSuboperations(List<Operation> suboperations) {
        this.suboperations = suboperations;
    }
}
