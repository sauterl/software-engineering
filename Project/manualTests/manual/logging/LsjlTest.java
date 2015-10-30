package manual.logging;

import util.logging.LevelX;
import util.logging.Logger;
import util.logging.LoggerManager;

public class LsjlTest {

	public static void main(String[] args) {
		System.err.println("test");
		System.setProperty(LoggerManager.VERBOSE_ENABLED_KEY, "true");
		System.setProperty(LoggerManager.VERBOSE_LEVEL_KEY, LevelX.DEBUG.getName() );
//		System.setProperty(LoggerManager.LOGGING_DISABLED_KEY, "false");
//		System.setProperty(LoggerManager.LOGGING_DEFAULT_LEVEL_KEY, LevelX.DEBUG.getName() );
		Logger l = Logger.getLogger("noConfig");
		Logger lthis = Logger.getLogger(LsjlTest.class);
		System.err.println("asdfa");
		l.debug("Should not be printed");
		l.config("No print too");
		l.info("print");
		lthis.info("Me print");
		System.err.println("blbo");
		System.out.println("ERROR: "+System.err.checkError() );
		System.out.println(": stuff");
	}

}
