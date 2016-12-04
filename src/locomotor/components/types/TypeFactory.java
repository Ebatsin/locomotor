package locomotor.components.types;

import java.util.ArrayList;
import java.util.TreeMap;
import org.bson.Document;

public class TypeFactory {

	public CUniverseType getUniverse(CEnumUniverseType type, Object o) {
		switch(type) {
			case BOOLEAN: {
					return getBoolean(o);
				}
			case INTEGER_INTERVAL:
			case FLOAT_INTERVAL: {
					return getInterval(type, o);
			}		
			case STRING_INTERVAL: {
				return getStringInterval(o);
			}
			case WEIGHTED_STRING_LIST: {
				return getWeightedStringList(o);
			}
			case TREE: {
					// todo
					return getTree(o);
			}
			default: {
				// error
		        System.err.println("Error: Illegal universe type");
		        System.err.println(type);
		        System.err.println(o);
		   		System.exit(0);
			}
		}
		return null;
	}

	private CBoolean getBoolean(Object o) {
		Boolean testvar = null;
		return new CBoolean(testvar);
	}

	private CInterval getInterval(CEnumUniverseType type, Object o) {
		Document universe = (Document)(new Document("universe", o)).get("universe");
		CInterval ci;
		// integer
		if(type == CEnumUniverseType.INTEGER_INTERVAL) {
			ci = new CIntervalInteger(universe.getLong("min"), universe.getLong("max"));
		// float
		} else {
			ci = new CIntervalDouble(universe.getDouble("min"), universe.getDouble("max"));
		}
		return ci;
	}

	private CMappedStringList getStringInterval(Object o) {
		ArrayList<Document> values = (ArrayList<Document>)o;
		TreeMap<Long, String> map = new TreeMap<Long, String>();
		for (Document value : values) {
			map.put(new Long(value.getInteger("id")), value.getString("name"));
		}
		return new CMappedStringList(map);
	}

	private CWeightedStringList getWeightedStringList(Object o) {
		Document universe = (Document)(new Document("universe", o)).get("universe");
		ArrayList<Document> values = (ArrayList<Document>)universe.get("values");
		TreeMap<Long, String> map = new TreeMap<Long, String>();
		for (Document value : values) {
			map.put(new Long(value.getInteger("value")), value.getString("name"));
		}
		return new CWeightedStringList(universe.getLong("min"), universe.getLong("max"), map);
	}

	private CTree<Long,String> getTree(Object o) {
		Document universe = (Document)(new Document("universe", o)).get("universe");
		CTree root = new CTree(new Long(universe.getInteger("id")), universe.getString("value"));
		ArrayList<Object> children = (ArrayList<Object>)universe.get("children");
		if (children != null) {
			for (Object child : children) {
				root.addChild(getTree(child));
			}
		}
		return root;
	}
}