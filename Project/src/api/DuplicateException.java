/**
 * 
 */
package api;

import java.util.Vector;

/**
 *An {@link DuplicateException} is thrown when, while running the command replace at least two data sets of the same name are encountered. 
 *
 *
 */
public class DuplicateException extends Exception {
	
	/**
	 * Ecplise generated serialID
	 */
	private static final long serialVersionUID = -2993399127143433563L;
	/**
	 * Default message which is given to the error
	 */
	private static final String defaultMessage = "There are duplicate names in you export command";
	
	/**
	 * Sends default message -> "There are duplicate names in you export command"
	 */
	public DuplicateException(){
		super(defaultMessage);
	}
	/**
	 * 
	 * @param message
	 * String with all duplicate names in the export command
	 */
	public DuplicateException(String message){
		super(message);
	}

}
