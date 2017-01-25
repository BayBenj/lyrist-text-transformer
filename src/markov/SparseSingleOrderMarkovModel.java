package markov;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class SparseSingleOrderMarkovModel<T> extends AbstractMarkovModel<T>{

	T[] states;
	Map<Integer,Double> logPriors;
	Map<Integer,Map<Integer,Double>> logTransitions;
	Map<T, Integer> stateIndex;
	Random rand = new Random();

	/*
	 * Makes deep copy of all input params for new instance of Markov Model
	 */
	@SuppressWarnings("unchecked")
	public SparseSingleOrderMarkovModel(T[] states, Map<Integer, Double> priors, Map<Integer,Map<Integer,Double>> transitions) {
		this.states = (T[]) new Object[states.length];
		this.logPriors = new HashMap<Integer,Double>(priors.size());
		this.logTransitions = new HashMap<Integer, Map<Integer, Double>>(transitions.size());
		
		Double probability;
		Map<Integer, Double> newInnerMap, oldInnerMap;
		for (int i = 0; i < states.length; i++) {
			assert(!stateIndex.containsKey(states[i]));
			this.states[i] = states[i];
			stateIndex.put(states[i], i);
			probability = priors.get(i);
			if (probability != null) {
				this.logPriors.put(i, Math.log(probability));
			}
			
			newInnerMap = new HashMap<Integer, Double>();
			this.logTransitions.put(i, newInnerMap);
			oldInnerMap = transitions.get(i);
			for (Entry<Integer, Double> entry : oldInnerMap.entrySet()) {
				newInnerMap.put(entry.getKey(),Math.log(entry.getValue()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public SparseSingleOrderMarkovModel(Map<T, Integer> statesByIndex, Map<Integer, Double> priors, Map<Integer, Map<Integer, Double>> transitions) {
		this.states = (T[]) new Object[statesByIndex.size()];
		this.stateIndex = statesByIndex;
		this.logPriors = new HashMap<Integer,Double>(priors.size());
		this.logTransitions = new HashMap<Integer, Map<Integer, Double>>(transitions.size());
		
		Double probability;
		Map<Integer, Double> newInnerMap, oldInnerMap;
		int i;
		for (Entry<T,Integer> stateIdx: stateIndex.entrySet()) {
			i = stateIdx.getValue();
			this.states[i] = stateIdx.getKey();
			probability = priors.get(i);
			if (probability != null) {
				this.logPriors.put(i, Math.log(probability));
			}
			
			newInnerMap = new HashMap<Integer, Double>();
			this.logTransitions.put(i, newInnerMap);
			oldInnerMap = transitions.get(i);
			for (Entry<Integer, Double> entry : oldInnerMap.entrySet()) {
				newInnerMap.put(entry.getKey(),Math.log(entry.getValue()));
			}
		}
	}

	public double probabilityOfSequence(T[] seq) {
		double logProb = 0;
		
		if(seq.length == 0)
			return Double.NaN;
		
		int lastStateIndex, nextStateIndex = stateIndex.get(seq[0]);
		Double value = logPriors.get(nextStateIndex);
		if (value == null) {
			return 0.;
		} else { 
			logProb += value;
		}
		Map<Integer, Double> innerMap;
		for (int i = 1; i < seq.length; i++) {
			lastStateIndex = nextStateIndex;
			nextStateIndex = stateIndex.get(seq[i]);

			innerMap = logTransitions.get(lastStateIndex);
			if (innerMap == null)
				return 0.;
			value = innerMap.get(nextStateIndex);
			if (value == null)
				return 0.;
			
			logProb += value;
		}
		
		return Math.exp(logProb);
	}

	public String toString()
	{
		StringBuilder str = new StringBuilder();
		
		str.append("logPriors:");
		str.append("[");
		for (Entry<Integer, Double> entry : logPriors.entrySet()) {
			str.append("\n\t");
			str.append(entry.getKey());
			str.append(", ");
			str.append(Math.exp(entry.getValue()));
		}
		str.append("\n]\n\n");
		
		str.append("logTransitions:");
		for (Entry<Integer, Map<Integer, Double>> entry: logTransitions.entrySet()) {
			str.append("[");
			for (Entry<Integer, Double> innerEntry : entry.getValue().entrySet()) {
				str.append("\n\t");
				str.append(entry.getKey());
				str.append(" - ");				
				str.append(innerEntry.getKey());				
				str.append(" : ");				
				str.append(Math.exp(innerEntry.getValue()));
			}
			str.append("\n]\n\n");
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
		
		Map<Integer, Double> transForPrevState = logTransitions.get(prevStateIdx);
		if (transForPrevState != null) {
			for (Entry<Integer, Double> entry : transForPrevState.entrySet()) {
				accumulativeProbability += Math.exp(entry.getValue());
				if (accumulativeProbability >= randomDouble)
				{
					return entry.getKey();
				}
			}
		}
		
		return -1;
	}

	private int sampleStartStateIdx() {
		double randomDouble = rand.nextDouble();
		
		double accumulativeProbability = 0.;
		
		for (Entry<Integer, Double> entry : logPriors.entrySet()) {
			accumulativeProbability += Math.exp(entry.getValue());
			if (accumulativeProbability >= randomDouble)
			{
				return entry.getKey();
			}
		}
		
		return -1;
	}
}
































































































