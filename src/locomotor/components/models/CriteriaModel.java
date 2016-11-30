package locomotor.components.models;

import locomotor.components.types.*;

/**
 * Holds useful and reusable data (model) of a criteria.
 */
public class CriteriaModel {

	/**
	 * The identifier.
	 */
	private int _id;
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
	private boolean _isComparable;

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
	public CriteriaModel(int id, String name, String question, boolean isComparable, CEnumUniverseType universeType, CEnumVehicleType vehicleType, CEnumUserType userType,
CUniverseType universe) {
		_id = id;
		_name = name;
		_question = question;
		_isComparable = isComparable;
		_universeType = universeType;
		_vehicleType = vehicleType;
		_userType = userType;
		_universe = universe;
	}
	
}