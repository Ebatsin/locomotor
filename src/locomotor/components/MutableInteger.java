package locomotor.components;

/**
 * A class for Integer objects that you can change.
 */
public class MutableInteger extends Number {

	/**
	 * The value holds by the class.
	 */
	int _value;

	/**
	 * Maximum value that can be hold by the class.
	 */
	static int MAX_VALUE = Integer.MAX_VALUE;

	/**
	 * Minimum value that can be hold by the class.
	 */
	static int MIN_VALUE = Integer.MIN_VALUE;
	
	/**
	 * Constructs the object.
	 */
	public MutableInteger() {
		_value = 0;
	}

	/**
	 * Constructs the object.
	 *
	 * @param      value  The value
	 */
	public MutableInteger(int value) {
		_value = value;
	}

	/**
	 * Constructs the object.
	 *
	 * @param      value  The value
	 */
	public MutableInteger(String value) {
		_value = Integer.parseInt(value);
	}

	/**
	 * The byte value.
	 *
	 * @return     the byte value
	 */
	public byte byteValue() {
		return (byte)_value;
	}

	/**
	 * The float value.
	 *
	 * @return     the float value
	 */
	public float floatValue() {
		return (float)_value;
	}

	/**
	 * The double value.
	 *
	 * @return     the double value
	 */
	public double doubleValue() {
		return (double)_value;
	}

	/**
	 * The int value.
	 *
	 * @return     the int value
	 */
	public int intValue() {
		return _value;
	}

	/**
	 * The long value.
	 *
	 * @return     the long value
	 */
	public long longValue() {
		return (long)_value;
	}

	/**
	 * The short value.
	 *
	 * @return     the short value
	 */
	public short shortValue() {
		return (short)_value;
	}

	/**
	 * Set the int value.
	 *
	 * @param      value  The value
	 */
	public void set(int value) {
		_value = value;
	}

	/**
	 * Set the String value.
	 *
	 * @param      value  The value
	 */
	public void set(String value) {
		_value = Integer.parseInt(value);
	}

	/**
	 * Set the double value.
	 *
	 * @param      value  The value
	 */
	public void set(double value) {
		_value = (int)value;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		return String.valueOf(_value);
	}
}