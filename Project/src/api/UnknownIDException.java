package api;

/**
 * The {@link UnknownIDException} is thrown whenever a data set identifier does
 * not address a data set.
 * 
 * In other words, this exception occurs when no data set is found for the given
 * identifier.
 * 
 * @author Loris
 * 
 */
public class UnknownIDException extends Exception {

    /**
     * Eclipse generated serialUID
     */
    private static final long serialVersionUID = -761613940081242678L;

    /**
     * The default message explaining the general cause of this exception.
     */
    private static final String defaultMessage = "The given identifier does not address a data set.";
    /**
     * The default detail message with a hint what's wrong, ready to get used with {@link String#format(String, Object...)}
     * where the single entry in the Object-vararg list the invalid id is.
     */
    private static final String detailMessage = "The given identifier (%d) does not address a data set.";
    
    /**
     * The unknown/invalid id.
     */
    private int id;
    
    /**
     * Constructs a {@link UnknownIDException} with default message.
     */
    public UnknownIDException() {
	super(defaultMessage);
    }
    
    /**
     * Constructs a {@link UnknownIDException} with given unknown identifier and default message.
     * @param id The identifier which is unknown.
     */
    public UnknownIDException(int id){
	super(String.format(detailMessage, id));
	this.id = id;
    }
    
    /**
     * Constructs a {@link UnknownIDException} with given unknown identifier and detail message.
     * @param message The detail message.
     * @param id The identifier which is unknown.
     */
    public UnknownIDException(String message, int id){
	super(message);
	this.id = id;
    }

    /**
     * Returns the identifier which was unknown.
     * @return The identifier which was unknown.
     */
    public int getUnknownID(){
	return id;
    }
}
