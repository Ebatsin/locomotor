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
import locomotor.components.models.CriteriaModel;
import locomotor.components.models.Item;
import locomotor.components.models.ItemCategory;
import locomotor.components.models.ItemCriteria;
import locomotor.components.types.CMappedStringList;
import locomotor.components.types.CGraphStringList;

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

			// ErrorContext ec = eh.get(pid);

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

			ArrayList<Item> items = db.getItems(catModel);

			System.out.println("Test compare");

			for (Item it : items) {
				ArrayList<ItemCategory> itc = it.getCategories();
				for (int i = 0; i < itc.size(); i++) {
					ArrayList<ItemCriteria> ica = (ArrayList<ItemCriteria>)itc.get(i).getCriterias();
					ArrayList<CriteriaModel> critModel = catModel.get(i).getCriterias();
					for (int j = 0; j < ica.size(); j++) {
						ItemCriteria ic = ica.get(j);
						CriteriaModel icm = critModel.get(j);
						// System.out.println(ic.getValue().getClass().getName());
						if (ic.getValue().getClass().getName().equals("locomotor.components.types.CMappedStringList")) {
							TreeMap<Integer, String> map = new TreeMap<Integer, String>();
							map.put(7, "osef");
							CMappedStringList cslUser = new CMappedStringList(map);
							CMappedStringList cslItem = (CMappedStringList)ic.getValue();
							CGraphStringList cslUniverse = (CGraphStringList)icm.getUniverse();
							System.out.println(cslUser);
							System.out.println(cslItem);
							System.out.println(cslUniverse);
							double note = cslItem.compare(cslUser, cslUniverse);
							System.out.println("Note = " + note);

						}
					}
				}
				// for (ItemCategory ica : i.getCategories()) {
				// 	for (ItemCriteria icr : (ArrayList<ItemCriteria>)ica.getCriterias()) {
				// 		System.out.println(icr.getValue().getClass().getName());
				// 		if (icr.getValue().getClass().getName().equals("locomotor.components.types.CMappedStringList")) {
				// 			System.out.println(icr);

				// 		}
				// 	}
				// }
				// System.out.println(i);
			}
			
			// db.createUser("test2", "motdepasse");
			// db.authUser("test", "motdepasse");
			// db.authUser("test2", "motdepasse");

			db.disconnect();

		}
		catch(Exception exception) {
			System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
			exception.printStackTrace();
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