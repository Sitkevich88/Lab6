package utils.auth;


import data.AuthMode;
import data.UserData;
import java.util.Scanner;
import static java.lang.System.out;

public class UserAuthorisation{

    private final Hash hasher;

    public UserAuthorisation(){
        hasher = new Hash();
    }

    public UserData authorise(){
        UserData userData = null;
        boolean work = true;
        while (work){
            switch (ask("Do you have an account? ").toLowerCase().trim()){
                case ("yes"):
                    userData = createUser(AuthMode.LOG_IN);
                    work = false;
                    break;
                case ("no"):
                    boolean workNo = true;
                    while (workNo){
                        switch (ask("Do you want to create a new account? ").toLowerCase().trim()){
                            case ("yes"):
                                userData = createUser(AuthMode.CREATE);
                                work = false;
                                workNo = false;
                                break;
                            case ("no"):
                                System.exit(0);
                                break;
                            default:
                                out.println("Incorrect answer");
                                out.println("Yes or No?");
                        }
                    }
                    break;
                default:
                    out.println("Incorrect answer");
                    out.println("Yes or No?");
            }
        }
        return userData;

    }


    private UserData createUser(AuthMode mode){
        String login = readLogin();
        byte[] password = hasher.stringToByte(readPassword());
        return new UserData(login,password,mode);

    }

    private String ask(String msg){
        Scanner scanner = new Scanner(System.in);
        String answer;
        do{
            out.print(msg);
            answer = scanner.nextLine();
        }while (answer==null);
        return answer;
    }

    /*
    public String readPassword(){
        Console console = System.console();
        String answer;
        do{
            answer = console.readPassword("Enter user password(5+ symbols): ").toString();
        }while (answer==null || answer.length()<5);
        return answer;

    }*/


    private String readPassword() {
        Scanner scanner = new Scanner(System.in);
        String answer;
        do{
            out.print("Enter user password(5+ symbols): ");
            answer = scanner.nextLine();
        }while (answer==null || answer.length()<5);
        return answer;
    }

    private String readLogin() {
        Scanner scanner = new Scanner(System.in);
        String answer;
        do{
            out.print("Enter user login(5+ symbols): ");
            answer = scanner.nextLine();
        }while (answer==null || answer.length()<5);
        return answer;
    }

}
