package commands.withMessage;

import commands.AbstractCommandWhichRequiresCollection;
import data.ClientRequest;
import data.MessageFromServerToClient;
import data.MusicBand;
import utils.MessagesForClient;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Command 'print_field_ascending_description'. Prints all descriptions in ascending alphabetical order
 */

public class PrintFieldAscendingDescription extends AbstractCommandWhichRequiresCollection {

    public PrintFieldAscendingDescription(LinkedBlockingQueue<MusicBand> collection, ClientRequest request) {
        super(collection, request);
    }


    public void invoke(){
        try {
            long counter = getCollection().stream().
                    map(MusicBand::getDescription).
                    filter(description->description!=null && description.length()!=0).
                    sorted().
                    peek(word->getClientRequest().addWords(new String[]{word})).
                    count();
            //getMessages().recordMessage(counter + " descriptions in total");
            //getClientRequest().getWords().add(Long.toString(counter));
            getClientRequest().setMessage(MessageFromServerToClient.PRINT_LIST);
        }catch (NullPointerException e){ }
    }

}
