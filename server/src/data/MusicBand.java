package data;


import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

public class MusicBand implements Comparable<MusicBand>, Serializable {

    public static final long serialVersionUID = 4000L;

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
     * This field cannot be null, The field value is generated automatically
     */
    private Date creationDate;
    /**
     * This field can be null, The value must be greater than 0
     */
    private Integer numberOfParticipants;
    /**
     * This field can be null
     */
    private String description;
    /**
     * This field cannot be null
     */
    private ZonedDateTime establishmentDate;
    /**
     * This field cannot be null
     */
    private MusicGenre genre;
    /**
     * This field cannot be null
     */
    private Album bestAlbum;

    private String owner;


    public MusicBand(String owner, long id, String name, Coordinates coordinates, Date creationDate,
                     Integer numberOfParticipants, String description,
                     ZonedDateTime establishmentDate, MusicGenre genre, Album bestAlbum) {
        this.owner = owner;
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.numberOfParticipants = numberOfParticipants;
        this.description = description;
        this.establishmentDate = establishmentDate;
        this.genre = genre;
        this.bestAlbum = bestAlbum;
    }

    public int compareTo(MusicBand musicBand){
        return name.compareTo(musicBand.getName());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(ZonedDateTime establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public Album getBestAlbum() {
        return bestAlbum;
    }

    public void setBestAlbum(Album bestAlbum) {
        this.bestAlbum = bestAlbum;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    @Override
    public String toString() {
        return "\n\t" + name +" - music band{" +
                "\n\t\towner = " + owner +
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
                Objects.equals(bestAlbum, musicBand.bestAlbum) &&
                Objects.equals(owner, musicBand.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, numberOfParticipants, description, establishmentDate, genre, bestAlbum, owner);
    }
}