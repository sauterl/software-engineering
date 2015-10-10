package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

public enum Command {
    
    ADD(Option.DESCRIPTION, Option.MOVE, Option.VERBOSE),
    REPLACE,
    DELETE,
    EXPORT,
    LIST,
    HELP;
    
    private final Option[] allowedOptions;
    
    private Command(Option...allowedOptions){
	this.allowedOptions = allowedOptions;
    }
    
    public Option[] getAvailableOptions(){
	return allowedOptions;
    }
    
    public boolean isOptionAllowed(Option option){
	for(Option o : allowedOptions){
	    if(option.equals(o)){
		return true;
	    }
	}
	return false;
    }
    
    public static Command parse(String str){
	if(str == null || str.length() == 0){
	    return null;
	}
	for(Command c : values() ){
	    if(str.equalsIgnoreCase(c.name() )){
		return c;
	    }
	}
	return null;
    }
    
}
