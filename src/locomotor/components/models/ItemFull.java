package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import locomotor.components.JSONDisplayable;
import locomotor.components.logging.ErrorHandler;

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
	 * Gets the name.
	 *
	 * @return     The name.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Gets the universe.
	 *
	 * @return     The universe.
	 */
	public String getUniverse() {
		return _universe;
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
	 * Return the JSON value of the item.
	 *
	 * @return     The item.
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

	/**
	 * Factory from representation JSON.
	 *
	 * @param      json  		The json
	 * @param      catsModel  	The model
	 *
	 * @return     A new ItemFull object.
	 */
	public static ItemFull fromJSON(JsonValue json, ArrayList<CategoryModel> catsModel) {
		System.out.println("Parsing item");
		
		JsonObject itemJSON = json.asObject();

		JsonArray categories = itemJSON.get("categories").asArray();
		ArrayList<ItemCategoryFull> itemCategories = new ArrayList<ItemCategoryFull>();

		// map to retrieve easier (perf)
		HashMap<String, CategoryModel> categoriesMap = new HashMap<String, CategoryModel>();
		for (CategoryModel cm : catsModel) {
			categoriesMap.put(cm.getID(), cm);
		}

		HashSet<String> categoriesFound = new HashSet();

		// delegate for each category
		for (JsonValue category : categories) {
			String identifier = category.asObject().get("categoryId").asString();

			// already found
			if(categoriesFound.contains(identifier)) {
				String message = "The categories " + categoriesMap.get(identifier).getName();
				message += " is present twice.";
				ErrorHandler.getInstance().push("fromJSON", true, message, message);
				return null;
			}
			categoriesFound.add(identifier);

			ItemCategoryFull ucf = ItemCategoryFull.fromJSON(category, categoriesMap.get(identifier));
			
			// error found
			if(ucf == null) {
				return null;
			}

			itemCategories.add(ucf);
		}

		// not same count, miss one category at least
		if(categoriesFound.size() != categoriesMap.size()) {
			String message = "At least one category is missing";
			ErrorHandler.getInstance().push("fromJSON", true, message, message);
			return null;
		}

		// either add (no id yet) or update (id already)
		String id;
		try {
			id = itemJSON.get("id").asString();
		}
		catch (Exception ex) {
			id = "";
		}

		String name = itemJSON.get("name").asString();
		String universe = itemJSON.get("universe").asString();
		String description = itemJSON.get("description").asString();
		String image = itemJSON.get("image").asString();
		return new ItemFull(id, name, universe, description, image, itemCategories);
	}

}