/**
 * 
 */
package ch.unibas.informatik.hs15.cs203.datarepository;

import java.util.LinkedList;

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
	    LinkedList<String> tokens = CommandParser.lex(args);
	    System.exit(0);
	}catch(Throwable t){
	    System.err.println(t.getMessage() != null ? t.getMessage() : "Unkown error");
	    System.exit(1);
	}

    }

}
