package data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class UserData implements Serializable {

    private AuthMode mode;

    private String login;
    private byte[] password;

    public UserData(String login, byte[] password, AuthMode mode){
        this.login = login;
        this.password = password;
        this.mode = mode;
    }

    public String getLogin() {
        return login;
    }

    public byte[] getPassword() {
        return password;
    }

    public AuthMode getMode() {
        return mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return mode == userData.mode &&
                Objects.equals(login, userData.login) &&
                Arrays.equals(password, userData.password);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mode, login);
        result = 31 * result + Arrays.hashCode(password);
        return result;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "mode=" + mode +
                ", login='" + login + '\'' +
                ", password=" + Arrays.toString(password) +
                '}';
    }
}
