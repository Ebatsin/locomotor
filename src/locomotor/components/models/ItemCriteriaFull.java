package locomotor.components.models;

import locomotor.components.types.CItemType;

/**
 * @todo.
 */
public class ItemCriteriaFull extends ItemCriteria {

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

}