package locomotor.components.types;

/**
 * The UniverseType enumeration
 */
public enum UniverseType {
    
    // integer interval 
    INTEGER_INTERVAL(0),
    // float interval
    FLOAT_INTERVAL(1),
    // list of string of characters
    LIST(2),
    // boolean value, indicate if the field is empty or not
    BOOLEAN(3),
    // tree
    TREE(4);

    // the id (integer value)
    private int _id;

    /**
     * Constructs the object (private to prevent other to instantiate new UniverseType)
    */
    private UniverseType(int id) {
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