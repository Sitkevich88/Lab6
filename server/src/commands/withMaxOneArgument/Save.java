package commands.withMaxOneArgument;

import utils.CollectionSaver;

import java.util.Collection;

/**
 * Command 'Save'. Saves collection to the file it was loaded initially from
 */

public class Save {

    /**
     *
     * @param collection - collection to be saved
     * @param collectionSaver - tool for saving the collection
     */

    public Save(Collection<?> collection, CollectionSaver collectionSaver) {
        collectionSaver.saveCollectionToFile(collection);
    }
}
