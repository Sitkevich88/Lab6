package utils;

/**
 * Generator id. It stores max existing id and generates new id which is by one greater than max existing id, after that
 * max existing id increases by one
 */

public class GeneratorId {


    private static long maxExistingId = 0;

    /**
     * Sets max eisting id
     * @param maxExistingId long max existing band id
     */
    public static void setMaxExistingId(long maxExistingId) {
        GeneratorId.maxExistingId = maxExistingId;
    }

    /**
     * Generates new id, increments max existing id
     * @return long new id
     */
    public long generate(){
        return ++maxExistingId;
    }


}
