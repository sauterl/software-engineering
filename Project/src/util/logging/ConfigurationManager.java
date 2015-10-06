package util.logging;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.Level;

import util.jsontools.Json;
import util.jsontools.JsonParser;

public class ConfigurationManager {

    private static final String VERSION_KEY = "version";
    private static final String VERSION = "dev 0.1";
    private static final String HANDLERS_KEY = "handlers";
    private static final String REF_KEY = "ref";
    private static final String NAME_KEY = "name";
    private static final String LEVEL_KEY = "level";
    private static final String LOGGERS_KEY = "loggers";
    private static final String HANDLER_KEY = "handler";

    private static ConfigurationManager instance = null;

    private HashMap<String, Handler> refHandlerMap;
    private HashMap<String, LoggerConfiguration> nameConfigMap;

    private ConfigurationManager() {
	// singleton
	refHandlerMap = new HashMap<String, Handler>();
	nameConfigMap = new HashMap<String, LoggerConfiguration>();
    }

    public static ConfigurationManager getConfigManager() {
	if (instance == null) {
	    instance = new ConfigurationManager();
	}
	return instance;
    }

    public Json readConfigFile(String filename) throws IOException {
	JsonParser parser = new JsonParser();
	Json config = parser.parseFile(filename);
	return config;
    }
    
    public void applyConfig(AbstractLogger log){
	if(nameConfigMap.containsKey(log.getName())){
	    LoggerConfiguration config = nameConfigMap.get(log.getName() );
	    Handler h = refHandlerMap.get(config.getHandlerRef());
	    if(h != null){
		log.resetHandlers();
		log.addHandler(h);
	    }
	    if(config.getLevel() != null){
		log.setLevel(config.getLevel());
	    }
	}
	//silently ignore
    }
    
    public Handler getHandlerForRef(String ref){
	return refHandlerMap.get(ref);
    }

    public void loadConfigFile(String file) throws IOException {
	Json jsonFile = readConfigFile(file);
	if (!validateStructure(jsonFile)) {
	    throw new IllegalArgumentException("no valid config");
	}
	for (Json hndlr : jsonFile.getSet(HANDLERS_KEY)) {
	    try {
		refHandlerMap.put(hndlr.getString(REF_KEY), parseHandlerObject(hndlr));
	    } catch (ClassNotFoundException | NoSuchMethodException
		    | SecurityException | InstantiationException
		    | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException e) {
		// TODO Effective exception handling
		e.printStackTrace();
	    }
	}//end iterate over handlers
	for(Json lggr : jsonFile.getSet(LOGGERS_KEY)){
	    nameConfigMap.put(lggr.getString(NAME_KEY), parseLoggerObject(lggr));
	}
	//done
    }

    public Handler parseHandlerObject(Json handlerObj)
	    throws ClassNotFoundException, NoSuchMethodException,
	    SecurityException, InstantiationException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException {
	String name = handlerObj.getString(NAME_KEY);
	Class<?> handlerClass = Class.forName(name);
	Constructor<?> hcConstr = handlerClass.getConstructor(null);
	Object hndlrObj = hcConstr.newInstance(null);
	if (hndlrObj instanceof Handler) {
	    Handler h = (Handler) hndlrObj;
	    if(handlerObj.containsEntry(LEVEL_KEY)){
		Level lvl = LevelX.parse(handlerObj.getString(LEVEL_KEY));
		if(lvl != null){
		    h.setLevel(lvl);
		}
	    }
	    return h;
	}
	return null;
    }
    
    public LoggerConfiguration parseLoggerObject(Json loggerObj){
	String handler = loggerObj.getString(HANDLER_KEY);
	Level lvl = LevelX.parse(loggerObj.getString(LEVEL_KEY) );
	return new LoggerConfiguration(handler, lvl);
    }

    public boolean validateStructure(Json json) {
	if (!validateVersion(json)) {
	    return false;
	}
	// version is valid
	if (json.containsSet(HANDLERS_KEY)) {
	    for (Json obj : json.getSet(HANDLERS_KEY)) {
		// iterate over handlers-array
		if (!validateHandler(obj)) {
		    // a handler-obj is invalid
		    return false;
		}
		// current handler-obj is valid
	    }
	    // all handler-objs are valid
	} else {
	    // no handlers
	    return false;
	}
	// valid handlers
	if (json.containsSet(LOGGERS_KEY)) {
	    for (Json obj : json.getSet(LOGGERS_KEY)) {
		// iterate over loggers-array
		if (!validateLogger(obj)) {
		    // a logger-obj is invalid
		    return false;
		}
		// current logger-obj is valid
	    }
	    // all logger-objs are valid
	} else {
	    // no loggers
	    return false;
	}
	// valid loggers
	return true;
    }

    public boolean validateVersion(Json parentObj) {
	if (!parentObj.containsKey(VERSION_KEY)) {
	    return false;
	}
	return checkVersion(parentObj.getString(VERSION_KEY));
    }

    public boolean checkVersion(String version) {
	return VERSION.equalsIgnoreCase(version);
    }

    public boolean validateHandler(Json handlerObj) {
	if (!handlerObj.containsKey(REF_KEY)) {
	    return false;
	}
	if (!handlerObj.containsKey(NAME_KEY)) {
	    return false;
	}
	// is likely valid: has ref and name keys
	return true;
    }

    public boolean validateLogger(Json loggerObj) {
	if (!loggerObj.containsKey(NAME_KEY)) {
	    return false;
	}
	if (!loggerObj.containsKey(HANDLER_KEY)) {
	    return false;
	}
	// is likey valid format: has name and handler key
	return true;
    }

    // TODO Read file as json, test if keys, structure subtypes exist. parse
}
