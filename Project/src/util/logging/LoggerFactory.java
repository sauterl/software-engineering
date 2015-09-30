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
    
    /**
     * Creates an {@link AbstractLogger} for a given {@link LoggerConfiguration}.
     * 
     * @param clazz
     * @param config
     * @return
     */
    public static AbstractLogger createLoggerForConfig(Class<?> clazz, LoggerConfiguration config){
	AbstractLogger l = util.logging.Logger.getLogger(clazz);
	Handler[] hs = l.getHandlers();
	if(hs != null && hs.length > 0){
	    for(int i=0; i<hs.length; i++){
		l.removeHandler(hs[i]);
	    }
	}
	Handler[] hndlrs = config.getHandlers();
	if(hndlrs != null && hndlrs.length > 0){
	    for(int i=0; i<hndlrs.length; i++){
		hndlrs[i].setFormatter(config.getFormatter());
		l.addHandler(hndlrs[i]);
	    }
	}
	l.setLevel(config.getLoggerLevel());
	l.setFilter(config.getLoggerFilter());
	return l;
    }

}
