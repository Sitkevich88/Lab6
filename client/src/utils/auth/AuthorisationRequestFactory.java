package utils.auth;

import data.AuthMode;
import data.UserData;
import gui.authorisation.CredentialsWithMode;


public class AuthorisationRequestFactory {

    private Hash hasher;

    public AuthorisationRequestFactory(){
        hasher = new Hash();
    }

    public UserData getAuthorisationRequest(CredentialsWithMode credentials){

        if (credentials.getLogin()==null){
            return null;
        }
        String login = credentials.getLogin();
        String password = credentials.getPassword();
        AuthMode mode = credentials.getMode();
        byte[] passwordInBytes = hasher.stringToByte(password);
        return new UserData(login, passwordInBytes, mode);

    }
}
