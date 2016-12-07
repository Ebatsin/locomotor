package locomotor.components.types;

import java.util.Map;
import java.util.TreeMap;

/**
 * Encapsulate a map between an Integer (32-bit) and the String associated.
 */
public abstract class CStringList implements CUniverseType, CItemType, CUserType {

	/**
	 * The list of string (unique index).
	 */
	private TreeMap<Integer, String> _values;

	/**
	 * Constructs the list of string.
	 *
	 * @param      values  The values to add
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
		String str = "";
		for(Map.Entry<Integer, String> value : _values.entrySet()) {
			
			Integer key = value.getKey();
			String val = value.getValue();

			str += key + " => " + val + "\n";
		}
		return str;
	}

	public TreeMap<Integer, String> getMap() {
		return _values;
	}

}