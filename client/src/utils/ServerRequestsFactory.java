package utils;


import data.ClientRequest;
import data.ServerRequest;
import data.UserData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ServerRequestsFactory {
    
    //Данные о текущем пользователе
    private UserData userData;
    //Формируемый запрос на сервер
    private ServerRequest request = null;
    //running=true, если введенная команда некорректна и нужно занаво получить от пользователя команду
    private boolean running = false;
    //массив из команды и аргументов
    private String[] str;
    //ответ сервера на прошлый запрос
    private ClientRequest microRequest = null;
    //true, если важен ответ сервера на прошлый запрос
    private boolean needToCheckServerAnswer = false;
    //true, если ответ на прошлый запрос прочитан, сброс на false, 
    private boolean idChecked = false;

    public ServerRequest getRequestFromConsole() {

        do{
            try {
                if (!needToCheckServerAnswer){
                    str = CommandsParser.parseArguments();
                }
                switch (str[0]) {
                    case ("help"):
                    case ("info"):
                    case ("show"):
                    case ("show_all"):
                    case ("sort"):
                    case ("exit"):
                    case ("sum_of_number_of_participants"):
                    case ("clear"):
                    case ("print_field_ascending_description"):
                        request = new ServerRequest(userData, str[0]);
                        running = false;
                        break;
                    case ("add"):
                        ProtoMusicBandWithCorrectFieldsCreator pBandCreator = new ProtoMusicBandWithCorrectFieldsCreator();
                        request = new ServerRequest(userData, str[0],pBandCreator.createProtoMusicBand());
                        running = false;
                        break;
                    case ("update"):
                        if (!idChecked){
                            validateId(str[1]);
                            checkId();
                            running = false;
                        } else {
                            doItBeforeHandlingServerAnswer();
                            if (microRequest.getResult()){
                                update();
                                running = false;
                            } else {
                                running = true;
                            }
                        }
                        break;
                    case ("remove_by_id"):
                        if (!idChecked){
                            validateId(str[1]);
                            checkId();
                            running = false;
                        } else {
                            doItBeforeHandlingServerAnswer();
                            if (microRequest.getResult()){
                                removeById();
                                running = false;
                            } else {
                                running = true;
                            }
                        }
                        break;
                    case ("remove_greater"):
                    case ("count_greater_than_best_album"):
                        request = new ServerRequest(userData, str[0], new String[]{str[1]});
                        running = false;
                        break;
                    case ("execute_script"):
                        String script = readScript(str[1]);
                        request = new ServerRequest(userData, str[0], script);
                        running = false;
                        break;
                    default:
                        if (str[0].length()!=0){
                            System.out.println("Unknown command");
                        }
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
            System.out.println("This file has not been found");
            script = "";
        }catch (IOException e){
            System.out.println("Not enough rights to open this file");
            script = "";
        }
        return script;
    }

    private void update(){
        ProtoMusicBandWithCorrectFieldsCreator pBandCreator2 = new ProtoMusicBandWithCorrectFieldsCreator();
        request = new ServerRequest(userData, str[0], new String[]{str[1]},pBandCreator2.createProtoMusicBand());
    }

    private void removeById(){
        request = new ServerRequest(userData, str[0], new String[]{str[1]});
    }

    public void setUserData(UserData userData){
        this.userData = userData;
    }
    
    private void handleServerAnswerLater(){
        needToCheckServerAnswer = true;
    }
    
    private void doItBeforeHandlingServerAnswer(){
        needToCheckServerAnswer = false;
        idChecked = false;
    }

    private void checkId(){
        request = new ServerRequest(userData, "check_id", new String[]{str[1]});
        handleServerAnswerLater();
        idChecked = true;
    }

    public void setMicroRequest(ClientRequest request){
        microRequest = request;
    }
    private void validateId(String id) throws IllegalArgumentException{
        long currentId = Long.valueOf(id);
        if (currentId<1){
            throw new IllegalArgumentException();
        }
    }

}
