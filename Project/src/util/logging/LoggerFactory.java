package util.logging;

import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * The {@code LoggerFactory} class creates {@link Logger} instances.
 * @author Loris
 *
 */
public class LoggerFactory {
    
    /*
     * TODO Rewrite this class to only be a factory method for some pre-configured loggers
     */
    
    private static LoggerFactory instance = null;
    
    /**
     * Singleton
     */
    private LoggerFactory(){
	
    }
    
    @Deprecated
    public static LoggerFactory getLoggerFactory(){
	if(instance == null){
	    instance = new LoggerFactory();
	}
	return instance;
    }
    
    @Deprecated
    public Logger getLoggerForClass(Class<?> clazz){
	return Logger.getLogger(clazz.getName() );
    }
    
}
