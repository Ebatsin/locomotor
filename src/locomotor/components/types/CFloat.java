package locomotor.components.types;

/**
 * @todo describe the class.
 */
public class CFloat implements CVehicleType {

	/**
	 * The float value (double-precision 64-bit IEEE 754 floating point)
	 */
	private Double _value;

	/**
	 * Constructs the CFloat object
	 *
	 * @param      value  The value
	 */
	public CFloat(Double value) {
		_value = value;
	}

}