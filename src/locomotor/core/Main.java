package locomotor.core;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import locomotor.components.logging.*;
import locomotor.components.models.CategoryModel;
import locomotor.components.models.Item;

import locomotor.components.network.IEndpointHandler;
import locomotor.components.network.NetworkHandler;

public class Main {
	
	/**
	 * One method to rule them all.
	 */
	public static void main(String[] args) {

		// disable logging on mongodb driver
		// cf http://mongodb.github.io/mongo-java-driver/3.0/driver/reference/management/logging/
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE); 

		try {

			ErrorHandler eh = ErrorHandler.getInstance();

			Long pid = new Long(1234);

			ErrorContext ec = eh.add(pid);

			String method1 = "methodGetCriteria";
			Logging log1 = new Logging("An error has occured while retrieving a criteria model", true, "context values or message");

			ec.add(method1, log1);

			String method2 = "methodAddBooking";
			Logging log2 = new Logging("An error has occured adding a booking, the vehicle does not exist", true, "user x, vehicle x");

			ec.add(method2, log2);

			System.out.println(ec);

			eh.remove(pid);

			// DBH db = DBH.getInstance();
			// db.connect("localhost", 27017);
			
			// db.connectToDatabase("locomotor");

			// ArrayList<CategoryModel> catModel = db.getCategoriesModel();

			// ArrayList<Item> items = db.getItems(catModel);
			
			// db.createUser("test2", "motdepasse");
			// db.authUser("test", "motdepasse");
			// db.authUser("test2", "motdepasse");

			// db.disconnect();

		}
		catch(Exception exception) {
			System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
		}

		// System.out.println("get network instance");
		// NetworkHandler nh = NetworkHandler.getInstance();
		// System.out.println("init");
		// nh.init(8000, "/", "key.pfx", "motdepasse");

		// System.out.println("Add handlers");
		// nh.link("api", new IEndpointHandler() {
		// 	public void handle(TreeMap<String, String> parameters) {
		// 		for(Map.Entry<String,String> entry : parameters.entrySet()) {
		// 			System.out.println(entry.getKey() + " => " + entry.getValue());
		// 		}
		// 	}
		// });

		// System.out.println("start");
		// nh.start();
		
	}
}