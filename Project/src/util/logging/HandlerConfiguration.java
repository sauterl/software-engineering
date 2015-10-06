package util.logging;

import java.util.logging.Level;

public class HandlerConfiguration {

    private String ref;
    private String name;
    private Level level;
    
    public HandlerConfiguration(String name, Level level) {
	this.name = name;
	this.level = level;
    }

}
