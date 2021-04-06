package commands.withMaxOneArgument;

import utils.MessagesForClient;

import java.util.Collection;

/**
 * Command 'show'. Prints all objects of the collection
 */

public class Show {
    /**
     * Executes the command.
     * @param collection - collection to be shown
     */
    public Show(Collection<?> collection){
        if (collection!=null && collection.size()!=0){
            MessagesForClient.recordMessage(collection.toString());
        }else {
            MessagesForClient.recordMessage("The collection is empty");
        }
    }
}
