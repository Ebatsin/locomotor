package locomotor.components.types;

public class CIntervalInteger extends CInterval {

    /**
     * Initializes a closed interval [min, max].
     *
     * @param  min the smaller endpoint
     * @param  max the larger endpoint
     */
    public CIntervalInteger(Integer min, Integer max) {
        super(min, max);
    }

}
