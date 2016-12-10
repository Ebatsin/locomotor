package locomotor.components.types;

/**
 * @todo.
 */
public class CInteger extends CLong implements CComparable<CIntervalInteger, CIntervalInteger> {

	/**
	 * Constructs the CFloat object.
	 *
	 * @param      value  The value
	 */
	public CInteger(Long value) {
		super(value);
	}

	/**
	 * Compare the integer value of the vehicle with the integer interval value of the user
	 *
	 * @param      user      The user
	 * @param      universe  The universe
	 *
	 * @return     1.0 (best match), tend toward 0.0 otherwise
	 */
	public double compare(CIntervalInteger user, CIntervalInteger universe) {
		// @todo
		return 0.0;
	}
	
}