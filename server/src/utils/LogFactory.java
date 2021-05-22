package utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LogFactory {

    private static JoranConfigurator configurator = new JoranConfigurator();

    public Logger getLogger(Object obj){
        Logger logger = LoggerFactory.getLogger(obj.getClass());
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        configurator.setContext(lc);
        lc.reset();
        try {
            //configurator.doConfigure("./src/logback-test.xml");
            File logFile = new File("."+ File.separator +"logback-test.xml");
            if (logFile.exists()){
                configurator.doConfigure(logFile.getName());
            }else{
                configurator.doConfigure("."+ File.separator +"src" + File.separator + "logback-test.xml");
            }

        } catch (JoranException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return logger;
    }
}
