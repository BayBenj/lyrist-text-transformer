package markovCHECK;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import conditionCHECK.ConstraintCondition;
import constraintCHECK.Constraint;
import utils.MathUtils;

public class NHMM<T> extends AbstractMarkovModel<T>{

	T [] states;
	double[] logPriors;
	double[][][] logTransitions; // first 2d matrix represents transitions from first to second position
	int[][] inSupport; // first matrix represents the number of non-zero transition probabilities to the ith state at pos 1 in the seq 
	int[][] outSupport; // first matrix represents the number of non-zero transition probabilities from the ith state at pos 0 in the seq
	Map<T, Integer> stateIndex;
	Random rand = new Random();
	
	public NHMM(SingleOrderMarkovModel<T> model, int length, List<Constraint<T>> constraints) {
		this.states = model.states;
		this.stateIndex = model.stateIndex;
		
		this.logPriors = new double[model.logPriors.length];
		this.inSupport = new int[length-1][];
		this.outSupport = new int[length-1][];

		for (int i = 0; i < this.inSupport.length; i++) {
			this.inSupport[i] = new int[states.length];
			this.outSupport[i] = new int[states.length];
			for (int j = 0; j < states.length; j++) {
				this.inSupport[i][j] = 0;
				this.outSupport[i][j] = 0;
			}
		}
		
		logTransitions = new double[length-1][][];
		for (int i = 0; i < logTransitions.length; i++) {
			logTransitions[i] = deepCopy(model.logTransitions);
		}
		
		for (int i = 0; i < this.logPriors.length; i++) {
			this.logPriors[i] = model.logPriors[i];

			// if the prior is non-zero
			if(this.logPriors[i] != Double.NEGATIVE_INFINITY)
			{
				for (int j = 0; j < states.length; j++) {
					// for each non-zero transition from the current state,
					if (model.logTransitions[i][j] != Double.NEGATIVE_INFINITY)
					{
						// increment the outSupport for the current state
						this.outSupport[0][i]++;
						// and increment the inSupport for the next state
						this.inSupport[0][j]++;
					}
				}
			}
			else { // prior is zero for state
				for (int j = 0; j < states.length; j++) {
					logTransitions[0][i][j] = Double.NEGATIVE_INFINITY;
				}
			}
		}
		
		for (int i = 1; i < this.inSupport.length; i++) {
			for (int j = 0; j < states.length; j++) {
				if (this.inSupport[i - 1][j] > 0) {
					for (int k = 0; k < states.length; k++) {
						if (logTransitions[i][j][k] != Double.NEGATIVE_INFINITY)
						{
							//there is a non-zero probability of going from j at position i-1 to k at position i
							this.outSupport[i][j]++;
							this.inSupport[i][k]++;
						}
					}
				}
				else {
					for (int k = 0; k < states.length; k++) {
						logTransitions[i][j][k] = Double.NEGATIVE_INFINITY;
					}
				}
			}
		}
		
//		System.out.println(this);

		if(!satisfiable())
		{
			throw new RuntimeException("Not satisfiable, even before constraining");
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

	private boolean satisfiable() {
		for (int is : inSupport[inSupport.length-1]) {
			if ( is != 0)
			{
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private void normalize() {
		
		double[][] currentMatrix = logTransitions[logTransitions.length-1];
		double[] oldAlphas = new double[currentMatrix.length];
		
		//normalize last matrix individually
		for (int row = 0; row < currentMatrix.length; row++) {
			// calculate sum for each row
			oldAlphas[row] = 0.0;
			for (int col = 0; col < currentMatrix[row].length; col++) {
				oldAlphas[row] += Math.exp(currentMatrix[row][col]);
			}
			// divide each value by the sum for its row
			for (int col = 0; col < currentMatrix[row].length; col++) {
				currentMatrix[row][col] = (oldAlphas[row] == 0? Double.NEGATIVE_INFINITY : Math.log(Math.exp(currentMatrix[row][col]) / oldAlphas[row]));
			}
		}
		
		//propagate normalization from right to left
		for (int i = logTransitions.length-2; i >= 0; i--) {
			// generate based on alphas from old prereq matrix
			currentMatrix = logTransitions[i];
			double[] newAlphas = new double[currentMatrix.length];
			for (int row = 0; row < currentMatrix.length; row++) {
				// calculate sum for each row (new alphas; not used until next matrix)
				newAlphas[row] = 0.0;
				// new val = currVal * oldAlpha
				for (int col = 0; col < currentMatrix[row].length; col++) {
					currentMatrix[row][col] = Math.exp(currentMatrix[row][col]) * oldAlphas[col];
					newAlphas[row] += currentMatrix[row][col];
				}
				// normalize
				for (int col = 0; col < currentMatrix[row].length; col++) {
					currentMatrix[row][col] = (newAlphas[row] == 0? Double.NEGATIVE_INFINITY : Math.log(currentMatrix[row][col] / newAlphas[row]));
				}
			}
			
			oldAlphas = newAlphas;
		}
		
		// propagate normalization to prior		
		double tmpSum = 0.0;
		for (int row = 0; row < logPriors.length; row++) {
			// new val = currVal * oldAlpha
			logPriors[row] = Math.exp(logPriors[row]) * oldAlphas[row];
			tmpSum += logPriors[row];
		}
		// normalize
		for (int row = 0; row < logPriors.length; row++) {
			logPriors[row] = (tmpSum == 0? Double.NEGATIVE_INFINITY : Math.log(logPriors[row] / tmpSum));
		}
	}
	
	private void logNormalize() {
		
		double[][] currentMatrix = logTransitions[logTransitions.length-1];
		double[] oldLogAlphas = new double[currentMatrix.length];
		
		//normalize last matrix individually
		for (int row = 0; row < currentMatrix.length; row++) {
			// calculate sum for each row
			oldLogAlphas[row] = Double.NEGATIVE_INFINITY;
			for (int col = 0; col < currentMatrix[row].length; col++) {
				oldLogAlphas[row] = MathUtils.logSum(oldLogAlphas[row], currentMatrix[row][col]);
			}
			// divide each value by the sum for its row
			for (int col = 0; col < currentMatrix[row].length; col++) {
				currentMatrix[row][col] = (oldLogAlphas[row] == Double.NEGATIVE_INFINITY? Double.NEGATIVE_INFINITY : currentMatrix[row][col] - oldLogAlphas[row]);
			}
		}
		
		//propagate normalization from right to left
		for (int i = logTransitions.length-2; i >= 0; i--) {
			// generate based on alphas from old prereq matrix
			currentMatrix = logTransitions[i];
			double[] newLogAlphas = new double[currentMatrix.length];
			for (int row = 0; row < currentMatrix.length; row++) {
				// calculate sum for each row (new alphas; not used until next matrix)
				newLogAlphas[row] = Double.NEGATIVE_INFINITY;
				// new val = currVal * oldAlpha
				for (int col = 0; col < currentMatrix[row].length; col++) {
					currentMatrix[row][col] = currentMatrix[row][col] + oldLogAlphas[col];
					newLogAlphas[row] = MathUtils.logSum(newLogAlphas[row], currentMatrix[row][col]);
				}
				// normalize
				for (int col = 0; col < currentMatrix[row].length; col++) {
					currentMatrix[row][col] = (newLogAlphas[row] == Double.NEGATIVE_INFINITY? Double.NEGATIVE_INFINITY : currentMatrix[row][col] - newLogAlphas[row]);
				}
			}
			
			oldLogAlphas = newLogAlphas;
		}
		
		// propagate normalization to prior		
		double tmpSum = Double.NEGATIVE_INFINITY;
		for (int row = 0; row < logPriors.length; row++) {
			// new val = currVal * oldAlpha
			logPriors[row] = logPriors[row] + oldLogAlphas[row];
			tmpSum = MathUtils.logSum(tmpSum, logPriors[row]);
		}
		// normalize
		for (int row = 0; row < logPriors.length; row++) {
			logPriors[row] = (tmpSum == Double.NEGATIVE_INFINITY? Double.NEGATIVE_INFINITY : logPriors[row] - tmpSum);
		}
	}

	static private double[][] deepCopy(double[][] matrix) {
		if (matrix == null)
			return null;
		
		if (matrix.length == 0)
			return new double[0][];
		
		double[][] newMatrix = new double[matrix.length][matrix[0].length];
		
		for (int i = 0; i < newMatrix.length; i++) {
			for (int j = 0; j < newMatrix[i].length; j++) {
				newMatrix[i][j] = matrix[i][j];
			}
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
			this.logPriors[stateIndex] = Double.NEGATIVE_INFINITY;
		}
		else
		{
			// address transitions *to* the removed state
			posStateToRemove.addAll(adjustTransitionsTo(position, stateIndex));
		}

		if(position < this.logTransitions.length)
		{
			// address transitions *from* the removed state
			posStateToRemove.addAll(adjustTransitionsFrom(position, stateIndex));
		}

		return posStateToRemove;
	}

	/**
	 * 
	 * @param position
	 * @param stateIndex
	 * @return
	 */
	private Set<PositionedState> adjustTransitionsTo(int position, int stateIndex) {
		Set<PositionedState> posStateToRemove = new HashSet<PositionedState>();
		for (int i = 0; this.inSupport[position-1][stateIndex] > 0 && i < this.logTransitions[position-1].length; i++) {
			if (this.logTransitions[position-1][i][stateIndex] != Double.NEGATIVE_INFINITY) {
				this.outSupport[position-1][i]--;
				this.inSupport[position-1][stateIndex]--;
				this.logTransitions[position-1][i][stateIndex] = Double.NEGATIVE_INFINITY;

				// If a is removed from the domain of V_i, remove all
				// b s.t. p(b|c) = 0 for all c≠a from domain of V_i+1
				if (this.outSupport[position-1][i] == 0)
				{
					posStateToRemove.add(new PositionedState(position-1, i));
				}
			}
		}
		return posStateToRemove;
	}

	private Set<PositionedState> adjustTransitionsFrom(int position, int stateIndex) {
		Set<PositionedState> posStateToRemove = new HashSet<PositionedState>();
		for (int i = 0; this.outSupport[position][stateIndex] > 0 && i < this.logTransitions[position].length; i++) {
			if (this.logTransitions[position][stateIndex][i] != Double.NEGATIVE_INFINITY) {
				this.outSupport[position][stateIndex]--;
				this.inSupport[position][i]--;
				this.logTransitions[position][stateIndex][i] = Double.NEGATIVE_INFINITY;

				// If a is removed from the domain of V_i, remove all
				// b s.t. p(b|c) = 0 for all c≠a from domain of V_i+1
				if (this.inSupport[position][i] == 0)
				{
					posStateToRemove.add(new PositionedState(position+1, i));
				}
			}
		}
		return posStateToRemove;
	}

	public double probabilityOfSequence(T[] sequence) {
		double logProb = 0;
		
		if(sequence.length == 0)
			return Double.NaN;
		
		int lastStateIndex, nextStateIndex = stateIndex.get(sequence[0]);
		logProb += logPriors[nextStateIndex];
		
		for (int i = 1; i < sequence.length; i++) {
			lastStateIndex = nextStateIndex;
			nextStateIndex = stateIndex.get(sequence[i]);

			logProb += logTransitions[i-1][lastStateIndex][nextStateIndex];
		}
		
		return Math.exp(logProb);
	}
	
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		
		str.append("[");
		for (int i = 0; i < logPriors.length; i++) {
			if(i!=0) str.append(", ");
//			str.append((logPriors[i]));
			str.append(Math.exp(logPriors[i]));
		}
		str.append("]\n\n");
		
		for (int i = 0; i < logTransitions.length; i++) {
			for (int row = 0; row < logTransitions[i].length; row++) {
				str.append("[");
				for (int col = 0; col < logTransitions[i][row].length; col++) {
					if(col!=0) str.append(", ");
					//str.append((logTransitions[i][row][col]));				
					str.append(Math.exp(logTransitions[i][row][col]));				
				}
				str.append("]\n");
			}
			str.append("\n\n");
		}
		
		str.append("outSupport:\n");
		for (int i = 0; i < outSupport.length; i++) {
			str.append("[");
			for (int row = 0; row < outSupport[i].length; row++) {
				if(row!=0) str.append(", ");
				str.append(outSupport[i][row]);				
			}
			str.append("]\n\n");
		}
		
		str.append("inSupport:\n");
		for (int i = 0; i < inSupport.length; i++) {
			str.append("[");
			for (int row = 0; row < inSupport[i].length; row++) {
				if(row!=0) str.append(", ");
				str.append(inSupport[i][row]);				
			}
			str.append("]\n\n");
		}
		
		return str.toString();
		
	}

	@Override
	public List<T> generate(int length) {
		int prevStateIdx = -1;
		int nextStateIdx = -1;
		
		length = Math.min(length, logTransitions.length+1);
		
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
		
		for (int i = 0; i < logTransitions[position-1][prevStateIdx].length; i++) {
			// removed states in this position have an outsupport of 0
			if (logTransitions[position-1][prevStateIdx][i] != Double.NEGATIVE_INFINITY) 
			{
				accumulativeProbability += Math.exp(logTransitions[position-1][prevStateIdx][i]);
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

	public T[] getStates() {
		return states;
	}

	public int length() {
		return logTransitions.length;
	}

	public void constrain(Constraint<T> constraint) {
		Set<PositionedState> posStateToRemove = new HashSet<PositionedState>();
		int position = constraint.getPosition();
		position = (position == Constraint.FINAL_POSITION ? logTransitions.length : position);
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












































































































