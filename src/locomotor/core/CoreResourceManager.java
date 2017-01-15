package locomotor.core;

import locomotor.components.ResourceManager;

public class CoreResourceManager extends ResourceManager {
	private CoreResourceManager() {
		super();
		_baseURL = "resources/core/";
	}

	public static synchronized ResourceManager getInstance() {
		if(_instance == null) {
			_instance = new CoreResourceManager();
		}

		return _instance;
	}
}