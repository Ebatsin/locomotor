package locomotor.components.types;

import locomotor.components.Compare;

/**
 * A interval between two Long.
 * @see CEnumUniverseType.
 * @see CEnumItemType.
 * @see CEnumUserType.
 */
public class CIntervalInteger extends CInterval implements CComparable<CIntervalInteger, CIntervalInteger> {

	/**
	 * Initializes a closed interval [min, max].
	 *
	 * @param  min the smaller endpoint (64-bit)
	 * @param  max the larger endpoint (64-bit)
	 */
	public CIntervalInteger(Long min, Long max) {
		super(min, max);
	}

	/**
	 * Compare the integer interval value of the vehicle with the integer interval value of the user
	 *
	 * @param      user      The user
	 * @param      universe  The universe
	 *
	 * @return     1.0 (best match), tend toward 0.0 otherwise
	 */
	public double compare(CIntervalInteger user, CIntervalInteger universe) {
		return Compare.intervalValue(user.min().doubleValue(), user.max().doubleValue(), min().doubleValue(), max().doubleValue());
	}

}
