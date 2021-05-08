package data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class ServerRequest implements Serializable {

    private String sender;
    private String command;
    private String[] arguments = null;
    private ProtoMusicBand band = null;
    private String script = null;

    public ServerRequest(String sender, String command){
        this.sender = sender;
        this.command = command;
    }

    public ServerRequest(String sender, String command, String[] arguments){
        this(sender, command);
        this.arguments = arguments;
    }
    public ServerRequest(String sender, String command, String[] arguments, ProtoMusicBand band){
        this(sender, command, arguments);
        this.band = band;
    }

    public ServerRequest(String sender, String command, ProtoMusicBand band){
        this(sender, command);
        this.band = band;
    }

    public ServerRequest (String sender, String command, String script){
        this(sender, command);
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
        return sender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerRequest that = (ServerRequest) o;
        return sender.equals(that.sender) &&
                command.equals(that.command) &&
                Arrays.equals(arguments, that.arguments) &&
                Objects.equals(band, that.band) &&
                Objects.equals(script, that.script);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(sender, command, band, script);
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }

    @Override
    public String toString() {
        return "ServerRequest{" +
                "sender='" + sender + '\'' +
                ", command='" + command + '\'' +
                ", arguments=" + Arrays.toString(arguments) +
                ", band=" + band +
                ", script='" + script + '\'' +
                '}';
    }
}
