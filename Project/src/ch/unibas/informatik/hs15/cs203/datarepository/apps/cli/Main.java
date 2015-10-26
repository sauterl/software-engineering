/**
 * 
 */
package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.util.logging.Level;

import util.logging.LevelX;
import util.logging.Logger;
import util.logging.LoggerManager;

/**
 * The Main entry point to this command line application. Read the
 * specifications under <a href=
 * "http://informatik.unibas.ch/fileadmin/Lectures/HS2015/software-engineering/specifications.pdf"
 * >informatik.unibas.ch</a> for further information on how to use this
 * application.
 * 
 * @author Loris
 * 
 */
public class Main {
	
	private static final Logger LOG = Logger.getLogger(Main.class);

	/**
	 * The main entry to this command line tool. See <a href=
	 * "http://informatik.unibas.ch/fileadmin/Lectures/HS2015/software-engineering/specifications.pdf"
	 * > for further information</a>
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		int exitStatus = 0;
		try {
			final CommandInterpreter interpreter = new CommandInterpreter();
			interpreter.interpret(args);
		} catch (final Throwable t) {
			//eror to stderr
			System.err.print("[ERROR]: ");
			System.err.print(t.getMessage() != null ? t.getMessage(): "Unkown error of type: "+t.getClass().getSimpleName());
			System.err.println();
			//ERROR to stdout
//			System.out.print("[ERROR]: ");
//			System.out.print(t.getMessage() != null ? t.getMessage(): "Unkown error of type: "+t.getClass().getSimpleName());
//			System.out.println();
			//log error if logging enabled
			LOG.log(LevelX.FATAL, "A fatal error has occurred. See stacktrace for further detail: ", t);
			exitStatus = 1;
		}
		System.exit(exitStatus);
	}

}
