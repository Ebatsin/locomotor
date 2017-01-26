package locomotor.components;

import com.eclipsesource.json.JsonValue;

/**
 * The object can be converted to a JSON format.
 */
public interface JSONDisplayable {

	/**
	 * Convert self to JSON format.
	 *
	 * @return     A JSON representation of self
	 */
	public JsonValue toJSON();

}