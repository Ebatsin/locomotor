package locomotor.core;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import locomotor.components.logging.*;
import locomotor.components.models.CategoryModel;
import locomotor.components.models.Item;

import locomotor.components.network.API;
import locomotor.components.network.IEndpointHandler;
import locomotor.components.network.NetworkData;
import locomotor.components.network.NetworkHandler;
import locomotor.components.network.NetworkJsonResponse;
import locomotor.components.network.NetworkResponse;
import locomotor.components.network.NetworkResponseFactory;

/**
 * Where all the magic happens.
 */
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

			// ErrorHandler eh = ErrorHandler.getInstance();

			// Long pid = new Long(1234);

			// ErrorContext ec = eh.add(pid);

			// String method1 = "methodGetCriteria";
			// Logging log1 = new Logging("An error has occured while retrieving a criteria model", true, "context values or message");

			// ec.add(method1, log1);

			// String method2 = "methodAddBooking";
			// Logging log2 = new Logging("An error has occured adding a booking, the vehicle does not exist", true, "user x, vehicle x");

			// ec.add(method2, log2);

			// System.out.println(ec);

			// eh.remove(pid);

			DBH db = DBH.getInstance();
			db.connect("localhost", 27017);
			
			db.connectToDatabase("locomotor");

			ArrayList<CategoryModel> catModel = db.getCategoriesModel();

			// ArrayList<Item> items = db.getItems(catModel);
			
			// db.createUser("test2", "motdepasse");
			// db.authUser("test", "motdepasse");
			// db.authUser("test2", "motdepasse");

			db.disconnect();

		}
		catch(Exception exception) {
			System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
		}

		// System.out.println("get network instance");
		// NetworkHandler nh = NetworkHandler.getInstance();
		// System.out.println("init");
		// nh.init(8000, "key.pfx", "motdepasse");

		/*nh.createEndpoint("/api/test", new IEndpointHandler() {
			public void handle(NetworkData data, NetworkResponseFactory response) {
				if(data.isValid()) {
					System.out.println("paramètres définis : " + data.getParametersName());

					if(data.isDefined("text1")) {
						System.out.println("Contenu de text1 : '" + data.getAsString("text1") + "'");
					}
					else {
						System.out.println("Le paramètre text1 n'a pas été trouvé");
					}
				}
				else {
					System.out.println("There was an error while parsing the parameters");
				}

				NetworkJsonResponse resp = response.getJsonContext();
				JsonObject obj = Json.object()
					.add("test", "ok")
					.add("message", "bonjour");
				resp.success(obj);
			}
		});*/

		// API.createHooks(nh);


		// System.out.println("start");
		// nh.start();
		
	}
}