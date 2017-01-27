package locomotor.components.models;

import locomotor.components.types.CItemType;

/**
 * A criteria of an item, identified by his criteria model and containing a value.
 * It can be compare with another criteria based on the same model criteria.
 */
public class ItemCriteria extends Criteria {

	/**
	 * Constructs the object.
	 *
	 * @param      id     The identifier of the criteria model
	 * @param      value  The value
	 */
	public ItemCriteria(String id, CItemType value) {
		super(id, value);
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		return super.toString();
	}

	/**
	 * Gets the value.
	 *
	 * @return     The value.
	 */
	public CItemType getValue() {
		return (CItemType)super.getValue();
	}

}