package locomotor.components.models;

/**
 * @todo describe the class
 */
public class CategoryModel {

	/**
	 * The identifier
	 */
	private int _id;
	/**
	 * The name of the category of criteria(s)
	 */
	private String name;

	/**
	 * Constructs the category of criteria(s)
	 *
	 * @param      id    The identifier
	 * @param      name  The name of the category
	 */
	public CategoryModel(int id, String name) {
		_id = id;
		_name = name;
	}

}