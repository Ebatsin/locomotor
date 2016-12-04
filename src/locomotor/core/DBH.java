package locomotor.core;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;

import java.util.ArrayList;

import locomotor.components.models.*;
import locomotor.components.types.*;

import org.bson.Document;

/**
 * Singleton class for handling database connection and requests
 */
public class DBH {

	private static DBH db = null;
	private static MongoClient mc = null;
	private static MongoDatabase md;

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

   	/**
   	 * Connects to database.
   	 *
   	 * @param      databaseName  The database name
   	 */
   	public static synchronized void connectToDatabase(String databaseName) {
   		md = mc.getDatabase(databaseName);
   	}

   	public static ArrayList<CategoryModel> getCategoriesModel() {
   		ArrayList<CategoryModel> listCatMod = new ArrayList<CategoryModel>();
   		
   		FindIterable<Document> catMod = md.getCollection("categoryModel").find();
   		
   		System.out.println("Retrieving the model categories");

   		catMod.forEach(new Block<Document>() {
   			@Override
			public void apply(final Document doc) {
				
				String id = doc.getObjectId("_id").toString();
				String name = doc.getString("name");
   				ArrayList<Document> critIt = (ArrayList<Document>)doc.get("criteria");

   				ArrayList<CriteriaModel> criteriasMod = new ArrayList<CriteriaModel>();

   				System.out.println("Retrieving the criterias of " + name);

		   		for (Document crit : critIt) {

		   			System.out.println("Retrieving the values of " + crit.getString("name"));

		   			TypeFactory typeFactory = new TypeFactory();

		   			CEnumUniverseType.valueOf(crit.getInteger("universeType"));
		   			CUniverseType universe = typeFactory.getUniverse(CEnumUniverseType.valueOf(crit.getInteger("universeType")), crit.get("universe"));

		   			CriteriaModel criteria = new CriteriaModel(
			   			crit.getObjectId("_id").toString(),
			   			crit.getString("name"),
			   			crit.getString("question"),
			   			crit.getBoolean("isComparable"),
			   			CEnumUniverseType.valueOf(crit.getInteger("universeType")),
			   			CEnumVehicleType.valueOf(crit.getInteger("itemType")),
			   			CEnumUserType.valueOf(crit.getInteger("userType")),
			   			universe
		 			);

		 			criteriasMod.add(criteria);

		   		}

   				CategoryModel catModel = new CategoryModel(id, name, criteriasMod);

   				listCatMod.add(catModel);

   			}
   		});

   		return listCatMod;
   	}

}