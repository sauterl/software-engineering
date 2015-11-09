/**
 *
 */
package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.processing.Factory;
import util.logging.LevelX;
import util.logging.Logger;

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
		public DataRepository create(final File repositoryFolder) {
			Factory factory = new Factory();
			return factory.create(repositoryFolder);
		}
	};

	/**
	 * The main entry to this command line tool. See <a href=
	 * "http://informatik.unibas.ch/fileadmin/Lectures/HS2015/software-engineering/specifications.pdf"
	 * > for further information</a>
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		int out = 1;
		Throwable t = null;
		String print = null;
		try{
			print = Client.execute(args, FACTORY);
		}catch(Throwable th){
			t = th;
		}
		if(Client.hasError() ){
			t = Client.getError();
		}
		if(t != null){
			System.err.print("[ERROR]: ");
			System.err.print(t.getMessage() != null ? t.getMessage()
					: "Unkown error of type: " + t.getClass().getSimpleName());
			System.err.println();
			// log error if logging enabled
			LOG.log(LevelX.FATAL,
					"A fatal error has occurred. See stacktrace for further detail: ",
					t);
		}else{
			out = 0;
			System.out.println(print);
		}
		System.exit(out);
	}

}
