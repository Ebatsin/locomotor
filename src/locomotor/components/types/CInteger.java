package locomotor.components.types;

/**
 * @todo describe the class.
 */
public class CInteger implements CVehicleType {

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
	
}