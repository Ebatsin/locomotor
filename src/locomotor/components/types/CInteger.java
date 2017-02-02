package locomotor.components.types;

import com.eclipsesource.json.JsonValue;

import locomotor.components.Compare;

/**
 * Encapsulate a 64-bit integer value.
 */
public class CInteger extends CLong implements CComparable<CIntervalInteger, CIntervalInteger> {

	/**
	 * Constructs the CFloat object.
	 *
	 * @param      value  The value
	 */
	public CInteger(Long value) {
		super(value);
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
	public double compare(CIntervalInteger user, CIntervalInteger universe, boolean disableFlexibility) {
		return Compare.uniqueValue(user.min().doubleValue(), user.max().doubleValue(), value().doubleValue(), disableFlexibility);
	}

	/**
	 * Factory from representation JSON.
	 *
	 * @param      json  The json
	 *
	 * @return     A new CInteger object.
	 */
	public static CInteger fromJSON(JsonValue json) {
		long value = json.asLong();
		return new CInteger(value);
	}
	
}