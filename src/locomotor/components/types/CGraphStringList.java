package locomotor.components.types;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import locomotor.components.Pair;

/**
 * @todo .
 */
public class CGraphStringList implements CUniverseType {

	/**
	 * The list of string (unique index).
	 */
	private TreeMap<Integer, String> _values;

	/**
	 * The list of relations between two nodes.
	 */
	private ArrayList<Pair<Integer, Integer>> _relations;

	/**
	 * Constructs the object.
	 *
	 * @param      values     The values
	 * @param      relations  The relations
	 */
	public CGraphStringList(TreeMap<Integer, String> values, ArrayList<Pair<Integer, Integer>> relations) {
		_values = values;
		_relations = relations;
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
		str += "Relations:\n";
		for(Pair<Integer, Integer> value : _relations) {
		
			str += value.toString() + "\n";
		}
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

	/**
	 * Gets the relations.
	 *
	 * @return     The relations.
	 */
	public ArrayList<Pair<Integer, Integer>> getRelations() {
		return _relations;
	}
	
}