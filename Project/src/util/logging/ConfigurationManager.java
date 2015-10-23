package util.logging;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.Level;

import util.jsontools.Json;
import util.jsontools.JsonParser;

/**
 * The {@link ConfigurationManager} manages logging configurations as its name
 * describes. <br />
 * On of the main purposes of this class is the parsing and interpretation of
 * configuration files for this logging API. Further the management of assigning
 * handlers to loggers is another important part of this class. The two ways of
 * configuring a logger or manager are described further below.
 * <p>
 * <h2>Default configuration</h2>
 * This class firstly checks if a system property with key
 * {@value LoggerManager#LOGGING_DISABLED_KEY} exists. If such a property was
 * found and equals case insensitive to <tt>true</tt>, the configuration manager
 * will set the level of each registered handler to {@link Level#OFF} (and to
 * handlers which will be registered in future).<br />
 * Does said property exist and equals case insensitive to <tt>false</tt>, then
 * the default logging level will be set to {@link Level#INFO}, except a system
 * property named {@value LoggerManager#LOGGING_DEFAULT_LEVEL_KEY} does exist.
 * In this case and if that property could get parsed with
 * {@link LevelX#parse(String)}, that level will be taken as default level.<br />
 * If neither of the above mentioned cases occurred, the logging is disabled.<br />
 * </p>
 * <p>
 * <h2>Configuration file</h2>
 * This logging API provides simple JSON-based config-file system. Configuration
 * files describe handlers by their fully qualified class name. With the
 * reference name handlers can be assigned to loggers which are addressed by
 * their name. Both, handlers and loggers, can have their level set through this
 * configuration.<br />
 * The following shows an example of such a configuration file:
 * 
 * <pre>
 * {@code
 * {
 * 	"version":"dev 0.2",
 * 	"handlers":[
 * 		{
 * 			"ref":"console",
 * 			"name":"util.logging.StandardConsoleHandler",
 * 			"level":"DEBUG"
 * 		}
 * 	],
 * 	"loggers":[
 * 		{
 * 			"name":"mypackage.MyApp",
 * 			"handler":"console",
 * 			"level":"DEBUG"
 * 		}
 * 	]
 * }
 * }
 * </pre>
 * 
 * <h3>Configuration file keys</h3>
 * <table>
 * <tr>
 * <th>Key name</th>
 * <th>Description</th>
 * <th>Remark</th>
 * </tr>
 * <tr>
 * <td>version</td>
 * <td>This describes the version of the configuration file. Currently this has
 * to be: <tt>dev 0.2</tt></td>
 * <td>mandatory</td>
 * </tr>
 * <tr>
 * <td>handlers</td>
 * <td>An array of <i>handler JSON objects</i></td>
 * <td>mandatory</td>
 * </tr>
 * <tr>
 * <td>loggers</td>
 * <td>An array of <i>logger JSON objects</i></td>
 * <td>mandatory</td>
 * </tr>
 * <tr>
 * <td><i>hander JSON object</i></td>
 * <td>Contains the keys <tt>name</tt>, <tt>ref</tt> and <tt>level</tt>, see
 * their descriptions.</td>
 * <td>mandatory</td>
 * </tr>
 * <tr>
 * <td><i>logger JSON object</i></td>
 * <td>Contains the keys <tt>name</tt>, <tt>handler</tt> and <tt>level</tt>, see
 * their descriptions.</td>
 * <td>mandatory</td>
 * </tr>
 * <tr>
 * <td>ref</td>
 * <td>A string to reference that handler. It is used in
 * <tt><i>logger</i>.handler</tt> to assign a handler to a certain logger.</td>
 * <td>mandatory</td>
 * </tr>
 * <tr>
 * <td>name</td><td>The full class name of the handler. <b>Or</b> the logger's complete name.</td><td>mandatory</td>
 * </tr>
 * <tr>
 * <td>level</td><td>The logger / handler's {@link LevelX}.</td><td>optional</td>
 * </tr>
 * <tr>
 * <td>handler</td><td>The reference string to a previous defined handler.</td><td>mandatory</td>
 * </tr>
 * </table>
 * </p>
 * <p>
 * A developer may use one of the following techniques to register the config
 * file to the API:
 * <ul>
 * <li>By setting a system property named
 * <tt>{@value LoggerManager#CONFIG_PATH_KEY}</tt> with the absolute or relative
 * path to the config file (inclusive name and suffix, e.g. lsjl.json</li>
 * <li>By placing a file in the root folder of the application (so were it gets
 * executed) with one of the following names:
 * <ul>
 * <li><code>lsjl</code></li>
 * <li><code>lsjl-config</code></li>
 * <li><code>logging</code></li>
 * <li><code>logging-config</code></li>
 * </ul>
 * And one of the follwing suffixes:
 * <ul>
 * <li><code>.json</code></li>
 * <li><code>.cfg</code></li>
 * <li><code>.config</code></li>
 * </ul>
 * </li>
 * </ul>
 * </p>
 * <p>
 * <h2>During Runtime</h2>
 * This way is not yet implemented.
 * </p>
 * 
 * @author Loris
 * 
 */
