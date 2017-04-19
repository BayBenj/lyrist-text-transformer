package rhyme;

import elements.Word;

import java.util.List;

public class RhymeClass {

    private int rhymeId;
    private SyllableGroup model = null;
    private List<Word> instances;

    public RhymeClass(Word model) {
        this.model = model.getRhymeTail();
    }

    public RhymeClass(SyllableGroup modelRhyme) {
        this.model = modelRhyme;
    }

    public RhymeClass(int rhymeId) {
        this.rhymeId = rhymeId;
    }

    public int getRhymeId() {
        return rhymeId;
    }

    public void setRhymeId(int rhymeId) {
        this.rhymeId = rhymeId;
    }

    public SyllableGroup getModel() {
        return model;
    }

    public void setModel(SyllableGroup model) {
        this.model = model;
    }

    public List<Word> getInstances() {
        return instances;
    }

    public void setInstances(List<Word> instances) {
        this.instances = instances;
    }

    public void addInstance(Word instance) {
        this.instances.add(instance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RhymeClass rhyme = (RhymeClass) o;

        return getRhymeId() == rhyme.getRhymeId();
    }

    @Override
    public int hashCode() {
        return getRhymeId();
    }
}
