package locomotor.components.models;

import locomotor.components.types.*;

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
	 * Can the criteria be use for computing?
	 */
	private Boolean _isComparable;

	/**
	 * @todo
	 */
	private CEnumUniverseType _universeType;

	/**
	 * @@todo
	 */
	private CEnumVehicleType _vehicleType;

	/**
	 * @todo
	 */
	private CEnumUserType _userType;

	/**
	 * @todo
	 */
	private CUniverseType _universe;

	/**
	 * Constructs the criteria
	 *
	 * @param      id            The identifier
	 * @param      name          The name
	 * @param      question      The question
	 * @param      isComparable  Indicates if comparable
	 * @param      universeType  The universe type
	 * @param      vehicleType   The vehicle type
	 * @param      userType      The user type
	 * @param      universe      The universe
	 */
	public CriteriaModel(String identifier, String name, String question, Boolean isComparable, CEnumUniverseType universeType, CEnumVehicleType vehicleType, CEnumUserType userType,
CUniverseType universe) {
		_identifier = identifier;
		_name = name;
		_question = question;
		_isComparable = isComparable;
		_universeType = universeType;
		_vehicleType = vehicleType;
		_userType = userType;
		_universe = universe;
	}

	public String toString() {
		String line = _identifier + " - " + _name + " - " + "Universe(" + _universeType + ") - " + "Vehicle(" +_vehicleType + ") - " + "User(" + _userType + ")\n";
		line += _universe.toString() + "\n";
		return line;
	}
	
}