package songtools;

import java.util.List;

public abstract class Operation {

    private Object output;

    public abstract Object run(List<Object> input);

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }
}
