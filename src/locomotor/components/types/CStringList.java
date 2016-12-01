package locomotor.components.types;

import java.util.TreeMap;

public abstract class CStringList implements CUniverseType, CVehicleType, CUserType {

	/**
	 * The list of string (unique index)
	 */
	private TreeMap<Long, String> _values;

	/**
	 * Constructs the list of string
	 *
	 * @param      value  The value
	 */
	public CStringList(TreeMap<Long, String> values) {
		_values = values;
	}

}