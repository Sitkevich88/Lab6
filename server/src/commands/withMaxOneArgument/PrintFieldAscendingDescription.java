package commands.withMaxOneArgument;

import commands.AbstractCommandWhichRequiresCollection;
import data.MusicBand;
import utils.MessagesForClient;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Command 'print_field_ascending_description'. Prints all descriptions in ascending alphabetical order
 */

public class PrintFieldAscendingDescription extends AbstractCommandWhichRequiresCollection {

    public PrintFieldAscendingDescription(LinkedBlockingQueue<MusicBand> collection, MessagesForClient messages) {
        super(collection, messages);
    }


    public void invoke(){

        try {
            long counter = getCollection().stream().
                    map(MusicBand::getDescription).
                    filter(description->description!=null && description.length()!=0).
                    sorted().
                    peek(getMessages()::recordMessage).count();
            getMessages().recordMessage(counter + " descriptions in total");
        }catch (NullPointerException e){ }
    }

}
