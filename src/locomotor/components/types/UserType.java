package locomotor.components.types;

/**
 * The UserType enumeration
 */
public enum UserType {
    
    // integer value
    INTEGER(0),
    // float value
    FLOAT(1),
    // integer interval 
    INTEGER_INTERVAL(2),
    // float interval
    FLOAT_INTERVAL(3),
    // string of characters
    STRING(4),
    // list of string of characters
    LIST(5),
    // boolean value
    BOOLEAN(6),
    // tree, each node or leaf is a string of characters
    TREE(7);

    // the id (integer value)
    private int _id;

    /**
     * Constructs the object (private to prevent other to instantiate new UserType)
    */
    private UserType(int id) {
    	_id = id;
    }

    /**
     * Gets the id.
     *
     * @return     The id.
     */
    public int getID() {
    	return _id;
    }

}