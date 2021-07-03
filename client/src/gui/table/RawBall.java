package gui.table;

import data.MusicBand;
import javafx.scene.paint.Color;

public class RawBall {

    private long id;
    private String owner;
    private Color colour;
    private int x;
    private int y;

    public RawBall(MusicBand band){
        id = band.getId();
        owner = band.getOwner();
        colour = ColourGetter.getColour(owner);
        x = (int) band.getCoordinates().getX();
        y = band.getCoordinates().getY();
    }

    public long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public Color getColour() {
        return colour;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
