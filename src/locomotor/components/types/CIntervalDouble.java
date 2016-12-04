package locomotor.components.types;

/**
 * A interval between two Double.
 * @see CEnumUniverseType.
 * @see CEnumVehicleType.
 * @see CEnumUserType.
 */
public class CIntervalDouble extends CInterval {

	/**
	 * Initializes a closed interval [min, max].
	 *
	 * @param  min the smaller endpoint (64-bit)
	 * @param  max the larger endpoint (64-bit)
	 */
	public CIntervalDouble(Double min, Double max) {
		super(min, max);
	}
}
