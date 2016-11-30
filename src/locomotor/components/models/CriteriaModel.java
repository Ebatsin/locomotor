package locomotor.components.models;

/**
 * @todo describe the class
 */
public class CriteriaModel {

	/**
	 * The identifier
	 */
	private int _id;
	/**
	 * The name of the category of criteria(s)
	 */
	private String _name;

	/**
	 * The question to ask
	 */
	private String _question;
	
	/**
	 * Can the criteria be use for computing
	 */
	private boolean _isComparable;

	/**
	 * The type of values that can take the criteria
	 */
	private UniverseType _typeUniverse;

	/**
	 * The values that can take the criteria
	 */
	private Type _universe;

	/**
	 * The actual type of values that take the criteria
	 */
	private UserType _typeUser;

	/**
	 * Constructs the category of criteria(s)
	 *
	 * @param      id            The identifier
	 * @param      name          The name
	 * @param      question      The question
	 * @param      isComparable  Indicates if comparable
	 * @param      typeUniverse  The type universe
	 * @param      universe      The universe
	 * @param      typeUser      The type user
	 */
	public CriteriaModel(int id, String name, String question, boolean isComparable, UniverseType typeUniverse, Type universe, UserType typeUser) {
		_id = id;
		_name = name;
		_question = question;
		_isComparable = isComparable;
		_typeUniverse = _typeUniverse
		_universe = universe;
		_typeUser = typeUser;
	}
	
}