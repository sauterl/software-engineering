/**
 * 
 */
package api.options;

/**
 * The {@link TextOption} class represents the text option several commands
 * have.
 * 
 * Whenever the word <i>parameter</i> is used within the documentation of this
 * option, the option's parameter is referred to, not the method's one.
 * 
 * @author Loris
 * 
 */
public class TextOption extends AbstractParameteredOption<String> {

    /**
     * The option's parameter value.
     */
    private String parameter;

    /**
     * Constructs a new {@link TextOption} with no parameter value set.
     */
    protected TextOption() {
	super("text");
    }

    /**
     * Constructs a new {@link TextOption} with given parameter value. The
     * parameter value must not be <code>null</code>, otherwise a
     * {@link NullPointerException} will be thrown.
     * 
     * @param parameter
     *            This option's parameter value, in this context a text snippet.
     * @throws NullPointerException
     *             If the given {@code parameter} is <code>null</code>.
     */
    public TextOption(String parameter) {
	this();
	if (parameter == null) {
	    throw new NullPointerException(
		    "The parameter of a NameOption must not be null.");
	}
	this.parameter = parameter;
    }

    /**
     * Sets this {@link TextOption}'s parameter value, which is in the context
     * of this class a text snippet. The parameter value must not be
     * <code>null</code>, otherwise a {@link NullPointerException} will be
     * thrown.
     * 
     * @param parameter
     *            This option's parameter value, in this context a text snippet.
     * @throws NullPointerException
     *             If the given {@code parameter} is <code>null</code>.
     */
    @Override
    public void set(String parameter) {
	if (parameter == null) {
	    throw new NullPointerException(
		    "The parameter of a NameOption must not be null.");
	}
	this.parameter = parameter;
    }

    /**
     * Returns this {@link TextOption}'s parameter value, in this context a text
     * snippet.
     * 
     * @return This option's parameter value, in the context of this class a
     *         text snippet.
     */
    @Override
    public String get() {
	return parameter;
    }

    /**
     * Returns this {@link TextOption}'s text snippet. This method behaves
     * exactly the same as {@link TextOption#get()}.
     * 
     * @return This option's text snippet.
     */
    public String getTextSnippet() {
	return get();
    }

}
