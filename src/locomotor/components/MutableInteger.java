package locomotor.components;

public class MutableInteger extends Number {
	int _value;

	static int MAX_VALUE = Integer.MAX_VALUE;
	static int MIN_VALUE = Integer.MIN_VALUE;

	public MutableInteger() {
		_value = 0;
	}

	public MutableInteger(int value) {
		_value = value;
	}

	public MutableInteger(String value) {
		_value = Integer.parseInt(value);
	}

	public byte byteValue() {
		return (byte)_value;
	}

	public float floatValue() {
		return (float)_value;
	}

	public double doubleValue() {
		return (double)_value;
	}

	public int intValue() {
		return _value;
	}

	public long longValue() {
		return (long)_value;
	}

	public short shortValue() {
		return (short)_value;
	}

	public void set(int value) {
		_value = value;
	}

	public void set(String value) {
		_value = Integer.parseInt(value);
	}

	public void set(double value) {
		_value = (int)value;
	}

	public String toString() {
		return String.valueOf(_value);
	}
}