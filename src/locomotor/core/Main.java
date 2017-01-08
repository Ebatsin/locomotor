package locomotor.core;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import locomotor.components.logging.*;
import locomotor.components.models.CategoryModel;
import locomotor.components.models.CriteriaModel;
import locomotor.components.models.Item;
import locomotor.components.models.ItemCategory;
import locomotor.components.models.ItemCriteria;
import locomotor.components.network.API;
import locomotor.components.network.IEndpointHandler;
import locomotor.components.network.NetworkData;
import locomotor.components.network.NetworkHandler;
import locomotor.components.network.NetworkJsonResponse;
import locomotor.components.network.NetworkResponse;
import locomotor.components.network.NetworkResponseFactory;
import locomotor.components.types.*;

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

		Gaussian g = Gaussian.getInstance();
		System.out.println("Tests gaussian cdf.");
		System.out.println("cdf(-5) = " + g.cdf(-5) + " true value : " + g.trueCdf(-5));
		System.out.println("cdf(5) = " + g.cdf(5) + " true value : " + g.trueCdf(5));
		System.out.println("cdf(-2) = " + g.cdf(-2) + " true value : " + g.trueCdf(-2));
		System.out.println("cdf(2) = " + g.cdf(2) + " true value : " + g.trueCdf(2));
		System.out.println("cdf(-2.004) = " + g.cdf(-2.004) + " true value : " + g.trueCdf(-2.004));
		System.out.println("cdf(2.004) = " + g.cdf(2.004) + " true value : " + g.trueCdf(2.004));
		System.out.println("cdf(0) = " + g.cdf(0) + " true value : " + g.trueCdf(0));
		System.out.println("Tests gaussian pdf.");
		System.out.println("pdf(-5) = " + g.pdf(-5) + " true value : " + g.truePdf(-5));
		System.out.println("pdf(5) = " + g.pdf(5) + " true value : " + g.truePdf(5));
		System.out.println("pdf(-2) = " + g.pdf(-2) + " true value : " + g.truePdf(-2));
		System.out.println("pdf(-2.004) = " + g.pdf(-2.004) + " true value : " + g.truePdf(-2.004));
		System.out.println("pdf(2.004) = " + g.pdf(2.004) + " true value : " + g.truePdf(2.004));
		System.out.println("pdf(2) = " + g.pdf(2) + " true value : " + g.truePdf(2));
		System.out.println("pdf(0) = " + g.pdf(0) + " true value : " + g.truePdf(0));

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
			ArrayList<Item> items;

			long startTime = System.nanoTime();

			for (int ii = 0; ii < 1000; ii++) {
				 items = db.getItems(catModel);
			}

			long estimatedTime = System.nanoTime() - startTime;
			System.out.println(TimeUnit.MILLISECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS) + " ms");
			

			// System.out.println("Test compare");

			// for (int rdm = 0; rdm < 1000; rdm++) {
				
			// 	for (Item it : items) {
			// 		ArrayList<ItemCategory> itc = it.getCategories();
			// 		for (int i = 0; i < itc.size(); i++) {
			// 			ArrayList<ItemCriteria> ica = (ArrayList<ItemCriteria>)itc.get(i).getCriterias();
			// 			ArrayList<CriteriaModel> critModel = catModel.get(i).getCriterias();
			// 			for (int j = 0; j < ica.size(); j++) {
			// 				ItemCriteria ic = ica.get(j);
			// 				CriteriaModel icm = critModel.get(j);
			// 				// System.out.println(ic.getValue().getClass().getName());
			// 				if (ic.getValue().getClass().getName().equals("locomotor.components.types.CMappedStringList")) {
			// 					TreeMap<Integer, String> map = new TreeMap<Integer, String>();
			// 					map.put(7, "osef");
			// 					CMappedStringList cslUser = new CMappedStringList(map);
			// 					CMappedStringList cslItem = (CMappedStringList)ic.getValue();
			// 					CGraphStringList cslUniverse = (CGraphStringList)icm.getUniverse();
			// 					// System.out.println("Set");
			// 					// System.out.println("User list " + cslUser);
			// 					// System.out.println("Item list " + cslItem);
			// 					double note = cslItem.compare(cslUser, cslUniverse);
			// 					// System.out.println("Note = " + note);

			// 				}
			// 				else if (ic.getValue().getClass().getName().equals("locomotor.components.types.CTree")) {
			// 					CTree cslUser = new CTree(8, "plat-enneigé");
			// 					CTree cslItem = (CTree)ic.getValue();
			// 					CGraphTree cslUniverse = (CGraphTree)icm.getUniverse();
			// 					// System.out.println("Tree");
			// 					// System.out.println("User tree " + cslUser);
			// 					// System.out.println("Item tree " + cslItem);
			// 					double note = cslItem.compare(cslUser, cslUniverse);
			// 					// System.out.println("Note = " + note);

			// 				}
			// 			}
			// 		}
			// 	}
			// }

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