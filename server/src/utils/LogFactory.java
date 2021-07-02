package utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LogFactory {

    private static JoranConfigurator configurator = new JoranConfigurator();

    public Logger getLogger(Object obj){
        Logger logger = null;
        try{
            logger = LoggerFactory.getLogger(obj.getClass());
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

            configurator.setContext(lc);
            lc.reset();
            //configurator.doConfigure("./src/logback-test.xml");
            File logFile = new File(/*"."+ File.separator +*/"logback-test.xml");
            System.out.println(logFile.getAbsolutePath());
            if (logFile.exists()){
                configurator.doConfigure(logFile.getName());
                if (true==false){
                    throw new PSQLException(new ServerErrorMessage("Error"));
                }
            }else {
                configurator.doConfigure("."+ File.separator +"src" + File.separator + "logback-test.xml");
            }

        } catch (PSQLException e){
            System.out.println("There are no free slots in database studs");
            System.exit(1);
        }
        catch (JoranException e) {
            System.out.println("logback-test.xml file must be in your current directory");
            System.exit(1);
        } catch (IllegalStateException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return logger;
    }
}
