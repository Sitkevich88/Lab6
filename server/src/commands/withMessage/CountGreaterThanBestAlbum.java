package commands.withMessage;


import commands.AbstractCommandWhichRequiresCollection;
import data.ClientRequest;
import data.MessageFromServerToClient;
import data.MusicBand;
import utils.MessagesForClient;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Command 'count_greater_than_best_album'. Counts the amount of albums which are further down the alphabet by title than albumName.
 */

public class CountGreaterThanBestAlbum extends AbstractCommandWhichRequiresCollection {

    public CountGreaterThanBestAlbum(LinkedBlockingQueue<MusicBand> collection, ClientRequest request) {
        super(collection, request);
    }

    /**
     * Executes the command.
     * @param albumName - the name of an album we compare albums in collection with
     */


    public void invoke(String albumName){

        int counter = 0;

        if (getCollection()!=null){
            counter = (int)getCollection().
                    stream().
                    filter(band->band.getBestAlbum().getName().compareToIgnoreCase(albumName)>0).
                    //peek(p-> getMessages().recordMessage(p.getBestAlbum().getName())).
                    count();
        }
        getClientRequest().setMessage(MessageFromServerToClient.PRINT_LIST);
        getClientRequest().addWords(new String[]{Integer.toString(counter)});
        //getMessages().recordMessage(counter + " albums are greater in the whole collection");
    }
}
