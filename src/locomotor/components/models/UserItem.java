package locomotor.components.models;

import java.util.ArrayList;

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

}