public class ConfigurationManager {

	private static final Logger LOGGER = LoggerManager
			.getLoggingLogger(ConfigurationManager.class);

	private static final String VERSION_KEY = "version";
	public static final String VERSION = "dev 0.2";
	private static final String HANDLERS_KEY = "handlers";
	private static final String REF_KEY = "ref";
	private static final String NAME_KEY = "name";
	private static final String LEVEL_KEY = "level";
	private static final String LOGGERS_KEY = "loggers";
	private static final String HANDLER_KEY = "handler";

	private static Level defaultLevel = null;
	private static boolean loggingDisabled;
	private static ConfigurationManager instance = null;

	/**
	 * Returns the configuration manager instance.
	 * 
	 * @return The configuration manager instance.
	 */
	public static ConfigurationManager getConfigManager() {
		if (instance == null) {
			instance = new ConfigurationManager();
		}
		return instance;
	}

	private static void loadDefaultLevel() {
		if (loggingDisabled) {
			defaultLevel = Level.OFF;
			return;
		}
		if (defaultLevel == null) {
			if (System.getProperty(LoggerManager.LOGGING_DEFAULT_LEVEL_KEY) != null) {
				defaultLevel = LevelX.parse(System
						.getProperty(LoggerManager.LOGGING_DEFAULT_LEVEL_KEY));
			} else {
				defaultLevel = Level.INFO;
			}
		}
		// dont load it again
		LOGGER.config("Default logging level: " + defaultLevel.getName());
	}

	/**
	 * Do <b>NOT</b> invoke this method before loadDefaultLevel()
	 */
	private static void setUpDefaultHandler() {
		if (defaultHandler == null) {
			defaultHandler = new StandardConsoleHandler(defaultLevel);
			if (loggingDisabled) {
				defaultHandler.setLevel(Level.OFF);
				LOGGER.debug("Since logging disabled, default handler is disabled");
			} else {
				// do nothing
			}
			LOGGER.config("Default console handler's level: "
					+ defaultHandler.getLevel().getName());
		}//

	}

	private final HashMap<String, Handler> refHandlerMap;

	private final HashMap<String, LoggerConfiguration> nameConfigMap;

	private static StandardConsoleHandler defaultHandler = null;

