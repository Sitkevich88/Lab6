package utils.sql;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public byte[] stringToByte(String line){
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Incorrect hash algorithm");
            System.exit(1);
        }
        return digester.digest(line.getBytes());
    }
}
