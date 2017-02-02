package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * An item described by a few informations.
 * Purpose, a quick glimpse.
 */
public class ItemSoft {

	/**
	 * The unique identifier.
	 */
	private String _id;

	/**
	 * The name of the item.
	 */
	private String _name;

	/**
	 * The url of the image.
	 */
	private String _urlImage;

	/**
	 * The name of the universe where belongs the item.
	 */
	private String _universeName;

	/**
	 * Constructs the object.
	 *
	 * @param      id            The identifier
	 * @param      name          The name
	 * @param      urlImage      The url image
	 * @param      universeName  The universe name
	 */
	public ItemSoft(String id, String name, String urlImage, String universeName) {
		_id = id;
		_name = name;
		_urlImage = urlImage;
		_universeName = universeName;
	}

	/**
	 * Return the JSON representation of the quick item.
	 *
	 * @return     The item.
	 */
	public JsonValue toJSON() {
		JsonObject quickItem = Json.object();
		quickItem.add("_id", _id);
		quickItem.add("name", _name);
		quickItem.add("image", _urlImage);
		quickItem.add("universe", _universeName);
		return quickItem;
	}

}