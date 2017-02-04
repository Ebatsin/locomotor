package locomotor.components.types;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.Map;
import java.util.TreeMap;

/**
 * Encapsulate a map between an Integer (32-bit) and the String associated.
 */
public abstract class CStringList implements CUniverseType, CItemType, CUserType {

	/**
	 * The list of string (unique index).
	 */
	protected TreeMap<Integer, String> _values;

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
	 * @return     { description_of_the_return_value }
	 */
	public JsonValue toJSON() {
		JsonArray values = Json.array();
		for(Map.Entry<Integer, String> v : _values.entrySet()) {
			JsonObject value = Json.object();
			value.add("value", v.getKey());
			value.add("name", v.getValue());
			values.add(value);
		}
		return values;
	}

	/**
	 * Factory from representation JSON.
	 *
	 * @param      values  The json
	 *
	 * @return     A new TreeMap object.
	 */
	protected static TreeMap<Integer, String> treeFromJSON(JsonArray values) {
		TreeMap<Integer, String> valuesTree = new TreeMap<Integer, String>();

		for (JsonValue value : values) {
			int val = value.asInt();
			valuesTree.put(val, "");
		}

		return valuesTree;
	}

}