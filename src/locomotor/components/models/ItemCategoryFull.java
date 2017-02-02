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

	/**
	 * Factory from representation JSON.
	 *
	 * @param      json      The json
	 * @param      catModel  The category model
	 *
	 * @return     A new ItemCategoryFull object.
	 */
	public static ItemCategoryFull fromJSON(JsonValue json, CategoryModel catModel) {

		// System.out.println("Parsing categories's item: " + catModel.getName());

		JsonObject category = json.asObject();
		JsonArray criterias = category.get("criteria").asArray();

		// map to retrieve easier (perf)
		HashMap<String, CriteriaModel> criteriasModel = new HashMap<String, CriteriaModel>();
		for (CriteriaModel cm : catModel.getCriterias()) {
			criteriasModel.put(cm.getID(), cm);
		}

		HashSet<String> criteriasFound = new HashSet();

		ArrayList<ItemCriteriaFull> itemCriterias = new ArrayList<ItemCriteriaFull>();

		// delegate for each criteria
		for (JsonValue criteria : criterias) {
			String id = criteria.asObject().get("criterionId").asString();

			// already found
			if(criteriasFound.contains(id)) {
				String message = "The criteria " + criteriasModel.get(id).getName();
				message += " is present twice.";
				ErrorHandler.getInstance().push("fromJSON", true, message, message);
				return null;
			}
			criteriasFound.add(id);

			ItemCriteriaFull uc = ItemCriteriaFull.fromJSON(criteria, criteriasModel.get(id));

			// error found
			if(uc == null) {
				return null;
			}

			itemCriterias.add(uc);
		}

		// not same count, miss one criteria at least
		if(criteriasFound.size() != criteriasModel.size()) {
			String message = "At least one criteria is missing";
			ErrorHandler.getInstance().push("fromJSON", true, message, message);
			return null;
		}

		String identifier = category.get("categoryId").asString();
		return new ItemCategoryFull(identifier, "", itemCriterias);
	}

}