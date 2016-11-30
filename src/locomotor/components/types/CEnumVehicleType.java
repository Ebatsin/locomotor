package locomotor.components.types;

public enum CEnumVehicleType {
    
    // integer
    INTEGER(0),
    // float
    FLOAT(1),
    // boolean
    BOOLEAN(2),
    // integer interval 
    INTEGER_INTERVAL(3),
    // float interval
    FLOAT_INTERVAL(4),
    // string interval
    STRING_INTERVAL(5),
    // tree
    TREE(7);

    // the id (integer value)
    private int _id;

    /**
     * Constructs the object (private to prevent other to instantiate new UniverseType)
    */
    private CEnumVehicleType(int id) {
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