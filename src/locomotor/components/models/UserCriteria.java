package locomotor.components.models;

import locomotor.components.types.CUserType;

/**
 * An user criteria can be compare with another criteria based on the same model criteria.
 */
public class UserCriteria extends Criteria {

	/**
	 * Constructs the object.
	 *
	 * @param      id     The user criteria identifier
	 * @param      value  The value
	 */
	public UserCriteria(String id, CUserType value) {
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

}