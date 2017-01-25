package rhyme;

public final class Rhyme {

    private int rhymeId;

    public Rhyme(int rhymeId) {
        this.rhymeId = rhymeId;
    }

    public int getRhymeId() {
        return rhymeId;
    }

    public void setRhymeId(int rhymeId) {
        this.rhymeId = rhymeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rhyme rhyme = (Rhyme) o;

        return getRhymeId() == rhyme.getRhymeId();
    }

    @Override
    public int hashCode() {
        return getRhymeId();
    }
}
