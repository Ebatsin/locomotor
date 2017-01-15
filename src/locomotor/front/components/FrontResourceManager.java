package locomotor.front.components;

import locomotor.components.ResourceManager;

public class FrontResourceManager extends ResourceManager {
	private FrontResourceManager() {
		super();
		_baseURL = "resources/front/";
	}

	public static synchronized ResourceManager getInstance() {
		if(_instance == null) {
			_instance = new FrontResourceManager();
		}

		return _instance;
	}
}