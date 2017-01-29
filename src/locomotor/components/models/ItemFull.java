package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;

import locomotor.components.JSONDisplayable;

/**
 * Full item containing all information.
 */
public class ItemFull extends Item implements JSONDisplayable {

	/**
	 * The name of the item.
	 */
	private String _name;

	/**
	 * The universe of the item.
	 */
	private String _universe;

	/**
	 * The description of the item.
	 */
	private String _description;
	
	/**
	 * The image of the image.
	 */
	private String _image;

	/**
	 * Constructs the object.
	 *
	 * @param      id           The identifier
	 * @param      name         The name
	 * @param      universe     The universe
	 * @param      description  The description
	 * @param      image        The image
	 * @param      categories   The categories
	 */
	public ItemFull(String id, String name, String universe, String description, String image, ArrayList<ItemCategoryFull> categories) {
		super(id, categories);
		_name = name;
		_universe = universe;
		_description = description;
		_image = image;
	}

	/**
	 * @todo.
	 *
	 * @return     { description_of_the_return_value }
	 */
	public JsonValue toJSON() {
		JsonObject itemFull = Json.object();
		itemFull.add("id", _identifier);
		itemFull.add("name", _name);
		itemFull.add("image", _image);
		itemFull.add("universeID", _universe);
		itemFull.add("description", _description);
		JsonArray categories = Json.array();
		for (ItemCategory category : _categories) {
			JsonValue cat = ((ItemCategoryFull)category).toJSON();
			categories.add(cat);
		}
		itemFull.add("categories", categories);
		return itemFull;
	}

}