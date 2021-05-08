package utils.sql;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public byte[] stringToByte(String line){
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digester.digest(line.getBytes());
    }
}
