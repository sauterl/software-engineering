/**
 * 
 */
package api.options;

/**
 * The {@link NameOption} class represents the name option several commands have.
 * @author Loris
 *
 */
public class NameOption extends AbstractParameteredOption<String> {
    /**
     * The parameter's value
     */
    private String param;

    /**
     * Creates a new NameOption.
     */
    protected NameOption(){
	super("name");
    }
    
    /**
     * Creates a new NameOption with given parameter.
     * @param parameter The parameter of the option.
     */
    public NameOption(String parameter){
	this();
	param = parameter;
    }

    @Override
    public void set(String parameter) {
	param = parameter;
    }

    @Override
    public String get() {
	return param;
    }

}
