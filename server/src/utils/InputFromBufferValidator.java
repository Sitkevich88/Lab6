package utils;

import data.MusicGenre;
import java.time.*;
import java.util.InputMismatchException;

/**
 * Input checker. Validates input
 */

public class InputFromBufferValidator {

    private MessagesForClient messages;
    
    public InputFromBufferValidator(MessagesForClient messages){
        this.messages = messages;
    }

    /**
     * Validates String
     * @param canBeNull - boolean if input can be null
     * @param canBeEmpty - boolean if input can be empty
     * @return String correct input
     */

    public String nextLine(String msg, boolean canBeEmpty, boolean canBeNull) throws EmptyBufferException {

        String correctString;
        do {
            hasBufferLineToRead();
            messages.recordMessage(msg);
            correctString = CommandsParser.getBufferLine();

        }while (!(emptyCheck(canBeEmpty, correctString) && nullCheck(canBeNull, correctString)));

        if (correctString.equals("null")){
            correctString=null;
        }

        return correctString;
    }

    /**
     * Validates long
     * @return long correct input
     */

    public long nextLong(String msg) throws EmptyBufferException {
        long correctLong = 0;
        boolean correct = false;
        String buffer;

        while (!correct){
            try {
                hasBufferLineToRead();
                messages.recordMessage(msg);
                buffer = CommandsParser.getBufferLine().trim();
                if (buffer.length()==0){
                    throw new InputMismatchException();
                }else{
                    correctLong = Long.valueOf(buffer).longValue();
                }
                correct=true;
            }catch (InputMismatchException | NumberFormatException e){
                //System.out.println("Input must be long type");
            }
        }
        return correctLong;
    }

    /**
     * Validates Integer
     * @param canBeNull - boolean if input can be null
     * @param mustBePositive - boolean if input value must be greater than 0
     * @return Integer correct input
     */

    public Integer nextInt(String msg, boolean canBeNull, boolean mustBePositive) throws EmptyBufferException {

        Integer correctInt = null;
        String buffer = null;
        boolean isInteger;
        do {
            isInteger = false;
            while (!isInteger){
                try {
                    hasBufferLineToRead();
                    messages.recordMessage(msg);
                    buffer = CommandsParser.getBufferLine().trim();
                    correctInt = Integer.valueOf(buffer);
                    isInteger = true;
                } catch (InputMismatchException | NumberFormatException e) {
                    if (buffer.equals("null")){
                        correctInt = null;
                        isInteger = true;
                    }else{
                        //System.out.println("Input must be Integer");
                    }
                }
            }
        }while (!(nullCheck(canBeNull, buffer) && positiveCheck(mustBePositive, correctInt)));
        return correctInt;
    }

    /**
     * Validates data.MusicGenre
     * @return data.MusicGenre correct input
     */
    public MusicGenre nextMusicGenre(String msg) throws EmptyBufferException {
        MusicGenre genre = null;
        boolean correct = false;
        while (!correct){
            hasBufferLineToRead();
            messages.recordMessage(msg);
            MusicGenre.printAllValues(messages);
            String input = CommandsParser.getBufferLine().trim();
            try {
                genre = MusicGenre.getEnum(input);
                correct = true;
            }catch (IllegalArgumentException e){
                //System.out.println("Incorrect enum");
            }
        }
        return genre;
    }

    /**
     * Validates time
     * @return ZonedDateTime correct input
     */
    public ZonedDateTime nextZonedDateTime() throws EmptyBufferException{
        ZonedDateTime time = null;
        LocalTime localTime = LocalTime.MIDNIGHT;
        LocalDate localDate = null;
        ZoneId zoneId = null;
        boolean correct = false;
        while (!correct){
            try {
                hasBufferLineToRead();
                messages.recordMessage("The format is year.month.day (2021.02.22)");
                String[] input = CommandsParser.getBufferLine().trim().split("\\.", 3);
                localDate = LocalDate.of(Integer.valueOf(input[0]).intValue(), Integer.valueOf(input[1]).intValue(), Integer.valueOf(input[2]).intValue());
                correct = true;
            }catch (IllegalArgumentException e){
                //System.out.println("Incorrect date");
            }

        }
        correct = false;
        while (!correct){
            try {
                hasBufferLineToRead();
                messages.recordMessage("Insert the offset in the format +/- and a number of hours in range[-18, +18] (+3): ");
                zoneId = ZoneId.of(CommandsParser.getBufferLine().trim());
                correct = true;
            }catch (DateTimeException e){
                //System.out.println("Incorrect offset");
            }
        }
        time = ZonedDateTime.of(localDate, localTime,zoneId);
        return time;
    }

    /**
     * Validates Float
     * @param canBeNull - boolean if input can be null
     * @param mustBePositive - boolean if input value must be greater than 0
     * @return Float correct input
     */

    public Float nextFloat(String msg, boolean canBeNull, boolean mustBePositive) throws EmptyBufferException {

        Float correctFloat = null;
        String buffer = null;
        boolean isFloat;
        do {
            isFloat = false;
            while (!isFloat){
                try {
                    hasBufferLineToRead();
                    messages.recordMessage(msg);
                    buffer = CommandsParser.getBufferLine().trim();
                    correctFloat = Float.valueOf(buffer);
                    isFloat = true;
                } catch (InputMismatchException | NumberFormatException e) {
                    if (buffer.equals("null")){
                        correctFloat = null;
                        isFloat = true;
                    }else{
                        //System.out.println("Input must be Float");
                    }
                }
            }
        }while (!(nullCheck(canBeNull, buffer) && positiveCheck(mustBePositive, correctFloat)));
        return correctFloat;
    }

    private boolean nullCheck(boolean canBeNull, String input){
        if (!canBeNull){
            try{
                if (input.equals("null")){
                    messages.recordMessage("Input cannot be null");
                    return false;
                }
            }catch (NullPointerException e){

            }

        }
        return true;
    }


    private boolean emptyCheck(boolean canBeEmpty, String input){
        if (!canBeEmpty){

            if (input.length()==0){
                messages.recordMessage("Input cannot be empty");
                return false;
            }

        }
        return true;
    }



    private boolean positiveCheck(boolean mustBePositive, Number input){

        if (mustBePositive && input!=null){
            switch (input.getClass().getSimpleName()){
                case ("Byte"):
                    if (input.byteValue()<=0){
                        messages.recordMessage("Input must be positive");
                        return false;
                    }
                    break;
                case ("Double"):
                    if (input.doubleValue()<=0){
                        messages.recordMessage("Input must be positive");
                        return false;
                    }
                    break;
                case ("Float"):
                    if (input.floatValue()<=0){
                        messages.recordMessage("Input must be positive");
                        return false;
                    }
                    break;
                case ("Integer"):
                    if (input.intValue()<=0){
                        messages.recordMessage("Input must be positive");
                        return false;
                    }
                    break;
                case ("Long"):
                    if (input.longValue()<=0){
                        messages.recordMessage("Input must be positive");
                        return false;
                    }
                    break;
                case ("Short"):
                    if (input.shortValue()<=0){
                        messages.recordMessage("Input must be positive");
                        return false;
                    }
                    break;
                default:
                    messages.recordMessage("Unexpected Number value");
                    return false;
            }
        }
        return true;
    }

    private String booleanToString(boolean value){
        if (value){
            return " ";
        }else {
            return "not ";
        }
    }

    private void hasBufferLineToRead() throws EmptyBufferException {
        if (CommandsParser.isBufferEmpty()==true){
            throw new EmptyBufferException();
        }
    }

}