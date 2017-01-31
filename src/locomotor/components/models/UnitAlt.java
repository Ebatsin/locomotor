package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import locomotor.components.JSONDisplayable;

/**
 * Represent an alternative form of a "base" unit (multiple).
 */
public class UnitAlt implements JSONDisplayable {

	/**
	 * The short name of the alternate form.
	 */
	private String _shortName;

	/**
	 * The long name of the alternate form.
	 */
	private String _longName;

	/**
	 * The factor (multiple), regarding to the base unit
	 */
	private double _factor;

	/**
	 * Constructs the object.
	 *
	 * @param      shortName  The short name
	 * @param      longName   The long name
	 * @param      factor     The factor
	 */
	public UnitAlt(String shortName, String longName, double factor) {
		_shortName = shortName;
		_longName = longName;
		_factor = factor;
	}

	/**
	 * Return the JSON value of the unit alternative.
	 *
	 * @return     The unit alternative
	 */
	public JsonObject toJSON() {
		JsonObject unitAlt = Json.object();
		unitAlt.add("shortName", _shortName);
		unitAlt.add("longName", _longName);
		unitAlt.add("factor", _factor);
		return unitAlt;
	}

}