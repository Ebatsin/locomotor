package locomotor.components.types;

import locomotor.components.logging.ErrorHandler;

/**
 * A interval between two T (Comparable).
 * @see CEnumUniverseType.
 * @see CEnumItemType.
 * @see CEnumUserType.
 */
public abstract class CInterval<T extends Number> implements CUniverseType, CItemType,
	CUserType {

	private T _min;
	private T _max;

	/**
	 * Initializes a closed interval [min, max].
	 *
	 * @param  min the smaller endpoint
	 * @param  max the larger endpoint
	 * @throws IllegalArgumentException if the min endpoint is greater than the max endpoint
	 */
	public CInterval(T min, T max) {
		if(min.doubleValue() < max.doubleValue()) {
			_min = min;
			_max = max;
		}
		else {
			String message = "Min: " + min.doubleValue() + " Max: " + max.doubleValue();
			ErrorHandler.getInstance().push("CInterval", true, "The min bound is upper the max bound", message);
			return;
		}
	}

	/**
	 * Returns the min endpoint of this interval.
	 *
	 * @return the min endpoint of this interval
	 */
	public T min() { 
		return _min;
	}

	/**
	 * Returns the max endpoint of this interval.
	 *
	 * @return the max endpoint of this interval
	 */
	public T max() { 
		return _max;
	}


	/**
	 * Returns true if this interval intersects the specified interval.
	 *
	 * @param  that the other interval
	 * @return true if this interval intersects the argument interval;
	 *         false otherwise
	 */
	public final boolean intersects(CInterval that) {
		if(_max.doubleValue() < that._min.doubleValue()) {
			return false;
		}
		if(_min.doubleValue() > that._max.doubleValue()) {
			return false;
		}
		return true;
	}

	/**
	 * Returns true if this interval contains the specified value.
	 *
	 * @param value the value
	 * @return true if this interval contains the value x;
	 *         false otherwise
	 */
	public final boolean contains(T value) {
		return _min.doubleValue() <= value.doubleValue() && _max.doubleValue() >= value.doubleValue();
	}
	
	/**
	 * Compares this transaction to the specified object.
	 *
	 * @param  other the other interval
	 * @return true if this interval equals the other interval;
	 *         false otherwise
	 */
	public final boolean equals(Object other) {
		if(other == this) {
			return true;
		}
		if(other == null) { 
			return false;
		}
		if(other.getClass() != this.getClass()) {
			return false;
		}
		CInterval that = (CInterval) other;
		return _min == that._min && _max == that._max;
	}

	/**
	 * Returns a string representation of this interval.
	 *
	 * @return a string representation of this interval in the form [min, max]
	 */
	public final String toString() {
		return "[" + _min + ", " + _max + "]";
	}
}