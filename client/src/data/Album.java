package data;

import java.io.Serializable;
import java.util.Objects;

public class Album implements Comparable<Album>, Serializable {

    private String name;
    /**
     * The field cannot be null, String cannot be empty
     */
    private int tracks;
    /**
     * The field value must be greater than 0
     */
    private int length;
    /**
     * The field value must be greater than 0
     */
    private Float sales;
    /**
     * The field can be null, The field value must be greater than 0
     */

    public Album(String name, int tracks, int length, Float sales) {
        this.name = name;
        this.tracks = tracks;
        this.length = length;
        this.sales = sales;
    }

    /**
     *
     * @return String album name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return int number of tracks
     */
    public int getTracks() {
        return tracks;
    }

    /**
     *
     * @return int album length in minutes
     */
    public int getLength() {
        return length;
    }

    /**
     *
     * @return Float album sales in thousand dollars
     */
    public Float getSales() {
        return sales;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return tracks == album.tracks &&
                length == album.length &&
                Objects.equals(name, album.name) &&
                Objects.equals(sales, album.sales);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tracks, length, sales);
    }

    @Override
    public String toString() {
        return "data.Album{" +
                "name = " + name +
                ", tracks = " + tracks +
                ", length = " + length +
                ", sales = " + sales +
                '}';
    }


    @Override
    public int compareTo(Album album) {
        return name.compareTo(album.getName());
    }
}