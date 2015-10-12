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

    private static final Logger LOGGER = LoggerManager.getLoggingLogger(ConfigurationManager.class);
    
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

    private static final StandardConsoleHandler defaultHandler = new StandardConsoleHandler();
    
    /**
     * Creates the configmanager
     */
    private ConfigurationManager() {
	// singleton
	refHandlerMap = new HashMap<String, Handler>();
	nameConfigMap = new HashMap<String, LoggerConfiguration>();
	LOGGER.debug("Initialization complete.");
    }

    /**
     * Returns the configuration manager
     * @return
     */
    public static ConfigurationManager getConfigManager() {
	if (instance == null) {
	    instance = new ConfigurationManager();
	}
	return instance;
    }

    private Json readConfigFile(String filename) throws IOException {
	LOGGER.debug("Going to read "+filename);
	JsonParser parser = new JsonParser();
	Json config = parser.parseFile(filename);
	LOGGER.debug("Loaded config file to vm");
	return config;
    }
    
    /**
     * Applies the configuration for the given logger.
     * If a config file was found and could get parsed and
     * the given logger has an entry, then it will be configured
     * as defined in the file. Otherwise the default configuration gets
     * applied.
     * @param log
     */
    public void applyConfig(AbstractLogger log){
	if(nameConfigMap.containsKey(log.getName())){
	    LOGGER.debug(String.format("Found config for logger %s", log.getName()));
	    LoggerConfiguration config = nameConfigMap.get(log.getName() );
	    Handler h = refHandlerMap.get(config.getHandlerRef());
	    if(h != null){
		log.resetHandlers();
		log.addHandler(h);
	    }
	    if(config.getLevel() != null){
		log.setLevel(config.getLevel());
	    }
	    LOGGER.config(String.format("For logger (%s), added handler with ref (%s) and set level to (%s)", log.getName(), config.getHandlerRef(), config.getLevel()));
	}else{
	    //apply default config
	    LOGGER.debug(String.format("No config for logger %s found, loading default config.", log.getName() ));
	    applyDefaultConfig(log);
	}
    }
    
    /**
     * Applies the default configuration to the given logger.
     * Default means: The level is INFO and one handler, a StandartConsoleHandler gets
     * added.
     * @param log
     */
    public void applyDefaultConfig(AbstractLogger log){
	log.resetHandlers();
	log.addHandler(defaultHandler);
	log.setLevel(LevelX.INFO);
    }
    
    /**
     * Returns the {@link Handler} with the given reference signature.
     * @param ref The reference signature of the handler.
     * @return The handler object or null if none was found.
     */
    public Handler getHandlerForRef(String ref){
	return refHandlerMap.get(ref);
    }

    public void loadConfigFile(String file) throws IOException {
	LOGGER.config("Config file: "+file);
	Json jsonFile = readConfigFile(file);
//	log.debug("Parsed config: \n"+jsonFile.toJson() );
	LOGGER.debug("Successfully parsed config file");
	if (!validateStructure(jsonFile)) {
	    throw new IllegalArgumentException("no valid config");
	}
	LOGGER.debug("Validated config file");
	parseHandlers(jsonFile);
	parseLoggers(jsonFile);
	LOGGER.info("Config loaded");
    }
    
    private void parseHandlers(Json config){
	LOGGER.debug("Parsing handler json objects");
	for (Json hndlr : config.getSet(HANDLERS_KEY)) {
	    try {
		registerHandler(hndlr.getString(REF_KEY), parseHandlerObject(hndlr));
	    } catch (ClassNotFoundException | NoSuchMethodException
		    | SecurityException | InstantiationException
		    | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException e) {
		LOGGER.warn("Could not parse handler: \n"+hndlr.toJson());
		LOGGER.log(LevelX.DEBUG, "Caught exception", e);
	    }
	}//end iterate over handlers
    }
    
    private void parseLoggers(Json config){
	LOGGER.debug("Parsing logger json objects");
	for(Json lggr : config.getSet(LOGGERS_KEY)){
	    nameConfigMap.put(lggr.getString(NAME_KEY), parseLoggerObject(lggr));
	}
    }
    
    /**
     * Register the given {@link Handler} object with the associated reference.
     * This method is designed to register handlers from another class.
     * @param ref The reference the handler is identified with, must be not null, not empty and unique.
     * @param handler The handler to register with the given refernece.
     */
    public void registerHandler(String ref, Handler handler){
	LOGGER.debug(String.format("Registration of %s for reference %s", (handler != null ? handler.getClass().getName() : "null"), ref ) );
	if(ref == null || ref.length() == 0){
	    throw new IllegalArgumentException("Cannot register handler for reference null or empty");
	}
	if(handler == null){
	    throw new IllegalArgumentException("Cannot register handler null");
	}
	if(refHandlerMap.containsKey(ref)){
	    throw new IllegalArgumentException("Cannot register handler for ref"+ref+", since this reference is already used.");
	}
	refHandlerMap.put(ref, handler);
    }
    
    private Handler parseHandlerObject(Json handlerObj)
	    throws ClassNotFoundException, NoSuchMethodException,
	    SecurityException, InstantiationException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException {
	String name = handlerObj.getString(NAME_KEY);
	LOGGER.debug(String.format("Parsing handler configuration for %s", name));
	Class<?> handlerClass = Class.forName(name);
	LOGGER.debug(String.format("Loaded class %s", handlerClass.getName() ));
	Constructor<?> hcConstr = handlerClass.getConstructor(null);
	LOGGER.debug("Found constructor for handler with signature: "+hcConstr.getName() );
	Object hndlrObj = hcConstr.newInstance((Object[])null);
	if (hndlrObj instanceof Handler) {
	    LOGGER.debug(String.format("Loaded and instantiated successfully handler class (%s)", handlerClass.getName()));
	    Handler h = (Handler) hndlrObj;
	    if(handlerObj.containsEntry(LEVEL_KEY)){
		Level lvl = LevelX.parse(handlerObj.getString(LEVEL_KEY));
		if(lvl != null){
		    h.setLevel(lvl);
		}
	    }
	    return h;
	}
	LOGGER.debug(String.format("Could not instantiate handler %s", name));
	return null;
    }
    
    private LoggerConfiguration parseLoggerObject(Json loggerObj){
	LOGGER.debug("Parsing logger object");
	String handler = loggerObj.getString(HANDLER_KEY);
	Level lvl = LevelX.parse(loggerObj.getString(LEVEL_KEY) );
	LOGGER.debug(String.format("Parsed logger object has handler %s and level %s", handler, lvl.toString() ) );
	return new LoggerConfiguration(handler, lvl);
    }

    private boolean validateStructure(Json json) {
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

    private boolean validateVersion(Json parentObj) {
	if (!parentObj.containsKey(VERSION_KEY)) {
	    return false;
	}
	return checkVersion(parentObj.getString(VERSION_KEY));
    }

    private boolean checkVersion(String version) {
	return VERSION.equalsIgnoreCase(version);
    }

    private boolean validateHandler(Json handlerObj) {
	if (!handlerObj.containsKey(REF_KEY)) {
	    return false;
	}
	if (!handlerObj.containsKey(NAME_KEY)) {
	    return false;
	}
	// is likely valid: has ref and name keys
	return true;
    }

    private boolean validateLogger(Json loggerObj) {
	if (!loggerObj.containsKey(NAME_KEY)) {
	    return false;
	}
	if (!loggerObj.containsKey(HANDLER_KEY)) {
	    return false;
	}
	// is likey valid format: has name and handler key
	return true;
    }
}