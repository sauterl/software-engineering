package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;
import ch.unibas.informatik.hs15.cs203.datarepository.common.DatasetPortConfiguration;
import util.logging.Logger;

/**
 * Has no constructor. Use PropertiesParser.parse();
 *
 */
class PropertiesParser {
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

	private static final Logger LOG = Logger.getLogger(ParseException.class);

	public static final String INCOMING_DIR_KEY = "incoming-directory";
	public static final String HTML_OVERVIEW_KEY = "html-overview";
	public static final String LOG_FILE_KEY = "log-file";
	public static final String CHECKING_INTERVAL_KEY = "checking-interval-in-seconds";

	public static final String CMPLTNSS_CLASS_KEY = "completeness-detection"
			+ "." + "class-name";

	private static ClassLoader loader = PropertiesParser.class.getClassLoader();

	public static DatasetPortConfiguration parse(final Properties props)
			throws ParseException {
		LOG.info("The properties object: \n" + props.toString());
		final Path inDir = parsePath(props, INCOMING_DIR_KEY);
		Path htmlPath = null;
		if(props.containsKey(HTML_OVERVIEW_KEY)){
			htmlPath = parsePath(props, HTML_OVERVIEW_KEY);
		}
		final Path logPath = parsePath(props, LOG_FILE_KEY);
		final int interval = parseInteger(props, CHECKING_INTERVAL_KEY);
		final Class<? extends CompletenessDetection> strategy = parseDetection(
				props);
		return new DatasetPortConfiguration(inDir, htmlPath, logPath, interval,
				strategy, props);
	}

	public static DatasetPortConfiguration parse(final String filepath)
			throws ParseException {
		final Properties props = new Properties();
		try {
			props.load(new FileReader(filepath));
		} catch (final IOException e) {
			throw new RuntimeException(
					"Error while loading properties file: " + e);
		}
		return parse(props);
	}

	/**
	 * Sets the {@link ClassLoader} with which the parses tries to load the
	 * CompletenessDetection class. <br />
	 * <b>Note: Use this method only if you <i>know</i> what you are doing</b>.
	 * <br />
	 * The default class loader is used if none is specified with this method.
	 *
	 * @param l
	 *            The classloader.
	 */
	public static void setClassLoader(final ClassLoader l) {
		loader = l;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Class<? extends CompletenessDetection> parseDetection(
			final Properties props) throws ParseException {
		final String cName = props.getProperty(CMPLTNSS_CLASS_KEY);
		if (cName == null) {
			throw new ParseException(CMPLTNSS_CLASS_KEY);
		}
		try {
			final Class<?> c = Class.forName(cName, false, loader);
			/*
			 * Seems correctly but does not work:
			 * CompletenessDetection.class.isInstance(c)
			 */
			final Class[] interfaces = c.getInterfaces();
			final List<Class> iList = Arrays.asList(interfaces);
			if (iList.contains(CompletenessDetection.class)) {
				return (Class<? extends CompletenessDetection>) c;
			} else {
				throw new IllegalArgumentException(
						"The specified CompletenessDetection class is not a CompletenessDetection: "
								+ c.getName());
			}

		} catch (final ClassNotFoundException e) {
			throw new IllegalArgumentException(
					"CompletenessDetection class (" + cName
							+ ")was not found. Is the plugin loaded properly?",
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
