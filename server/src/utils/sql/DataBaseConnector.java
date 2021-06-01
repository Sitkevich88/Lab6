package utils.sql;

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import utils.LogFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static java.lang.System.out;

public class DataBaseConnector {

    private static Connection connection = null;

    public void connect(){
        LogFactory logFactory = new LogFactory();
        Logger logger = null;
        try {
            logger = logFactory.getLogger(this);
            Class.forName("org.postgresql.Driver");
            String ps = readPassword();
            //connection = DriverManager.getConnection("jdbc:postgresql://localhost:5430/studs", "s312693", ps);
            connection = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", "s312693", ps);
            logger.info("Accessed to database");
        } catch (ClassNotFoundException e) {
            logger.error("Postgresql Driver has not been found");
            System.exit(1);
        } catch (PSQLException e){
            out.println("Ошибка при попытке подсоединения");
            System.exit(1);
        }
        catch (SQLException throwables) {
            logger.error("Incorrect password or database is currently unavailable");
            throwables.printStackTrace();
            System.exit(0);
        } catch (IllegalStateException e){
            out.println(e.getMessage());
            System.exit(1);
        } catch (Exception e){
            out.println(e.getMessage());
            System.exit(1);
        }

    }


   /* public String readPassword(){
        Console console = System.console();
        char[] ch = console.readPassword("Enter db password : ");
        if (ch==null){System.exit(0);}
        return ch.toString();
    }*/


    private String readPassword() {
        String line = null;
        Scanner scanner = new Scanner(System.in);
        out.print("Enter db password: ");
        try {
            line = scanner.nextLine();
        }catch (NoSuchElementException | NullPointerException e){
            System.exit(0);
        }
        return line;
    }

    public static Connection getConnection() {
        return connection;
    }
}
