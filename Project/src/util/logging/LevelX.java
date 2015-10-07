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
    
    /**
     * Parses a given name to a level.
     * The mapping is only to levels ERROR, WARN, INFO, DEBUG and CONFIG (as well as OFF).
     * The parameter can either be the name (case gets ignored) or the level's value.
     * If no level mapping was found, an IllegalArgumentException is thrown.
     * @param name The name of the level or its integer value.
     * @return The corresponding level
     * @throws IllegalArgumentException If the parameter was null, had zero length (was empty string) or no mapping was found.
     */
    public static Level parse(String name) throws IllegalArgumentException{
	if(name == null || name.length() == 0){
	    throw new IllegalArgumentException("Cannot parse null or empty string.");
	}
	try{
	    return parseValue(Integer.parseInt(name));
	}catch(NumberFormatException ex){
	    return parseName(name);
	}
    }
    
    private static Level parseName(String name){
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
	throw new IllegalArgumentException("No mapping was found for name: "+name);
    }
    
    /**
     * Parses a given integer value to the mapping Level.
     * The levels defined in LevelX are also considered and
     * if no matching was found, a illegalargumentexception is thrown.
     * @param value The level value
     * @return The corresponding level.
     */
    private static Level parseValue(int value){
	if(value == ERROR.intValue() ){
	    return ERROR;
	}
	if(value == WARN.intValue() ){
	    return WARN;
	}
	if(value == DEBUG.intValue() ){
	    return DEBUG;
	}
	if(value == INFO.intValue() ){
	    return INFO;
	}
	if(value == CONFIG.intValue() ){
	    return CONFIG;
	}
	if(value == OFF.intValue() ){
	    return OFF;
	}
	throw new IllegalArgumentException("Unknown level value: "+value);
    }

}
