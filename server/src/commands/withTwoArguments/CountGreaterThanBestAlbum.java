package commands.withTwoArguments;


import data.MusicBand;
import utils.MessagesForClient;

import java.util.Collection;

/**
 * Command 'count_greater_than_best_album'. Counts the amount of albums which are further down the alphabet by title than albumName.
 */

public class CountGreaterThanBestAlbum {

    /**
     * Executes the command.
     * @param collection - collection to be examined
     * @param albumName - the name of an album we compare albums in collection with
     */


    public void invoke(Collection<MusicBand> collection, String albumName){

        int counter = 0;

        if (collection!=null){
            counter = (int)collection.
                    stream().
                    filter(band->band.getBestAlbum().getName().compareToIgnoreCase(albumName)>0).
                    peek(p-> MessagesForClient.recordMessage(p.getBestAlbum().getName())).
                    count();
        }
        MessagesForClient.recordMessage(counter + " albums are greater");
    }
}
