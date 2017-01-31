package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;

import locomotor.components.JSONDisplayable;

/**
 * Displayable form of an item's category.
 */
public class ItemCategoryFull extends ItemCategory implements JSONDisplayable {

	/**
	 * The name of the category.
	 */
	private String _name;

	/**
	 * Constructs the object.
	 *
	 * @param      id         The identifier
	 * @param      name       The name
	 * @param      criterias  The criterias
	 */
	public ItemCategoryFull(String id, String name, ArrayList<ItemCriteriaFull> criterias) {
		super(id, criterias);
		_name = name;
	}

	/**
	 * Return the JSON value of the item's category.
	 *
	 * @return     The item's category and its criterias.
	 */
	public JsonValue toJSON() {
		JsonObject catFull = Json.object();
		catFull.add("categoryModel", _idCatModel);
		catFull.add("name", _name);
		JsonArray criterias = Json.array();
		for (Criteria criteria : _criterias) {
			JsonValue cri = ((ItemCriteriaFull)criteria).toJSON();
			criterias.add(cri);
		}
		catFull.add("criteria", criterias);
		return catFull;
	}

}