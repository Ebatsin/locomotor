package locomotor.components;

/**
 * Generics pair.
 *
 * @param      <L>   The left element.
 * @param      <R>   The right element.
 */
public class Pair<L, R> {

	/**
	 * The left element.
	 */
	private L _left;

	/**
	 * The right element.
	 */
	private R _right;

	/**
	 * Constructs the object.
	 *
	 * @param	left   The left element.
	 * @param	right  The right element.
     */
	public Pair(L left, R right) {
		_left = left;
		_right = right;
	}

	/**
	 * Gets the left element.
	 *
	 * @return     The left element.
	 */
	public L getLeft() {
		return _left;
	}

	/**
	 * Gets the right element.
	 *
	 * @return     The right element.
	 */
	public R getRight() {
		return _right;
	}

	/**
	 * Sets the left.
	 *
	 * @param      l     L
	 */
	public void setLeft(L l) {
		_left = l;
	}

	/**
	 * Sets the right.
	 *
	 * @param      r     R
	 */
	public void setRight(R r) {
		_right = r;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		return _right + ";" + _left;
	}
}