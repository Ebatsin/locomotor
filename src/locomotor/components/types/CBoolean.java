package locomotor.components.types;

/**
 * Encapsulate a Boolean value.
 * @see CEnumUniverseType.
 * @see CEnumItemType.
 * @see CEnumUserType.
 */
public class CBoolean implements CUniverseType, CItemType, CUserType, CComparable<CBoolean, CBoolean> {

	/**
	 * The boolean value.
	 */
	private Boolean _value;

	/**
	 * Constructs the CBoolean object.
	 *
	 * @param      value  The value
	 */
	public CBoolean(Boolean value) {
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
	 * Compare boolean value
	 *
	 * @param      item      The item
	 * @param      universe  The universe
	 *
	 * @return     1.0 (match), 0.0 otherwise
	 */
	public double compare(CBoolean item, CBoolean universe) {
		return (_value == item._value) ? 1.0 : 0.0;
	}

}