package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Properties;

public class PropertiesParser {

	public static final String INCOMING_DIR_KEY = "incoming-directory";
	public static final String HTML_OVERVIEW_KEY = "html-overview";
	public static final String LOG_FILE_KEY = "log-file";
	public static final String CHECKING_INTERVAL_KEY = "checking-interval-in-seconds";
	public static final String CMPLTNSS_CLASS_KEY = "completeness-detection"
			+ "." + "class-name";

	public static class ParseException extends RuntimeException {
		public ParseException(String key) {
			super(String.format(
					"The following key was expected but could not get found: %s.",
					key));
		}

		public ParseException(Throwable cause) {
			super("Failed parsing due to the specified cause.", cause);
		}

		public ParseException(String key, Throwable cause) {
			super(String.format(
					"Failed parsing entry for key %s due to the specified cause.",
					key), cause);
		}
	}

	private final Properties props;

	public PropertiesParser(final String path) throws IOException {
		props = loadProperties(path);
	}

	public DatasetPortConfiguration parse() {
		Path inDir = parsePath(INCOMING_DIR_KEY);
		Path htmlPath = parsePath(HTML_OVERVIEW_KEY);
		Path logPath = parsePath(LOG_FILE_KEY);
		int interval = parseInteger(CHECKING_INTERVAL_KEY);
		return new DatasetPortConfiguration(inDir, htmlPath, logPath, interval);
	}

	private Path parsePath(String key) throws ParseException {
		try {
			String path = props.getProperty(key);
			if (path != null) {
				return Paths.get(path);
			} else {
				throw new ParseException(key);
			}
		} catch (InvalidPathException ex) {
			throw new ParseException(key, ex);
		}
	}
	
	private int parseInteger(String key) throws ParseException{
		try{
			String it = props.getProperty(key);
			if(it != null){
				return Integer.parseInt(it);
			}else{
				throw new ParseException(key);
			}
		}catch(NumberFormatException ex){
			throw new ParseException(key, ex);
		}
	}

	/**
	 * Loads a property file f
	 * 
	 * @throws IOException
	 *             is thrown if either the firom a given filepath
	 * @param filepath
	 *            has to be the path to a well formatted propertyfile
	 * @returnlepath is incorrect or the data is formatted incorrectly
	 */
	private Properties loadProperties(final String filepath)
			throws IOException {
		final Properties output = new Properties();
		final FileInputStream in = new FileInputStream(filepath);
		if (filepath.endsWith(".xml")) {
			output.loadFromXML(in);
		} else {
			output.load(in);
		}
		return output;
	}

}
