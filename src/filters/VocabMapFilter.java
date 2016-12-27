package filters;

import word2vec.VocabMap;

public abstract class VocabMapFilter extends StringFilter {

    protected VocabMap vocabMap;

    public VocabMapFilter(VocabMap vocabMap) {
        this.setVocabMap(vocabMap);
    }

    public VocabMapFilter(Direction direction, VocabMap vocabMap) {
        super(direction);
        this.setVocabMap(vocabMap);
    }

    protected VocabMap getVocabMap() {
        return vocabMap;
    }

    private void setVocabMap(VocabMap vocabMap) {
        this.vocabMap = vocabMap;
    }

}



