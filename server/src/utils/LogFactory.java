package utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFactory {
    public Logger getLogger(Object obj){
        Logger logger = LoggerFactory.getLogger(obj.getClass());
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        try {
            configurator.doConfigure(".\\src\\logback-test.xml");
        } catch (JoranException e) {
            e.printStackTrace();
        }
        return logger;
    }
}
