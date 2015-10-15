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
 * <li> {@link Logger#severe(String)} is masked by
 * {@link AbstractLogger#error(String)}</li>
 * <li> {@link Logger#warning(String)} is masked by
 * {@link AbstractLogger#warn(String)}</li>
 * </ul>
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
public class Logger{
	
	private final java.util.logging.Logger logger;

	private static final LoggerManager manager = LoggerManager.getManager();

	public static Logger getLogger(final Class<?> clazz) {
		return getLogger(clazz.getName());
	}
	
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
	 * @param log
	 */
	protected Logger(final java.util.logging.Logger log) {
		this.logger = log;
	}

	public void addHandler(final Handler handler) {
		this.logger.addHandler(handler);
	}

	public void config(final String msg) {
		log(Level.CONFIG, msg);
	}

	public void debug(final String msg) {
		log(LevelX.DEBUG, msg);
	}

	public void error(final String msg) {
		log(LevelX.ERROR, msg);
	}

	public void error(final String msg, final Throwable thrown) {
		log(LevelX.ERROR, msg, thrown);
	}

	public Filter getFilter() {
		return logger.getFilter();
	}

	public Handler[] getHandlers() {
		return logger.getHandlers();
	}

	public Level getLevel() {
		return logger.getLevel();
	}

	public void info(final String msg) {
		log(Level.INFO, msg);

	}

	public void log(final Level level, final String msg) {
		this.logger.log(level, msg);
	}

	public void log(final Level level, final String msg, final Throwable thrown) {
		this.logger.log(level, msg, thrown);
	}

	public void log(final LogRecord record) {
		this.logger.log(record);
	}

	public void removeHandler(final Handler handler) {
		logger.removeHandler(handler);
	}

	public void setFilter(final Filter filter) {
		logger.setFilter(filter);
	}

	public void setLevel(final Level level) {
		logger.setLevel(level);
	}

	public void warn(final String msg) {
		log(LevelX.WARN, msg);
	}

	public void warn(final String msg, final Throwable thrown) {
		log(LevelX.WARN, msg, thrown);
	}
	
	public void resetHandlers() {
		for (final Handler handler : logger.getHandlers()) {
			logger.removeHandler(handler);
		}
	}
}
