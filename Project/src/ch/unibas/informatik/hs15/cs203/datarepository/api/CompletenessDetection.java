package ch.unibas.informatik.hs15.cs203.datarepository.api;

import java.nio.file.Path;
import java.util.Properties;

/**
 * This is an interface for the completness detection methods used when this programm runs as server
 *
 */
public interface CompletenessDetection {
	/**
	 * Initializes the detection by parsing and checking the properties
	 * @param properties
	 * These are {@link #Properties} which contain the parameters for the detection. 
	 */
	void initializeDetection(Properties properties);
	/**
	 * Runs a completness check according to the actual implementation
	 * @return
	 * True if complete, false if not complete
	 */
	boolean verifyCompletness(Path file);
	
	public static class CompletenessException extends RuntimeException{

		/**
		 * Generated UID
		 */
		private static final long serialVersionUID = 1891152562381110832L;
		
		private String logMessage;
		
		public String getLogMessage(){
			return logMessage;
		}
		
		public CompletenessException(String msg, String logMessage){
			super(msg);
			this.logMessage = logMessage;
		}
		
		public CompletenessException(String logMessage){
			super();
			this.logMessage = logMessage;
		}
		
		public CompletenessException(String msg, String logMessage, Throwable cause){
			super(msg, cause);
			this.logMessage = logMessage;
		}
		
		public CompletenessException(String logMessage, Throwable cause){
			super(cause);
			this.logMessage = logMessage;
		}
	}
}
