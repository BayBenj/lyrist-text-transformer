package markovCHECK;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SingleOrderMarkovModel<T> extends AbstractMarkovModel<T>{

	T[] states;
	double[] logPriors;
	double[][] logTransitions;
	Map<T, Integer> stateIndex = new HashMap<T, Integer>();
	Random rand = new Random();

	/*
	 * Makes deep copy of all input params for new instance of Markov Model
	 */
	@SuppressWarnings("unchecked")
	public SingleOrderMarkovModel(T[] states, double[] priors, double[][] transitions) {
		assert(states.length == priors.length);
		assert(transitions.length == transitions[0].length);
		
		this.states = (T[]) new Object[states.length];
		this.logPriors = new double[priors.length];
		this.logTransitions = new double[transitions.length][transitions.length];
		
		for (int i = 0; i < states.length; i++) {
			assert(!stateIndex.containsKey(states[i]));
			this.states[i] = states[i];
			stateIndex.put(states[i], i);
			this.logPriors[i] = Math.log(priors[i]);
			for (int j = 0; j < transitions[i].length; j++) {
				this.logTransitions[i][j] = Math.log(transitions[i][j]);
			}
		}
	}

	public double probabilityOfSequence(T[] seq) {
		double logProb = 0;
		
		if(seq.length == 0)
			return Double.NaN;
		
		int lastStateIndex, nextStateIndex = stateIndex.get(seq[0]);
		logProb += logPriors[nextStateIndex];
		
		for (int i = 1; i < seq.length; i++) {
			lastStateIndex = nextStateIndex;
			nextStateIndex = stateIndex.get(seq[i]);

			logProb += logTransitions[lastStateIndex][nextStateIndex];
		}
		
		return Math.exp(logProb);
	}

	public String toString()
	{
		StringBuilder str = new StringBuilder();
		
		str.append("[");
		for (int i = 0; i < logPriors.length; i++) {
			if(i!=0) str.append(", ");
			str.append(Math.exp(logPriors[i]));
		}
		str.append("]\n\n");
		
		for (int row = 0; row < logTransitions.length; row++) {
			str.append("[");
			for (int col = 0; col < logTransitions[row].length; col++) {
				if(col!=0) str.append(", ");
				str.append(Math.exp(logTransitions[row][col]));				
			}
			str.append("]\n");
		}
		
		return str.toString();
		
	}

	@Override
	public List<T> generate(int length) {
		int prevStateIdx = -1;
		int nextStateIdx = -1;
		
		List<T> newSeq = new ArrayList<T>();
		
		for (int i = 0; i < length; i++) {
			if(i==0)
			{
				nextStateIdx = sampleStartStateIdx();
			}
			else
			{
				nextStateIdx = sampleNextState(prevStateIdx);
			}
			
			if(nextStateIdx == -1)
			{
				return newSeq;
			}

			newSeq.add(states[nextStateIdx]);
			prevStateIdx = nextStateIdx;
		}
		
		return newSeq;
	}
	
	private int sampleNextState(int prevStateIdx) {
		double randomDouble = rand.nextDouble();
		
		double accumulativeProbability = 0.;
		
		for (int i = 0; i < logTransitions[prevStateIdx].length; i++) {
			// removed states in this position have an outsupport of 0
			if (logTransitions[prevStateIdx][i] != Double.NEGATIVE_INFINITY) 
			{
				accumulativeProbability += Math.exp(logTransitions[prevStateIdx][i]);
				if (accumulativeProbability >= randomDouble)
				{
					return i;
				}
			}
		}
		
		return -1;
	}

	private int sampleStartStateIdx() {
		double randomDouble = rand.nextDouble();
		
		double accumulativeProbability = 0.;
		
		for (int i = 0; i < logPriors.length; i++) {
			if (logPriors[i] != Double.NEGATIVE_INFINITY) // removed states in this position have an outsupport of 0
			{
				accumulativeProbability += Math.exp(logPriors[i]);
				if (accumulativeProbability >= randomDouble)
				{
					return i;
				}
			}
		}
		
		// we assume that in order to be satisfiable that there has to be some continuation and it must be the last
		return -1;
	}
}
































































































