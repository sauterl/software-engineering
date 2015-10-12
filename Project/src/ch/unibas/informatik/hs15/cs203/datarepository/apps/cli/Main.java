/**
 * 
 */
package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;


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

    /**
     * The main entry to this command line tool. See <a href=
     * "http://informatik.unibas.ch/fileadmin/Lectures/HS2015/software-engineering/specifications.pdf"
     * > for further information</a>
     * 
     * @param args
     */
    public static void main(String[] args) {
	try {
	    CommandInterpreter interpreter = new CommandInterpreter();
	    interpreter.interpret(args);
	    System.exit(0);
	} catch (Throwable t) {
	    System.err.print("[ERROR]: ");
	    System.err.print(t.getMessage() != null ? t.getMessage()
		    : "Unkown error");
	    System.err.println();
	    System.exit(1);
	}

    }

}
