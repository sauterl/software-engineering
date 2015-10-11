package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.util.LinkedList;

final class CommandParser {

    public static final String OPTION_SEPARATOR = "=";

    /**
     * This class is not to be instantiated.
     */
    private CommandParser() {

    }

    public final static LinkedList<String> lex(String[] args) {
	// TODO Replace illegal argument with more accurate exceptions
	if (args == null) {
	    throw new IllegalArgumentException(
		    "Null is not a valid string array to tokenize.");
	}
	LinkedList<String> tokens = new LinkedList<String>();
	if (args.length < 1) {
	    tokens.add(Command.HELP.name());
	    return tokens;
	}
	// assert(args.length >= 1);
	Command cmd = parseCommand(args[0]);
	tokens.add(cmd.name());
	for (int i = 1; i < args.length; i++) {
	    String s = args[i];
	    if (s.startsWith(Option.OPTION_MARKER)) {// test for option
		Option opt = parseOption(s.substring(Option.OPTION_MARKER
			.length()));
		if (opt.isFlag()) {// has option a argument
		    tokens.add(opt.name());
		} else {
		    if (cmd.isOptionAllowed(opt)) {// is option allowed
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
		if (s == null || s.length() == 0) {
		    throw new IllegalArgumentException("Argument is null.");
		} else {
		    tokens.add(s);
		}
	    }// end test for option / regular argument
	}

	return tokens;
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
