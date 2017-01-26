package locomotor.components;

import com.eclipsesource.json.JsonValue;

/**
 * The object can be generated from a JSON object.
 */
public interface JSONConvertissable {

	/**
	 * Factory current object from its JSON representation.
	 *
	 * @return     The object.
	 */
	public static Object fromJSON(JsonValue json) {
		return null;
	}

}