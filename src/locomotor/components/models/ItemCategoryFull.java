package locomotor.components.models;

import java.util.ArrayList;

/**
 * @todo.
 */
public class ItemCategoryFull extends ItemCategory {

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

}