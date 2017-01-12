package locomotor.core;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import locomotor.components.Pair;

import locomotor.components.logging.ErrorHandler;

import locomotor.components.models.CategoryModel;
import locomotor.components.models.CriteriaModel;
import locomotor.components.models.Item;
import locomotor.components.models.ItemCategory;
import locomotor.components.models.ItemCriteria;

import locomotor.components.types.CEnumItemType;
import locomotor.components.types.CEnumUniverseType;
import locomotor.components.types.CEnumUserType;
import locomotor.components.types.CItemType;
import locomotor.components.types.CUniverseType;
import locomotor.components.types.TypeFactory;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Singleton class for handling database connection and requests.
 */
public class DBH {

	/**
	 * Singleton database handler object, with lazy instanciation.
	 */
	private static DBH db = null;

	/**
	 * Singleton client object, with lazy instanciation.
	 */
	private static MongoClient mc = null;

	/**
	 * Singleton database object, with lazy instanciation.
	 */
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

					System.out.println("Retrieving the criteria " + crit.getString("name"));

					CUniverseType universe = typeFactory.getUniverse(CEnumUniverseType.valueOf(
						crit.getInteger("universeType")), crit.get("universe"));

					CriteriaModel criteria = new CriteriaModel(
						crit.getObjectId("_id").toString(),
						crit.getString("name"),
						crit.getString("question"),
						crit.getBoolean("isComparable"),
						CEnumUniverseType.valueOf(crit.getInteger("universeType")),
						CEnumItemType.valueOf(crit.getInteger("itemType")),
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
	 * Gets the items.
	 *
	 * @param      catModel  The categories model
	 *
	 * @return     The items.
	 */
	public static ArrayList<Item> getItems(ArrayList<CategoryModel> catModel) {
	
		ArrayList<Item> listItems = new ArrayList<Item>();
		
		FindIterable<Document> items = md.getCollection("items").find();
		
		System.out.println("Retrieving the items");

		items.forEach(new Block<Document>() {
			@Override
			public void apply(final Document doc) {

				ArrayList<Document> catIt = (ArrayList<Document>)doc.get("categories");

				HashMap<String, Document> categoriesItemMap = new HashMap<String, Document>();
				for (Document d : catIt) {
					categoriesItemMap.put(d.getObjectId("categoryModel").toString(), d);
				}

				ArrayList<ItemCategory> categories = new ArrayList<ItemCategory>();

				System.out.println("Retrieving the categories of " + doc.getString("name"));
				
				TypeFactory typeFactory = new TypeFactory();

				// iterator
				ListIterator<CategoryModel> itCatMod = catModel.listIterator();

				while(itCatMod.hasNext()) {

					CategoryModel currentCatMod = itCatMod.next();
					// not found yet
					Document currentCat = categoriesItemMap.get(currentCatMod.getID());

					// not found
					if (currentCat == null) {
						// @todo: error
						System.err.println("Error: Category model does not match any category of the current item");
						System.err.println(currentCatMod);
						System.err.println(catIt);
						System.exit(0);
					}

					String identifierCat = currentCat.getObjectId("categoryModel").toString();
					System.out.println("Category " + identifierCat);

					ArrayList<ItemCriteria> criterias = new ArrayList<ItemCriteria>();
					ArrayList<Document> critIt = (ArrayList<Document>)currentCat.get("criteria");

					HashMap<String, Document> criteriasItemMap = new HashMap<String, Document>();
					for (Document d : critIt) {
						criteriasItemMap.put(d.getObjectId("criterionModel").toString(), d);
					}

					ArrayList<CriteriaModel> critModIt = currentCatMod.getCriterias();

					// iterator to iterate simultaneously
					ListIterator<CriteriaModel> itCriMod = critModIt.listIterator();

					while(itCriMod.hasNext()) {

						CriteriaModel currentCritMod = itCriMod.next();
						// not found yet
						Document currentCrit = criteriasItemMap.get(currentCritMod.getID());
						
						// not found
						if(currentCrit == null) {
							// @todo: error
							System.err.println("Error: Criteria model does not match any criteria of the current category");
							System.err.println(currentCritMod);
							System.err.println(critIt);
							System.exit(0);
						}

						String identifierCrit = currentCrit.getObjectId("criterionModel").toString();
						System.out.println("Criterion " + identifierCrit);


						// creation criteria
						CItemType value = typeFactory.getItem(currentCritMod.getItemType(),
							currentCrit.get("value"), currentCritMod.getUniverse());

						ItemCriteria criteria = new ItemCriteria(
							identifierCrit,
							value
						);

						criterias.add(criteria);

						System.out.println(criteria);

					}

					ItemCategory category = new ItemCategory(
						identifierCat, criterias
					);

					categories.add(category);

				}

				String id = doc.getObjectId("_id").toString();
				Item item = new Item(id, categories);

				listItems.add(item);

			}
		});

		return listItems;

	}

	/**
	 * Register an user.
	 *
	 * @param      username  The username
	 * @param      password  The password
	 *
	 * @return     The ObjectID of the user and his role
	 */
	public static Pair<String,Boolean> registerUser(String username, String password) {
		MongoCollection<Document> users = md.getCollection("users");
		Document userAlreadyExists = users.find(eq("username", username)).first();
		
		// check already exist
		if (userAlreadyExists != null) {
			// handle username already exists
			ErrorHandler.getInstance().push("registerUser", true, "The username is already taken by another user", "");
			return null;
		}
			
		Document user = new Document();
		user.append("username", username.trim());
		user.append("password", "");
		user.append("isAdmin", false);
		user.append("notifications", new ArrayList<Document>());
		users.insertOne(user);
			
		ObjectId id = (ObjectId)user.get("_id");
		String passwordHash = "";
			
		try {

			passwordHash = PasswordStorage.createHash(password + id.toString());

		}
		catch(Exception ex) {
	
			// delete the user from the database
			users.deleteOne(eq("_id", id));
			ErrorHandler.getInstance().push("registerUser", true, "An error occurred while processing the registration. Please retry.", "");
			return null;

		}
		
		// add hashed password
		users.updateOne(eq("_id", id), set("password", passwordHash));

		return new Pair(id.toString(), user.getBoolean("isAdmin"));
	}

	/**
	 * Authentificate an user with his username and password.
	 *
	 * @param      username  The username
	 * @param      password  The password
	 *
	 * @return     The ObjectID of the user and his role
	 */
	public static Pair<String,Boolean> authUser(String username, String password) {

		MongoCollection<Document> users = md.getCollection("users");
		Document user = users.find(eq("username", username)).first();
		
		// no user with that username
		if (user == null) {
			ErrorHandler.getInstance().push("authUser", true, "The username or the password is not correct", "The username does not exist");
			return null;
		}
		
		ObjectId id = (ObjectId)user.get("_id");
		String correctHash = user.getString("password");
		Boolean isPasswordValid = null;

		try {
			
			isPasswordValid = PasswordStorage.verifyPassword(password + id.toString(), correctHash);
		
		}
		catch(Exception ex) {
			
			// bad password
			ErrorHandler.getInstance().push("authUser", true, "Error while hashing the password", "");
			return null;

		}

		if (!isPasswordValid) {
			ErrorHandler.getInstance().push("authUser", true, "The username or the password is not correct", "The password is not correct");
			return null;
		}

		// @todo: check pending notifications
		return new Pair(id.toString(), user.getBoolean("isAdmin"));

	}

}