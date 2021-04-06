package data;


import java.io.Serializable;
import java.time.ZonedDateTime;

public class ProtoMusicBand implements Serializable {


    /**
     * The field cannot be null, String cannot be empty
     */
    private String name;
    /**
     * The field cannot be null
     */
    private Coordinates coordinates;
    /**
     * The field can be null, The field value must be greater than 0
     */
    private Integer numberOfParticipants;
    /**
     * The field can be null
     */
    private String description;
    /**
     * The field cannot be null
     */
    private ZonedDateTime establishmentDate;
    /**
     * The field cannot be null
     */
    private MusicGenre genre;
    /**
     * The field cannot be null
     */
    private Album bestAlbum;


    public ProtoMusicBand(String name, Coordinates coordinates, Integer numberOfParticipants, String description, ZonedDateTime establishmentDate, MusicGenre genre, Album bestAlbum){
        this.name = name;
        this.coordinates = coordinates;
        this.numberOfParticipants = numberOfParticipants;
        this.description = description;
        this.establishmentDate = establishmentDate;
        this.genre = genre;
        this.bestAlbum = bestAlbum;
    }


    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public String getDescription() {
        return description;
    }

    public ZonedDateTime getEstablishmentDate() {
        return establishmentDate;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public Album getBestAlbum() {
        return bestAlbum;
    }

}
