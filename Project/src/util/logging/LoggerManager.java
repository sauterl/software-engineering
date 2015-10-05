package util.logging;

import java.util.HashMap;

public class LoggerManager {
    
    private static LoggerManager instance = null;
    
    private HashMap<String, AbstractLogger> nameLoggerMap;
    
    private LoggerManager() {
	// TODO fix capacity
	nameLoggerMap = new HashMap<>(10);
    }
    
    AbstractLogger registerLogger(AbstractLogger logger){
	if(logger == null){
	    throw new NullPointerException("Registering null loggers not allowed");
	}
	if(logger.getName() == null){
	    throw new IllegalArgumentException("Registering anonymous loggers not allowed");
	}
	//logger is valid 
	if(nameLoggerMap.containsKey(logger.getName() ) ){
	    //logger is already registered
	    return nameLoggerMap.get(logger.getName() );
	}else{
	    //register the new Logger
	    nameLoggerMap.put(logger.getName(), logger);
	    return logger;
	}
    }
    
    
    public static LoggerManager getManager(){
	if(instance == null){
	    instance = new LoggerManager();
	}
	return instance;
    }

}
