package locomotor.components.types;

public abstract class Interval<T extends Comparable> {

	private T min;
    private T max;

    /**
     * Initializes a closed interval [min, max].
     *
     * @param  min the smaller endpoint
     * @param  max the larger endpoint
     * @throws IllegalArgumentException if the min endpoint is greater than the max endpoint
     * @todo handle error better
     */
    public Interval(T min, T max) {
        if (min.compareTo(max) <= 0) {
            this.min = min;
            this.max = max;
        }
        else {
        	// error
        	System.err.println("Error: Illegal interval");
   			System.exit(0);
        }
    }

    /**
     * Returns the min endpoint of this interval.
     *
     * @return the min endpoint of this interval
     */
    public T min() { 
        return min;
    }

    /**
     * Returns the max endpoint of this interval.
     *
     * @return the max endpoint of this interval
     */
    public T max() { 
        return max;
    }


    /**
     * Returns true if this interval intersects the specified interval.
     *
     * @param  that the other interval
     * @return true if this interval intersects the argument interval;
     *         false otherwise
     */
    public final boolean intersects(Interval that) {
        if (this.max.compareTo(that.min) < 0) return false;
        if (that.max.compareTo(this.min) < 0) return false;
        return true;
    }

    /**
     * Returns true if this interval contains the specified value.
     *
     * @param x the value
     * @return true if this interval contains the value x;
     *         false otherwise
     */
    public final boolean contains(T x) {
        return (min.compareTo(x) <= 0) && (max.compareTo(x) >= 0);
    }
	
	/**
     * Compares this transaction to the specified object.
     *
     * @param  other the other interval
     * @return true if this interval equals the other interval;
     *         false otherwise
     */
    public final boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Interval that = (Interval) other;
        return this.min == that.min && this.max == that.max;
    }

    /**
     * Returns a string representation of this interval.
     *
     * @return a string representation of this interval in the form [min, max]
     */
    public final String toString() {
        return "[" + min + ", " + max + "]";
    }
}