package locomotor.core;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

/**
 * Singleton class for handling database connection and requests
 */
public class DBH extends MongoClient {

	private static DBH db = null;

	/**
	 * Private constructor to prevent any other class to instantiate
	 */
	private DBH() {
		super();
	}

	/**
	 * Private constructor to prevent any other class to instantiate
	 *
	 * @param      ip    The IP address
	 * @param      port  The port
	 */
	private DBH(String ip, int port) {
        super(ip, port);
    }

	/**
	 * Gets the instance.
	 *
	 * @param      ip    The IP address
	 * @param      port  The port
	 *
	 * @return     The instance.
	 */
	public static DBH getInstance(String ip, int port) {
		if (db == null) {
			synchronized (DBH.class) {
				// double check lock
				if (db == null) {
					db = new DBH(ip, port);
				}
			}
		}
   		return db;
   	}

   	/**
	 * Gets the instance of the database
	 *
	 * @return     The instance.
	 */
	public static DBH getInstance() {
		if (db == null) {
			synchronized (DBH.class) {
				// double check lock
				if (db == null) {
					db = new DBH();
				}
			}
		}
   		return db;
   	}

}