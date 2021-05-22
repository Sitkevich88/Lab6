package utils;

import utils.sql.DataBaseConnector;


/**
 * @author Valerii Sitkevich
 */

public class Main {

    public static void main(String[] args){

        new DataBaseConnector().connect();

        ServerNew server = new ServerNew(19117, "localhost");
        server.start();
    }
}