/**
 * 
 */
package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;

import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.processing.Factory;

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
	
	private static final DataRepositoryFactory FACTORY = new DataRepositoryFactory() {
		@Override
		public DataRepository create(File repositoryFolder) {
		return Factory.create(repositoryFolder);
		}
	};
	
	
	
	/**
	 * The main entry to this command line tool. See <a href=
	 * "http://informatik.unibas.ch/fileadmin/Lectures/HS2015/software-engineering/specifications.pdf"
	 * > for further information</a>
	 * 
	 * @param args
	 */
	public static void main(final String[] args){
		String ret="1";
		try{
		ret=execute(args, FACTORY);
		}catch (final Throwable t) {
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

		}
		System.out.println(ret);
		System.exit(ret.charAt(0)=='1'?1:0);
	}
	
	static String execute(String[] args, DataRepositoryFactory factory) throws IllegalArgumentException {
		final CommandInterpreter interpreter = new CommandInterpreter();
		try{
		return interpreter.interpret(args);}
		catch(ParseException | IOException e){
		return "1";
		}
	}

}
