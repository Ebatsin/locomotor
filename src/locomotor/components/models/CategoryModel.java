package locomotor.components.models;

/**
 * Holds useful and reusable data (model) of a category of criteria(s).
 */
public class CategoryModel {

	/**
	 * The identifier.
	 */
	private int _identifier;
	/**
	 * The name of the category of criteria(s).
	 */
	private String _name;

	/**
	 * Constructs the category of criteria(s)
	 *
	 * @param      id    The identifier
	 * @param      name  The name of the category
	 */
	public CategoryModel(int id, String name) {
		_identifier = id;
		_name = name;
	}

}