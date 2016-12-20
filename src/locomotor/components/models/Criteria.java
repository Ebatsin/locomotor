package locomotor.components.models;

import locomotor.components.types.CComparableType;

/**
 * A criteria can be compare with another criteria based on the same model criteria.
 */
public abstract class Criteria {

	/**
	 * Identify the criteria model
	 * It is possible to infer the type of the value
	 */
	private String _idCritModel;

	/**
	 * The value of the criteria
	 */
	private CComparableType _value;

	/**
	 * Constructs the object.
	 *
	 * @param      id     The criterai model identifier
	 * @param      value  The value
	 */
	public Criteria(String id, CComparableType value) {
		_idCritModel = id;
		_value = value;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		return _value.toString() + "\n";
	}

}