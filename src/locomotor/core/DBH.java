package locomotor.core;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.ListIterator;

import locomotor.components.models.Category;
import locomotor.components.models.CategoryModel;
import locomotor.components.models.Criteria;
import locomotor.components.models.CriteriaModel;
import locomotor.components.models.Vehicle;

import locomotor.components.types.CEnumUniverseType;
import locomotor.components.types.CEnumUserType;
import locomotor.components.types.CEnumVehicleType;
import locomotor.components.types.CUniverseType;
import locomotor.components.types.CVehicleType;
import locomotor.components.types.TypeFactory;

import org.bson.Document;

/**
 * Singleton class for handling database connection and requests.
 */
public class DBH {

	private static DBH db = null;
	private static MongoClient mc = null;
	private static MongoDatabase md;

	/**
	 * Private constructor to prevent any other class to instantiate.
	 */
	private DBH() {}

	/**
	 * Gets the instance.
	 *
	 * @return     The instance.
	 */
	public static synchronized DBH getInstance() {
		if(db == null) {
			db = new DBH();
		}
		return db;
	}

	/**
	 * Connect to the default database.
	 */
	public static synchronized void connect() {
		if(mc == null) {
			mc = new MongoClient();
		}
		else {
			// disconnect first
			disconnect();
			mc = new MongoClient();
		}
		System.out.println("Connected to the database");
	}

	/**
	 * Connect to the database.
	 *
	 * @param      ip    The IP address
	 * @param      port  The port
	 */
	public static synchronized void connect(String ip, int port) {
		if(mc == null) {
			mc = new MongoClient(ip, port);
		}
		else {
			// disconnect first
			disconnect();
			mc = new MongoClient(ip, port);
		}
		System.out.println("Connected to the database");
	}

	/**
	 * Disconnect from the database.
	 */
	public static synchronized void disconnect() {
		mc.close();
		mc = null;
		System.out.println("Disconnected from the database");
	}

	/**
	 * Connects to database.
	 *
	 * @param      databaseName  The database name
	 */
	public static synchronized void connectToDatabase(String databaseName) {
		md = mc.getDatabase(databaseName);
	}

	/**
	 * Gets the categories model.
	 *
	 * @return     The categories model.
	 */
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
				
				TypeFactory typeFactory = new TypeFactory();

				for(Document crit : critIt) {

					CUniverseType universe = typeFactory.getUniverse(CEnumUniverseType.valueOf(
						crit.getInteger("universeType")), crit.get("universe"));

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

					System.out.print(criteria);

				}

				CategoryModel catModel = new CategoryModel(id, name, criteriasMod);

				listCatMod.add(catModel);

			}
		});

		return listCatMod;
	}

	/**
	 * Gets the vehicles.
	 *
	 * @param      catModel  The categories model
	 *
	 * @return     The vehicles.
	 */
	public static ArrayList<Vehicle> getVehicles(ArrayList<CategoryModel> catModel) {
	
		ArrayList<Vehicle> listVehicles = new ArrayList<Vehicle>();
		
		FindIterable<Document> vehicles = md.getCollection("items").find();
		
		System.out.println("Retrieving the vehicles");

		vehicles.forEach(new Block<Document>() {
			@Override
			public void apply(final Document doc) {

				String id = doc.getObjectId("_id").toString();
				ArrayList<Document> catIt = (ArrayList<Document>)doc.get("categories");

				ArrayList<Category> categories = new ArrayList<Category>();

				System.out.println("Retrieving the categories of " + doc.getString("name"));
				
				TypeFactory typeFactory = new TypeFactory();

				// iterator to iterate simultaneously
				ListIterator<CategoryModel> itCatMod = catModel.listIterator();
				ListIterator<Document> itCat = catIt.listIterator();

				while(itCatMod.hasNext() && itCat.hasNext()) {

					CategoryModel currentCatMod = itCatMod.next();
					Document currentCat = itCat.next();

					String identifierCat = currentCat.getObjectId("categoryModel").toString();
					System.out.println("Category " + identifierCat);

					// check category model
					if(!currentCatMod.getID().equals(identifierCat)) {
						// error
						System.err.println("Error: Category model does not match current category");
						System.err.println(currentCat);
						System.err.println(currentCatMod);
						System.exit(0);
					}

					ArrayList<Criteria> criterias = new ArrayList<Criteria>();
					ArrayList<Document> critIt = (ArrayList<Document>)currentCat.get("criteria");
					ArrayList<CriteriaModel> critModIt = currentCatMod.getCriterias();

					// iterator to iterate simultaneously
					ListIterator<CriteriaModel> itCriMod = critModIt.listIterator();
					ListIterator<Document> itCrit = critIt.listIterator();

					while(itCriMod.hasNext() && itCrit.hasNext()) {

						CriteriaModel currentCritMod = itCriMod.next();
						Document currentCrit = itCrit.next();

						String identifierCrit = currentCrit.getObjectId("criterionModel").toString();
						System.out.println("Criterion " + identifierCrit);

						// check criteria model
						if(!currentCritMod.getID().equals(identifierCrit)) {
							// error
							System.err.println("Error: Criteria model does not match current criteria");
							System.err.println(currentCrit);
							System.err.println(currentCritMod);
							System.exit(0);
						}

						// creation criteria
						CVehicleType value = typeFactory.getVehicle(currentCritMod.getVehicleType(),
							currentCrit.get("value"), currentCritMod.getUniverse());

						Criteria criteria = new Criteria(
							identifierCrit,
							value
						);

						criterias.add(criteria);

						System.out.println(criteria);

					}

					Category category = new Category(
						identifierCat, criterias
					);

					categories.add(category);

				}

			}
		});

		return listVehicles;

	}

}