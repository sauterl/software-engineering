package manual.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import util.logging.LoggerFactory;

public class FirstTest {

    private static final Logger logger = LoggerFactory.getLoggerFactory().getLoggerForClass(FirstTest.class);
    private static final Logger log = Logger.getLogger(FirstTest.class.getName());
    public static void main(String[] args) {
//	logger.info("Heyjooo");
	log.info("heyoo");
//	logger.fine("fine logging - debug?");
	log.fine("fine logging - debug?");
	try{
	    int d = 3 / 0;
	    System.out.println(d);
	}catch(ArithmeticException ex){
//	    logger.log(Level.WARNING, "nooooo", ex);
	    log.log(Level.WARNING, "nooooo", ex);
	}
	
//	logger.info("Done: ");
	log.info("Done: ");
	log.info("name: "+log.getName());
    }

}
