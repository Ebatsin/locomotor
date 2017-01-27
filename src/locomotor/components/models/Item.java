package locomotor.components.models;

import java.util.ArrayList;

/**
 * An item described by a list of categories of criterias.
 */
public class Item {

	/**
	 * The identifier of the item.
	 */
	private String _identifier;

	/**
	 * The categories of criterias.
	 */
	private ArrayList<ItemCategory> _categories;

	/**
	 * Constructs the object.
	 *
	 * @param      id          The item identifier
	 * @param      categories  The categories
	 */
	public Item(String id, ArrayList<ItemCategory> categories) {
		_identifier = id;
		_categories = categories;
	}

	/**
	 * Gets the id.
	 *
	 * @return     The id.
	 */
	public String getID() {
		return _identifier;
	}

	/**
	 * Gets the categories.
	 *
	 * @return     The categories.
	 */
	public ArrayList<ItemCategory> getCategories() {
		return _categories;
	}

}