/**
 * 
 */
package util.logging;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A {@link Filter} which filters log messages by its level for a specified logger.
 * 
 * <p>
 * In other words, this filter acts if the given 
 * </p>
 * @author Loris
 *
 */
public class NameLevelFilter implements Filter {
    
    private final String name;
    private final Level level;

    /**
     * 
     */
    public NameLevelFilter(String loggerName, Level level) {
	this.name = loggerName;
	this.level = level;
    }

    /* (non-Javadoc)
     * @see java.util.logging.Filter#isLoggable(java.util.logging.LogRecord)
     */
    @Override
    public boolean isLoggable(LogRecord record) {
	// TODO Auto-generated method stub
	return false;
    }

}
