package util.logging;

import java.util.logging.LogManager;
import java.util.logging.Logger;

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
	return LogManager.getLogManager().getLogger(clazz.getCanonicalName() );
    }

}
