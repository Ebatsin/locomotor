package locomotor.core;

import java.io.File;

/*
* Handles the resources versionning and allows the system to send a resource only when it's not up to date
* client side.
*/
public class ResourceManager {
	static ResourceManager _instance = null;
	String _baseURL;

	private ResourceManager() {
		_baseURL = "resources/core/"
	}

	static public synchronized ResourceManager getInstance() {
		if(_instance == null) {
			_instance = new ResourceManager();
		}

		return _instance;
	}

	/**
	* Returns the last modified timestamp of the file. Used as a version number.
	* @param resource The pathname of the resource needed (ex. "images/cat.png")
	* @return The version number if the file exists or 0 otherwise.
	*/
	public long getVersion(String resource) {
		File file = new File(_baseURL + resource);

		// if the file does not exist, abort
		if(!file.exists() || file.isDirectory()) {
			return 0;
		}
		
		return file.lastModified();
	}

	/**
	* Returns the file requested if it exists.
	* @param resource The pathname of the resource needed (ex. "images/cat.png")
	* @return The file if it is found, null otherwise.
	*/
	public File getResource(String resource) {
		File file = new File(_baseURL + resource);

		if(!file.exists() || file.isDirectory()) {
			return null;
		}

		return file;
	}
}