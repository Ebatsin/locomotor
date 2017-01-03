package locomotor.components.models;

import java.util.ArrayList;

/**
 * @todo
 */
public abstract class Category {
	
	/**
	 * The unique identifier of the category.
	 */
	private String _idCatModel;

	/**
	 * The list of criterias.
	 */
	protected ArrayList<? extends Criteria> _criterias;

	/**
	 * Constructs the object.
	 *
	 * @param      id         The identifier of the category
	 * @param      criterias  The criterias
	 */
	public Category(String id, ArrayList<? extends Criteria> criterias) {
		_idCatModel = id;
		_criterias = criterias;
	}
	
	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		return _idCatModel;
	}

	public ArrayList<? extends Criteria> getCriterias() {
		return _criterias;
	}
}