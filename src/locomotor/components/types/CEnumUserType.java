package locomotor.components.types;

import java.util.HashMap;
import java.util.Map;

/**
 * @todo Describe the class.
 */
public enum CEnumUserType {
	
	// boolean
	BOOLEAN(2),
	// integer interval 
	INTEGER_INTERVAL(3),
	// float interval
	FLOAT_INTERVAL(4),
	// string interval
	STRING_INTERVAL(5),
	// weighted string list
	WEIGHTED_STRING_LIST(6),
	// tree
	TREE(7);

	/**
	 * The identifier.
	 */
	private int _id;

	/**
	 * Map to retrieve value from integer.
	 */
	private static Map<Integer, CEnumUserType> _map = new HashMap<Integer, CEnumUserType>();

	static {
		for (CEnumUserType univEnum : CEnumUserType.values()) {
			_map.put(univEnum._id, univEnum);
		}
	}

	/**
	 * Constructs the object (private to prevent other to instantiate new CEnumUserType).
	 *
	 * @param      id    The identifier.
	 */
	private CEnumUserType(int id) {
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
	public static CEnumUserType valueOf(int id) {
		return _map.get(id);
	}
	
}