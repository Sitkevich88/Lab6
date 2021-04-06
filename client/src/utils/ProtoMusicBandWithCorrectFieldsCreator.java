package utils;

import data.Album;
import data.Coordinates;
import data.MusicGenre;
import data.ProtoMusicBand;

import java.time.ZonedDateTime;

public class ProtoMusicBandWithCorrectFieldsCreator {

    private String name;
    private Coordinates coordinates;
    private Integer numberOfParticipants;
    private String description;
    private ZonedDateTime establishmentDate;
    private MusicGenre genre;
    private Album album;
    private InputChecker checker = new InputChecker();


    public ProtoMusicBand createProtoMusicBand(){
        name = checker.nextLine("Insert the name: ", false, false);
        coordinates = createCoordinates();
        numberOfParticipants = checker.nextInt("Insert the number of participants : ", true, true);
        description = checker.nextLine("Insert the description : ", true, true);
        establishmentDate = checker.nextZonedDateTime("Insert the establishment date : ");
        genre = checker.nextMusicGenre("Choose the music genre : ");
        album = createAlbum();
        return new ProtoMusicBand(name, coordinates, numberOfParticipants, description, establishmentDate, genre, album);
    }

    private Coordinates createCoordinates(){
        long x = checker.nextLong("Insert the coordinate x: ");
        Integer y = checker.nextInt("Insert the coordinate y: ", false,false);
        return new Coordinates(x,y);
    }

    private Album createAlbum(){
        String albumName = checker.nextLine("Insert the name of their best album : ", false, false);
        int tracks = checker.nextInt("Insert the number of tracks in their best album : ", false, true);
        int length = checker.nextInt("Insert the length of their best album : ", false, true);
        Float sales = checker.nextFloat("Insert the sales of their best album : ", true,true);
        return new Album(albumName, tracks, length, sales);
    }
}
