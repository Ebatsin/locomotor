package locomotor.components.types;

/**
 * Encapsulate a Long value.
 * @see CEnumItemType.
 */
public class CInteger implements CItemType, CComparable<CIntervalInteger, CIntervalInteger> {

	/**
	 * The integer value (64-bit).
	 */
	private Long _value;

	/**
	 * Constructs the CFloat object.
	 *
	 * @param      value  The value
	 */
	public CInteger(Long value) {
		_value = value;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		return "(" + _value + ")";
	}

	/**
	 * Compare the integer value of the vehicle with the integer interval value of the user
	 *
	 * @param      user      The user
	 * @param      universe  The universe
	 *
	 * @return     1.0 (best match), tend toward 0.0 otherwise
	 */
	public double compare(CIntervalInteger user, CIntervalInteger universe) {
		// @todo
		return 0.0;
	}
	
}