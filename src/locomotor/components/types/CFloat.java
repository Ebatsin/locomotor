package locomotor.components.types;

/**
 * Encapsulate a Double value.
 * @see CEnumVehicleType.
 */
public class CFloat implements CVehicleType {

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

}