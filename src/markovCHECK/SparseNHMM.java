package markovCHECK;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import conditionCHECK.ConstraintCondition;
import constraintCHECK.Constraint;
import utils.MathUtils;

public class SparseNHMM<T> extends AbstractMarkovModel<T>{

	T [] states;
	Map<Integer, Double> logPriors;
	List<Map<Integer, Map<Integer,Double>>> logTransitions; // first 2d matrix represents transitions from first to second position
	List<Map<Integer, Integer>> inSupport; // first matrix represents the number of non-zero transition probabilities to the ith state at pos 1 in the seq 
	Map<T, Integer> stateIndex;
	Random rand = new Random();
	
	public SparseNHMM(SparseSingleOrderMarkovModel<T> model, int length, List<Constraint<T>> constraints) {
		this.states = model.states;
		this.stateIndex = model.stateIndex;
		this.logPriors = model.logPriors;
		
		this.inSupport = new ArrayList<Map<Integer, Integer>>(Math.max(0, length-1));
		logTransitions = new ArrayList<Map<Integer, Map<Integer, Double>>>(Math.max(0, length-1));
		if (length > 1) {
			for (int i = 0; i < length-1; i++) {
				this.inSupport.add(new HashMap<Integer, Integer>());
			}
			
			for (int i = 0; i < length-1; i++) {
				logTransitions.add(deepCopy(model.logTransitions));
			}
		
		
			Entry<Integer, Map<Integer, Double>> next;
			Map<Integer, Integer> inSupportAtPosLessOne, inSupportAtPos = inSupport.get(0);
			// for each possible transition (fromState -> toState) from the original naive transition matrix
			Integer fromState;
			for(Iterator<Entry<Integer,Map<Integer,Double>>> it = logTransitions.get(0).entrySet().iterator(); it.hasNext();) {
				next = it.next();
				fromState = next.getKey();
				// if the logPriors doesn't give any probability to the fromState
				if (!this.logPriors.keySet().contains(fromState)) {
					// remove the transition from the logTransitions at position 0
					it.remove();
				} else { // otherwise
					// make note that via this fromState, each of the toStates is accessible via yet one more path
					for (Integer toIndex : next.getValue().keySet()) {
						incrementCount(inSupportAtPos,toIndex);
					}
				}
			}
			
			Integer pathsToFromState;
			for (int i = 1; i < length-1; i++) {
				inSupportAtPos = inSupport.get(i);
				inSupportAtPosLessOne = inSupport.get(i-1);
				// for each possible transition (fromState -> toState) from the original naive transition matrix
				for(Iterator<Entry<Integer,Map<Integer,Double>>> it = logTransitions.get(i).entrySet().iterator(); it.hasNext();) {
					next = it.next();
					fromState = next.getKey();
					pathsToFromState = inSupportAtPosLessOne.get(fromState);
					// if there are NO ways of getting to fromState
					if (pathsToFromState == null) {
						// remove the transition from the logTransitions at position 0
						it.remove();
					} else { // otherwise
						// make note that via this fromState, each of the toStates is accessible via yet one more path
						for (Integer toIndex : next.getValue().keySet()) {
							incrementCount(inSupportAtPos,toIndex);
						}
					}
				}
			}
			
	//		System.out.println(this);
			
			if(!satisfiable())
			{
				throw new RuntimeException("Not satisfiable, even before constraining");
			}
		}
		
		for (Constraint<T> constraint : constraints) {
			constrain(constraint);
			if(!satisfiable())
			{
				throw new RuntimeException("Not satisfiable upon addition of constraintCHECK: " + constraint);
			}		
		}
		
		logNormalize();
	}

	private void incrementCount(Map<Integer, Integer> map, Integer key) {
		Integer value = map.get(key);
		if(value == null)
			map.put(key, 1);
		else
			map.put(key, value+1);
	}
	
	private void decrementCountOrRemove(Map<Integer, Integer> map, Integer key) {
		Integer value = map.get(key);
		if(value == null)
			throw new RuntimeException("Cannot decrement: not present in map");
		else if (value > 1)
			map.put(key, value-1);
		else
			map.remove(key);
	}

	private boolean satisfiable() {
		if (inSupport.size() == 0)
			return (logPriors.size() > 0);
		else
			return inSupport.get(inSupport.size()-1).size() > 0;
	}

