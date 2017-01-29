package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import locomotor.components.JSONDisplayable;

/**
 * Describe the universe where the items come from.
 */
public class Universe implements JSONDisplayable {

	/**
	 * Identify the universe.
	 */
	private String _identifier;

	/**
	 * The name of the universe.
	 */
	private String _name;

	/**
	 * The description of the universe.
	 */
	private String _description;

	/**
	 * The image of the universe.
	 */
	private String _image;

	/**
	 * Constructs the object.
	 *
	 * @param      id           The identifier
	 * @param      name         The name
	 * @param      description  The description
	 * @param      image        The image
	 */
	public Universe(String id, String name, String description, String image) {
		_identifier = id;
		_name = name;
		_description = description;
		_image = image;
	}

	/**
	 * Return the JSON value of the universe.
	 *
	 * @return     The universe as JSON form.
	 */
	public JsonValue toJSON() {
		JsonObject universe = Json.object();
		universe.add("_id", _identifier);
		universe.add("name", _name);
		universe.add("description", _description);
		universe.add("image", _image);
		return universe;
	}

}