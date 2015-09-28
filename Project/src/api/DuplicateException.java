/**
 * 
 */
package api;

/**
 * An {@link DuplicateException} is thrown when, while running the command
 * replace at least two data sets of the same name are encountered.
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
    private static final String defaultMessage = "There are duplicate data set entries in your export command";

    /**
     * Constructs a {@link DuplicateException} with default message.
     */
    public DuplicateException() {
	super(defaultMessage);
    }

    /**
     * Constructs a {@link DuplicateException} with detail message.
     * 
     * @param message
     *            The detail message.
     */
    public DuplicateException(String message) {
	super(message);
    }

}
