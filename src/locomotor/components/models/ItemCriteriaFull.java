package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import locomotor.components.JSONDisplayable;
import locomotor.components.logging.ErrorHandler;
import locomotor.components.types.CItemType;
import locomotor.components.types.TypeFactory;

/**
 * Displayable form of an item's criteria.
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
	 * Return the JSON value of the item's criteria.
	 *
	 * @return     The item's criteria.
	 */
	public JsonValue toJSON() {
		JsonObject critFull = Json.object();
		critFull.add("criterionModel", _idCritModel);
		critFull.add("name", _name);
		JsonValue value = ((CItemType)_value).toJSON();
		critFull.add("value", value);
		return critFull;
	}

	/**
	 * Factory from representation JSON.
	 *
	 * @param      json       The json
	 * @param      critModel  The criteria model
	 *
	 * @return     A new ItemCriteriaFull object.
	 */
	public static ItemCriteriaFull fromJSON(JsonValue json, CriteriaModel critModel) {

		// System.out.println("Parsing criteria's item: " + critModel.getName());

		JsonObject criteria = json.asObject();
		String identifier = criteria.get("criterionId").asString();
		JsonValue value = criteria.get("value");

		// delegate instanciation to the factory
		TypeFactory typeFactory = new TypeFactory();
		CItemType itemValue = typeFactory.getItemFromJson(critModel.getItemType(), value, critModel.getUniverse());
		
		if (itemValue == null) {
			String message = "Error while parsing the criteria";
			ErrorHandler.getInstance().push("fromJSON", true, message, message);
			return null;
		}

		return new ItemCriteriaFull(identifier, "", itemValue);
	}

}