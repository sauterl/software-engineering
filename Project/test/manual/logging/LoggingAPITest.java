package manual.logging;

import java.util.logging.Handler;

import util.logging.AbstractLogger;
import util.logging.LevelX;
import util.logging.Logger;
import util.logging.NameLevelFilter;
import util.logging.StandartConsoleHandler;

public class LoggingAPITest {

    public static void main(String[] args) {
	AbstractLogger log = Logger.getLogger(LoggingAPITest.class);
	Handler[] hs = log.getHandlers();
	for(Handler h : hs){
	    log.removeHandler(h);
	}
	log.addHandler(new StandartConsoleHandler());//handler 1
	log.addHandler(new StandartConsoleHandler(LevelX.DEBUG));//handler 2
	
	log.setLevel(LevelX.DEBUG);
	
	//should get printed twice (both handlers)
	log.info("Hello World");
	
	//should get printed twice (both handlers)
	log.warn("Some warning");
	
	//should get printed once (by handler 2)
	log.debug("Debugging");
	
	//should get printed once (by handler 2)
	log.config("Configuration");
	
	//to get nullpointer
	String s = null;
	try{
	    s.contains("W");
	}catch(NullPointerException ex){
	    //should get printed twice (both handlers)
	    log.error("Some null encountered", ex);
	}
	
    }

}
