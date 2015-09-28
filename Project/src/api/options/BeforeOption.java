/**
 * 
 */
package api.options;

/**
 * The {@link BeforeOption} class represents the before option several commands
 * have.
 * 
 * Timestamps are passed with data type long.
 * 
 * Whenever the word <i>parameter</i> is used within the documentation of this
 * option, the option's parameter is referred to, not the method's one.
 * 
 * @author Loris
 * 
 */
public class BeforeOption extends AbstractParameteredOption<Long> {

    /**
     * This {@link BeforeOption}'s parameter's value.
     */
    private long value;

    /**
     * Creates a new {@link BeforeOption} without a parameter set.
     */
    protected BeforeOption() {
	super("after");
    }

    /**
     * Creates a new {@link BeforeOption} with the given timestamp as parameter.
     * 
     * @param timestamp
     *            This option's parameter value.
     */
    public BeforeOption(long timestamp) {
	this();
	this.value = timestamp;
    }

    /**
     * Sets this {@link BeforeOption}'s parameter value.
     * 
     * @param parameter
     *            The option's parameter value, which is in the context of this
     *            {@link BeforeOption} a timestamp.
     */
    @Override
    public void set(Long parameter) {
	value = parameter;
    }

    /**
     * Returns this {@link BeforeOption}'s parameter value.
     * 
     * @return This option's parameter value, which is in the context of this
     *         {@link BeforeOption} a timestamp.
     */
    @Override
    public Long get() {
	return value;
    }

    /**
     * Returns this {@link BeforeOption}'s timestamp. This method behaves
     * exactly the same as {@link BefreOption#get()}.
     * 
     * @return This option's timestamp.
     * @see BeforeOption#get()
     */
    public long getBefore() {
	return get();
    }

}
