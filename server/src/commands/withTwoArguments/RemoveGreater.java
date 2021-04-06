package commands.withTwoArguments;

import data.MusicBand;
import utils.MessagesForClient;

import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Command 'remove_greater'. Removes all greater elements in the collection
 */

public class RemoveGreater {

    /**
     * Executes the command.
     * @param collection - old collection
     * @param number - int specific index
     * @return updated collection
     */

    public Stack<MusicBand> invoke(Stack<MusicBand> collection, int number) throws IllegalArgumentException{

        try {
            int initialLength = collection.size();

            collection = collection.stream().
                    limit(number).
                    collect(Collectors.toCollection(Stack<MusicBand>::new));

            if (initialLength==collection.size()){
                throw new NullPointerException();
            }

        }catch (NullPointerException e){
            MessagesForClient.recordMessage("Nothing has been removed. This index is out of range.");
        }
        return collection;
    }
}
