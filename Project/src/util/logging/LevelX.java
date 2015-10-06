package util.logging;

import java.util.logging.Level;

public class LevelX extends Level {

    /**
     * Eclipse generated serialID
     */
    private static final long serialVersionUID = 4867372786151143760L;

    public static Level ERROR = new LevelX("ERROR", Level.SEVERE.intValue());

    public static Level WARN = new LevelX("WARN", Level.WARNING.intValue());
    
    public static Level DEBUG = new LevelX("DEBUG", Level.CONFIG.intValue()-100);

    protected LevelX(String name, int value) {
	super(name, value);
    }
    
    public static Level parse(String name){
	if(name == null || name.length() == 0){
	    throw new IllegalArgumentException();
	}
	// TODO integer parsing
	if(name.equalsIgnoreCase(ERROR.getName())){
	    return ERROR;
	}
	if(name.equalsIgnoreCase(WARN.getName() )){
	    return WARN;
	}
	if(name.equalsIgnoreCase(DEBUG.getName() )){
	    return DEBUG;
	}
	if(name.equalsIgnoreCase(INFO.getName())){
	    return INFO;
	}
	if(name.equalsIgnoreCase(CONFIG.getName() )){
	    return CONFIG;
	}
	if(name.equalsIgnoreCase(OFF.getName() )){
	    return OFF;
	}
	throw new IllegalArgumentException();
    }

}
