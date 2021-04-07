package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Json Fixer. Removes objects with incorrect numbers out of the collection in json String format
 */

public class JsonFixer {

    /**
     * Validates json number fields of an object
     * @param json String json object of the broken collection
     * @return boolean: if this object is broken it returns false, else it returns true
     */

    private boolean isJsonObjectGood(String json){
        // String to be scanned to find the pattern.
        String pattern = "\"(\\w+)\"\\s*:\\s+(\\d*\\.?\\d*)?";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher matcher = r.matcher(json);

        boolean areFieldsGood = true;

        while (matcher.find()) {
            try {
                switch (matcher.group(1)){
                    case ("id"):
                    case ("x"):
                        Long.valueOf(matcher.group(2)).longValue();
                        break;
                    case ("y"):
                        Integer.valueOf(matcher.group(2));
                        break;
                    case ("numberOfParticipants"):
                        try{
                            Integer.valueOf(matcher.group(2));
                        }catch (NullPointerException e){}
                        break;
                    case ("tracks"):
                    case ("length"):
                        Integer.valueOf(matcher.group(2)).intValue();
                        break;
                    case ("sales"):
                        try{
                            Float.valueOf(matcher.group(2));
                        }catch (NullPointerException e){}
                        break;
                    default:

                }
            }catch (NumberFormatException e){
                areFieldsGood = false;
                break;
            }
        }
        return areFieldsGood;
    }

    /**
     * Reads json String, finds an object, validates it, if the object is good, then adds it
     * @param badJsonString broken collection in json String
     * @return fixed collection without broken objects
     */


    public String fixAll(String badJsonString){

        // String to be scanned to find the pattern.
        /*String pattern = "\\s*\\{\\s*\"id\"\\s*:\\s*\\d+,\\s*\"name\""+
                "\\s*:\\s*\"?.*\"?,\\s*\"coordinates\"\\s*:\\s*\\{\\s*\"x\"\\s*:\\s*\"?.*\"?,\\s*"+
                "\"y\"\\s*:\\s*\"?.*\"?\\s*\\},\\s*\"creationDate\"\\s*:\\s*\".*\",\\s*"+
                "\"numberOfParticipants\"\\s*:\\s*\"?.*\"?,\\s*\"description\""+
                "\\s*:\\s*\"?.*\"?,\\s*\"establishmentDate\"\\s*:\\s*\".*\",\\s*"+
                "\"genre\"\\s*:\\s*\".*\",\\s*\"bestAlbum\"\\s*:\\s*\\{\\s*"+
                "\"name\"\\s*:\\s*\"?.*\"?,\\s*\"tracks\"\\s*:\\s*\\d+,\\s*"+
                "\"length\"\\s*:\\s*\\d+,\\s*\"sales\"\\s*:\\s*\\d+\\.?\\d*\\s*\\}\\s*\\}\\s?";*/

        String pattern = "\\s*\\{(\\S|\\W)*?\\},(\\S|\\W)*?\\}(\\S|\\W)*?\\}";
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher matcher = r.matcher(badJsonString);
        StringBuilder newLine = new StringBuilder("[");
        while (matcher.find()) {
            if (isJsonObjectGood(matcher.group(0))){
                if (newLine.length()>1){
                    newLine.append(',');
                }
                newLine.append(matcher.group(0));
            }
        }
        newLine.append(']');
        return newLine.toString();
    }

}
