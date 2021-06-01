package commands.withMaxOneArgument;

import commands.AbstractCommandWhichRequiresCollection;
import data.MusicBand;
import utils.MessagesForClient;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * Command 'sort'. Sorts collection by band name
 */

public class Sort extends AbstractCommandWhichRequiresCollection {

    public Sort(LinkedBlockingQueue<MusicBand> collection, MessagesForClient messages) {
        super(collection, messages);
    }


    public LinkedBlockingQueue invoke(){
        if (getCollection()==null){
            return new LinkedBlockingQueue();
        }
        //Collections.sort(getCollection());
        setCollection(getCollection().stream().
                sorted().
                collect(Collectors.toCollection(LinkedBlockingQueue::new)));
        getMessages().recordMessage("The whole collection has been sorted by name");
        return (LinkedBlockingQueue) getCollection();
    }

}
