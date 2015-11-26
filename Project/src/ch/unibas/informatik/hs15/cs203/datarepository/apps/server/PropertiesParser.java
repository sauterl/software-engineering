package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Properties;

import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;

/**
 * Has no constructor. Use PropertiesParser.parse();
 * @author Silvan
 *
 */
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

	public static DatasetPortConfiguration parse(String filepath) {
		Path inDir = parsePath(INCOMING_DIR_KEY);
		Path htmlPath = parsePath(HTML_OVERVIEW_KEY);
		Path logPath = parsePath(LOG_FILE_KEY);
		int interval = parseInteger(CHECKING_INTERVAL_KEY);
		CompletenessDetection strategy = parseDetection();
		return new DatasetPortConfiguration(inDir, htmlPath, logPath, interval, strategy.getClass());
	}

	private static CompletenessDetection parseDetection() {
		// TODO Auto-generated method stub
		return null;
	}

	private static Path parsePath(String key) throws ParseException {
//		try {
//			String path = props.getProperty(key);
//			if (path != null) {
//				return Paths.get(path);
//			} else {
//				throw new ParseException(key);
//			}
//		} catch (InvalidPathException ex) {
//			throw new ParseException(key, ex);
//		}
		return null;
	}
	
	private static int parseInteger(String key) throws ParseException{
//		try{
//			String it = props.getProperty(key);
//			if(it != null){
//				return Integer.parseInt(it);
//			}else{
//				throw new ParseException(key);
//			}
//		}catch(NumberFormatException ex){
//			throw new ParseException(key, ex);
//		}
		return 0;
	}

}
