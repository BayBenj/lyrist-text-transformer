package filters;

public abstract class VocabListFilter extends StringFilter {

    protected VocabList vocabList;

    public VocabListFilter(VocabList vocabList) {
        this.setVocabList(vocabList);
    }

    public VocabListFilter(ReturnType returnType, VocabList vocabList) {
        super(returnType);
        this.setVocabList(vocabList);
    }

    protected VocabList getVocabList() {
        return vocabList;
    }

    private void setVocabList(VocabList vocabList) {
        this.vocabList = vocabList;
    }
}

//    public Object filterWords(VocabList vocabList, HashSet<String> w2vSuggestions) {
//            for (String tempString : w2vSuggestions)
//                if (!vocabList.contains(w2vSuggestions))
//                    w2vSuggestions.remove(tempString);
//            return w2vSuggestions;
//    }























































































