package locomotor.components.types;

import locomotor.components.Compare;

/**
 * @todo.
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
	 * @param      user      The user string list
	 * @param      universe  The universe string list (containg the graph)
	 *
	 * @return     1.0 (best match), tend toward 0.0 otherwise
	 */
	public double compare(CWeightedStringList user, CWeightedStringList universe) {
		return Compare.uniqueValue(user.min(), user.max(), _value);
	}
	
}