package utils;

import data.MusicBand;

import java.util.Collection;
import java.util.Date;

/**
 * Collection Analyzer. Deals with last initialization date and max id search
 */

public class CollectionAnalyzer {

    private Collection<MusicBand> collection;

    /**
     * Loads collection to this class
     * @param collection - collection to be examined
     */
    public CollectionAnalyzer(Collection<MusicBand> collection){
        this.collection = collection;
    }

    /**
     * Finds max id in the collection
     * @return long max id
     */
    public long findMaxId(){
        if (collection==null||collection.size()==0){return 0;}
        return collection.stream().
                mapToLong(MusicBand::getId).
                max().
                getAsLong();
    }

    /**
     * Initialization tracker. Traces last initialization date
     */

    public static class InitializationTracker {

        private static Date lastInit;

        /**
         * Updates last initialization date
         */
        public static void updateLastInit() {
            lastInit = new Date();
        }

        /**
         * Gets last initialization date
         * @return Date last initialization date
         */
        public static Date getLastInit() {
            return lastInit;
        }
    }


}
