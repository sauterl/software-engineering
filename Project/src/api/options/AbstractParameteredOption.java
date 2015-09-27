/**
 * 
 */
package api.options;

/**
 * The {@link AbstractParameteredOption} class represents a named option with a parameter.
 * Since different options need different parameter types, this abstract class is written generic.
 * 
 * Any derived class specifies its parameter's type and sets the name in the subclasses constructor.
 * 
 * @author Loris
 *
 */
public abstract class AbstractParameteredOption<T> extends AbstractOption{
    
    /**
     * Constructs a new named parameter option.
     * 
     * @param name The non empty name of this option. The name is not <code>null</code> nor does it contain non-alphabetical characters.
     * @throws IllegalArgumentException If the {@code name} is empty or does contain any non-alphabetical character.
     * @throws NullPointerException If the {@code name} is <code>null</code>.
     */
    protected AbstractParameteredOption(String name)
	    throws IllegalArgumentException, NullPointerException {
	super(name);
    }

    /**
     * Sets the parameter of this option.
     * @param parameter The parameter of the option.
     */
    public abstract void set(T parameter);
    
    /**
     * Returns the parameter's value.
     * @return The parameter's value.
     */
    public abstract T get();
    

}
