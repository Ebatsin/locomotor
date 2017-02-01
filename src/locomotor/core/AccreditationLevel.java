package locomotor.core;

import java.util.HashMap;
import java.util.Map;

/**
* The accreditation level available.
 */
public enum AccreditationLevel {
		
	/**
	 * Simple user, can only search/book.
	 */
	MUNDANE(0),

	/**
	* First level of administration, can manage item, universe, etc.
	*/
	ADMIN(1),

	/**
	* Final floor, can upgrade (downgrade) user to admin (admin to user).
	*/
	GOD(2);
	
	/**
	 * The identifier.
	 */
	private final int _id;

	/**
	 * Map to retrieve value from integer.
	 */
	private static Map<Integer, AccreditationLevel> _map
		= new HashMap<Integer, AccreditationLevel>();

	/**
	 * Construct the map between integer value and universe type
	 */
	static {
		for (AccreditationLevel univEnum : AccreditationLevel.values()) {
			_map.put(univEnum._id, univEnum);
		}
	}

	/**
	 * Constructs the object (private to prevent other to instantiate new AccreditationLevel).
	 *
	 * @param      id    The identifier.
	 */
	private AccreditationLevel(int id) {
		_id = id;
	}

	/**
	 * Gets the value.
	 *
	 * @return     The value.
	 */
	public int getValue() {
		return _id;
	}

	/**
	 * Value of.
	 *
	 * @param      id    The identifier
	 *
	 * @return     The value of the integer
	 */
	public static AccreditationLevel valueOf(int id) {
		return _map.get(id);
	}

	public static boolean isAdmin(AccreditationLevel level) {
		return (
			(level == AccreditationLevel.ADMIN) ||
			(level == AccreditationLevel.GOD)
		);
	} 
}