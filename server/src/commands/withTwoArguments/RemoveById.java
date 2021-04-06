package commands.withTwoArguments;


import data.MusicBand;
import utils.MessagesForClient;

import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Command 'remove_by_id'. Removes an element by id from the collection
 */

public class RemoveById {

    /**
     * Executes the command.
     * @param collection - old collection
     * @param id - specific id
     * @return updated collection
     */

    public Stack<MusicBand> invoke(Stack<MusicBand> collection, long id){

        int initialLength = 0;
        try{
            initialLength = collection.size();
            collection = collection.
                    stream().
                    filter(band->band.getId()!=id).
                    collect(Collectors.toCollection(Stack<MusicBand>::new));
        }catch (NullPointerException e){
            warnIdDoesNotExist();
            return collection;
        }

        if (initialLength==collection.size()){
            warnIdDoesNotExist();
        }
        return collection;
    }
    private void warnIdDoesNotExist(){
        MessagesForClient.recordMessage("This id does not exist");
    }
}
