/**
 * 
 */
package util.logging;

import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Loris
 * 
 */
public class Logger extends AbstractLogger {

	private static final LoggerManager manager = LoggerManager.getManager();

	public static AbstractLogger getLogger(final Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	public static AbstractLogger getLogger(final String name) {
		final AbstractLogger logger = new Logger(
				java.util.logging.Logger.getLogger(name));
		return manager.registerLogger(logger);
	}

	/**
	 * @param log
	 */
	protected Logger(final java.util.logging.Logger log) {
		super(log);
	}

	@Override
	public void addHandler(final Handler handler) {
		this.logger.addHandler(handler);
	}

	@Override
	public void config(final String msg) {
		log(Level.CONFIG, msg);
	}

	@Override
	public void debug(final String msg) {
		log(LevelX.DEBUG, msg);
	}

	@Override
	public void error(final String msg) {
		log(LevelX.ERROR, msg);
	}

	@Override
	public void error(final String msg, final Throwable thrown) {
		log(LevelX.ERROR, msg, thrown);
	}

	@Override
	public Filter getFilter() {
		return logger.getFilter();
	}

	@Override
	public Handler[] getHandlers() {
		return logger.getHandlers();
	}

	@Override
	public Level getLevel() {
		return logger.getLevel();
	}

	@Override
	public void info(final String msg) {
		log(Level.INFO, msg);

	}

	@Override
	public void log(final Level level, final String msg) {
		this.logger.log(level, msg);
	}

	@Override
	public void log(final Level level, final String msg, final Throwable thrown) {
		this.logger.log(level, msg, thrown);
	}

	@Override
	public void log(final LogRecord record) {
		this.logger.log(record);
	}

	@Override
	public void removeHandler(final Handler handler) {
		logger.removeHandler(handler);
	}

	@Override
	public void setFilter(final Filter filter) {
		logger.setFilter(filter);
	}

	@Override
	public void setLevel(final Level level) {
		logger.setLevel(level);
	}

	@Override
	public void warn(final String msg) {
		log(LevelX.WARN, msg);
	}

	@Override
	public void warn(final String msg, final Throwable thrown) {
		log(LevelX.WARN, msg, thrown);
	}
}
