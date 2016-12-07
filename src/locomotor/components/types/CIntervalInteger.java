package locomotor.components.types;

/**
 * A interval between two Long.
 * @see CEnumUniverseType.
 * @see CEnumItemType.
 * @see CEnumUserType.
 */
public class CIntervalInteger extends CInterval {

	/**
	 * Initializes a closed interval [min, max].
	 *
	 * @param  min the smaller endpoint (64-bit)
	 * @param  max the larger endpoint (64-bit)
	 */
	public CIntervalInteger(Long min, Long max) {
		super(min, max);
	}

}
