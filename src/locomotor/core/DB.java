package locomotor.core;

/**
 * Singleton class for handling database connection and requests
 */
public class DB {

	private static DB db = null;

	/**
	 * Private constructor to prevent 
	 */
	private DB() {}

	/**
	 * Gets the instance of the database
	 *
	 * @return     The instance.
	 */
	public static DB getInstance() {
		if (db == null) {
			synchronized (DB.class) {
				// double check lock
				if (db == null) {
					db = new DB();
				}
			}
		}
   		return db;
   	}

}