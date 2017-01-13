package locomotor.components.types;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import locomotor.components.Pair;

/**
 * Represents a set of string value, identified by a unique integer, with a graph of relations.
 */
public class CGraphStringList extends CSetGraph implements CUniverseType {

	/**
	 * The list of string (unique index).
	 */
	private TreeMap<Integer, String> _values;

	/**
	 * Constructs the object.
	 *
	 * @param      values     The values
	 * @param      relations  The relations
	 */
	public CGraphStringList(TreeMap<Integer, String> values, ArrayList<Pair<Integer, Integer>> relations) {
		super(values.keySet(), relations);
		_values = values;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		String str = "Set:\n";
		for(Map.Entry<Integer, String> value : _values.entrySet()) {
			
			Integer key = value.getKey();
			String val = value.getValue();

			str += key + "(" + val + ")\n";
		}
		str += super.toString();
		return str;
	}

	/**
	 * Gets the map.
	 *
	 * @return     The map.
	 */
	public TreeMap<Integer, String> getMap() {
		return _values;
	}
	
}