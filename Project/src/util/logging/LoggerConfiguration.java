package util.logging;

import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;

/**
 * TODO write javadoc
 * @author Loris
 *
 */
public class LoggerConfiguration {
    
    private Level loggerLevel;
    private Level handlerLevel;
    
    private Filter loggerFilter;
    
    @Deprecated
    private Filter handlerFilter;
    
    private Handler[] handlers;
    private volatile boolean singleHandler = false;//volatile to be ensure correct value
    private Formatter formatter;

    public LoggerConfiguration() {
	
    }
    
    public LoggerConfiguration(Level level, Filter filter, Formatter formatter, Handler...handlers){
	if(level == null){
	    level = Level.INFO;
	}
	loggerLevel = level;
	handlerLevel = level;
	
	loggerFilter = filter;
	
	if(formatter == null){
	    formatter = new StandartFormatter();
	}
	this.formatter = formatter;
	
	if(handlers.length > 1){
	    this.handlers = new Handler[handlers.length];
	    for(int i=0; i<handlers.length; i++){
		this.handlers[i] = handlers[i];
	    }
	}else{
	    singleHandler = true;
	    this.handlers = new Handler[1];
	    this.handlers[0] = handlers[0];
	}
    }

    /**
     * @return the loggerLevel
     */
    public Level getLoggerLevel() {
        return loggerLevel;
    }

    /**
     * @return the handlerLevel
     */
    public Level getHandlerLevel() {
        return handlerLevel;
    }

    /**
     * @return the loggerFilter
     */
    public Filter getLoggerFilter() {
        return loggerFilter;
    }

    /**
     * @return the handlerFilter
     */
    @Deprecated
    public Filter getHandlerFilter() {
        return handlerFilter;
    }

    /**
     * @return the handlers
     */
    public Handler[] getHandlers() {
        return handlers;
    }

    /**
     * @return the formatters
     */
    public Formatter getFormatters() {
        return formatter;
    }

}
