package locomotor.components.types;

/**
 * Define two type that implements the comparison process.
 */
public interface CComparable<S extends CUserType, N extends CUniverseType> {

	/**
	 * Compare a item and an user criteria, return a mark between 0 and 1.
	 *
	 * @param      user      The user criteria to compare.
	 * @param      universe  The universe (might not be use).
	 *
	 * @return     A mark between 1 (best match) and 0.
	 */
	public double compare(S user, N universe);

}