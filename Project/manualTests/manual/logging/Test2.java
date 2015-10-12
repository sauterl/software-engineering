package manual.logging;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import util.logging.LoggerFactory;

public class Test2 {

    public static void main(String[] args) {
	LoggerFactory lf = LoggerFactory.getLoggerFactory();
	if (lf == null) {
	    System.out.println("LF is null");
	} else {
	    Logger lfl = lf.getLoggerForClass(Test2.class);
	    if (lfl == null) {
		System.out.println("lfl is null");
	    } else {
		lfl.info("Hello World");
	    }

	}
	
	LogManager lm = LogManager.getLogManager();
	if (lm == null) {
	    System.out.println("LM is null");
	}else{
	    Logger lml = lm.getLogger(Test2.class.getName() );
	    if(lml == null){
		System.out.println("lml is null");
	    }else{
		lml.info("Hello WOrld");
	    }
	}
    }

}
