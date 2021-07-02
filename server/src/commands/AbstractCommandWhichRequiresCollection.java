package commands;

import data.ClientRequest;
import data.MessageFromServerToClient;
import data.MusicBand;
import java.util.AbstractCollection;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractCommandWhichRequiresCollection {

    private LinkedBlockingQueue<MusicBand> collection;
    private ClientRequest request;

    public AbstractCommandWhichRequiresCollection(LinkedBlockingQueue<MusicBand> collection, ClientRequest request) {
        this.collection = collection;
        this.request = request;
    }

    public AbstractCollection<MusicBand> getCollection() {
        return collection;
    }

    public void setCollection(LinkedBlockingQueue<MusicBand> collection) {
        this.collection = collection;
    }

    public ClientRequest getClientRequest() {
        return request;
    }
}
