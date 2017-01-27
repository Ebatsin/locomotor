package locomotor.components.types;

import locomotor.components.Compare;

/**
 * Represents a weighted integer.
 */
public class CWeightedInteger extends CLong implements CComparable<CWeightedStringList, CWeightedStringList> {

	/**
	 * Constructs the CFloat object.
	 *
	 * @param      value  The value
	 */
	public CWeightedInteger(Long value) {
		super(value);
	}

	/**
	 * Compare the string list of the vehicle with the string list of the user
	 *
	 * @param      user                The user string list
	 * @param      universe            The universe string list (containg the graph)
	 * @param      disableFlexibility  Disable the flexibility
	 *
	 * @return     1.0 (match), 0.0 otherwise or -1.0 if does not match perfectly (flexibility disable)
	 */
	public double compare(CWeightedStringList user, CWeightedStringList universe, boolean disableFlexibility) {
		return Compare.uniqueValue(user.min(), user.max(), value(), disableFlexibility);
	}
	
}