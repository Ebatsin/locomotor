package locomotor.core;

import java.util.logging.*;
import java.util.ArrayList;

import locomotor.components.models.*;
import locomotor.components.types.*;

public class Main {
	
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

		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		
	}
}