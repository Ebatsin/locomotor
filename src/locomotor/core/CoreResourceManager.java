package locomotor.core;

import locomotor.components.ResourceManager;

public class CoreResourceManager extends ResourceManager {
	protected static CoreResourceManager _instance = null;

	protected CoreResourceManager() {
		super();
		_baseURL = "resources/core/";
	}
	/**
	* Returns a singleton on the CoreResourceManager object.
	* @returns A singleton on the CoreResourceManager object
	*/
	public static synchronized CoreResourceManager getInstance() {
		if(_instance == null) {
			_instance = new CoreResourceManager();
		}

		return _instance;
	}
}