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

    /**
     * @param log
     */
    protected Logger(java.util.logging.Logger log) {
	super(log);
    }

    @Override
    public void error(String msg) {
	log(LevelX.ERROR, msg);
    }

    @Override
    public void error(String msg, Throwable thrown) {
	log(LevelX.ERROR, msg, thrown);
    }

    @Override
    public void warn(String msg) {
	log(LevelX.WARN, msg);
    }

    @Override
    public void warn(String msg, Throwable thrown) {
	log(LevelX.WARN, msg, thrown);
    }

    @Override
    public void debug(String msg) {
	log(LevelX.DEBUG, msg);
    }

    @Override
    public void log(Level level, String msg) {
	this.logger.log(level, msg);
    }

    @Override
    public void log(Level level, String msg, Throwable thrown) {
	this.logger.log(level, msg, thrown);
    }

    @Override
    public void log(LogRecord record) {
	this.logger.log(record);
    }

    @Override
    public void addHandler(Handler handler) {
	this.logger.addHandler(handler);
    }

    @Override
    public Handler[] getHandlers() {
	return logger.getHandlers();
    }

    @Override
    public void removeHandler(Handler handler) {
	logger.removeHandler(handler);
    }

    @Override
    public void setLevel(Level level) {
	logger.setLevel(level);
    }

    @Override
    public Level getLevel() {
	return logger.getLevel();
    }

    @Override
    public void setFilter(Filter filter) {
	logger.setFilter(filter);
    }

    @Override
    public Filter getFilter() {
	return logger.getFilter();
    }
    
    public static AbstractLogger getLogger(Class<?> clazz){
	return getLogger(clazz.getName() );
    }
    
    public static AbstractLogger getLogger(String name){
	AbstractLogger logger = new Logger(java.util.logging.Logger.getLogger(name));
	return manager.registerLogger(logger);
    }
    
    static Logger getLoggingLogger(Class<?> clazz){
	// TODO may pass responsibility to loggermanager.
	Logger l = new Logger(java.util.logging.Logger.getLogger(clazz.getSimpleName()));
	l.resetHandlers();
	l.addHandler( new VerboseHandler(LevelX.DEBUG));
	l.setLevel(LevelX.DEBUG);
	return l;
    }

    @Override
    public void info(String msg) {
	log(Level.INFO, msg);
	
    }

    @Override
    public void config(String msg) {
	log(Level.CONFIG, msg);
    }
}
