package commands;

import data.MusicBand;
import utils.MessagesForClient;
import java.util.AbstractCollection;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractCommandWhichRequiresCollection {

    private LinkedBlockingQueue<MusicBand> collection;
    private MessagesForClient messages;

    public AbstractCommandWhichRequiresCollection(LinkedBlockingQueue<MusicBand> collection, MessagesForClient messages) {
        this.collection = collection;
        this.messages = messages;
    }

    public AbstractCollection<MusicBand> getCollection() {
        return collection;
    }

    public void setCollection(LinkedBlockingQueue<MusicBand> collection) {
        this.collection = collection;
    }

    public MessagesForClient getMessages() {
        return messages;
    }
}
