package utils;


import data.*;
import gui.App;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ServerRequestsFactory {
    
    //Данные о текущем пользователе
    private static UserData userData;
    //Формируемый запрос на сервер
    private static ServerRequest request = null;
    //массив из команды и аргументов
    private static String[] str;

    /*private static int numberOfHandlingThreads = 0;

    public static void handleCommand(String[] stuff) {
        if (numberOfHandlingThreads==0){
            Runnable r = () ->{
                try {
                    queue.put(stuff);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                numberOfHandlingThreads--;

            };
            numberOfHandlingThreads++;
            new Thread(r).start();
        }
    }*/

    public static void handleCommand(String[] str1){
        //getRequest(str);
        ServerRequest serverRequest = getRequest(str1);
        if (serverRequest!=null){
            App.getClient().send(serverRequest);
        }
    }

    public static ServerRequest getRequest(String[] str) {

        switch (str[0]) {
            case ("info"):
            case ("exit"):
            case ("sum_of_number_of_participants"):
            case ("clear"):
            case ("print_field_ascending_description"):
                request = new ServerRequest(userData, str[0]);
                break;
            case ("add"):
            case ("update"):
                ProtoMusicBandWithCorrectFieldsCreator pBandCreator = new ProtoMusicBandWithCorrectFieldsCreator();
                ProtoMusicBand pBand = pBandCreator.createProtoMusicBand(str[1], str[2], str[3], str[4],
                        str[5], str[6], str[7], str[8], str[9], str[10], str[11]);
                if (str[0].equals("add")){
                    request = new ServerRequest(userData, str[0], pBand);
                }else {
                    request = new ServerRequest(userData, str[0], new String[]{str[12]}, pBand);
                }
                break;
            case ("remove_by_id"):
            case ("remove_greater"):
            case ("count_greater_than_best_album"):
                request = new ServerRequest(userData, str[0], new String[]{str[1]});
                break;
            case ("execute_script"):
                String script = readScript(str[1]);
                if (script == null){
                    //return getRequest();
                }
                request = new ServerRequest(userData, str[0], script);
                break;
            default:
                request = null;
                doDefault();
        }

        return request;
    }

    public static void setUserData(UserData userData1){
        userData = userData1;
    }

    private static String readScript(String path){

        File file = new File(path);
        StringBuilder commands = new StringBuilder();
        String script;
        try (FileReader fr = new FileReader(file)) {
            int content;
            while ((content = fr.read()) != -1) {
                commands.append((char) content);
            }
            script = commands.toString();
        } catch (IOException e){
            doDefault();
            //System.out.println("This file has not been found");
            script = null;
        }
        return script;
    }

    private static void doDefault(){
        /*ClientRequest clientRequest = new ClientRequest();
        clientRequest.setMessage(MessageFromServerToClient.ERROR);
        App.receiveClientRequest(clientRequest);*/
        //App.getTableWindowCommunicator().putRequest(clientRequest);
    }

}