	private void logNormalize() {
		if (logTransitions.size() == 0) {
			Double value;
			//normalize only priors
			// calculate sum for each row
			value = Double.NEGATIVE_INFINITY;
			for (Entry<Integer, Double> col: logPriors.entrySet()) {
				value = MathUtils.logSum(value, col.getValue());
			}
			// divide each value by the sum for its row
			for (Entry<Integer, Double> col: logPriors.entrySet()) {
				col.setValue(col.getValue() - value);
			}
			
			return;
		}
		
		Map<Integer, Map<Integer, Double>> currentMatrix = logTransitions.get(logTransitions.size()-1);
		Map<Integer, Double> oldLogAlphas = new HashMap<Integer, Double>(currentMatrix.size());
		
		Double value;
		//normalize last matrix individually
		for (Entry<Integer, Map<Integer, Double>> row: currentMatrix.entrySet()) {
			// calculate sum for each row
			value = Double.NEGATIVE_INFINITY;
			for (Entry<Integer, Double> col: row.getValue().entrySet()) {
				value = MathUtils.logSum(value, col.getValue());
			}
			// divide each value by the sum for its row
			for (Entry<Integer, Double> col: row.getValue().entrySet()) {
				col.setValue(col.getValue() - value);
			}
			oldLogAlphas.put(row.getKey(),value);
		}
		
		Map<Integer, Double> newLogAlphas;
		//propagate normalization from right to left
		for (int i = logTransitions.size()-2; i >= 0; i--) {
			// generate based on alphas from old prereq matrix
			currentMatrix = logTransitions.get(i);
			newLogAlphas = new HashMap<Integer, Double>(currentMatrix.size());
			for (Entry<Integer, Map<Integer, Double>> row: currentMatrix.entrySet()) {
				// calculate sum for each row (new alphas; not used until next matrix)
				value = Double.NEGATIVE_INFINITY;
				// new val = currVal * oldAlpha
				for (Entry<Integer, Double> col: row.getValue().entrySet()) {
					col.setValue(col.getValue() + oldLogAlphas.get(col.getKey()));
					value = MathUtils.logSum(value, col.getValue());
				}
				// normalize
				for (Entry<Integer, Double> col: row.getValue().entrySet()) {
					col.setValue(col.getValue() - value);
				}
				newLogAlphas.put(row.getKey(), value);
			}
			
			oldLogAlphas = newLogAlphas;
		}
		
		// propagate normalization to prior		
		double tmpSum = Double.NEGATIVE_INFINITY;
		for (Entry<Integer,Double> row : logPriors.entrySet()) {
			// new val = currVal * oldAlpha
			row.setValue(row.getValue() + oldLogAlphas.get(row.getKey()));
			tmpSum = MathUtils.logSum(tmpSum, row.getValue());
		}
		// normalize
		for (Entry<Integer,Double> row : logPriors.entrySet()) {
			row.setValue(row.getValue() - tmpSum);
		}
	}

	static private Map<Integer, Map<Integer, Double>> deepCopy(Map<Integer, Map<Integer, Double>> otherLogTransitions) {
		if (otherLogTransitions == null)
			return null;
		
		if (otherLogTransitions.size() == 0)
			return new HashMap<Integer, Map<Integer, Double>>(0);
		
		Map<Integer, Map<Integer, Double>> newMatrix = new HashMap<Integer, Map<Integer, Double>>(otherLogTransitions.size());
		
		Map<Integer,Double> newInnerMap;
		for (Entry<Integer,Map<Integer, Double>> entry : otherLogTransitions.entrySet()) {
			newInnerMap = new HashMap<Integer, Double>();
			newMatrix.put(entry.getKey(), newInnerMap);
			newInnerMap.putAll(entry.getValue());
		}
		
		return newMatrix;
	}
	
	/*
	 *  set all transition probs to/from the state to zero
	 *  decrementing the corresponding in/outSupport values
	 *  removing states accordingly whose in/outSupport values result in 0
	 */
	public Set<PositionedState> removeState(int position, int stateIndex) {
		Set<PositionedState> posStateToRemove = new HashSet<PositionedState>();

		if(position == 0)
		{
			this.logPriors.remove(stateIndex);
		}
		else
		{
			// address transitions *to* the removed state
			posStateToRemove.addAll(adjustTransitionsTo(position, stateIndex));
		}

		if(position < this.logTransitions.size())
		{
			// address transitions *from* the removed state
			posStateToRemove.addAll(adjustTransitionsFrom(position, stateIndex));
		}

		return posStateToRemove;
	}

	/**
	 * Presumably the state at position position and at state index statIndex has been removed.
	 * We now adjust the inSupport and outSupport and logTransition matrices that transition *to* the removed state
	 * @param position
	 * @param stateIndex
	 * @return
	 */
	private Set<PositionedState> adjustTransitionsTo(int position, int stateIndex) {
		Set<PositionedState> posStateToRemove = new HashSet<PositionedState>();
		Integer fromState;
		Map<Integer, Double> toStates;
		Map<Integer, Integer> inSupportAtPos = this.inSupport.get(position-1);
		for (Entry<Integer,Map<Integer, Double>> entry : this.logTransitions.get(position-1).entrySet()) {
			fromState = entry.getKey();
			toStates = entry.getValue();

			if (toStates.containsKey(stateIndex)) {
				decrementCountOrRemove(inSupportAtPos,stateIndex);
				toStates.remove(stateIndex);
				
				if (toStates.isEmpty()) {
					posStateToRemove.add(new PositionedState(position-1, fromState));
				}
			}
		}
		
		return posStateToRemove;
	}

