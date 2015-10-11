/**
 * 
 */
package ch.unibas.informatik.hs15.cs203.datarepository;

import ch.unibas.informatik.hs15.cs203.datarepository.apps.cli.CommandInterpreter;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.cli.CommandParser;

/**
 * @author Loris
 *
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
	try{
	    CommandInterpreter interpreter = new CommandInterpreter();
	    interpreter.execute(args);
	    System.exit(0);
	}catch(Throwable t){
	    System.err.print("[ERROR]: ");
	    System.err.print(t.getMessage() != null ? t.getMessage() : "Unkown error");
	    System.err.println();
	    System.exit(1);
	}

    }

}
