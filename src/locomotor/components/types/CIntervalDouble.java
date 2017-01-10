package locomotor.components.types;

import java.lang.Math;
import locomotor.core.Gaussian;

/**
 * A interval between two Double.
 * @see CEnumUniverseType.
 * @see CEnumItemType.
 * @see CEnumUserType.
 */
public class CIntervalDouble extends CInterval implements CComparable<CIntervalDouble, CIntervalDouble> {

	/**
	 * Initializes a closed interval [min, max].
	 *
	 * @param  min the smaller endpoint (64-bit)
	 * @param  max the larger endpoint (64-bit)
	 */
	public CIntervalDouble(Double min, Double max) {
		super(min, max);
	}

	/**
	 * Compare the double interval value of the vehicle with the double interval value of the user
	 *
	 * @param      user      The user
	 * @param      universe  The universe
	 *
	 * @return     1.0 (best match), tend toward 0.0 otherwise
	 */
	public double compare(CIntervalDouble user, CIntervalDouble universe) {
		return universe.compareDelegate(user, this);
	}
}
