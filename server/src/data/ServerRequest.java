package data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class ServerRequest implements Serializable {

    private UserData userData;
    private String command;
    private String[] arguments = null;
    private ProtoMusicBand band = null;
    private String script = null;

    public ServerRequest(UserData userData, String command){
        this.userData = userData;
        this.command = command;
    }

    public ServerRequest(UserData userData, String command, String[] arguments){
        this(userData, command);
        this.arguments = arguments;
    }
    public ServerRequest(UserData userData, String command, String[] arguments, ProtoMusicBand band){
        this(userData, command, arguments);
        this.band = band;
    }

    public ServerRequest(UserData userData, String command, ProtoMusicBand band){
        this(userData, command);
        this.band = band;
    }

    public ServerRequest (UserData userData, String command, String script){
        this(userData, command);
        this.script = script;
    }

    public String getCommand() {
        return command;
    }

    public ProtoMusicBand getBand() {
        return band;
    }

    public String[] getArguments() {
        return arguments;
    }

    public String getScript() {
        return script;
    }

    public String getSender() {
        return userData.getLogin();
    }

    public UserData getUserData() {
        return userData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerRequest that = (ServerRequest) o;
        return userData.equals(that.userData) &&
                command.equals(that.command) &&
                Arrays.equals(arguments, that.arguments) &&
                Objects.equals(band, that.band) &&
                Objects.equals(script, that.script);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(userData, command, band, script);
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }

    @Override
    public String toString() {
        return "ServerRequest{" +
                "userData=" + userData +
                ", command='" + command + '\'' +
                ", arguments=" + Arrays.toString(arguments) +
                ", band=" + band +
                ", script='" + script + '\'' +
                '}';
    }
}
