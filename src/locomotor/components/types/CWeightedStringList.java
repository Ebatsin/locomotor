package locomotor.components.types;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
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
	 * Get the min.
	 *
	 * @return     The min value.
	 */
	public Long min() {
		return _min;
	}

	/**
	 * Get the max.
	 *
	 * @return     The max value.
	 */
	public Long max() {
		return _max;
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
	 * Factory from representation JSON.
	 *
	 * @param      json  The json
	 *
	 * @return     A new CWeightedStringList object.
	 */
	public static CWeightedStringList fromJSON(JsonValue json) {
		TreeMap<Integer, String> values = new TreeMap<Integer, String>();
		JsonObject interval = json.asObject();
		long min = interval.get("min").asLong();
		long max = interval.get("max").asLong();

		return new CWeightedStringList(min, max, values);
	}
}