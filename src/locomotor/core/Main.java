package locomotor.core;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import locomotor.components.models.CategoryModel;
import locomotor.components.models.Vehicle;

import locomotor.components.network.IEndpointHandler;
import locomotor.components.network.NetworkData;
import locomotor.components.network.NetworkHandler;
import locomotor.components.network.NetworkResponseFactory;

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

			DBH db = DBH.getInstance();
			db.connect("localhost", 27017);
			
			db.connectToDatabase("locomotor");

			ArrayList<CategoryModel> catModel = db.getCategoriesModel();

			ArrayList<Vehicle> vehicles = db.getVehicles(catModel);

			db.disconnect();

		}
		catch(Exception exception) {
			System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
		}

		System.out.println("get network instance");
		NetworkHandler nh = NetworkHandler.getInstance();
		System.out.println("init");
		nh.init(8000, "key.pfx", "motdepasse");

		nh.createEndpoint("/api/test", new IEndpointHandler() {
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
			}
		});


		System.out.println("start");
		nh.start();
		
	}
}