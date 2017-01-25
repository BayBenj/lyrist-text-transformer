package songtools;

public class LinkedOperation {

    private GeneralOperation operation;
    private LinkedOperation outputDestination;

    public LinkedOperation(Operation operation, LinkedOperation outputDestination) {
        this.operation = operation;
        this.outputDestination = outputDestination;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}





































































