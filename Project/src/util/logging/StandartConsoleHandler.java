package util.logging;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;

/**
 * The {@link StandartConsoleHandler} is a {@link ConsoleHandler} which logs to
 * {@link System#out}.
 * 
 * The logs get formatted using the {@link StandartFormatter} and the default
 * object has no {@link Filter} set.
 * 
 * @author Loris
 * 
 */
public class StandartConsoleHandler extends ConsoleHandler {

    /**
     * Creates a new {@link StandartConsoleHandler} with default settings.
     * Forwarded logs are formatted using the {@link StandartFormatter}, the log
     * level {@link Level#INFO} is set and no {@link Filter} is installed.
     * 
     * @see Handler
     */
    public StandartConsoleHandler() {
	config(null, null, null, null);
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
     *            {@link StandartFormatter}.
     * @param level
     *            The minimal log level, log messages below this level are
     *            ignored. Default is {@link Level#INFO}.
     * @param filter
     *            A filter to separate which log messages get published. Default
     *            is <code>null</code>.
     */
    private void config(OutputStream outStream, Formatter formatter,
	    Level level, Filter filter) {
	if (outStream == null) {
	    outStream = System.out;
	}
	if (formatter == null) {
	    formatter = new StandartFormatter();
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
     * Creates a {@link StandartConsoleHandler} with specified log level and
     * otherwise default configuration.
     * 
     * @param level
     *            The new value for the log level
     */
    public StandartConsoleHandler(Level level) {
	config(null, null, level, null);
    }
}
