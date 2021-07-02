package data;

import java.io.Serializable;
import java.util.ArrayList;

public class AuthorisationServerAnswer implements Serializable {

    private AuthorisationResult result;
    private ArrayList<MusicBand> bands;

    public static final long serialVersionUID = 139876L;

    public AuthorisationServerAnswer(AuthorisationResult result){
        this.result = result;
    }

    public boolean isAuthorised(){
        if (result!=null && result.equals(AuthorisationResult.OK)){
            return true;
        }else {
            return false;
        }
    }

    public AuthorisationResult getAuthorisationResult(){
        return result;
    }

    public ArrayList<MusicBand> getBands() {
        return bands;
    }

    public void setBands(ArrayList<MusicBand> bands) {
        this.bands = bands;
    }
}
