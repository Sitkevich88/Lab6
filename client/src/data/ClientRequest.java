package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientRequest implements Serializable {

    public static final long serialVersionUID = 2000L;

    private String author;

    private MessageFromServerToClient message;
    private ArrayList<MusicBand> bands;
    private ArrayList<String> words = new ArrayList<>();

    public ClientRequest(){}

    public ClientRequest(MessageFromServerToClient message, ArrayList<MusicBand> bands){
        this.message = message;
        this.bands = bands;
    }

    public ArrayList<MusicBand> getBands() {
        return bands;
    }

    public void setBands(ArrayList<MusicBand> bands) {
        this.bands = bands;
    }

    public MessageFromServerToClient getMessage() {
        return message;
    }

    public void setMessage(MessageFromServerToClient message) {
        this.message = message;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void addWords(String[] newWords) {
        words.addAll(Arrays.asList(newWords));
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
