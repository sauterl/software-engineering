package manual.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import util.logging.StandardFormatter;

public class LoggerTests {

    public static final void main(String[] args){
	Logger l = Logger.getLogger(LoggerTests.class.getName() );
//	Logger l = LoggerFactory.getLoggerFactory().getLoggerForClass(LoggerTests.class);
	l.info("Hello World");
	
	ConsoleHandler ch = new ConsoleHandler();
	ch.setFormatter(new StandardFormatter());
	
	l.addHandler(ch);
	
	l.info("New infos");
	l.warning("Oho");
	try {
	    Thread.sleep(1000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	l.severe("all down");
    }

}
