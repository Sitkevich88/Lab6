package data;


import utils.GeneratorId;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

public class MusicBand implements Comparable<MusicBand>, Serializable {

    /**
     * The field value must be greater than 0, The field value must be unique, The field value is generated automatically
     */
    private long id;
    /**
     * The field cannot be null, String cannot be empty
     */
    private String name;
    /**
     * The field cannot be null
     */
    private Coordinates coordinates;
    /**
     * The field cannot be null, The field value is generated automatically
     */
    private Date creationDate;
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


    public MusicBand(String name, Coordinates coordinates,
                     Integer numberOfParticipants, String description,
                     ZonedDateTime establishmentDate, MusicGenre genre, Album bestAlbum) {
        //super(name, coordinates, numberOfParticipants, description, establishmentDate, genre, bestAlbum);
        GeneratorId generatorId = new GeneratorId();
        this.id = generatorId.generate();
        this.name = name;
        this.coordinates = coordinates;
        creationDate = new Date();
        this.numberOfParticipants = numberOfParticipants;
        this.description = description;
        this.establishmentDate = establishmentDate;
        this.genre = genre;
        this.bestAlbum = bestAlbum;
    }

    public MusicBand(ProtoMusicBand pBand){
        this(pBand.getName(), pBand.getCoordinates(),
                pBand.getNumberOfParticipants(), pBand.getDescription(),
                pBand.getEstablishmentDate(), pBand.getGenre(), pBand.getBestAlbum());
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEstablishmentDate(ZonedDateTime establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public void setBestAlbum(Album bestAlbum) {
        this.bestAlbum = bestAlbum;
    }

    public int compareTo(MusicBand musicBand){
        return name.compareTo(musicBand.getName());
    }

    @Override
    public String toString() {
        /*return "\n\tMusic band{" +
                "\n\t\tid = " + id +
                ",\n\t\tname = " + name +
                ",\n\t\tcoordinates = " + coordinates +
                ",\n\t\tcreation date = " + creationDate +
                ",\n\t\tnumber of participants = " + numberOfParticipants +
                ",\n\t\tdescription = " + description +
                ",\n\t\testablishment date = " + establishmentDate +
                ",\n\t\tgenre = " + genre +
                ",\n\t\tbest album = " + bestAlbum +
                "\n\t}\n";*/
        return "\n\t" + name +" - music band{" +
                "\n\t\tid = " + id +
                ",\n\t\tcoordinates = " + coordinates +
                ",\n\t\tcreation date = " + creationDate +
                ",\n\t\tnumber of participants = " + numberOfParticipants +
                ",\n\t\tdescription = " + description +
                ",\n\t\testablishment date = " + establishmentDate +
                ",\n\t\tgenre = " + genre +
                ",\n\t\tbest album = " + bestAlbum +
                "\n\t}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicBand musicBand = (MusicBand) o;
        return id == musicBand.id &&
                Objects.equals(name, musicBand.name) &&
                Objects.equals(coordinates, musicBand.coordinates) &&
                Objects.equals(creationDate, musicBand.creationDate) &&
                Objects.equals(numberOfParticipants, musicBand.numberOfParticipants) &&
                Objects.equals(description, musicBand.description) &&
                Objects.equals(establishmentDate, musicBand.establishmentDate) &&
                genre == musicBand.genre &&
                Objects.equals(bestAlbum, musicBand.bestAlbum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, numberOfParticipants, description, establishmentDate, genre, bestAlbum);
    }
}