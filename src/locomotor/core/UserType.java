package locomotor.core;

/**
 * The UserType enumeration
 */
public enum UserType {
    
    // integer value
    INTEGER(),
    // float value
    FLOAT(),
    // integer interval 
    INTEGER_INTERVAL(),
    // float interval
    FLOAT_INTERVAL(),
    // string of characters
    STRING(),
    // list of string of characters
    LIST(),
    // boolean value
    BOOLEAN(),
    // tree, each node or leaf is a string of characters
    TREE();

    /**
     * Constructs the object (private to prevent other to instantiate new UserType)
    */
    private UserType() {}

}