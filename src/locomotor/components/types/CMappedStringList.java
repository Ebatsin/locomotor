package locomotor.components.types;

import java.util.TreeMap;
import java.util.Set;

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
	 * @param      user      The user string list
	 * @param      universe  The universe string list (containg the graph)
	 *
	 * @return     1.0 (best match), tend toward 0.0 otherwise
	 */
	public double compare(CMappedStringList user, CGraphStringList universe) {
		Set<Integer> userKey = user._values.keySet();
		Set<Integer> itemKey = this._values.keySet();

		boolean isUserSubsetOfItem = itemKey.containsAll(userKey);

		// case 1: user is included or egal to item
		if (itemKey.equals(userKey) || isUserSubsetOfItem) {
			return 1.0;
		}
		
		boolean isItemSubsetOfUser = userKey.containsAll(itemKey);

		// case 2: item is included in user
		if (isItemSubsetOfUser) {
			return itemKey.size()/userKey.size();
		}

		// case 3: item intersect or not user
		double diameter = universe.getDiameter();
		int numberOfPaths = userKey.size()*itemKey.size();
		double distance;
		double distanceBis, sumOfDistance = 0.0;

		for (Integer ui : userKey) {
			for (Integer ij : itemKey) {
				distance = universe.distance(ui, ij);
				distanceBis = Math.max(0, ((diameter-distance)/diameter));
				sumOfDistance += distanceBis;
			}
		}

		// avoid divide by zero
		return (sumOfDistance == 0.0) ? sumOfDistance : sumOfDistance/numberOfPaths;
	}
	
}