package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;

/**
 * Has no constructor. Use PropertiesParser.parse();
 *
 * @author Silvan
 *
 */
public class PropertiesParser {

	public static class ParseException extends RuntimeException {
		/**
		 * Eclipse generated
		 */
		private static final long serialVersionUID = 6025360097347436337L;

		public ParseException(final String key) {
			super(String.format(
					"The following key was expected but could not get found: %s.",
					key));
		}

		public ParseException(final String key, final Throwable cause) {
			super(String.format(
					"Failed parsing entry for key %s due to the specified cause.",
					key), cause);
		}

		public ParseException(final Throwable cause) {
			super("Failed parsing due to the specified cause.", cause);
		}
	}

	public static final String INCOMING_DIR_KEY = "incoming-directory";
	public static final String HTML_OVERVIEW_KEY = "html-overview";
	public static final String LOG_FILE_KEY = "log-file";
	public static final String CHECKING_INTERVAL_KEY = "checking-interval-in-seconds";

	public static final String CMPLTNSS_CLASS_KEY = "completeness-detection"
			+ "." + "class-name";

	public static DatasetPortConfiguration parse(final String filepath)
			throws ParseException {
		final Properties props = new Properties();
		try {
			props.load(new FileReader(filepath));
		} catch (final IOException e) {
			throw new RuntimeException(
					"Error while loading properties file: " + e);
		}
		final Path inDir = parsePath(props, INCOMING_DIR_KEY);
		final Path htmlPath = parsePath(props, HTML_OVERVIEW_KEY);
		final Path logPath = parsePath(props, LOG_FILE_KEY);
		final int interval = parseInteger(props, CHECKING_INTERVAL_KEY);
		final Class<? extends CompletenessDetection> strategy = parseDetection(
				props);
		return new DatasetPortConfiguration(inDir, htmlPath, logPath, interval,
				strategy);
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends CompletenessDetection> parseDetection(
			final Properties props) throws ParseException {

		try {
			final Class<?> c = Class
					.forName(props.getProperty(CMPLTNSS_CLASS_KEY));
			if (CompletenessDetection.class.isInstance(c)) {
				return (Class<? extends CompletenessDetection>) c;
			} else {
				throw new ParseException(
						"The specified CompletenessDetection class is not a CompletenessDetection: "
								+ c.getName());
			}

		} catch (final ClassNotFoundException e) {
			throw new ParseException(
					"CompletenessDetection class was not found. Is the plugin loaded properly?",
					e);
		}

	}

	private static int parseInteger(final Properties props, final String key)
			throws ParseException {
		try {
			final String it = props.getProperty(key);
			if (it != null) {
				return Integer.parseInt(it);
			} else {
				throw new ParseException(key);
			}
		} catch (final NumberFormatException ex) {
			throw new ParseException(key, ex);
		}
	}

	private static Path parsePath(final Properties props, final String key)
			throws ParseException {
		try {
			final String path = props.getProperty(key);
			if (path != null) {
				return Paths.get(path);
			} else {
				throw new ParseException(key);
			}
		} catch (final InvalidPathException ex) {
			throw new ParseException(key, ex);
		}
	}

}
