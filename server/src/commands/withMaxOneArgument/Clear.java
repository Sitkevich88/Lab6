package commands.withMaxOneArgument;


import data.MusicBand;

import java.util.Stack;

/**
 * Command 'clear'. Clears the collection.
 */

public class Clear {

    /**
     *
     * Executes the command.
     * @param collection - old collection
     * @return empty collection.
     */

    public Stack<MusicBand> invoke(Stack<MusicBand> collection){

        if (collection!=null){
            collection.clear();
        }

        return collection;
    }
}
