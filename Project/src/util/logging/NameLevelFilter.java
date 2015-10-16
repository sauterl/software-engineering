/**
 * 
 */
package util.logging;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A {@link Filter} which filters log messages by its level for a specified
 * logger.
 * 
 * <p>
 * In other words, this filter restricts log messages of the logger with the
 * specified name below the given level.
 * </p>
 * 
 * @author Loris
 * 
 */
@Deprecated
public class NameLevelFilter implements Filter {

	private final String name;
	private final Level level;

	/**
     * 
     */
	public NameLevelFilter(final String loggerName, final Level level) {
		this.name = loggerName;
		this.level = level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Filter#isLoggable(java.util.logging.LogRecord)
	 */
	@Override
	public boolean isLoggable(final LogRecord record) {
		if (record.getLoggerName().equals(name)
				&& record.getLevel().intValue() >= level.intValue()) {
			return true;
		}
		return false;
	}

}
