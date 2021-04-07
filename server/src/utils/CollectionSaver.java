package utils;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import data.MusicBand;
import java.io.*;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Stack;

/**
 * Collection saver. Saves file to collection and vice versa
 */
public class CollectionSaver {


    private String path;
    private Gson gson = Converters.registerZonedDateTime(new GsonBuilder()).setPrettyPrinting().create();
    public CollectionSaver(String path){
        this.path=path;
    }

    /**
     *
     * @return String path to the file
     */
    public String getPath() {
        return path;
    }


    /**
     * Saves file to the collection
     * @return created collection
     */
    public Stack<MusicBand> saveFileToCollection() {

        StringBuilder jsonString = new StringBuilder();
        Stack<MusicBand> collection = null;
        Type listType =  new TypeToken<Stack<MusicBand>>() {}.getType();

        try {
            FileReader fr = new FileReader(getValidFile(Necessity.READ));
            int content;
            while ((content = fr.read()) != -1) {
                jsonString.append((char) content);
            }
            collection = gson.fromJson(jsonString.toString(), listType);

        } catch (IOException e) {

            System.out.println("IO error");
            MessagesForClient.recordMessage(e.getStackTrace().toString());

        } catch (JsonSyntaxException e){
            try{
                utils.JsonFixer fixer = new utils.JsonFixer();
                String fixedJson = fixer.fixAll(jsonString.toString());
                collection = gson.fromJson(fixedJson, listType);
            }catch (JsonSyntaxException e1){
                MessagesForClient.recordMessage("Unfixable json syntax exception occurred");
            }
        }

        utils.CollectionAnalyzer analyzer = new utils.CollectionAnalyzer(collection);
        GeneratorId.setMaxExistingId(analyzer.findMaxId());
        utils.CollectionAnalyzer.InitializationTracker.updateLastInit();
        return collection;
    }




    /**
     * Saves collection to the file
     * @param collection - collection to be saved
     */

    public void saveCollectionToFile(Collection<?>collection){

        String json = gson.toJson(collection);
        BufferedOutputStream outputStream;
        setValidPath(Necessity.WRITE);

        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(path));
            byte[] buffer = json.getBytes();
            outputStream.write(buffer,0, buffer.length);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            System.out.println("IO error");
            MessagesForClient.recordMessage("IO error");
        }
    }


    private enum Necessity{
        READ,
        WRITE
    }


    private void setValidPath(Necessity necessity){
        File file = new File(path);
        boolean shouldChangePath;
        do {
            shouldChangePath = false;
            if (!file.exists()){
                MessagesForClient.recordMessage("This file does not exist");
                try {
                    file.createNewFile();
                    shouldChangePath = false;
                } catch (IOException e) {
                    MessagesForClient.recordMessage(e.getStackTrace().toString());
                    shouldChangePath = true;
                }
            }else{
                if (!file.isFile()){
                    MessagesForClient.recordMessage("It is not a file");
                    shouldChangePath = true;
                }else {
                    if (necessity.equals(Necessity.READ) && !file.canRead()) {
                        MessagesForClient.recordMessage("There are no rights to read from this file");
                        shouldChangePath = true;
                    }else if (necessity.equals(Necessity.WRITE) && !file.canWrite()){
                        System.out.println("There are no rights to write in the file");
                        MessagesForClient.recordMessage("There are no rights to write in the file");
                        shouldChangePath = true;
                    }
                }
                if (shouldChangePath){
                    path = path + "1";
                }
            }
        }while (shouldChangePath);

    }

    private File getValidFile(Necessity necessity){
        setValidPath(necessity);
        return new File(path);
    }

}
