/*
package utils.auth;


import data.AuthMode;
import data.UserData;
import java.util.concurrent.SynchronousQueue;

public class UserAuthorisation{

    private final Hash hasher;

    private static SynchronousQueue<Boolean> authResult = new SynchronousQueue<>();

    private static SynchronousQueue<String[]> queue = new SynchronousQueue<>();

    private static AuthMode mode;

    public UserAuthorisation(){
        hasher = new Hash();
    }

    public UserData requestAuthorisationFromClient(){

        boolean successful = false;

        UserData userData = null;

        try {
            String[] cred = queue.take();
            userData = createUser(cred);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return userData;

    }


    private UserData createUser(String[] cred){
        byte[] passwordInByte = hasher.stringToByte(cred[1]);
        return new UserData(cred[0] ,passwordInByte, mode);
    }


    public static boolean requestAuthorisationFromApp(String login, String password, AuthMode mode) {
        boolean allGood = false;
        try {
            UserAuthorisation.mode = mode;
            queue.put(new String[]{login, password});
            allGood = authResult.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allGood;
    }

    public void loadAuthResultFromClient(boolean isAuthorised){
        try {
            authResult.put(isAuthorised);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
*/
