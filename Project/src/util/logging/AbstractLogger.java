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
 * The {@link AbstractLogger} describes the interface for {@code util.logging} loggers.
 * 
 * <p>
 * This class is a wrapper class around the {@link Logger} class of {@code java.util.logging} API.
 * It masks several log methods to more intuitive names and hides other methods to be stay simple.
 * </p>
 * <p>
 * The following {@code java.util.logging.}{@link Logger} methods are masked:
 * <ul>
 * <li> {@link Logger#severe(String)} is masked by {@link AbstractLogger#error(String)}</li>
 * <li> {@link Logger#warning(String)} is masked by {@link AbstractLogger#warn(String)}</li>
 * </ul>
 * Additionally {@link AbstractLogger}s can log on {@link LevelX#DEBUG} log level, a level between {@link Level#CONFIG} and
 * {@link Level#FINE}.
 * </p>
 * @see Logger
 * @see Level
 * @see LevelX
 * @author Loris
 *
 */
public abstract class AbstractLogger {

    protected final Logger logger;
    
    protected AbstractLogger(Logger log){
	this.logger = log;
    }
    
    public abstract void error(String msg);
    
    public abstract void error(String msg, Throwable thrown);
    
    public abstract void warn(String msg);

    public abstract void warn(String msg, Throwable thrown);
    
    public abstract void debug(String msg);
    
    public abstract void log(Level level, String msg);
    
    public abstract void log(Level level, String msg, Throwable thrown);
    
    public abstract void log(LogRecord record);
    
    public abstract void addHandler(Handler handler);
    
    public abstract Handler[] getHandlers();
    
    public abstract void removeHandler(Handler handler);
    
    public abstract void setLevel(Level level);
    
    public abstract Level getLevel();
    
    public abstract void setFilter(Filter filter);
    
    public abstract Filter getFilter();
    
}
