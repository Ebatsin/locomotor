package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;

import locomotor.components.JSONDisplayable;

/**
 * Represent a unit, that gives a meaning to a value.
 */
public class Unit implements JSONDisplayable {

	/**
	 * The identifier of the unit.
	 */
	private String _identifier;

	/**
	 * The name of the unit.
	 */
	private String _unitName;

	/**
	 * The alternate forms (multiple) of the unit.
	 */
	private ArrayList<UnitAlt> _alternativeForms;

	/**
	 * Constructs the object.
	 *
	 * @param      id                The identifier
	 * @param      unitName          The unit name
	 * @param      alternativeForms  The alternative forms
	 */
	public Unit(String id, String unitName, ArrayList<UnitAlt> alternativeForms) {
		_identifier = id;
		_unitName = unitName;
		_alternativeForms = alternativeForms;
	}

	/**
	 * Return the JSON value of the unit.
	 *
	 * @return     The unit
	 */
	public JsonValue toJSON() {
		JsonObject unit = Json.object();
		unit.add("_id", _identifier);
		unit.add("name", _unitName);
		JsonArray altForms = Json.array();
		for (UnitAlt ua : _alternativeForms) {
			JsonObject objUa = ua.toJSON();
			altForms.add(objUa);
		}
		unit.add("alt", altForms);
		return unit;
	}

}