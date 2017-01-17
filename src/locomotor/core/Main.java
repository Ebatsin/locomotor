package locomotor.core;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import locomotor.components.Compare;
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
			
		DBH db = DBH.getInstance();
		db.connect("localhost", 27017);
		db.connectToDatabase("locomotor");

		NetworkHandler nh = NetworkHandler.getInstance();
		nh.init(8000, "key.pfx", "motdepasse");

		API.createHooks(nh);
		nh.start();
		
		// ArrayList<CategoryModel> catModel = db.getCategoriesModel();
		// for (CategoryModel cm : catModel) {
		// 	System.out.println(cm.toJSON().toString(WriterConfig.PRETTY_PRINT));
		// }

		// db.disconnect();
		
		// try {

			// ArrayList<CategoryModel> catModel = db.getCategoriesModel();
			// ArrayList<Item> items = db.getItems(catModel);

			// long startTime = System.nanoTime();

			// long estimatedTime = System.nanoTime() - startTime;
			// System.out.println(TimeUnit.MILLISECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS) + " ms");
			

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

		// }
		// catch(Exception exception) {
		// 	System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
		// 	exception.printStackTrace();
		// }		
		
	}
}