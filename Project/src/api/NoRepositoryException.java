/**
 * 
 */
package api;

/**
 * An {@link NoRepositoryException} is thrown if a path, which has to point to a
 * repository, does not point to a valid repo.
 * 
 * @author Loris
 * 
 */
public class NoRepositoryException extends Exception {

    /**
     * Universal default message.
     */
    private static final String defaultMessage = "The given repository path does not point to a valid repository.";

    /**
     * The message ready to get used with
     * {@link String#format(String, Object...)}, where the only passed object is
     * a string containing the path.
     */
    private static final String detailMessage = "The repository path (%s) does not point to a valid repository.";

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
     * Constructs a {@link NoRepositoryException} with specified invalid path.
     * 
     * @param path
     *            The path which does not point to a repository.
     */
    public NoRepositoryException(String path) {
	super(String.format(detailMessage, path));
    }

}
