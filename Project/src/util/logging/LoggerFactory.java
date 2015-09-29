package util.logging;

import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The {@code LoggerFactory} class creates {@link Logger} instances.
 * @author Loris
 *
 */
public class LoggerFactory {
    
    private static LoggerFactory instance = null;
    
    /**
     * Singleton
     */
    private LoggerFactory(){
	
    }
    
    public static LoggerFactory getLoggerFactory(){
	if(instance == null){
	    instance = new LoggerFactory();
	}
	return instance;
    }
    
    public Logger getLoggerForClass(Class<?> clazz){
	return Logger.getLogger(clazz.getName() );
    }

}
