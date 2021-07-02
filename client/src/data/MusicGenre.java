package data;

import java.io.Serializable;
import java.util.Arrays;


public enum MusicGenre implements Serializable {
    ROCK ("rock"),
    PSYCHEDELIC_ROCK ("psychedelic_rock"),
    RAP ("rap"),
    HIP_HOP("hip-hop"),
    POP ("pop");

    private final String title;

    public static final long serialVersionUID = 5000L;

    MusicGenre(String title) {
        this.title = title.toLowerCase();
    }

    public String getTitle() {
        return title;
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