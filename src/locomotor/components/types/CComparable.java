package locomotor.components.types;

/**
 * Define two type that implements the comparison process.
 */
public interface CComparable<S extends CUserType, N extends CUniverseType> {

	/**
	 * Compare a item and an user criteria, return a mark between -1 and 1.
	 *
	 * @param      user                The user
	 * @param      universe            The universe
	 * @param      disableFlexibility  Disable the flexibility?
	 *
	 * @return     A mark between 1 (best match) and 0. -1 if does not perfectly match (flexibility disable)
	 */
	public double compare(S user, N universe, boolean disableFlexibility);

}