package util.logging;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * The {@link LoggerManager} registers {@link Logger}s by their name and manages
 * them. <br />
 * To read more about logging configuration see {@link ConfigurationManager}.
 * 
 * @author Loris
 * 
 */
public class LoggerManager {

	private static LoggerManager instance = null;
	private static final Logger LOGGER;

	private static Level internalLevel;
	/**
	 * This logging API's name: <b>L</b>ightweight and <b>S</b>imple <b>J</b>ava
	 * <b>L</b>ogging API.<br />
	 * The value is the prefix for every system property of this API.<br />
	 * The value is: {@value}
	 */
	public final static String API_NAME = "lsjl";
	/**
	 * Contains the system property key to specify the log level of the logging
	 * api itself. The value is: {@value}
	 */
	public final static String VERBOSE_LEVEL_KEY = API_NAME + ".verbose.level";
	/**
	 * Contains the system property key to specify if the logging api logs its
	 * actions.The value is: {@value}
	 */
	public final static String VERBOSE_ENABLED_KEY = API_NAME
			+ ".verbose.enabled";
	/**
	 * Contains the system property key to specify the path of the config file.The value is: {@value}
	 */
	public final static String CONFIG_PATH_KEY = API_NAME
			+ ".logging.config.path";
	/**
	 * Contains the system property key to specify if the logging api is
	 * completely disabled.<br />
	 * If a system property with this key exists and is set to <tt>true</tt>,
	 * the logging is completely disabled.The value is: {@value}
	 */
	public final static String LOGGING_DISABLED_KEY = API_NAME
			+ ".logging.disabled";
	/**
	 * Contains the system property key to specify the default logging level. <br />
	 * If no system property with this key exists, the logging is disabled.The value is: {@value}
	 */
	public final static String LOGGING_DEFAULT_LEVEL_KEY = API_NAME
			+ ".logging.level";

	private final static String[] configFileNames = { API_NAME, "logging",
			API_NAME + "-config", "logging-config" };
	private final static String[] configFileSuffixes = { ".json", ".cfg",
			".config" };

	/**
	 * Returns the LoggerManager instance.
	 * 
	 * @return
	 */
	public static LoggerManager getManager() {
		if (instance == null) {
			instance = new LoggerManager();
		}
		return instance;
	}

	static Logger getLoggingLogger(final Class<?> clazz) {
		final Logger l = new Logger(java.util.logging.Logger.getLogger(clazz
				.getSimpleName()));
		l.resetHandlers();
		l.addHandler(new VerboseHandler(internalLevel));
		l.setLevel(LevelX.DEBUG);
		return l;
	}

	private final HashMap<String, Logger> nameLoggerMap;

	private final ConfigurationManager configManager = ConfigurationManager
			.getConfigManager();

	static {
		// verbose preparation
		if (Boolean.parseBoolean(System.getProperty(VERBOSE_ENABLED_KEY))) {
			try {
				internalLevel = LevelX.parse(System
						.getProperty(VERBOSE_LEVEL_KEY));
			} catch (final IllegalArgumentException ex) {
				internalLevel = LevelX.WARN;
			}
		} else {
			internalLevel = Level.OFF;
		}
		LOGGER = getLoggingLogger(LoggerManager.class);
		// check logging disalbed
		if (Boolean.parseBoolean(System.getProperty(LOGGING_DISABLED_KEY))) {
			System.setProperty(LOGGING_DEFAULT_LEVEL_KEY, Level.OFF.getName());
		}
	}

	private LoggerManager() {
		nameLoggerMap = new HashMap<>();
		try {
			final URL url = findConfigFile();
			if (url != null) {
				configManager.setConfigFilePath(url);
//				configManager.loadConfigFile(url.getPath());
				configManager.loadConfigFile();
			} else {
				LOGGER.warn("Did not find a configuration file.");
			}
		} catch (final IOException e) {
			LOGGER.warn("Reading config failed: ", e);
			LOGGER.error("Could not read config file");
		} catch (final IllegalArgumentException ex){
			LOGGER.warn("Config file not parseable: ",ex);
			LOGGER.error("Could not parse config file");
		}
	}

	/**
	 * Returns the names of all registered Loggers.
	 * 
	 * @return Returns the names of all registered and Loggers. If this method
	 *         gets invoked before a single logger has been registered, it
	 *         returns a zero-lengthed string array.
	 */
	public String[] getLoggerNames() {
		if (nameLoggerMap.size() == 0) {
			return new String[0];
		}
		final String[] names = new String[nameLoggerMap.size()];
		final Iterator<String> keysIt = nameLoggerMap.keySet().iterator();
		int i = 0;
		while (keysIt.hasNext()) {
			names[i++] = keysIt.next();
		}
		return names;
	}

	public void resetHandlers(final String loggerName) {
		final Logger log = nameLoggerMap.get(loggerName);
		if (log != null) {
			log.resetHandlers();
		}
	}

	Logger registerLogger(final Logger logger) {
		if (logger == null) {
			throw new NullPointerException(
					"Registering null loggers not allowed");
		}
		if (logger.getName() == null) {
			throw new IllegalArgumentException(
					"Registering anonymous loggers not allowed");
		}
		// logger is valid
		if (nameLoggerMap.containsKey(logger.getName())) {
			// logger is already registered
			return nameLoggerMap.get(logger.getName());
		} else {
			// register the new Logger
			// look if there is a config for the logger
			// if none, go with default
			// else go with created
			configManager.applyConfig(logger);
			nameLoggerMap.put(logger.getName(), logger);
			return logger;
		}
	}

	private URL findConfigFile() {
		final String pathPerProperty = System.getProperty(CONFIG_PATH_KEY);
		URL out = null;
		if (pathPerProperty == null) {
			for (final String suffix : configFileSuffixes) {
				for (final String name : configFileNames) {
					LOGGER.debug("Currently trying to load config file: "
							+ name + suffix);
					out = getClass().getClassLoader()
							.getResource(name + suffix);
					if (out == null) {
						continue;
					} else {
						break;
					}
				}
				if (out == null) {
					continue;
				} else {
					break;
				}
			}
		} else {
			LOGGER.debug("Config file path read from system property is: "
					+ pathPerProperty);
			try {
				out = getClass().getClassLoader().getResource(pathPerProperty);
			} catch (final SecurityException ex) {
				LOGGER.warn("SecurityManager denied access to classloader.");
			}
		}
		LOGGER.debug("Config file path: " + out);
		return out;
	}
}
