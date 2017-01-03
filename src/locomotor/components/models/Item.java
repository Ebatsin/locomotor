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

}