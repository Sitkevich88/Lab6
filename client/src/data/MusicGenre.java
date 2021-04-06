package data;

import java.io.Serializable;
import java.util.Arrays;


public enum MusicGenre implements Serializable {
    ROCK ("rock"),
    PSYCHEDELIC_ROCK ("psychedelic rock"),
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
    public static void printAllValues(){
        System.out.print("\u001B[34m");
        for (MusicGenre genre : MusicGenre.values()){
            System.out.print(genre.getTitle() + '\n');
        }
        System.out.print("\u001B[0m");
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