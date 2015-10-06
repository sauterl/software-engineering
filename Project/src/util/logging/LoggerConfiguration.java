package util.logging;

import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;

/**
 * TODO write javadoc
 * @author Loris
 *
 */
public class LoggerConfiguration {
    
    private String name;
    private String handlerRef;
    private Level level;
    
    public LoggerConfiguration(String handlerRef, Level level){
	this.handlerRef = handlerRef;
	this.level = level;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the handlerRef
     */
    public String getHandlerRef() {
        return handlerRef;
    }

    /**
     * @return the level
     */
    public Level getLevel() {
        return level;
    }
    
    
    
}
