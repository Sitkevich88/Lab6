/*
package commands.unknownUsage;

import commands.AbstractCommandWhichRequiresCollection;
import data.MusicBand;
import utils.MessagesForClient;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

*/
/**
 * Command 'show'. Prints all objects of the collection
 *//*


public class Show extends AbstractCommandWhichRequiresCollection {

    public Show(LinkedBlockingQueue<MusicBand> collection, MessagesForClient messages) {
        super(collection, messages);
    }

    public void invoke(String sender){
        if (getCollection()!=null && getCollection().size()!=0){
            ArrayList list = getCollection().stream().
                    filter(musicBand->musicBand.getOwner().equals(sender)).
                    collect(Collectors.toCollection(ArrayList<MusicBand>::new));
            getMessages().recordMessage(list.toString());
        }else {
            getMessages().recordMessage("The collection is empty");
        }
    }
}
*/