	{
		// logging disabled?
		String disabledPropertyValue = System
				.getProperty(LoggerManager.LOGGING_DISABLED_KEY);
		if (disabledPropertyValue != null) {
			// logging disabled key exists
			if ("false".equalsIgnoreCase(disabledPropertyValue)) {
				loggingDisabled = false;
				LOGGER.config("Logging enabled");
			} else {
				loggingDisabled = true;
				LOGGER.config("Logging disabled");
			}
		}
		// load default level
		loadDefaultLevel();
		setUpDefaultHandler();
	}

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
	 * Applies the configuration for the given logger. If a config file was
	 * found and could get parsed and the given logger has an entry, then it
	 * will be configured as defined in the file. Otherwise the default
	 * configuration gets applied.
	 * 
	 * @param log
	 */
	public void applyConfig(final Logger log) {
		if (nameConfigMap.containsKey(log.getName())) {
			LOGGER.debug(String.format("Found config for logger %s",
					log.getName()));
			final LoggerConfiguration config = nameConfigMap.get(log.getName());
			final Handler h = refHandlerMap.get(config.getHandlerRef());
			if (h != null) {
				log.resetHandlers();
				log.addHandler(h);
			}
			if (config.getLevel() != null) {
				log.setLevel(config.getLevel());
			}
			LOGGER.config(String
					.format("For logger (%s), added handler with ref (%s) and set level to (%s)",
							log.getName(), config.getHandlerRef(),
							config.getLevel()));
		} else {
			// apply default config
			LOGGER.debug(String.format(
					"No config for logger %s found, loading default config.",
					log.getName()));
			applyDefaultConfig(log);
		}
	}

	/**
	 * Applies the default configuration to the given logger. Default means: The
	 * level is INFO and one handler, a StandartConsoleHandler gets added.
	 * 
	 * @param log
	 */
	public void applyDefaultConfig(final Logger log) {
		log.resetHandlers();
		log.addHandler(defaultHandler);
		log.setLevel(defaultLevel);
	}

	/**
	 * Returns the {@link Handler} with the given reference signature.
	 * 
	 * @param ref
	 *            The reference signature of the handler.
	 * @return The handler object or null if none was found.
	 */
	public Handler getHandlerForRef(final String ref) {
		return refHandlerMap.get(ref);
	}

	/**
	 * Loads the config file specified by the given path. It is recommended to
	 * give the absolute path - to minimize errors.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void loadConfigFile(final String file) throws IOException {
		LOGGER.config("Config file: " + file);
		final Json jsonFile = readConfigFile(file);
		// log.debug("Parsed config: \n"+jsonFile.toJson() );
		LOGGER.debug("Successfully parsed config file");
		if (!validateStructure(jsonFile)) {
			throw new IllegalArgumentException("No valid config");
		}
		LOGGER.debug("Validated config file");
		parseHandlers(jsonFile);
		parseLoggers(jsonFile);
		LOGGER.info("Config loaded");
	}

	/**
	 * Register the given {@link Handler} object with the associated reference.
	 * This method is designed to register handlers from another class.
	 * 
	 * @param ref
	 *            The reference the handler is identified with, must be not
	 *            null, not empty and unique.
	 * @param handler
	 *            The handler to register with the given refernece.
	 */
	public void registerHandler(final String ref, final Handler handler) {
		LOGGER.debug(String.format("Registration of %s for reference %s",
				(handler != null ? handler.getClass().getName() : "null"), ref));
		if (ref == null || ref.length() == 0) {
			throw new IllegalArgumentException(
					"Cannot register handler for reference null or empty");
		}
		if (handler == null) {
			throw new IllegalArgumentException("Cannot register handler null");
		}
		if (refHandlerMap.containsKey(ref)) {
			throw new IllegalArgumentException(
					"Cannot register handler for ref" + ref
							+ ", since this reference is already used.");
		}
		if (loggingDisabled) {
			LOGGER.debug(String
					.format("Set %s 's (ref: %s) level to OFF, since logging is disabled.",
							handler.getClass().getName(), ref));
			handler.setLevel(Level.OFF);
		}
		refHandlerMap.put(ref, handler);
	}

	private boolean checkVersion(final String version) {
		return VERSION.equalsIgnoreCase(version);
	}

