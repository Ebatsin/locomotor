package locomotor.components.types;

/**
 * Encapsulate a Double value.
 * @see CEnumItemType.
 */
public class CFloat implements CItemType, CComparable<CIntervalDouble, CIntervalDouble> {

	/**
	 * The float value (double-precision 64-bit IEEE 754 floating point).
	 */
	private Double _value;

	/**
	 * Constructs the CFloat object.
	 *
	 * @param      value  The value
	 */
	public CFloat(Double value) {
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
	 * Compare the double value of the vehicle with the double interval value of the user
	 *
	 * @param      user      The user
	 * @param      universe  The universe
	 *
	 * @return     1.0 (best match), tend toward 0.0 otherwise
	 */
	public double compare(CIntervalDouble user, CIntervalDouble universe) {
		// @todo
		return 0.0;
	}

}