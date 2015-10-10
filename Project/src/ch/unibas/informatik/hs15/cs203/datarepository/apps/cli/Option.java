package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

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
	for(Option o : values() ){
	    if(str.equalsIgnoreCase(o.name() )){
		return o;
	    }
	}
	return null;
    }
}
