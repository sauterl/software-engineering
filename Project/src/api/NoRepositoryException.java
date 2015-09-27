/**
 * 
 */
package api;

/**
 * An {@link NoRepositoryException} is thrown if a path, which has to point to a repository, does not point to a valid repo.
 * @author Loris
 *
 */
public class NoRepositoryException extends Exception {
    
    private static final String defaultMessage = "The given repository path does not point to a valid repository";

    /**
     * Eclipse auto-generated serialVersionUID.
     */
    private static final long serialVersionUID = -8081711227907755064L;

    /**
     * Constructs a {@link NoRepositoryException} with no specific message.
     */
    public NoRepositoryException() {
	super(defaultMessage);
    }

    /**
     * Constructs a {@link NoRepositoryException} with the specified detail message.
     * @param message
     */
    public NoRepositoryException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public NoRepositoryException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public NoRepositoryException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public NoRepositoryException(String message, Throwable cause,
	    boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
	// TODO Auto-generated constructor stub
    }

}
