package locomotor.components;

import java.lang.Math;
import java.util.Set;
import locomotor.components.types.CBoolean;
import locomotor.components.types.CSetGraph;
import locomotor.core.Gaussian;

public class Compare {

	/**
	* Compare the item criteria's value with the user's interval and returns its mark.
	* @param userLowBound The lower bound of the user's interval
	* @param userHighBound The higher bound of the user's interval
	* @param itemValue The item criteria's value
	* @param disableFlexibility Disable the flexibility
	* @return The mark generated for this criteria's value and this user given interval
	*/
	public static double uniqueValue(double userLowBound, double userHighBound, double itemValue, boolean disableFlexibility) {
		
		if(disableFlexibility) {
			return (userLowBound <= itemValue && itemValue >= userHighBound) ? 1.0: 0.0;
		}

		Gaussian gaussian = Gaussian.getInstance();

		double mappedIntervalMax = 0.66; // the mapped interval is [-0.66; 0.66]

		double userFactor = (2.0 * mappedIntervalMax) / (userHighBound - userLowBound);
		double userOffset = (userLowBound * userFactor) + mappedIntervalMax;

		double offsetedPoint = itemValue * userFactor - userOffset;

		double factor = 1.0 / gaussian.pdf(mappedIntervalMax);

		return Math.max(0.0, Math.min(1.0, gaussian.pdf(offsetedPoint) * factor));
	}

	/**
	* Compare the item criteria's interval with the user's interval and returns its mark.
	* @param userLowBound The lower bound of the user's interval
	* @param userHighBound The higher bound of the user's interval
	* @param itemLowBound The lower bound of the item's interval
	* @param itemHighBound The higher bound of the item's interval
	* @param disableFlexibility Disable the flexibility
	* @return The mark generated for this criteria's interval value and this user given interval
	*/
	public static double intervalValue(double userLowBound, double userHighBound, double itemLowBound, double itemHighBound, boolean disableFlexibility) {
		
		if(disableFlexibility) {
			return (userLowBound <= itemLowBound && itemHighBound <= userHighBound) ? 1.0: 0.0;
		}

		Gaussian gaussian = Gaussian.getInstance();
		float mappedIntervalMax = 2; // the mapped interval is [-2; 2]

		double userFactor = (2.0 * mappedIntervalMax) / (userHighBound - userLowBound);
		double userOffset = (userLowBound * userFactor) + mappedIntervalMax;

		double offsetedMinPoint = itemLowBound * userFactor - userOffset;
		double offsetedMaxPoint = itemHighBound * userFactor - userOffset;

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
				double diff = Math.min(gaussian.cdf(-mappedIntervalMax), 
					gaussian.cdf(mappedIntervalMax) - gaussian.cdf(offsetedMaxPoint)) * factor;
				mark -= diff;
			}
			if(offsetedMinPoint > -mappedIntervalMax) {
				double diff = Math.min(gaussian.cdf(-mappedIntervalMax), 
					gaussian.cdf(offsetedMinPoint) - gaussian.cdf(-mappedIntervalMax)) * factor;
				mark -= diff;
			}

			// normalize the mark and map it in [0.25; 1]
			mark = Math.max(0.0, Math.min(1.0, mark)) * 0.75 + 0.25;
		}

		return mark;
	}

	/**
	 * Compare the string list of the vehicle with the string list of the user
	 *
	 * @param      userKey      The user key set
	 * @param      itemKey      The item key set
	 * @param      universeKey  The universe set
	 * @param      disableFlexibility  The disable flexibility
	 *
	 * @return     1.0 (best match), tend toward 0.0 otherwise, -1.0 if flexibility disable and no matching
	 */
	public static double graphValue(Set<Integer> userKey, Set<Integer> itemKey, CSetGraph universeKey, boolean disableFlexibility) {

		boolean isUserSubsetOfItem = itemKey.containsAll(userKey);

		// case 1: user is included or egal to item
		if (itemKey.equals(userKey) || isUserSubsetOfItem) {
			return 1.0;
		}

		if(disableFlexibility) {
			return -1.0;
		}
		
		boolean isItemSubsetOfUser = userKey.containsAll(itemKey);

		// case 2: item is included in user
		if (isItemSubsetOfUser) {
			return itemKey.size() / userKey.size();
		}

		// case 3: item intersect or not user
		double diameter = universeKey.getDiameter();
		double distance;
		double sumOfDistance = 0.0;
		int numberOfPaths = userKey.size() * itemKey.size();

		for (Integer ui : userKey) {
			for (Integer ij : itemKey) {
				distance = universeKey.distance(ui, ij);
				distance = Math.max(0, ((diameter - distance) / diameter));
				sumOfDistance += distance;
			}
		}

		// avoid divide by zero
		return (sumOfDistance == 0.0) ? sumOfDistance : (sumOfDistance / numberOfPaths);
	}

	public static double booleanValue(CBoolean user, CBoolean item, boolean disableFlexibility) {		
		return (item.value() == user.value()) ? 1.0 : (disableFlexibility ? -1.0 : 0.0);
	}
}