package locomotor.components.types;

/**
 * @todo describe the class.
 */
public class CBoolean implements CUniverseType, CVehicleType, CUserType {

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

}