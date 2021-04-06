package utils;


import data.ServerRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ServerRequestsFactory {

    public ServerRequest getRequestFromConsole() {
        ServerRequest request = null;
        boolean running = false;
        String[] str;
        do{
            try {
                str = CommandsParser.parseArguments();
                switch (str[0]) {
                    case ("help"):
                    case ("info"):
                    case ("show"):
                    case ("exit"):
                    case ("sort"):
                    case ("sum_of_number_of_participants"):
                    case ("clear"):
                    case ("print_field_ascending_description"):
                        request = new ServerRequest(str[0]);
                        running = false;
                        break;
                    case ("add"):
                    case ("update"):
                        ProtoMusicBandWithCorrectFieldsCreator pBandCreator = new ProtoMusicBandWithCorrectFieldsCreator();
                        request = new ServerRequest(str[0], pBandCreator.createProtoMusicBand());
                        running = false;
                        break;
                    case ("remove_by_id"):
                    case ("insert_at"):
                    case ("remove_greater"):
                    case ("count_greater_than_best_album"):
                        request = new ServerRequest(str[0], new String[]{str[1]});
                        running = false;
                        break;
                    case ("execute_script"):
                        String script = readScript(str[1]);
                        request = new ServerRequest(str[0], script);
                        break;
                    default:
                        System.out.println("Unknown command");
                        running = true;
                }
            }catch (IndexOutOfBoundsException | IllegalArgumentException e){
                System.out.println("Incorrect argument");
                running = true;
            }catch (Exception e){
                e.printStackTrace();
                running = true;
            }
        }while (running);
        return request;
    }

    private String readScript(String path){

        StringBuilder commands = new StringBuilder();
        String script;
        File file = new File(path);
        try (FileReader fr = new FileReader(file)) {
            int content;
            while ((content = fr.read()) != -1) {
                commands.append((char) content);
            }
            script = commands.toString();
        } catch (FileNotFoundException e){
            System.out.println("The file is not found");
            script = "";
        }catch (IOException e){
            System.out.println("There are not enough rights to open this file");
            script = "";
        }
        return script;
    }

}
