package locomotor.components.models;

import java.util.ArrayList;

/**
 * An user category can be compare with another category based on the same model category.
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
		return "Category user: " + super.toString();
	}
}