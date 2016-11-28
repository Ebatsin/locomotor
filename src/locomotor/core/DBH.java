package locomotor.core;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

/**
 * Singleton class for handling database connection and requests
 */
public class DBH {

	private static DBH db = null;
	private static MongoClient mc = null;

	/**
	 * Private constructor to prevent any other class to instantiate
	 */
	private DBH() {}

	/**
	 * Gets the instance.
	 *
	 * @return     The instance.
	 */
	public static synchronized DBH getInstance() {
		if (db == null) {
			db = new DBH();
		}
   		return db;
   	}

   	/**
   	 * Connect to the default database
   	 */
	public static synchronized void connect() {
		if (mc == null) {
			mc = new MongoClient();
		} else {
			// disconnect first
			disconnect();
			mc = new MongoClient();
		}
		System.out.println("Connected to the client");
   	}

   	/**
   	 * Connect to the database
   	 *
   	 * @param      ip    The IP address
   	 * @param      port  The port
   	 */
   	public static synchronized void connect(String ip, int port) {
		if (mc == null) {
				mc = new MongoClient(ip, port);
		} else {
			// disconnect first
			disconnect();
			mc = new MongoClient(ip, port);
		}
		System.out.println("Connected to the client");
   	}

   	/**
   	 * Disconnect from the database
   	 */
   	public static synchronized void disconnect() {
   		mc.close();
   		mc = null;
   		System.out.println("Disconnected from the client");
   	}

}