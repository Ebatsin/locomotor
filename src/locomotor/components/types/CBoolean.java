package locomotor.components.types;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import locomotor.components.Compare;

/**
 * Encapsulate a Boolean value.
 * @see CEnumUniverseType.
 * @see CEnumItemType.
 * @see CEnumUserType.
 */
public class CBoolean implements CUniverseType, CItemType, CUserType, CComparable<CBoolean, CBoolean> {

	/**
	 * The boolean value.
	 */
	private Boolean _value;

	/**
	 * Constructs the CBoolean object.
	 *
	 * @param      value  The value
	 */
	public CBoolean(Boolean value) {
		_value = value;
	}

	public Boolean value() {
		return _value;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		return "(" + _value + ")";
	}

	/**
	 * Compare boolean value
	 *
	 * @param      user      The user criteria
	 * @param      universe  The universe
	 *
	 * @return     1.0 (match), 0.0 otherwise
	 */
	public double compare(CBoolean user, CBoolean universe) {
		return Compare.booleanValue(user, this);
	}

	/**
	 * Return the JSON value of the universe.
	 *
	 * @return     null
	 */
	public JsonValue toJSON() {
		return Json.value(null);
	}

	/**
	 * @todo.
	 *
	 * @param      json  The json
	 *
	 * @return     { description_of_the_return_value }
	 */
	public static CBoolean fromJSON(JsonValue json) {
		return null;
	}

}