	/**
	 * Presumably the state at position position and at state index stateIndex has been removed.
	 * We now adjust the inSupport and outSupport and logTransition matrices that transition *from* the removed state
	 * @param position
	 * @param stateIndex
	 * @return
	 */
	private Set<PositionedState> adjustTransitionsFrom(int position, int stateIndex) {
		Set<PositionedState> posStateToRemove = new HashSet<PositionedState>();
		
		Integer toState;
		Map<Integer, Integer> inSupportAtPos = this.inSupport.get(position);
		Map<Integer, Map<Integer, Double>> logTransitionsAtPos = this.logTransitions.get(position);
		for (Entry<Integer, Double> entry : logTransitionsAtPos.get(stateIndex).entrySet()) {
			toState = entry.getKey();

			decrementCountOrRemove(inSupportAtPos,toState);
				
			if (!inSupportAtPos.containsKey(toState)) {
				posStateToRemove.add(new PositionedState(position+1, toState));
			}
		}
		
		logTransitionsAtPos.remove(stateIndex);
		
		return posStateToRemove;
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

			innerMap = logTransitions.get(i).get(lastStateIndex);
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
		
		str.append("logPriors:\n");
		str.append("[");
		for (Entry<Integer, Double> entry : logPriors.entrySet()) {
			str.append("\n\t");
			str.append(entry.getKey());
			str.append(", ");
			str.append(Math.exp(entry.getValue()));
		}
		str.append("\n]\n\n");
		
		str.append("logTransitions:\n");
		for (int i = 0; i < logTransitions.size(); i++) {
			for (Entry<Integer, Map<Integer, Double>> entry: logTransitions.get(i).entrySet()) {
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
			str.append("\n");
		}
		
		str.append("outSupport:\n");
		for (int i = 0; i < logTransitions.size(); i++) {
			str.append("[");
			for (Entry<Integer, Map<Integer, Double>> entry: logTransitions.get(i).entrySet()) {
				str.append("\n\t");
				str.append(entry.getKey());
				str.append(" - ");
				str.append(entry.getValue().size());
			}
			str.append("\n]\n\n");
		}
		
		str.append("inSupport:\n");
		for (int i = 0; i < logTransitions.size(); i++) {
			str.append("[");
			for (Entry<Integer, Integer> entry: inSupport.get(i).entrySet()) {
				str.append("\n\t");
				str.append(entry.getKey());
				str.append(" - ");
				str.append(entry.getValue());
			}
			str.append("\n]\n\n");
		}
		
		return str.toString();
		
	}

	@Override
	public List<T> generate(int length) {
		int prevStateIdx = -1;
		int nextStateIdx = -1;
		
		length = Math.min(length, logTransitions.size()+1);
		
		List<T> newSeq = new ArrayList<T>();
		
		for (int i = 0; i < length; i++) {
			if(i==0)
			{
				nextStateIdx = sampleStartStateIdx();
			}
			else
			{
				nextStateIdx = sampleNextState(prevStateIdx, i);
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

	private int sampleNextState(int prevStateIdx, int position) {
		double randomDouble = rand.nextDouble();
		
		double accumulativeProbability = 0.;
		
		Map<Integer, Double> transForPrevState = logTransitions.get(position-1).get(prevStateIdx);
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
		
		// we assume that in order to be satisfiable that there has to be some continuation and it must be the last
		return -1;
	}

	public T[] getStates() {
		return states;
	}

	public int length() {
		return logTransitions.size();
	}

	public void constrain(Constraint<T> constraint) {
		Set<PositionedState> posStateToRemove = new HashSet<PositionedState>();
		int position = constraint.getPosition();
		position = (position == Constraint.FINAL_POSITION ? logTransitions.size() : position);
		ConstraintCondition<T> condition = constraint.getCondition();
		boolean desiredConditionState = constraint.getDesiredConditionState();
		
		for (int stateIndex = 0; stateIndex < states.length; stateIndex++) {
			// if the considered state satisfies/dissatisfies the conditionCHECK contrary to what we wanted
			if(condition.isSatisfiedBy(states[stateIndex]) ^ desiredConditionState)
			{
				// remove it
				posStateToRemove.addAll(removeState(position, stateIndex));
			}
		}
		
		while(!posStateToRemove.isEmpty())
		{
			PositionedState stateToRemove = posStateToRemove.iterator().next();
			posStateToRemove.remove(stateToRemove);
			posStateToRemove.addAll(removeState(stateToRemove.getPosition(), stateToRemove.getStateIndex()));
		}		
	}
}
































































































