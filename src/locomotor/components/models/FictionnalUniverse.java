package locomotor.components.models;

/**
 * @todo describe the class
 */
public class FictionnalUniverse {

	/**
	 * The unique identifier
	 */
	private int _id;

	/**
	 * The name of the (fictionnal) universe
	 */
	private String _name;
	/**
	 * The name of the image that illustrate the universe
	 */
	private String _imageName;

	/**
	 * Constructs the fictionnal universe
	 *
	 * @param      id         The identifier
	 * @param      name       The name
	 * @param      imageName  The image name
	 */
	public FictionnalUniverse(int id, String name, String imageName) {
		_id = id;
		_name = name;
		_imageName = imageName;
	}
	
}