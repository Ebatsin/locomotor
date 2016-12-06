package locomotor.components.models;

import java.util.ArrayList;

/**
 * Holds useful and reusable data (model) of a category of criteria(s).
 */
public class CategoryModel {

	/**
	 * The identifier.
	 */
	private String _identifier;
	
	/**
	 * The name of the category of criteria(s).
	 */
	private String _name;

	/**
	 * The criterias.
	 */
	private ArrayList<CriteriaModel> _criterias;

	/**
	 * Constructs the category of criteria(s).
	 *
	 * @param      id    The identifier
	 * @param      name  The name of the category
	 */
	public CategoryModel(String id, String name, ArrayList<CriteriaModel> criterias) {
		_identifier = id;
		_name = name;
		_criterias = criterias;
	}

	/**
	 * Returns the identifier of this category.
	 *
	 * @return The identifier of this category
	 */
	public String getID() {
		return _identifier;
	}

	/**
	 * Returns all the criteria of the category.
	 *
	 * @return The criteria of the category
	 */
	public ArrayList<CriteriaModel> getCriterias() {
		return _criterias;
	}
	
	/**
	 * Outputs a human readable representation of the model.
	 *
	 * @return    The representation of the model.
	 */
	public String toString() {
		String str = _identifier + " - " + _name + "\n";
		for (CriteriaModel c : _criterias) {
			str += c.toString();
		}
		return str;
	}


}