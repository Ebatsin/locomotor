package locomotor.components.models;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An user category, identified by his category model and containing a list of criterias.
 * It can be compare with another category based on the same model category.
 */
public class UserCategory extends Category {

	/**
	 * Constructs the object.
	 *
	 * @param      id         The user category identifier
	 * @param      criterias  The criterias
	 */
	public UserCategory(String id, ArrayList<UserCriteria> criterias) {
		super(id, criterias);
	}
	
	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		String display = "Category user: " + super.toString() + "\n";
		for (Criteria uc : getCriterias()) {
			display += ((UserCriteria)uc).toString() + "\n";
		}
		return display;
	}

	/**
	 * Create the perfect item's category (user perspective).
	 *
	 * @param      json      The json
	 * @param      catModel  The category model
	 *
	 * @return     The perfect item's category.
	 */
	public static UserCategory fromJSON(JsonValue json, CategoryModel catModel) {
		JsonObject category = json.asObject();
		String identifier = category.get("categoryId").asString();
		JsonArray criterias = category.get("criteria").asArray();

		// map to retrieve easier (perf)
		HashMap<String, CriteriaModel> criteriasModel = new HashMap<String, CriteriaModel>();
		for (CriteriaModel cm : catModel.getCriterias()) {
			criteriasModel.put(cm.getID(), cm);
		}

		ArrayList<UserCriteria> userCriterias = new ArrayList<UserCriteria>();

		// delegate for each criteria
		for (JsonValue criteria : criterias) {
			String id = criteria.asObject().get("criterionId").asString();
			UserCriteria uc = UserCriteria.fromJSON(criteria, criteriasModel.get(id));
			userCriterias.add(uc);
		}
		return new UserCategory(identifier, userCriterias);
	}
}