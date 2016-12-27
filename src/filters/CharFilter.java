package filters;

import song. CharList;

public abstract class  CharFilter extends StringFilter {

    protected  CharList charList;

    public  CharFilter( CharList  charList) {
        this.setCharList(charList);
    }

    public  CharFilter(Direction direction,  CharList  charList) {
        super(direction);
        this.setCharList( charList);
    }

    protected CharList getCharList() {
        return  charList;
    }

    private void setCharList( CharList  charList) {
        this. charList =  charList;
    }
}














































































