package locomotor.components.models;

import locomotor.components.types.CEnumUniverseType;
import locomotor.components.types.CEnumUserType;
import locomotor.components.types.CEnumVehicleType;
import locomotor.components.types.CUniverseType;

/**
 * Holds useful and reusable data (model) of a criteria.
 */
public class CriteriaModel {

	/**
	 * The identifier.
	 */
	private String _identifier;
	/**
	 * The name of the criteria.
	 */
	private String _name;

	/**
	 * The question to display to the user.
	 */
	private String _question;
	
	/**
	 * Wether the criteria can be used for computing or not.
	 */
	private Boolean _isComparable;

	/**
	 * The type of the structure that represents the universe.
	 */
	private CEnumUniverseType _universeType;

	/**
	 * The type of the structure that represents the vehicle's criteria value.
	 */
	private CEnumVehicleType _vehicleType;

	/**
	 * The type of the structure that represents what the user will have to submit.
	 */
	private CEnumUserType _userType;

	/**
	 * The universe of the values accepted for this criteria.
	 */
	private CUniverseType _universe;

	/**
	 * Constructs the criteria.
	 *
	 * @param      identifier    The identifier
	 * @param      name          The name
	 * @param      question      The question
	 * @param      isComparable  Indicates if comparable
	 * @param      universeType  The universe type
	 * @param      vehicleType   The vehicle type
	 * @param      userType      The user type
	 * @param      universe      The universe
	 */
	public CriteriaModel(String identifier, String name, String question, Boolean isComparable, 
			CEnumUniverseType universeType, CEnumVehicleType vehicleType, 
			CEnumUserType userType, CUniverseType universe) {
		_identifier = identifier;
		_name = name;
		_question = question;
		_isComparable = isComparable;
		_universeType = universeType;
		_vehicleType = vehicleType;
		_userType = userType;
		_universe = universe;
	}

	/**
	 * Outputs a human readable representation of the model.
	 *
	 * @return    The representation of the model.
	 */
	public String getID() {
		return _identifier;
	}

	public CEnumVehicleType getVehicleType() {
		return _vehicleType;
	}

	public CUniverseType getUniverse() {
		return _universe;
	}

	/**
	 * Outputs a human readable representation of the model.
	 *
	 * @return    The representation of the model.
	 */
	public String toString() {
		String line = _identifier + " - " + _name + " - " + "Universe(" + _universeType
			+ ") - " + "Vehicle(" + _vehicleType + ") - " + "User(" + _userType + ")\n";
		line += _universe.toString() + "\n";
		return line;
	}	
}