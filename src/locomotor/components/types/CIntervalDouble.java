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
		Gaussian gaussian = Gaussian.getInstance();
		float mappedIntervalMax = 2; // the mapped interval is [-2; 2]

		double userFactor = (2.0 * mappedIntervalMax) / (user._max - user._max);
		double userOffset = (user._min * userFactor) + mappedIntervalMax;

		double offsetedMinPoint = _min * userFactor - userOffset;
		double offsetedMaxPoint = _max * userFactor - userOffset;

		double factor = 1.0 / (gaussian.cdf(mappedIntervalMax) - gaussian.cdf(-mappedIntervalMax));

		double mark = 0;

		if(offsetedMaxPoint < -mappedIntervalMax || offsetedMinPoint > mappedIntervalMax) { // out of the user's interval
			mark = gaussian.cdf(offsetedMaxPoint) - gaussian.cdf(offsetedMinPoint);
			// map the value in [0; 0.25]
			mark *= 0.25 / gaussian.cdf(-mappedIntervalMax);
		}
		else {
			mark = Math.min(1.0, (gaussian.cdf(offsetedMaxPoint) - gaussian.cdf(offsetedMinPoint)) * factor);

			if(offsetedMaxPoint < mappedIntervalMax) { // penality application
				double diff = Math.min(gaussian.cdf(-mappedIntervalMax), gaussian.cdf(mappedIntervalMax) - gaussian.cdf(offsetedMaxPoint)) * factor;
				mark -= diff;
			}
			if(offsetedMinPoint > -mappedIntervalMax) {
				double diff = Math.min(gaussian.cdf(-mappedIntervalMax), gaussian.cdf(offsetedMinPoint) - gaussian.cdf(-mappedIntervalMax)) * factor;
				mark -= diff;
			}

			// normalize the mark and map it in [0.25; 1]
			mark = Math.max(0.0, Math.min(1.0, mark)) * 0.75 + 0.25;
		}

		return mark;
	}
}
