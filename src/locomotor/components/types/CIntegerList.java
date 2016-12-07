package locomotor.components.types;

import java.util.ArrayList;

/**
 * Encapsulate a array of integer (32-bit) values.
 * @see CEnumItemType.
 */
public class CIntegerList implements CItemType {

	/**
	 * The list of integer value (32-bit).
	 */
	private ArrayList<Integer> _value;

	/**
	 * Constructs the CIntegerList object.
	 *
	 * @param      value  The value
	 */
	public CIntegerList(ArrayList<Integer> value) {
		_value = value;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		String str = "";
		for(Integer l : _value) {
			str += " " + l;
		}
		return str;
	}

}