package locomotor.components.models;

import java.util.ArrayList;

/**
 * Full item containing all information.
 */
public class ItemFull extends Item {

	/**
	 * The name of the item.
	 */
	private String _name;

	/**
	 * The universe of the item.
	 */
	private String _universe;

	/**
	 * The description of the item.
	 */
	private String _description;
	
	/**
	 * The image of the image.
	 */
	private String _image;

	/**
	 * Constructs the object.
	 *
	 * @param      id           The identifier
	 * @param      name         The name
	 * @param      universe     The universe
	 * @param      description  The description
	 * @param      image        The image
	 * @param      categories   The categories
	 */
	public ItemFull(String id, String name, String universe, String description, String image, ArrayList<ItemFullCategory> categories) {
		super(id, categories);
		_name = name;
		_universe = universe;
		_description = description;
		_image = image;
	}

}