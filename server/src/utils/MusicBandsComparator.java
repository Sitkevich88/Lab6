package utils;

import data.MusicBand;
import java.util.Comparator;

public class MusicBandsComparator implements Comparator<MusicBand> {

    @Override
    public int compare(MusicBand b1, MusicBand b2) {
        return Long.compare(b1.getId(), b2.getId());
    }
}
