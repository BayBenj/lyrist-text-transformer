package filters;

public abstract class Filter extends FilterObject {

    private ReturnType returnType;

    public Filter() {
        this.setReturnType(ReturnType.MATCHES);
    }

    public Filter(ReturnType returnType) {
        this.setReturnType(returnType);
    }

    protected ReturnType getReturnType() {
        return returnType;
    }

    private void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }
}



































































