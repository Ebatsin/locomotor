package locomotor.components.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum that list all the possible types of data of the "universe".
 */
public enum CEnumUniverseType {
	
	/**
	 * Boolean value
	 */
	BOOLEAN(2),

	/**
	 * Integer interval
	 */
	INTEGER_INTERVAL(3),
	
	/**
	 * Float interval
	 */
	FLOAT_INTERVAL(4),

	/**
	 * String interval
	 */
	STRING_INTERVAL(5),

	/**
	 * Weighted string list (set)
	 */	
	WEIGHTED_STRING_LIST(6),
	
	/**
	 * Tree
	 */
	TREE(7);

	/**
	 * The identifier of the enum item.
	 */
	private int _id;

	/**
	 * Map to retrieve value from integer.
	 */
	private static Map<Integer, CEnumUniverseType> _map
		= new HashMap<Integer, CEnumUniverseType>();

	/**
	 * Construct the map between integer value and universe type
	 */
	static {
		for (CEnumUniverseType univEnum : CEnumUniverseType.values()) {
			_map.put(univEnum._id, univEnum);
		}
	}

	/**
	 * Constructs the object (private to prevent other to instantiate new CEnumUniverseType).
	 *
	 * @param      id    The identifier.
	 */
	private CEnumUniverseType(int id) {
		_id = id;
	}

	/**
	 * Gets the id.
	 *
	 * @return     The id.
	 */
	public int getID() {
		return _id;
	}

	/**
	 * Value of.
	 *
	 * @param      id    The identifier
	 *
	 * @return     The value of the integer
	 */
	public static CEnumUniverseType valueOf(int id) {
		return _map.get(id);
	}
	
}