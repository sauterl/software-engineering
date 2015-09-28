/**
 * 
 */
package api.options;

/**
 * The {@link TextOption} class represents the text option several commands have.
 * 
 * @author Loris
 *
 */
public class TextOption extends AbstractParameteredOption<String> {

    private String parameter;
    
    protected TextOption(){
	super("text");
    }

    public TextOption(String parameter){
	this();
	if(parameter == null){
	    throw new NullPointerException("The parameter of a NameOption must not be null.");
	}
	this.parameter = parameter;
    }
    
    @Override
    public void set(String parameter) {
	if(parameter == null){
	    throw new NullPointerException("The parameter of a NameOption must not be null.");
	}
	this.parameter = parameter;
    }

    @Override
    public String get() {
	return parameter;
    }
    
    public String getText(){
	return get();
    }

}
