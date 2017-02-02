package locomotor.components.types;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;

/**
 * Encapsulate a Long value.
 * @see CEnumItemType.
 */
public abstract class CLong implements CItemType {

	/**
	 * The integer value (64-bit).
	 */
	private Long _value;

	/**
	 * Constructs the CFloat object.
	 *
	 * @param      value  The value
	 */
	public CLong(Long value) {
		_value = value;
	}

	/**
	 * Get the value.
	 *
	 * @return     The value.
	 */
	public Long value() {
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
	 * Return the JSON value of the integer.
	 *
	 * @return     JSON representation
	 */
	public JsonArray toJSON() {
		return Json.array().add(_value);
	}
	
}