	private Handler parseHandlerObject(final Json handlerObj)
			throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		final String name = handlerObj.getString(NAME_KEY);
		LOGGER.debug(String
				.format("Parsing handler configuration for %s", name));
		final Class<?> handlerClass = Class.forName(name);
		LOGGER.debug(String.format("Loaded class %s", handlerClass.getName()));
		final Constructor<?> hcConstr = handlerClass.getConstructor(null);
		LOGGER.debug("Found constructor for handler with signature: "
				+ hcConstr.getName());
		final Object hndlrObj = hcConstr.newInstance((Object[]) null);
		if (hndlrObj instanceof Handler) {
			LOGGER.debug(String.format(
					"Loaded and instantiated successfully handler class (%s)",
					handlerClass.getName()));
			final Handler h = (Handler) hndlrObj;
			if (handlerObj.containsEntry(LEVEL_KEY)) {
				final Level lvl = LevelX.parse(handlerObj.getString(LEVEL_KEY));
				if (lvl != null) {
					h.setLevel(lvl);
				}
			}
			return h;
		}
		LOGGER.debug(String.format("Could not instantiate handler %s", name));
		return null;
	}

	private void parseHandlers(final Json config) {
		LOGGER.debug("Parsing handler json objects");
		for (final Json hndlr : config.getSet(HANDLERS_KEY)) {
			try {
				registerHandler(hndlr.getString(REF_KEY),
						parseHandlerObject(hndlr));
			} catch (ClassNotFoundException | NoSuchMethodException
					| SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.warn("Could not parse handler: \n" + hndlr.toJson());
				LOGGER.log(LevelX.DEBUG, "Caught exception", e);
			}
		}// end iterate over handlers
	}

	private LoggerConfiguration parseLoggerObject(final Json loggerObj) {
		LOGGER.debug("Parsing logger object");
		final String handler = loggerObj.getString(HANDLER_KEY);
		final Level lvl = LevelX.parse(loggerObj.getString(LEVEL_KEY));
		LOGGER.debug(String.format(
				"Parsed logger object has handler %s and level %s", handler,
				lvl.toString()));
		return new LoggerConfiguration(handler, lvl);
	}

	private void parseLoggers(final Json config) {
		LOGGER.debug("Parsing logger json objects");
		for (final Json lggr : config.getSet(LOGGERS_KEY)) {
			nameConfigMap
					.put(lggr.getString(NAME_KEY), parseLoggerObject(lggr));
		}
	}

	private Json readConfigFile(final String filename) throws IOException {
		LOGGER.debug("Going to read " + filename);
		final JsonParser parser = new JsonParser();
		final Json config = parser.parseFile(filename);
		LOGGER.debug("Loaded config file to vm");
		return config;
	}

	private boolean validateHandler(final Json handlerObj) {
		if (!handlerObj.containsKey(REF_KEY)) {
			return false;
		}
		if (!handlerObj.containsKey(NAME_KEY)) {
			return false;
		}
		// is likely valid: has ref and name keys
		return true;
	}

	private boolean validateLogger(final Json loggerObj) {
		if (!loggerObj.containsKey(NAME_KEY)) {
			return false;
		}
		if (!loggerObj.containsKey(HANDLER_KEY)) {
			return false;
		}
		// is likey valid format: has name and handler key
		return true;
	}

	private boolean validateStructure(final Json json) {
		if (!validateVersion(json)) {
			return false;
		}
		// version is valid
		if (json.containsSet(HANDLERS_KEY)) {
			for (final Json obj : json.getSet(HANDLERS_KEY)) {
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
			for (final Json obj : json.getSet(LOGGERS_KEY)) {
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

	private boolean validateVersion(final Json parentObj) {
		if (!parentObj.containsKey(VERSION_KEY)) {
			return false;
		}
		return checkVersion(parentObj.getString(VERSION_KEY));
	}
}
