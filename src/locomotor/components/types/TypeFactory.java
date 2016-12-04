package locomotor.components.types;

import java.util.ArrayList;
import java.util.TreeMap;

import org.bson.Document;

/**
 * @todo Describe the class.
 */
public class TypeFactory {
	/**
	 * @todo Describe the method.
	 */
	public CUniverseType getUniverse(CEnumUniverseType type, Object object) {
		switch(type) {
			case BOOLEAN: {
				Boolean testvar = null;
				return new CBoolean(testvar);
			}
			case INTEGER_INTERVAL: {
				Document universe = (Document)(new Document("universe", object)).get("universe");
				return new CIntervalInteger(universe.getLong("min"), universe.getLong("max"));
			}

			case FLOAT_INTERVAL: {
				Document universe = (Document)(new Document("universe", object)).get("universe");
				return new CIntervalDouble(universe.getDouble("min"), universe.getDouble("max"));
			}		
			case STRING_INTERVAL: {
				ArrayList<Document> values = (ArrayList<Document>)object;
				TreeMap<Integer, String> map = new TreeMap<Integer, String>();
				for(Document value : values) {
					map.put(value.getInteger("id"), value.getString("name"));
				}
				return new CMappedStringList(map);
			}
			case WEIGHTED_STRING_LIST: {
				Document universe = (Document)(new Document("universe", object)).get("universe");
				ArrayList<Document> values = (ArrayList<Document>)universe.get("values");
				TreeMap<Integer, String> map = new TreeMap<Integer, String>();
				for(Document value : values) {
					map.put(value.getInteger("value"), value.getString("name"));
				}
				return new CWeightedStringList(universe.getLong("min"), universe.getLong("max"), map);
			}
			case TREE: {
				return getTree(object);
			}
			default: { // error
				System.err.println("Error: Illegal universe type");
				System.err.println(type);
				System.err.println(object);
				System.exit(0);
			}
		}
		return null;
	}

	/**
	 * @todo Describe the method.
	 */
	public CVehicleType getVehicle(CEnumVehicleType type, Object object, CUniverseType universe) {
		switch(type) {
			case INTEGER: {
				Document value = (Document)(new Document("value", object));
				return new CInteger(value.getLong("value"));
			}
			case FLOAT: {
				Document value = (Document)(new Document("value", object));
				return new CFloat(value.getDouble("value"));
			}
			case BOOLEAN: {
				Document value = (Document)(new Document("value", object));
				return new CBoolean(value.getBoolean("value"));
			}
			case INTEGER_INTERVAL: {
				Document value = (Document)(new Document("value", object)).get("value");
				return new CIntervalInteger(value.getLong("min"), value.getLong("max"));
			}
			case FLOAT_INTERVAL: {
				Document value = (Document)(new Document("value", object)).get("value");
				return new CIntervalDouble(value.getDouble("min"), value.getDouble("max"));
			}		
			case INTEGER_LIST: {
				ArrayList<Integer> values = (ArrayList<Integer>)object;
				TreeMap<Integer, String> map = new TreeMap<Integer, String>();
				
				CStringList listMod = (CStringList)universe;
				TreeMap<Integer, String> critMap = listMod.getMap();
				
				for(Integer value : values) {
					map.put(value, critMap.get(value));
				}
				return new CMappedStringList(map);
			}
			case INTEGER_TREE: {
				CTree treeMod = (CTree)universe;
				CTree tree = getTree(object, treeMod);
				return tree;
			}	
			default: { // error
				System.err.println("Error: Illegal vehicle type");
				System.err.println(type);
				System.err.println(object);
				System.exit(0);
			}
		}
		return null;
	}

	private CTree getTree(Object o) {
		Document universe = (Document)(new Document("universe", o)).get("universe");
		CTree root = new CTree(universe.getInteger("id"), universe.getString("value"));
		ArrayList<Object> children = (ArrayList<Object>)universe.get("children");
		if (children != null) {
			for (Object child : children) {
				root.addChild(getTree(child));
			}
		}
		return root;
	}

	private CTree getTree(Object o, CTree tree) {
		Document value = (Document)(new Document("value", o)).get("value");
		
		Integer id = value.getInteger("value");
		String name = tree.getData();	
		
		CTree root = new CTree(id, name);
		ArrayList<Object> children = (ArrayList<Object>)value.get("children");
		if (children != null) {
			ArrayList<CTree> childrenMod = tree.getChildren();
			for (Object child : children) {
				
				// find which childMod match the current child
				Integer idToFind = ((Document)(new Document("value", child)).get("value")).getInteger("value");
				
				for (CTree c : childrenMod) {

					if (c.getID().equals(idToFind)) {
						root.addChild(getTree(child, c));
						break;
					}
				}
				
			}
		}
		return root;
	}
}