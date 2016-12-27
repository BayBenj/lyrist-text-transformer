package markovCHECK;

public class PositionedState {

	private int stateIndex;
	private int position;

	public PositionedState(int position, int stateIndex) {
		this.position = position;
		this.stateIndex = stateIndex;
	}

	public int getPosition() {
		return this.position;
	}

	public int getStateIndex() {
		return this.stateIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + position;
		result = prime * result + stateIndex;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PositionedState))
			return false;
		PositionedState other = (PositionedState) obj;
		if (position != other.position)
			return false;
		if (stateIndex != other.stateIndex)
			return false;
		return true;
	}
	
	public String toString() {
		return "pos " + position + " state " + stateIndex;
	}
}





















































































