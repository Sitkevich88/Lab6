package utils;

import data.Album;
import data.Coordinates;
import data.MusicGenre;
import data.ProtoMusicBand;

import java.time.ZonedDateTime;

public class ProtoMusicBandWithCorrectFieldsCreator {

    private String name;
    private Coordinates coordinates;
    private Integer numberOfParticipants = null;
    private String description = "";
    private ZonedDateTime establishmentDate;
    private MusicGenre genre;
    private Album album;



    public ProtoMusicBand createProtoMusicBand(String name, String x, String y, String numberOfParticipants1, String description,
                                               String establishmentDate, String genre,
                                               String albumName, String tracks, String length, String sales){
        this.name = name;
        this.coordinates = createCoordinates(x, y);
        if (numberOfParticipants1!=null && numberOfParticipants1.length()>0){
            this.numberOfParticipants = Integer.valueOf(numberOfParticipants1);
        }
        this.description = description;
        this.establishmentDate = ZonedDateTime.parse(establishmentDate);
        this.genre = MusicGenre.getEnum(genre);
        this.album = createAlbum(albumName, tracks, length, sales);
        return new ProtoMusicBand(this.name, this.coordinates, this.numberOfParticipants,
                this.description, this.establishmentDate, this.genre, this.album);
    }

    private Coordinates createCoordinates(String x1, String y1){
        long x = Long.valueOf(x1);
        Integer y = Integer.valueOf(y1);
        return new Coordinates(x,y);
    }

    private Album createAlbum(String albumName1, String tracks1, String length1, String sales1){
        String albumName = albumName1;
        int tracks = Integer.valueOf(tracks1);
        int length = Integer.valueOf(length1);
        Float sales = null;
        if (sales1!=null && sales1.length()>0){
            sales = Float.valueOf(sales1);
        }

        return new Album(albumName, tracks, length, sales);
    }
}
