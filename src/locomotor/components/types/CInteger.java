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
	 * Compare the integer value of the vehicle with the integer interval value of the user
	 *
	 * @param      user      The user
	 * @param      universe  The universe
	 *
	 * @return     1.0 (best match), tend toward 0.0 otherwise
	 */
	public double compare(CIntervalInteger user, CIntervalInteger universe) {
		return Compare.uniqueValue(user.min().doubleValue(), user.max().doubleValue(), value().doubleValue());
	}
	
}