package locomotor.components.types;

/**
 * @todo describe the class
 */
public class CIntervalInteger extends CInterval {

    /**
     * Initializes a closed interval [min, max].
     *
     * @param  min the smaller endpoint (64-bit)
     * @param  max the larger endpoint (64-bit)
     */
    public CIntervalInteger(Long min, Long max) {
        super(min, max);
    }

}
