package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import locomotor.components.JSONDisplayable;
import locomotor.components.types.CItemType;

/**
 * @todo.
 */
public class ItemCriteriaFull extends ItemCriteria implements JSONDisplayable {

	/**
	 * The name of the criteria.
	 */
	private String _name;

	/**
	 * Constructs the object.
	 *
	 * @param      id     The identifier
	 * @param      name   The name
	 * @param      value  The value
	 */
	public ItemCriteriaFull(String id, String name, CItemType value) {
		super(id, value);
		_name = name;
	}

	/**
	 * { function_description }
	 *
	 * @return     { description_of_the_return_value }
	 */
	public JsonValue toJSON() {
		JsonObject critFull = Json.object();
		critFull.add("criterionModel", _idCritModel);
		critFull.add("name", _name);
		JsonValue value = ((CItemType)_value).toJSON();
		critFull.add("value", value);
		return critFull;
	}

}