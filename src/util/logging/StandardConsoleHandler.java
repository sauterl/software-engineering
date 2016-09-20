package util.logging;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * The {@link StandardConsoleHandler} is a {@link ConsoleHandler} which logs to
 * {@link System#out}.
 * 
 * The logs get formatted using the {@link StandardFormatter} and the default
 * object has no {@link Filter} set.
 * 
 * @author Loris
 * 
 */
public class StandardConsoleHandler extends StreamHandler {

	/**
	 * Creates a new {@link StandardConsoleHandler} with default settings.
	 * Forwarded logs are formatted using the {@link StandardFormatter}, the log
	 * level {@link Level#INFO} is set and no {@link Filter} is installed.
	 * 
	 * @see Handler
	 */
	public StandardConsoleHandler() {
		config(null, null, null, null);
	}

	/**
	 * Creates a {@link StandardConsoleHandler} with specified log level and
	 * otherwise default configuration.
	 * 
	 * @param level
	 *            The new value for the log level
	 */
	public StandardConsoleHandler(final Level level) {
		config(null, null, level, null);
	}

	/**
	 * Configures this Handler with the specified parameters. Each parameter can
	 * be <code>null</code> if so, the default value is used.
	 * 
	 * @param outStream
	 *            The output stream to publish logs on. Defaults to
	 *            {@link System#out}.
	 * @param formatter
	 *            The formatter to use to format logs. Defaults to
	 *            {@link StandardFormatter}.
	 * @param level
	 *            The minimal log level, log messages below this level are
	 *            ignored. Default is {@link Level#INFO}.
	 * @param filter
	 *            A filter to separate which log messages get published. Default
	 *            is <code>null</code>.
	 */
	private void config(OutputStream outStream, Formatter formatter,
			Level level, final Filter filter) {
		if (outStream == null) {
			outStream = System.out;
		}
		if (formatter == null) {
			formatter = new StandardFormatter();
		}
		if (level == null) {
			level = Level.INFO;
		}
		setOutputStream(outStream);
		setFormatter(formatter);
		setLevel(level);
		if (filter != null) {
			setFilter(filter);
		}

	}
	
	/**
     * Publish a <tt>LogRecord</tt>.
     * <p>
     * The logging request was made initially to a <tt>Logger</tt> object,
     * which initialized the <tt>LogRecord</tt> and forwarded it here.
     * <p>
     * @param  record  description of the log event. A null record is
     *                 silently ignored and is not published
     */
    @Override
    public void publish(LogRecord record) {
        super.publish(record);
        flush();
    }

    /**
     * Override <tt>StreamHandler.close</tt> to do a flush but not
     * to close the output stream.  That is, we do <b>not</b>
     * close <tt>System.err</tt>.
     */
    @Override
    public void close() {
        flush();
    }
}
