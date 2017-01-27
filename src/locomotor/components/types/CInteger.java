package locomotor.components.types;

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
	
}