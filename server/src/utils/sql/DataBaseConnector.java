package utils.sql;
import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import static java.lang.System.out;

public class DataBaseConnector {

    private static Connection connection = null;

    public void connect(){
        try {
            Class.forName("org.postgresql.Driver");
            String ps = readPassword();
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5430/studs",
                    "s312693", ps);
        } catch (ClassNotFoundException e) {
            out.println("Postgresql Driver has not been found");
            System.exit(1);
        } catch (SQLException throwables) {
            out.println("Incorrect password or database is currently unavailable");
            out.println(throwables.getMessage());
            System.exit(0);
        }

    }

    /*
    public String readPassword(){
        Console console = System.console();
        char[] ch = console.readPassword("Enter db password : ");
        return ch.toString();
    }*/


    private String readPassword() {
        Scanner scanner = new Scanner(System.in);
        out.print("Enter db password: ");
        return scanner.nextLine();
    }

    public static Connection getConnection() {
        return connection;
    }
}
