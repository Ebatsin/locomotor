package locomotor.core;

import locomotor.components.ResourceManager;

/**
 * Class for core resource manager.
 */
public class CoreResourceManager extends ResourceManager {
	
	/**
	 * The singleton.
	 */
	protected static CoreResourceManager _instance = null;

	/**
	 * Constructs the object.
	 */
	protected CoreResourceManager() {
		super();
		_baseURL = "server-resources/";
	}
	
	/**
	* Returns a singleton on the CoreResourceManager object.
	* @return A singleton on the CoreResourceManager object
	*/
	public static synchronized CoreResourceManager getInstance() {
		if(_instance == null) {
			_instance = new CoreResourceManager();
		}

		return _instance;
	}
}