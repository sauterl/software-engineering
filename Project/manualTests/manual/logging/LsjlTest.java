package manual.logging;

import util.logging.LevelX;
import util.logging.Logger;
import util.logging.LoggerManager;

public class LsjlTest {

	public static void main(String[] args) {
		System.setProperty(LoggerManager.VERBOSE_ENABLED_KEY, "true");
		System.setProperty(LoggerManager.VERBOSE_LEVEL_KEY, LevelX.CONFIG.getName() );
		System.setProperty(LoggerManager.LOGGING_DISABLED_KEY, "false");
		System.setProperty(LoggerManager.LOGGING_DEFAULT_LEVEL_KEY, LevelX.DEBUG.getName() );
		Logger l = Logger.getLogger("noConfig");
		l.debug("Should not be printed");
		l.config("No print too");
		l.info("print");
	}

}
