package commands.withTwoArguments;

import data.MusicBand;
import data.ProtoMusicBand;
import utils.MessagesForClient;

import java.util.List;
import java.util.Stack;

/**
 * Command 'insert_at'. Inserts an element into the collection at the specific index
 */

public class InsertAt {

    /**
     * Executes the command.
     * @param collection - old collection
     * @param index - specific index
     * @return - Updated collection with newly inserted element
     */

    public Stack<MusicBand> invoke(Stack<MusicBand> collection, int index, ProtoMusicBand pBand){

        List<MusicBand> list = collection;

        try{

            if (index >= collection.size()||index<0){
                MessagesForClient.recordMessage("There is no such index in the collection");
                return collection;
            }

            MusicBand band = new MusicBand(pBand);

            list.add(index, band);

        }catch (IndexOutOfBoundsException e){
            MessagesForClient.recordMessage("There is no such index in the collection");
        }catch (NullPointerException e){
            MessagesForClient.recordMessage("This collection is empty");
        }
        return (Stack<MusicBand>) list;
    }
}
