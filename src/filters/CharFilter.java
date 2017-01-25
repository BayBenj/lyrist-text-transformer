package filters;

public abstract class  CharFilter extends StringFilter {

    protected  CharList charList;

    public  CharFilter( CharList  charList) {
        this.setCharList(charList);
    }

    public  CharFilter(ReturnType returnType, CharList  charList) {
        super(returnType);
        this.setCharList( charList);
    }

    protected CharList getCharList() {
        return  charList;
    }

    private void setCharList( CharList  charList) {
        this. charList =  charList;
    }
}














































































