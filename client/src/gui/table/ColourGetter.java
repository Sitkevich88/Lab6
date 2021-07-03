package gui.table;


import data.MusicBand;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ColourGetter {

    private static HashMap<String, Color> hashMap = new HashMap<>();

    public static Color getColour(String owner){
        return hashMap.get(owner);
    }

    public static void updateColours(ArrayList<MusicBand> bands){
        for (MusicBand band : bands){
            if (!hashMap.containsKey(band.getOwner())){
                hashMap.put(band.getOwner(), getRandomColour());
            }
        }
    }

    private static Color getRandomColour(){
        Random random = new Random();
        double r = random.nextDouble();
        double g = random.nextDouble();
        double b = random.nextDouble();

        return new Color(r,g,b,1);
    }

}
