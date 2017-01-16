package locomotor.components.types;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

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

	public JsonObject toJSON() {
		// @todo
		return Json.object();
	}
}