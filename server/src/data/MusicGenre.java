package data;

import utils.MessagesForClient;

import java.io.Serializable;
import java.util.Arrays;


public enum MusicGenre implements Serializable {
    ROCK ("rock"),
    PSYCHEDELIC_ROCK ("psychedelic_rock"),
    RAP ("rap"),
    HIP_HOP("hip-hop"),
    POP ("pop");

    private final String title;

    MusicGenre(String title) {
        this.title = title.toLowerCase();
    }

    public String getTitle() {
        return title;
    }

    /**
     * Prints all enum values
     */
    public static void printAllValues(MessagesForClient messages){

        //MessagesForClient.recordMessage("\u001B[34m");
        for (MusicGenre genre : MusicGenre.values()){

            messages.recordMessage(  "\u001B[34m" + genre.getTitle() );
        }
        messages.appendLastMessage("\u001B[0m");
    }

    /**
     *
     * @param value - enum lower case title
     * @return - enum value
     */
    public static MusicGenre getEnum(String value) {

        MusicGenre genre = Arrays.stream(MusicGenre.values()).
                filter(m -> m.title.equals(value.toLowerCase())).
                findAny().orElse(null);
        if (genre==null){
            throw new IllegalArgumentException();
        }
        return genre;
    }


    @Override
    public String toString() {
        return this.getTitle();
    }
}