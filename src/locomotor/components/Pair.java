package locomotor.components;

/**
 * Generics pair
 *
 * @param      <L>   The left element
 * @param      <R>   The right element
 */
public class Pair<L, R> {
    
    /**
     * The left value
     */
    private L _left;
    /**
     * The right value
     */
    private R _right;

    /**
     * Constructs the object.
     *
     * @param      left   The left
     * @param      right  The right
     */
    public Pair(L left, R right) {
        _left = left;
        _right = right;
    }

    /**
     * Gets the left.
     *
     * @return     The left.
     */
    public L getLeft() {
        return _left;
    }

    /**
     * Gets the right.
     *
     * @return     The right.
     */
    public R getRight() {
        return _right;
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