package gui.common;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import java.util.*;

public class LanguagesSwitcher {

    private HashMap<String, Locale> languagesMap = new HashMap<>();

    public void loadLanguages(ComboBox<String> button, Locale locale){
        ArrayList<String> list = new ArrayList<>(Arrays.asList("Русский", "English(Canada)", "Română", "Lietuvos"));
        ObservableList<String> languages = FXCollections.observableArrayList(list);

        Locale russian = new Locale("ru");
        Locale english = new Locale("en", "CA");
        Locale romanian = new Locale("ro");
        Locale lithuanian = new Locale("lt");

        languagesMap.put(list.get(0), russian);
        languagesMap.put(list.get(1), english);
        languagesMap.put(list.get(2), romanian);
        languagesMap.put(list.get(3), lithuanian);

        button.setItems(languages);
        button.setValue(list.get(0));

        for (int i = 0; i<languagesMap.size(); i++){
            if (languagesMap.get(list.get(i)).equals(locale)){
                button.setValue(list.get(i));
            }
        }

    }

    public Locale getLocale (ComboBox<String> button){
        return languagesMap.get(button.getValue());
    }

    public String getLocaleString(ResourceBundle labels, String key){
        return labels.getString(key);
    }
}
