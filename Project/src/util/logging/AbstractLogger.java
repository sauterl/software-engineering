/**
 * 
 */
package util.logging;

import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * The {@link AbstractLogger} describes the interface for {@code util.logging}
 * loggers.
 * 
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
 * Additionally {@link AbstractLogger}s can log on {@link LevelX#DEBUG} log
 * level, a level between {@link Level#CONFIG} and {@link Level#FINE}.
 * </p>
 * 
 * @see Logger
 * @see Level
 * @see LevelX
 * @author Loris
 * 
 */
public abstract class AbstractLogger {

	/*
	 * TODO Write all log methods TODO Write javadoc for methods
	 */

	protected final Logger logger;

	protected AbstractLogger(final Logger log) {
		this.logger = log;
	}

	public abstract void addHandler(Handler handler);

	public abstract void config(String msg);

	public abstract void debug(String msg);

	public abstract void error(String msg);

	public abstract void error(String msg, Throwable thrown);

	public abstract Filter getFilter();

	public abstract Handler[] getHandlers();

	public abstract Level getLevel();

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

	public abstract void info(String msg);

	public abstract void log(Level level, String msg);

	public abstract void log(Level level, String msg, Throwable thrown);

	public abstract void log(LogRecord record);

	public abstract void removeHandler(Handler handler);

	public void resetHandlers() {
		for (final Handler handler : logger.getHandlers()) {
			logger.removeHandler(handler);
		}
	}

	public abstract void setFilter(Filter filter);

	public abstract void setLevel(Level level);

	public abstract void warn(String msg);

	public abstract void warn(String msg, Throwable thrown);

}
