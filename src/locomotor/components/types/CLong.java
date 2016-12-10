package locomotor.components.types;

/**
 * Encapsulate a Long value.
 * @see CEnumItemType.
 */
public abstract class CLong implements CItemType {

	/**
	 * The integer value (64-bit).
	 */
	private Long _value;

	/**
	 * Constructs the CFloat object.
	 *
	 * @param      value  The value
	 */
	public CLong(Long value) {
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
	
}