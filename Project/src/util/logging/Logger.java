/**
 * 
 */
package util.logging;

import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A {@link Logger} logs messages. These messages are mostly from different parts of the 
 * application. For further information about loggers, see {@link java.util.logging.Logger}.
 * <br />
 * The {@link Logger} provides all functionalities to log those messages.
 * <p>
 * To <i>see/read</i> those messages a {@link Handler} needs to be registered by calling the {@link Logger#addHandler(Handler)} method.
 * And then again, the handler needs to have a {@link Formatter} which formats the log message.<br />
 * </p>
 * <p>
 * This class is a wrapper class around the {@link Logger} class of
 * {@code java.util.logging} API. It masks several log methods to more intuitive
 * names and hides other methods to stay simple.
 * </p>
 * <p>
 * The following {@code java.util.logging.}{@link Logger} methods are masked:
 * <ul>
 * <li> {@link java.util.logging.Logger#severe(String)} is masked by
 * {@link Logger#error(String)}</li>
 * <li> {@link java.util.logging.Logger#warning(String)} is masked by
 * {@link Logger#warn(String)}</li>
 * </ul>
 * <p>
 * Additionally {@link java.util.logging.Logger}s can log on {@link LevelX#DEBUG} log
 * level, a level between {@link Level#CONFIG} and {@link Level#FINE}.
 * </p>
 * 
 * @see java.util.logging.Logger
 * @see Level
 * @see LevelX
 * @author Loris
 * 
 */
public final class Logger{
	
	private final java.util.logging.Logger logger;

	private static final LoggerManager manager = LoggerManager.getManager();

	/**
	 * Creates a {@link Logger} for the given class.
	 * The class parameter is used to name the logger, since loggers are settled in a hierarchical name space.
	 * @param clazz The class for which this logger logs messages.
	 * @return A {@link Logger} for the specified class.
	 */
	public static Logger getLogger(final Class<?> clazz) {
		return getLogger(clazz.getName());
	}
	
	/**
	 * Creates a named {@link Logger}.
	 * Usually class names are used to create logger names, but sometimes
	 * a logger needs a different name.
	 * <br />
	 * <b>Note: It is recommended to use the {@link Logger#getLogger(Class)} method in most use cases.</b>
	 * @param name The name of the logger.
	 * @return A {@link Logger} with the specified name.
	 */
	public static Logger getLogger(final String name) {
		final Logger logger = new Logger(
				java.util.logging.Logger.getLogger(name));
		return manager.registerLogger(logger);
	}
	
	/**
	 * Returns the name of the internal java.util.logging.Logger name. This
	 * method gets forwarded to {@link Logger#getName()} and returns the
	 * method's return value.
	 * 
	 * @see Logger#getName()
	 * @return The name of the logger.
	 */
	public String getName() {
		return this.logger.getName();
	}

	/**
	 * Constructs a new {@link Logger}, with the given internal {@link java.util.logging.Logger}.
	 * @param log The internal {@link java.util.logging.Logger}, with which this logger logs.
	 */
	Logger(final java.util.logging.Logger log) {
		this.logger = log;
	}

	/**
	 * Adds the specified handler to this logger.
	 * In particular, the underlying {@link java.util.logging.Logger} gets the specified
	 * handler added.
	 * @param handler The handler for handling log messages.
	 * @see java.util.logging.Logger#addHandler(Handler)
	 */
	public void addHandler(final Handler handler) {
		this.logger.addHandler(handler);
	}

	/**
	 * Logs a message with message level config.
	 * If this logger has a level config or higher enabled, the message will be forwarded
	 * to all its registered handlers.
	 * @see java.util.logging.Logger#log(Level, String)
	 * @param msg The message to log.
	 */
	public void config(final String msg) {
		log(Level.CONFIG, msg);
	}

	/**
	 * Logs a message with message level debug.
	 * If this logger has a level debug or higher enabled, the message will be forwarded
	 * to all its registered handlers.
	 * @see java.util.logging.Logger#log(Level, String)
	 * @param msg The message to log.
	 */
	public void debug(final String msg) {
		log(LevelX.DEBUG, msg);
	}

	/**
	 * Logs a message with message level error.
	 * If this logger has a level error or higher enabled, the message will be forwarded
	 * to all its registered handlers.
	 * @see java.util.logging.Logger#log(Level, String)
	 * @param msg The message to log.
	 */
	public void error(final String msg) {
		log(LevelX.ERROR, msg);
	}

	/**
	 * Logs a message with message level error and given throwable.
	 * If this logger has a level error or higher enabled, the message will be forwarded
	 * to all its registered handlers.
	 * @see java.util.logging.Logger#log(Level, String)
	 * @param msg The message to log.
	 * @param thrown The throwable to give some details
	 */
	public void error(final String msg, final Throwable thrown) {
		log(LevelX.ERROR, msg, thrown);
	}

	/**
	 * Returns the currently active filter for this logger.
	 * This may be <code>null</code> if no filter has set yet.
	 * @return The currently active filter or <code>null</code> if none was set.
	 * @see java.util.logging.Logger#getFilter()
	 */
	public Filter getFilter() {
		return logger.getFilter();
	}

	/**
	 * Returns the registered handlers of this logger.
	 * @return The registered handlers of this logger.
	 * @see java.util.logging.Logger#getHandlers()
	 */
	public Handler[] getHandlers() {
		return logger.getHandlers();
	}

	/**
	 * Returns the currently enabled level of this logger.
	 * @return The currently enabled level of this logger.
	 * @see java.util.logging.Logger#getLevel()
	 */
	public Level getLevel() {
		return logger.getLevel();
	}

	/**
	 * Logs a message with message level config.
	 * If this logger has a level config or higher enabled, the message will be forwarded
	 * to all its registered handlers.
	 * @see java.util.logging.Logger#log(Level, String)
	 * @param msg The message to log.
	 */
	public void info(final String msg) {
		log(Level.INFO, msg);

	}

	/**
	 * Logs a message with specified level.
	 * If this logger has the specified level (or higher) enabled, the message will be forwarded
	 * to all its registered handlers.
	 * @see java.util.logging.Logger#log(Level, String)
	 * @param msg The message to log.
	 * @param level The level of the log
	 */
	public void log(final Level level, final String msg) {
		this.logger.log(level, msg);
	}

	/**
	 * Logs a message with specified level and associated throwable.
	 * If this logger has the specified level (or higher) enabled, the message will be forwarded
	 * to all its registered handlers.
	 * @see java.util.logging.Logger#log(Level, String)
	 * @param msg The message to log.
	 * @param level The level of the log
	 * @param thrown The throwable associated with this log.
	 */
	public void log(final Level level, final String msg, final Throwable thrown) {
		this.logger.log(level, msg, thrown);
	}

	/**
	 * Logs a specified {@link LogRecord}.
	 * @see java.util.logging.Logger#log(LogRecord)
	 * @param record The record to log.
	 */
	public void log(final LogRecord record) {
		this.logger.log(record);
	}

	/**
	 * Removes the specified handler from this logger's handlers list.
	 * The specified handler will no longer receive log messages from this
	 * logger and thus will no longer handle those messages.
	 * @param handler The handler to remove
	 * @see java.util.logging.Logger#removeHandler(Handler)
	 */
	public void removeHandler(final Handler handler) {
		logger.removeHandler(handler);
	}

	public void setFilter(final Filter filter) {
		logger.setFilter(filter);
	}

	public void setLevel(final Level level) {
		logger.setLevel(level);
	}

	/**
	 * Logs a message with message level config.
	 * If this logger has a level config or higher enabled, the message will be forwarded
	 * to all its registered handlers.
	 * @see java.util.logging.Logger#log(Level, String)
	 * @param msg The message to log.
	 */
	public void warn(final String msg) {
		log(LevelX.WARN, msg);
	}

	/**
	 * Logs a message with message level config.
	 * If this logger has a level config or higher enabled, the message will be forwarded
	 * to all its registered handlers.
	 * @see java.util.logging.Logger#log(Level, String)
	 * @param msg The message to log.
	 */
	public void warn(final String msg, final Throwable thrown) {
		log(LevelX.WARN, msg, thrown);
	}
	
	/**
	 * Resets this loggers handler list.
	 * That results in a logger without handlers and thus
	 * no logs of this logger are forwarded.
	 */
	public void resetHandlers() {
		for (final Handler handler : logger.getHandlers()) {
			logger.removeHandler(handler);
		}
	}
}
