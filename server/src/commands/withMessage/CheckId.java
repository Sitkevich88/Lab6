/*
package commands.withMessage;

import commands.AbstractCommandWhichRequiresCollection;
import data.MusicBand;
import utils.MessagesForClient;

import java.util.concurrent.LinkedBlockingQueue;

public class CheckId extends AbstractCommandWhichRequiresCollection {

    public CheckId(LinkedBlockingQueue<MusicBand> collection, MessagesForClient messages) {
        super(collection, messages);
    }

    public void invoke(String sender, long id){
        boolean found = getCollection().stream().
                filter(musicBand -> musicBand.getOwner().equals(sender)).
                mapToLong(MusicBand::getId).
                anyMatch(Long.valueOf(id)::equals);
        getMessages().setCheckResult(found);
        */
/*System.out.println(found);
        System.out.println("id found - " + getMessages().isCheckResult());
        System.out.println(id);*//*

        if (!found){
            getMessages().recordMessage("There is no such id in your collection");
        }
    }
}
*/
