package locomotor.components.types;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.TreeMap;

/**
 * Encapsulate a map between an Integer (32-bit) and the String associated.
 * The Integer represents the weighted.
 */
public class CWeightedStringList extends CStringList {

	/**
	 * The minimum bound.
	 */
	private Long _min;
	/**
	 * The maximum bound.
	 */
	private Long _max;

	public Long min() {
		return _min;
	}

	public Long max() {
		return _max;
	}

	/**
	 * Builds a Weighted list.
	 * @param min     The lightest weight
	 * @param max     The biggest weight
	 * @param values  The map that contains the weights mapped to their string
	 */
	public CWeightedStringList(Long min, Long max, TreeMap<Integer, String> values) {
		super(values);
		_min = min;
		_max = max;
	}

	/**
	 * Return the JSON value of the universe.
	 *
	 * @return     The values, min and max bound
	 */
	public JsonValue toJSON() {
		JsonValue values = super.toJSON();
		JsonObject obj = Json.object();
		obj.add("values", values);
		obj.add("min", _min);
		obj.add("max", _max);
		return obj;
	}

	/**
	 * @todo.
	 *
	 * @param      json  The json
	 *
	 * @return     { description_of_the_return_value }
	 */
	public static CWeightedStringList fromJSON(JsonValue json) {
		return null;
	}
}