/**
 * 
 */
package api.options;

/**
 * The {@link NameOption} class represents the name option several commands
 * have.
 * 
 * Whenever the word <i>parameter</i> is used within the documentation of this
 * option, the option's parameter is referred to, not the method's one.
 * 
 * @author Loris
 * 
 */
public class NameOption extends AbstractParameteredOption<String> {
    /**
     * The parameter's value
     */
    private String param;

    /**
     * Creates a new NameOption without a paramter set.
     */
    protected NameOption() {
	super("name");
    }

    /**
     * Creates a new NameOption with given parameter.
     * 
     * @param parameter
     *            The option's parameter value (which is anything but
     *            <code>null</code>).
     * @throws NullPointerException
     *             If the given {@code parameter} is <code>null</code>
     */
    public NameOption(String parameter) throws NullPointerException {
	this();
	if (parameter == null) {
	    throw new NullPointerException(
		    "The parameter of a NameOption must not be null.");
	}
	param = parameter;
    }

    /**
     * Sets the parameter's value for this {@link NameOption}.
     * 
     * @param parameter
     *            The option's parameter value (which is anything but
     *            <code>null</code>).
     * @throws NullPointerException
     *             If the given {@code parameter} is <code>null</code>
     */
    @Override
    public void set(String parameter) throws NullPointerException {
	if (parameter == null) {
	    throw new NullPointerException(
		    "The parameter of a NameOption must not be null.");
	}
	param = parameter;
    }

    /**
     * Returns the parameter's value of this {@link NameOption}.
     * 
     * @return The parameter's value of this {@link NameOption}. In case no
     *         parameter's value was set, <code>null</code> is returned.
     */
    @Override
    public String get() {
	return param;
    }

    /**
     * Returns the {@link NameOption}'s name, so it's value. This method behaves
     * exactly the same as the {@link NameOption#get()} method.
     * 
     * @return The {@link NameOption}'s name, thus it's value.
     * @see NameOption#get()
     */
    public String getName() {
	return get();
    }

}
