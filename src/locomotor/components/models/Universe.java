package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import locomotor.components.JSONConvertissable;
import locomotor.components.JSONDisplayable;

/**
 * Describe the universe where the items come from.
 */
public class Universe implements JSONDisplayable, JSONConvertissable {

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
	 * Gets the id.
	 *
	 * @return     The id.
	 */
	public String getID() {
		return _identifier;
	}

	/**
	 * Gets the name.
	 *
	 * @return     The name.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Gets the description.
	 *
	 * @return     The description.
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * Gets the image.
	 *
	 * @return     The image.
	 */
	public String getImage() {
		return _image;
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

	/**
	 * Factory from representation JSON.
	 *
	 * @param      json  		The json
	 *
	 * @return     A new Universe object.
	 */
	public static Universe fromJSON(JsonValue json) {
		System.out.println("Parsing universe");
		
		JsonObject universeJSON = json.asObject();

		// either add (no id yet) or update (id already)
		String id;
		try {
			id = universeJSON.get("id").asString();
		}
		catch (Exception ex) {
			id = "";
		}
		
		String name = universeJSON.get("name").asString();
		String description = universeJSON.get("description").asString();
		String image = universeJSON.get("image").asString();
		return new Universe(id, name, description, image);
	}

}