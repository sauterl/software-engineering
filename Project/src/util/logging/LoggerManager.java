package util.logging;

import java.util.HashMap;
import java.util.Iterator;

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
    
    /**
     * Returns the names of all registered Loggers.
     * 
     * @return Returns the names of all registered and Loggers. If this method gets invoked before a single logger has been registered, it returns a zero-lengthed string array.
     */
    public String[] getLoggerNames(){
	if(nameLoggerMap.size() == 0){
	    return new String[0];
	}
	String[] names = new String[nameLoggerMap.size() ];
	Iterator<String> keysIt = nameLoggerMap.keySet().iterator();
	int i=0;
	while(keysIt.hasNext() ){
	    names[i++] = keysIt.next();
	}
	return names;
    }
    
    
    public static LoggerManager getManager(){
	if(instance == null){
	    instance = new LoggerManager();
	}
	return instance;
    }

}
