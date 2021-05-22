package utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFactory {

    private static JoranConfigurator configurator = new JoranConfigurator();

    public Logger getLogger(Object obj){
        Logger logger = LoggerFactory.getLogger(obj.getClass());
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        configurator.setContext(lc);
        lc.reset();
        try {
            //configurator.doConfigure("./src/logback-test.xml");
            configurator.doConfigure(".\\src\\logback-test.xml");
        } catch (JoranException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return logger;
    }
}
