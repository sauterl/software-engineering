/**
 * 
 */
package api.options;

/**
 * The {@link AbstractOption} class represents a named option with no parameter.
 * 
 * An unnamed option without parameter could be a flag.
 * 
 * @author Loris
 *
 */
public abstract class AbstractOption {
    
    /**
     * The prefix of each option name: '--' (two times the minus character).
     */
    public static final String OPTION_PREFIX = "--";

    /**
     * The name of the option.
     */
    private final String name;
    
    /**
     * Creates a new option with the given name.
     * @param name The name of the option. The {@code name} is not empty nor does it contain anything but alphabetical characters.
     * @throws IllegalArgumentException If the {@code name} is empty or does contain any non-alphabetical character.
     * @throws NullPointerException If the {@code name} is <code>null</code>.
     */
    protected AbstractOption(String name) throws IllegalArgumentException {
	if(name == null){
	    throw new NullPointerException("The name of an option must not be null!");
	}
	if(name.length() <= 0){
	    throw new IllegalArgumentException("The name of an option must not be empty!");
	}
	// TODO: Write regex-check if only alphabetical characters
	this.name = name;
    }
    
    /**
     * Returns the simple name of this option.
     * @return The simple name of this option.
     */
    public String getSimpleName(){
	return name;
    }
    
    /**
     * Returns this option's appearance in the synopsis of commands without it's parameters name.
     * If this option would be named like 'option' this method would return {@code --option}.
     * @return This option's appearance in the synopsis of commands without it's parameters name.
     */
    public final String getOptionName(){
	return OPTION_PREFIX+name;
    }

}
