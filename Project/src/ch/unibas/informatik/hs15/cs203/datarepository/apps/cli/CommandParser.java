package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.util.LinkedList;

/**
 * The {@link CommandParser} class provides parsing mechanisms. The main feature
 * is the 'lexer' method ( {@link CommandParser#lex(String[])} ). A 'lexer', or
 * tokenizer splits a given input into logically linked parts, called tokens.
 * 
 * The {@link CommandParser} is not a lexer in the traditional way since it also
 * verifies the basic structure of the given command.
 * 
 * @author Loris
 * 
 */
final class CommandParser {

    /**
     * The global string which separates the option's name from its value.
     */
    public static final String OPTION_SEPARATOR = "=";

    /**
     * This class is not to be instantiated.
     */
    private CommandParser() {

    }

    /**
     * Splits the given command line arguments into a logical and structural
     * correct list of tokens.
     * 
     * In particular this method links option identifiers (like '--description')
     * and the following option value, if one is needed, (like '--description
     * "Some description").
     * 
     * A rough validation of the command is being executed as well, so that this
     * method throws exceptions if
     * <ul>
     * <li>inappropriate options for the command are used (like '--id' after the
     * 'add' command)</li>
     * <li>the value of an option is missing (like '--description --id'),</li>
     * <li>either the command or the option is unknown or</li>
     * <li>the command is not completely</li>
     * </ul>
     * 
     * Note that if the parameter <code>args</code> is empty but not <code>null</code>,
     * a single token containing the 'help' command is returned.
     * 
     * @param args
     *            The command line arguments.
     * @return The tokens in form of a {@link LinkedList}. The order of the
     *         linked list represents the (expected) order of the command
     *         options and arguments.
     */
    public final static LinkedList<String> lex(String[] args) {
	// TODO Replace illegal argument with more accurate exceptions
	checkNullParam(args);
	if (args.length < 1) {
	    return createDefaultTokens();
	}
	// assert(args.length >= 1);
	LinkedList<String> tokens = new LinkedList<String>();
	Command cmd = parseCommand(args[0]);
	tokens.add(cmd.name());
	for (int i = 1; i < args.length; i++) {
	    if (Option.isStringOption(args[i]) ) {// test for option
		Option opt = parseOption(args[i]);
		if (opt.isFlag()) {// has option a argument
		    tokens.add(opt.name());
		} else {
		    if (cmd.isOptionAppropriate(opt)) {// is option allowed
			try {
			    tokens.add(parseOptionsArgument(opt, args[++i]));
			} catch (ArrayIndexOutOfBoundsException ex) {
			    throw new IllegalArgumentException(
				    "Unexpected end of command");
			}
		    } else {
			throw new IllegalArgumentException(String.format(
				"Command %s does not allow option --%s",
				cmd.name(), opt.name()));
		    }// parameterized-option parsed
		}// flag-option parsed
	    } else {// current token is likely not an option
		if (args[i] == null || args[i].length() == 0) {
		    throw new IllegalArgumentException("Argument is null.");
		} else {
		    tokens.add(args[i]);
		}
	    }// end test for option / regular argument
	}

	return tokens;
    }
    
    private static LinkedList<String> createDefaultTokens(){
	LinkedList<String> out = new LinkedList<String>();
	out.add(Command.HELP.name() );
	return out;
    }
    
    private static void checkNullParam(String[] args){
	if(args == null){
	    throw new IllegalArgumentException(
		    "Null is not a valid string array to tokenize.");
	}
    }

    private static String parseOptionsArgument(Option opt, String arg) {
	if (opt == null || arg == null || arg.length() == 0) {
	    throw new IllegalArgumentException(String.format(
		    "Illegal argument for option %s: %s", opt.name(), arg));
	} else {
	    if (!arg.startsWith(Option.OPTION_MARKER)) {
		return opt.name().concat(OPTION_SEPARATOR).concat(arg);
	    } else {
		throw new IllegalArgumentException(
			String.format(
				"Argument of option (%s) must not start with options marker (%s): %s",
				opt.name(), Option.OPTION_MARKER, arg));
	    }
	}
    }

    private static Option parseOption(String option) {
	Option opt = Option.parse(option);
	if (opt == null) {
	    throw new IllegalArgumentException("Unkown option: " + option);
	}
	return opt;
    }

    private static Command parseCommand(String str) {
	Command out = Command.parse(str);
	if (out == null) {
	    throw new IllegalArgumentException("Unknown command: " + str);
	}
	return out;
    }

}
