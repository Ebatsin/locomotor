package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An user item, described by a list of categories of criterias, is the perfect item wanted by the user.
 */
public class UserItem {

	/**
	 * The categories of criterias.
	 */
	private ArrayList<UserCategory> _categories;

	/**
	 * Constructs the object.
	 *
	 * @param      categories  The categories of criterias
	 */
	public UserItem(ArrayList<UserCategory> categories) {
		_categories = categories;
	}

	/**
	 * Gets the categories.
	 *
	 * @return     The categories.
	 */
	public ArrayList<UserCategory> getCategories() {
		return _categories;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		String display = "UserItem\n";
		for (UserCategory uc : _categories) {
			display += uc.toString() + "\n";
		}
		return display;
	}

	/**
	 * Create the perfect item (user perspective).
	 *
	 * @param      json       The json
	 * @param      catsModel  The categories model
	 *
	 * @return     The perfect item.
	 */
	public static UserItem fromJSON(JsonValue json, ArrayList<CategoryModel> catsModel) {
		JsonArray categories = json.asArray();

		// map to retrieve easier (perf)
		HashMap<String, CategoryModel> categoriesMap = new HashMap<String, CategoryModel>();
		for (CategoryModel cm : catsModel) {
			categoriesMap.put(cm.getID(), cm);
		}

		ArrayList<UserCategory> userCategories = new ArrayList<UserCategory>();

		// delegate for each category
		for (JsonValue category : categories) {
			String identifier = category.asObject().get("categoryId").asString();
			UserCategory uc = UserCategory.fromJSON(category, categoriesMap.get(identifier));
			userCategories.add(uc);
		}
		return new UserItem(userCategories);
	}

}