package locomotor.components.types;

import java.util.Map;
import java.util.TreeMap;

public abstract class CStringList implements CUniverseType, CVehicleType, CUserType {

	/**
	 * The list of string (unique index)
	 */
	private TreeMap<Integer, String> _values;

	/**
	 * Constructs the list of string
	 *
	 * @param      value  The value
	 */
	public CStringList(TreeMap<Integer, String> values) {
		_values = values;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		String s = "";
		for(Map.Entry<Integer, String> value : _values.entrySet()) {
			
			Integer key = value.getKey();
			String val = value.getValue();

			s += key + " => " + val + "\n";
		}
		return s;
	}

	public TreeMap<Integer, String> getMap() {
		return _values;
	}

}