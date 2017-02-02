package locomotor.components.types;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import locomotor.components.Compare;

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
	 * Get the value.
	 *
	 * @return     The double value;
	 */
	public Double value() {
		return _value;
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
	 * @param      user                The user criteria
	 * @param      universe            The universe
	 * @param      disableFlexibility  Disable the flexibility
	 *
	 * @return     1.0 (match), 0.0 otherwise or -1.0 if does not match perfectly (flexibility disable)
	 */
	public double compare(CIntervalDouble user, CIntervalDouble universe, boolean disableFlexibility) {
		return Compare.uniqueValue(user.min().doubleValue(), user.max().doubleValue(), _value.doubleValue(), disableFlexibility);
	}

	/**
	 * Return the JSON value of the integer.
	 *
	 * @return     JSON representation
	 */
	public JsonArray toJSON() {
		return Json.array().add(_value);
	}

	/**
	 * Factory from representation JSON.
	 *
	 * @param      json  The json
	 *
	 * @return     A new CFloat object.
	 */
	public static CFloat fromJSON(JsonValue json) {
		double value = json.asDouble();
		return new CFloat(value);
	}

}