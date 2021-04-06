package utils;

import data.MusicGenre;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.InputMismatchException;

/**
 * Input checker. Validates input
 */

public class InputChecker {


    Helper sc = new Helper();

    /**
     * Validates String
     * @param str - String input request
     * @param canBeNull - boolean if input can be null
     * @param canBeEmpty - boolean if input can be empty
     * @return String correct input
     */

    public String nextLine(String str, boolean canBeEmpty, boolean canBeNull){

        String correctString;
        do {
            System.out.printf("(String, can%sbe empty, can%sbe null) %s",booleanToString(canBeEmpty), booleanToString(canBeNull), str);
            correctString = sc.nextLineWithNoTrim();
        }while (!(emptyCheck(canBeEmpty, correctString) && nullCheck(canBeNull, correctString)));

        if (correctString.equals("null")){
            correctString=null;
        }

        return correctString;
    }

    /**
     * Validates long
     * @param str - String input request
     * @return long correct input
     */

    public long nextLong(String str)  {
        long correctLong = 0;
        boolean correct = false;
        String buffer;

        while (!correct){
            try {
                System.out.print("(long) " + str);
                buffer = sc.nextLine();
                if (buffer.length()==0){
                    throw new InputMismatchException();
                }else{
                    correctLong = Long.valueOf(buffer).longValue();
                }
                correct=true;
            }catch (InputMismatchException | NumberFormatException e){
                System.out.println("Input must be long type");
            }
        }
        return correctLong;
    }

    /**
     * Validates Integer
     * @param str - String input request
     * @param canBeNull - boolean if input can be null
     * @param mustBePositive - boolean if input value must be greater than 0
     * @return Integer correct input
     */

    public Integer nextInt(String str, boolean canBeNull, boolean mustBePositive)  {

        Integer correctInt = null;
        String buffer = null;
        boolean isInteger;
        do {
            isInteger = false;
            while (!isInteger){
                try {
                    System.out.printf("(Integer, can%sbe null, can%sbe <=0) %s",booleanToString(canBeNull), booleanToString(!mustBePositive), str);
                    buffer = sc.nextLine();
                    correctInt = Integer.valueOf(buffer);
                    isInteger = true;
                } catch (InputMismatchException | NumberFormatException e) {
                    if (buffer.equals("null")){
                        correctInt = null;
                        isInteger = true;
                    }else{
                        System.out.println("Input must be Integer");
                    }
                }
            }
        }while (!(nullCheck(canBeNull, buffer) && positiveCheck(mustBePositive, correctInt)));
        return correctInt;
    }

    /**
     * Validates data.MusicGenre
     * @param str - String input request
     * @return data.MusicGenre correct input
     */
    public MusicGenre nextMusicGenre(String str){
        MusicGenre genre = null;
        boolean correct = false;
        while (!correct){
            System.out.println(str);
            MusicGenre.printAllValues();
            String input = sc.nextLine();
            try {
                genre = MusicGenre.getEnum(input);
                correct = true;
            }catch (IllegalArgumentException e){
                System.out.println("Incorrect enum");
            }
        }
        return genre;
    }

    /**
     * Validates time
     * @param str - String input request
     * @return ZonedDateTime correct input
     */
    public ZonedDateTime nextZonedDateTime(String str){
        ZonedDateTime time = null;
        LocalTime localTime = LocalTime.MIDNIGHT;
        LocalDate localDate = null;
        ZoneId zoneId = null;
        boolean correct = false;
        while (!correct){
            try {
                System.out.println(str);
                System.out.println("The format is year.month.day (2021.02.22)");
                String[] input = sc.nextLine().split("\\.", 3);
                localDate = LocalDate.of(Integer.valueOf(input[0]).intValue(), Integer.valueOf(input[1]).intValue(), Integer.valueOf(input[2]).intValue());
                correct = true;
            }catch (Exception e){
                System.out.println("Incorrect date");
            }

        }
        correct = false;
        while (!correct){
            try {
                System.out.print("Insert the offset in the format +/- and a number of hours in range[-18, +18] (+3): ");
                zoneId = ZoneId.of(sc.nextLine());
                correct = true;
            }catch (Exception e){
                System.out.println("Incorrect offset");
            }
        }
        time = ZonedDateTime.of(localDate, localTime,zoneId);
        return time;
    }

    /**
     * Validates Float
     * @param str - String input request
     * @param canBeNull - boolean if input can be null
     * @param mustBePositive - boolean if input value must be greater than 0
     * @return Float correct input
     */

    public Float nextFloat(String str, boolean canBeNull, boolean mustBePositive){

        Float correctFloat = null;
        String buffer = null;
        boolean isFloat;
        do {
            isFloat = false;
            while (!isFloat){
                try {
                    System.out.printf("(Float, can%sbe null, can%sbe <=0) %s",booleanToString(canBeNull), booleanToString(!mustBePositive), str);
                    buffer = sc.nextLine();
                    correctFloat = Float.valueOf(buffer);
                    isFloat = true;
                } catch (InputMismatchException | NumberFormatException e) {
                    if (buffer.equals("null")){
                        correctFloat = null;
                        isFloat = true;
                    }else{
                        System.out.println("Input must be Float");
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
                    System.out.println("Input cannot be null");
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
                System.out.println("Input cannot be empty");
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
                        System.out.println("Input must be positive");
                        return false;
                    }
                    break;
                case ("Double"):
                    if (input.doubleValue()<=0){
                        System.out.println("Input must be positive");
                        return false;
                    }
                    break;
                case ("Float"):
                    if (input.floatValue()<=0){
                        System.out.println("Input must be positive");
                        return false;
                    }
                    break;
                case ("Integer"):
                    if (input.intValue()<=0){
                        System.out.println("Input must be positive");
                        return false;
                    }
                    break;
                case ("Long"):
                    if (input.longValue()<=0){
                        System.out.println("Input must be positive");
                        return false;
                    }
                    break;
                case ("Short"):
                    if (input.shortValue()<=0){
                        System.out.println("Input must be positive");
                        return false;
                    }
                    break;
                default:
                    System.out.println("Unexpected Number value");
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
}