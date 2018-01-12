package constraints;

import elements.Word;
import filters.ReturnType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CosineSimilarityConstraint extends DoubleConstraint {

	public CosineSimilarityConstraint(NonModelNum comparison) {
		super(comparison);
		this.oldWordSpecific = false;
	}

	public CosineSimilarityConstraint(ModelNum comparison) {
		super(comparison);
		this.oldWordSpecific = false;
	}

	public CosineSimilarityConstraint(ModelNum comparison, double dbl, ReturnType returnType) {
		super(comparison, dbl, returnType);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~Word transforming~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public Map<Word, Double> wordsToSpecficDoubleType(Collection<Word> words) {
		return wordsToRhymeScores(words);
	}

	@Override
	public Double wordToSpecificDoubleType(Word word) {
		return word.getCosineSimilarity();
	}

	private static Map<Word,Double> wordsToRhymeScores(Collection<Word> words) {
		Map<Word,Double> result = new HashMap<>();
		for (Word w : words)
			result.put(w, w.getCosineSimilarity());
		return result;
	}

	@Override
	public boolean weaken() {
		return this.weaken(.1);
	}

	@Override
	public String toString() {
		return "CosineSimilarityConstraint";
	}

}
