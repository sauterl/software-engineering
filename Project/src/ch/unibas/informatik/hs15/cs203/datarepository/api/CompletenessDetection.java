package ch.unibas.informatik.hs15.cs203.datarepository.api;

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
	boolean verifyCompletness();
}
