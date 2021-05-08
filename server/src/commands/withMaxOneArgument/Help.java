package commands.withMaxOneArgument;

import utils.MessagesForClient;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 * Command 'help'. Prints all available commands with description
 */

public class Help{

    public Help(){

        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_BLACK = "\u001B[30m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_GREEN = "\u001B[32m";
        //final String ANSI_GREEN = "\u001B";

        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_BLUE = "\u001B[34m";
        final String ANSI_PURPLE = "\u001B[35m";
        final String ANSI_CYAN = "\u001B[36m";
        final String ANSI_WHITE = "\u001B[37m";


        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("info", "вывести в стандартный поток вывода информацию о коллекции пользователя");
        map.put("show", "вывести в стандартный поток вывода все элементы коллекции пользователя в строковом представлении");
        map.put("show_all", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        map.put("add", "добавить новый элемент в коллекцию");
        map.put("update id", "обновить значение элемента коллекции, id которого равен заданному");
        map.put("remove_by_id id", "удалить элемент из коллекции по его id");
        map.put("clear", "очистить свою коллекцию");
        map.put("execute_script file_name", "считать и исполнить скрипт из указанного файла");
        map.put("exit", "завершить сеанс");
        map.put("insert_at index", "добавить новый элемент в заданную позицию");
        map.put("remove_greater id", "удалить из коллекции все элементы, превышающие заданный");
        map.put("sort", "отсортировать коллекцию в естественном порядке");
        map.put("sum_of_number_of_participants", "вывести сумму значений поля numberOfParticipants для всех элементов коллекции");
        map.put("count_greater_than_best_album bestAlbum", "вывести количество элементов, значение поля bestAlbum которых больше заданного");
        map.put("print_field_ascending_description", "вывести значения поля description всех элементов в порядке возрастания");


        for (String key : map.keySet()){
            MessagesForClient.recordMessage( ANSI_GREEN + key + ANSI_PURPLE + " - " + map.get(key));
        }
        MessagesForClient.appendLastMessage(ANSI_RESET);
    }
}