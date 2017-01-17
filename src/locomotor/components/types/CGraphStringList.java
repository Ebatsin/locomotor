package locomotor.components.types;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

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

	/**
	 * Return the JSON value of the universe.
	 *
	 * @return     The nodes and the relations
	 */
	public JsonObject toJSON() {
		JsonValue relations = super.toJSON();
		JsonObject obj = Json.object();
		JsonArray nodes = Json.array();
		for(Map.Entry<Integer, String> value : _values.entrySet()) {
			JsonObject node = Json.object();
			node.add("id", value.getKey());
			node.add("name", value.getValue());
			nodes.add(node);
		}
		obj.add("nodes", nodes);
		obj.add("relations", relations);
		return obj;
	}
	
}