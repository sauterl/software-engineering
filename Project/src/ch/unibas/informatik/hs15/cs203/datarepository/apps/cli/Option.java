package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

/**
 * The {@link Option} enum represents available options for commands of this application.
 * 
 * Options have an {@link Option#OPTION_MARKER} which is an indicator that a given argument is an option name.
 * Without this marker, options are not recognized as themselves.
 * 
 * Usually options do have an argument, which is directly followed by the option name, but some
 * options do not have an argument and are called 'flag' options.
 * 
 * An example of an option with argument would be <code>--description "The description"</code>
 * Do note the quotes of the text and the whitespace between the option's name (description) and its
 * argument ("The description").
 * 
 * The option <code>--verbose</code> is a typical 'flag' option.
 * 
 * <b>Note:</b> Option names are case insensitive
 * 
 * @author Loris
 *
 */
public enum Option{
    
    DESCRIPTION,
    MOVE(true),
    VERBOSE(true),
    ID,
    NAME,
    TEXT,
    BEFORE,
    AFTER;
    
    public static final String OPTION_MARKER = "--";

    private Option(){
	this(false);
    }
    
    private boolean flag;
    
    private Option(boolean flag){
	this.flag = flag;
    }
    
    public boolean isFlag(){
	return flag;
    }
    
    public static Option parse(String str){
	if(str == null || str.length() == 0){
	    return null;
	}
	if(isStringOption(str)){
	    str = str.substring(OPTION_MARKER.length() );
	}
	for(Option o : values() ){
	    if(str.equalsIgnoreCase(o.name() )){
		return o;
	    }
	}
	return null;
    }
    
    public static boolean isStringOption(String str){
	if(str == null || str.length() == 0){
	    return false;
	}else{
	    return str.startsWith(OPTION_MARKER);
	}
    }
}
