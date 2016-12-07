package locomotor.components.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum that list all the possible types of data of the "item".
 */
public enum CEnumItemType {
	
	// integer
	INTEGER(0),
	// float
	FLOAT(1),
	// boolean
	BOOLEAN(2),
	// integer interval 
	INTEGER_INTERVAL(3),
	// float interval
	FLOAT_INTERVAL(4),
	// integer list
	INTEGER_LIST(8),
	// integer list
	INTEGER_TREE(9);

	/**
	 * The identifier.
	 */
	private int _id;

	/**
	 * Map to retrieve value from integer.
	 */
	private static Map<Integer, CEnumItemType> _map
		= new HashMap<Integer, CEnumItemType>();

	static {
		for (CEnumItemType univEnum : CEnumItemType.values()) {
			_map.put(univEnum._id, univEnum);
		}
	}

	/**
	 * Constructs the object (private to prevent other to instantiate new CEnumItemType).
	 *
	 * @param      id    The identifier.
	 */
	private CEnumItemType(int id) {
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
	public static CEnumItemType valueOf(int id) {
		return _map.get(id);
	}
	
}