package util.logging;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

public class LoggerManager {
    
    private static LoggerManager instance = null;
    
    private static Level internalLevel;
    private final static String verboseLevelKey = "lsjl.verbose.level";
    private final static String verboseEnabledKey = "lsjl.verbose.enabled";
    
    
    private HashMap<String, AbstractLogger> nameLoggerMap;
    private ConfigurationManager configManager = ConfigurationManager.getConfigManager();
    
    private LoggerManager() {
	// TODO fix capacity
	nameLoggerMap = new HashMap<>(10);
	try {
	    // TODO change to pre-defined chain of places to look for.
	    URL uri = getClass().getClassLoader().getResource("util/logging/logging.json");
	    configManager.loadConfigFile(uri.getPath() );
	} catch (IOException e) {
	    System.err.println("no file found");
	    e.printStackTrace();
	}
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
	    // look if there is a config for the logger
	    // if none, go with default
	    // else go with created
	    configManager.applyConfig(logger);
	    nameLoggerMap.put(logger.getName(), logger);
	    return logger;
	}
    }
    
    public void resetHandlers(String loggerName){
	AbstractLogger log = nameLoggerMap.get(loggerName);
	if(log != null){
	    log.resetHandlers();
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

    static Logger getLoggingLogger(Class<?> clazz){
	Logger l = new Logger(java.util.logging.Logger.getLogger(clazz.getSimpleName()));
	l.resetHandlers();
	l.addHandler( new VerboseHandler(internalLevel));
	l.setLevel(LevelX.DEBUG);
	return l;
    }
    
    static{
	// verbose preparation
	if(Boolean.parseBoolean(System.getProperty(verboseEnabledKey)) ){
	   try{
	       internalLevel = LevelX.parse(System.getProperty(verboseLevelKey));
	   }catch(IllegalArgumentException ex){
	       internalLevel = LevelX.INFO;
	   }
	}else{
	    internalLevel = LevelX.OFF;
	}
    }
}
