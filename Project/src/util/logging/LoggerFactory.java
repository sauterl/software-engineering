package util.logging;

import java.util.logging.Logger;

/**
 * The {@code LoggerFactory} class creates {@link Logger} instances.
 * 
 * @author Loris
 * 
 */
@Deprecated
public class LoggerFactory {

	/*
	 * TODO Rewrite this class to only be a factory method for some
	 * pre-configured loggers
	 */

	private static LoggerFactory instance = null;

	@Deprecated
	public static LoggerFactory getLoggerFactory() {
		if (instance == null) {
			instance = new LoggerFactory();
		}
		return instance;
	}

	/**
	 * Singleton
	 */
	private LoggerFactory() {

	}

	@Deprecated
	public Logger getLoggerForClass(final Class<?> clazz) {
		return Logger.getLogger(clazz.getName());
	}

}
