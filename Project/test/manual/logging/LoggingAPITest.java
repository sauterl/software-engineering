package manual.logging;

import java.util.logging.Handler;

import util.logging.AbstractLogger;
import util.logging.LevelX;
import util.logging.Logger;
import util.logging.LoggerFactory;
import util.logging.LoggerManager;
import util.logging.StandartConsoleHandler;

public class LoggingAPITest {

    public static void main(String[] args) {
	LoggerManager mngr = LoggerManager.getManager();
	if(mngr.getLoggerNames().length == 0){
	    System.out.println("Bene");
	}else{
	    System.err.println("OOOOOOh");
	}
	AbstractLogger log = Logger.getLogger(LoggingAPITest.class);
	AbstractLogger log2 = Logger.getLogger(LoggingAPITest.class);
	AbstractLogger log3 = Logger.getLogger(LoggerFactory.class);
	System.out.println(log);
	System.out.println(log2);
	Handler[] hs = log.getHandlers();
	for(Handler h : hs){
	    log.removeHandler(h);
	}
	log.addHandler(new StandartConsoleHandler());//handler 1
	log.addHandler(new StandartConsoleHandler(LevelX.DEBUG));//handler 2
	log3.addHandler(new StandartConsoleHandler() );//handler 3
	log.setLevel(LevelX.DEBUG);
	
	//should get printed twice (both handlers)
	log.info("Hello World");
	
	//should get printed twice (both handlers)
	log.warn("Some warning");
	
	//should get printed once (by handler 2)
	log.debug("Debugging");
	
	//should get printed once (by handler 2)
	log.config("Configuration");
	log3.info("test bump3");
	
	//to get nullpointer
	String s = null;
	try{
	    s.contains("W");
	}catch(NullPointerException ex){
	    //should get printed twice (both handlers)
	    log.error("Some null encountered", ex);
	}
	for(String str : mngr.getLoggerNames()){
	    System.out.println(str);
	}
	
	
    }

}
