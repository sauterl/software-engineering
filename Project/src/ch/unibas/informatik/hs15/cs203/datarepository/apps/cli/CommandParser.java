package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import ch.unibas.informatik.hs15.cs203.datarepository.common.CriteriaWrapper;

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
	 * Splits the given command line arguments into a logical and structural
	 * correct list of tokens.
	 * <br />
	 * <b>Note: 'parameterized options' are represented by a single token.
	 * The token contains the option's name and the parameter / argument, separated
	 * by the {@value #OPTION_SEPARATOR}-character.</b>
	 * <br />
	 * In particular this method links option identifiers (like '--description')
	 * and the following option value, if one is needed, (like '--description
	 * "Some description").
	 * <p>
	 * A rough validation of the command is being executed as well, so that this
	 * method throws exceptions if
	 * <ul>
	 * <li>inappropriate options for the command are used (like '--id' after the
	 * 'add' command)</li>
	 * <li>the value of an option is missing (like '--description --id'),</li>
	 * <li>either the command or the option is unknown or</li>
	 * <li>the command is not completely</li>
	 * </ul>
	 * </p>
	 * Note that if the parameter <code>args</code> is empty but not
	 * <code>null</code>, a single token containing the 'help' command is
	 * returned.
	 * 
	 * @param args
	 *            The command line arguments.
	 * @return The tokens in form of a {@link LinkedList}. The order of the
	 *         linked list represents the (expected) order of the command
	 *         options and arguments.
	 */
	public final static LinkedList<String> lex(final String[] args) throws IllegalArgumentException {
		// TODO Replace illegal argument with more accurate exceptions
		checkNullParam(args);
		if (args.length < 1) {
			return createDefaultTokens();
		}
		// assert(args.length >= 1);
		final LinkedList<String> tokens = new LinkedList<String>();
		final Command cmd = parseCommand(args[0]);
		tokens.add(cmd.name());
		for (int i = 1; i < args.length; i++) {
			if (Option.isLikelyOption(args[i])) {// test for option
				final Option opt = parseOption(args[i],i);
				if (opt.isFlag()) {// has option a argument
					tokens.add(opt.name());
				} else {
					if (cmd.isOptionAppropriate(opt)) {// is option allowed
						try {
							tokens.add(parseOptionsArgument(opt, args[++i]));
						} catch (final ArrayIndexOutOfBoundsException ex) {
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

	/**
	 * Parses a given arguments list into {@link Option} value pairs. <br />
	 * It is assumed that the given list was created by a {@link CommandParser}.
	 *
	 * @param args
	 *            The list of arguments, created by a {@link CommandParser}.
	 * @param strict
	 *            Set this to <tt>true</tt> to have an exception thrown, if any
	 *            entry in the list is not an option-parameter-pair.
	 * @return A Map containing the option and its value.
	 * @throws IllegalArgumentException
	 *             If the given arguments list is ill formatted.
	 */
	public static Map<Option, String> parseOptionValues(final LinkedList<String> args,
			final boolean strict) throws IllegalArgumentException {
		final HashMap<Option, String> out = new HashMap<Option, String>();
		final Iterator<String> it = args.iterator();
		while (it.hasNext()) {
			final String curr = it.next();
			if (curr.contains(CommandParser.OPTION_SEPARATOR)) {
				final Option op = Option.parse(curr.substring(0,
						curr.indexOf(CommandParser.OPTION_SEPARATOR)));

				String value = null;
				try {
					value = curr.substring(
							curr.indexOf(CommandParser.OPTION_SEPARATOR) + 1);
				} catch (final StringIndexOutOfBoundsException e) {
					throw new IllegalArgumentException(String.format(
							"Error while parsing entry. %s, option has *no* value, but should",
							curr));
				}
				if (!out.containsKey(op)) {
					out.put(op, value);
				} else {
					throw new IllegalArgumentException(String.format(
							"Error while parsing entry: %s. It *may* be a duplicate.",
							curr));
				}
			} else {
				if (strict) {
					throw new IllegalArgumentException(String.format(
							"Entry >%s< is not a parsable option.", curr));
				}
			}
		}
		return out;
	}
	
	/**
	 * Parses the given command's arguments to a {@link CriteriaWrapper}.
	 * <br />This parsing is done command sensitive. For example if a command
	 * accepts an ID as an option parameter or as argument, this parsing algorithm detects
	 * this and fails if both are present.
	 * @param cmd The command
	 * @param args The command's arguments
	 * @return A CriteriaWrapper created based on the options in the arguments.
	 */
	public static CriteriaWrapper parseCriteria(final Command cmd,
			final LinkedList<String> args) {
		final LinkedList<String> command = new LinkedList<String>(args);
		command.addFirst(cmd.name());
		final ArgumentsAnalyzer analyzer = new ArgumentsAnalyzer(command);
		analyzer.analyze();
		final Map<Option, String> optVals = CommandParser.parseOptionValues(command);
		// check if only ID
		if (cmd.isIDArgumentAllowed()) {
			// id argument is possible
			final boolean hasIDOption = optVals.containsKey(Option.ID);
			final boolean hasIDArgument = analyzer.getNbArguments() > analyzer
					.getNbMandatoryArgs();
			if ((hasIDOption && hasIDArgument)) {
				throw new IllegalArgumentException(
						"Do only specify *either* --id, or give data set identifier argument. But not both!");
			}
			String id = null;
			if (hasIDOption) {
				id = optVals.get(Option.ID);
			} else if (hasIDArgument) {
				id = args.get(1);// always second argument
			}
			if (id != null) {
				return CriteriaWrapper.forId(id);
			}
		}
		return new CriteriaWrapper(optVals.get(Option.NAME),
				optVals.get(Option.TEXT), ParseUtils.parseDate(optVals.get(Option.AFTER)),
				ParseUtils.parseDate(optVals.get(Option.BEFORE)));
	}
	
	/**
	 * Parses a given list of command argument tokens into option-parameter pairs.
	 * <br />The list should be produces by {@link #lex(String[])} method to ensure reliable results.
	 * <br />This is a shortcut to {@linkplain #parseOptionValues(LinkedList, false)}
	 * @param arguments The command argument tokens
	 * @return A map containing option-parameter pairs.
	 * @see CommandParser#parseOptionValues(LinkedList, boolean)
	 */
	public static Map<Option, String> parseOptionValues(LinkedList<String> arguments){
		return parseOptionValues(arguments, false);
	}

	private static void checkNullParam(final String[] args)  throws IllegalArgumentException{
		if (args == null) {
			throw new IllegalArgumentException(
					"Null is not a valid string array to tokenize.");
		}
	}

	private static LinkedList<String> createDefaultTokens() {
		final LinkedList<String> out = new LinkedList<String>();
		out.add(Command.HELP.name());
		return out;
	}

	private static Command parseCommand(final String str) throws IllegalArgumentException {
		final Command out = Command.parse(str);
		if (out == null) {
			throw new IllegalArgumentException("Unknown command: " + str);
		}
		return out;
	}

	private static Option parseOption(final String option,int index) throws IllegalArgumentException{
		final Option opt = Option.parse(option);
		if (opt == null) {
			throw new IllegalArgumentException("Error in "+(index+1)+". argument ["+option+"]: Unknown option.");
		}
		return opt;
	}

	private static String parseOptionsArgument(final Option opt,
			final String arg) throws IllegalArgumentException {
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

	/**
	 * This class is not to be instantiated.
	 */
	private CommandParser() {

	}

}
