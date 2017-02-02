package locomotor.components.types;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import java.util.Set;
import java.util.TreeMap;

/**
 * Encapsulate a map between an Integer (32-bit) and the String associated.
 */
public class CMappedStringList extends CStringList implements CComparable<CMappedStringList, CGraphStringList> {

	/**
	 * Constructs the object.
	 *
	 * @param      values  The values
	 */
	public CMappedStringList(TreeMap<Integer, String> values) {
		super(values);
	}

	/**
	 * Compare the string list of the vehicle with the string list of the user
	 *
	 * @param      user                The user string list
	 * @param      universe            The universe string list (containg the graph)
	 * @param      disableFlexibility  Disable the flexibility
	 *
	 * @return     1.0 (match), 0.0 otherwise or -1.0 if does not match perfectly (flexibility disable)
	 */
	public double compare(CMappedStringList user, CGraphStringList universe, boolean disableFlexibility) {
		Set<Integer> userKey = user._values.keySet();
		Set<Integer> itemKey = this._values.keySet();

		return universe.compare(userKey, itemKey, disableFlexibility);

	}

	/**
	 * Factory from representation JSON.
	 *
	 * @param      json  The json
	 *
	 * @return     A new CMappedStringList object.
	 */
	public static CMappedStringList fromJSON(JsonValue json) {
		JsonArray values = json.asArray();
		
		TreeMap<Integer, String> valuesTree = treeFromJSON(values);

		return new CMappedStringList(valuesTree);
	}
	
}