package locomotor.core;

/**
 * The UniverseType enumeration
 */
public enum UniverseType {
    
    // integer interval 
    INTEGER_INTERVAL(),
    // float interval
    FLOAT_INTERVAL(),
    // list of string of characters
    LIST(),
    // boolean value, indicate if the field is empty or not
    BOOLEAN(),
    // tree
    TREE();

    /**
     * Constructs the object (private to prevent other to instantiate new UniverseType)
    */
    private UniverseType() {}
    
}