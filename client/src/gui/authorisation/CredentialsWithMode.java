package gui.authorisation;

import data.AuthMode;

public class CredentialsWithMode {

    private String login;
    private String password;
    private AuthMode mode;

    public CredentialsWithMode(String login, String password, AuthMode mode) {
        this.login = login;
        this.password = password;
        this.mode = mode;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public AuthMode getMode() {
        return mode;
    }

}
