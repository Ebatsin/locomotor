package locomotor.components.types;

import java.util.ArrayList;

/**
 * @todo describe the class
 */
public class CIntegerList implements CVehicleType {

	/**
	 * The list of integer value (64-bit)
	 */
	private ArrayList<Long> _value;

	/**
	 * Constructs the CIntegerList object
	 *
	 * @param      value  The value
	 */
	public CIntegerList(ArrayList<Long> value) {
		_value = value;
	}

}