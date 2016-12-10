package locomotor.components.types;

/**
 * @todo.
 */
public interface CComparable<I extends CItemType, U extends CUniverseType> {

	/**
	 * Compare a item and an user criteria, return a mark between 0 and 1.
	 *
	 * @param      item      The item to compare.
	 * @param      universe  The universe (might not be use).
	 *
	 * @return     A mark between 1 (best match) and 0.
	 */
	public double compare(I item, U universe);

}