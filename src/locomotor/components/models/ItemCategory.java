package locomotor.components.models;

import java.util.ArrayList;

/**
 * An item category, identified by his category model and containing a list of criterias.
 * It can be compare with another category based on the same model category.
 */
public class ItemCategory extends Category {

	/**
	 * Constructs the object.
	 *
	 * @param      id         The item category identifier
	 * @param      criterias  The criterias
	 */
	public ItemCategory(String id, ArrayList<ItemCriteria> criterias) {
		super(id, criterias);
	}
	
	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		return "Category item: " + super.toString();
	}
}