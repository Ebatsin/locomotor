package locomotor.components.types;

public class CIntervalDouble extends CInterval {

    /**
     * Initializes a closed interval [min, max].
     *
     * @param  min the smaller endpoint
     * @param  max the larger endpoint
     */
    public CIntervalDouble(Double min, Double max) {
        super(min, max);
    }

}
