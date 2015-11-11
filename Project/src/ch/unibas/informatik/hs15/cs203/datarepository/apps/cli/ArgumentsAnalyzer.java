package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * The class ArgumentsAnalyzer analyzes arguments command sensitive. <br />
 * This class is designed to gather basic information of a given list of tokens
 * which contains option-parameter pairs, flag-options and command arguments.
 * Such a list like {@link CommandParser#lex(String[])} returns. <b>Be aware
 * that the results of this class are only reliable if used in conjunction with
 * above mentioned lexer.</b>
 * <p>
 * A command consists of several space-separated tokens. The first one ist the
 * command itself, represented by the {@link Command} enumeration. Then an
 * optional, variable list of options follows. Some options must be followed by
 * their argument / parameter and some do not have such a parameter. Options are
 * represented by the {@link Option} enumeration. Finally a list of command
 * arguments follows. These arguments are divided into two categories: the
 * mandatory arguments and the optional ones.
 * </p>
 * To see which commands are supported, one must read the specifications.
 * <p>
 * <h2>Usage</h2> To create and set up an <tt>analyzer</tt> there are two ways:
 * <ol>
 * <li>Use the empty constructor {@link #ArgumentsAnalyzer()} in conjunction
 * with one of the <tt>setSubject</tt> methods ({@link #setSubject(LinkedList)}
 * or {@link #setSubject(Command, LinkedList)}).</li>
 * <li>Use one of the setup constructors {@link #ArgumentsAnalyzer(LinkedList)}
 * or {@link #ArgumentsAnalyzer(Command, LinkedList)}</li>
 * </ol>
 * The difference between the two parameters, the {@link LinkedList} or
 * {@link Command} and {@link LinkedList} is, that in the first case the
 * <tt>analyizer</tt> assumes to get the complete command, thus the first token
 * is the command itself while the latter parameters indicate that the
 * {@link LinkedList} only contains the arguments (thus the first token is the
 * first argument).
 * <p>
 * Before using the <tt>getNb*</tt> methods, one must invoke <tt>analyze()</tt>,
 * otherwise an {@link IllegalStateException} would be thrown.
 * </p>
 * </p>
 *
 * @author Loris
 *
 */
public class ArgumentsAnalyzer {

	/**
	 * Copy of the arguments
	 */
	private LinkedList<String> args;
	/**
	 * The command
	 */
	private Command command = null;

	/**
	 * Negative value indicate no analysis yet
	 */
	private int nbOptions = -1;
	/**
	 * Negative value indicate no analysis yet
	 */
	private int nbArguments = -1;

	/**
	 * Creates an unprepared analyzer. <br />
	 * The analyzer must be set up with one of the <tt>setSubject</tt> methods:
	 * {@link #setSubject(LinkedList)} or
	 * {@link #setSubject(Command, LinkedList)}.
	 */
	public ArgumentsAnalyzer() {
		args = null;
	}

	/**
	 * Creates a prepare analyzer for the given command and its arguments.<br />
	 *
	 * @param cmd
	 *            The command.
	 * @param arguments
	 *            its arguments, without the command name (thus the first of
	 *            <tt>arguments</tt> is the first argument: either a flag
	 *            option, a option-parameter pair or an argument).
	 */
	public ArgumentsAnalyzer(final Command cmd,
			final LinkedList<String> arguments) {
		setSubject(cmd, arguments);
	}

	/**
	 * Creates a prepared analyzer for the given command.<br />
	 *
	 * @param command
	 *            The full command, inclusive command name and its arguments
	 *            (thus the first token of <tt>command</tt> is the command name
	 *            itself).
	 */
	public ArgumentsAnalyzer(final LinkedList<String> command) {
		setSubject(command);
	}

	/**
	 * Analyzes the prepared subject.<br />
	 * If this analyzer is not prepared, an {@link IllegalStateException} will
	 * be thrown. Or in a more technical manner: If an invocation of
	 * {@link #isReady()} before the call of this method would return
	 * <tt>false</tt> an {@link IllegalStateException} is thrown.
	 */
	public void analyze() {
		validateReady();
		count();
	}

	/**
	 * Returns the command of the subject.
	 *
	 * @return The command of the subject.
	 * @throws IllegalStateException
	 *             If the analyzer is not ready.
	 */
	public Command getCommand() {
		validateReady();
		return command;
	}

	/**
	 * Returns the total number of arguments of the subject.
	 *
	 * @return The total number of arguments of the subject.
	 * @throws IllegalStateException
	 *             If the analyzer is not ready.
	 */
	public int getNbArguments() {
		validateReady();
		return nbArguments;
	}

	/**
	 * Returns the amount of mandatory arguments the command has. This passed to
	 * {@link Command#getMandatoryArgsCount()}.
	 *
	 * @return The amount of mandatory arguments the command has.
	 * @throws IllegalStateException
	 *             If the analyzer is not ready.
	 */
	public int getNbMandatoryArgs() {
		validateReady();
		return command.getMandatoryArgsCount();
	}

	/**
	 * Returns the number of options of the subject. Options are either
	 * flag-options or option-parameter pairs. Since options are typed before
	 * the arguments, this number is the offset of the arguments within the
	 * subject.
	 *
	 * @return The number of options of the subject.
	 * @throws IllegalStateException
	 *             If the analyzer is not ready.
	 */
	public int getNbOptions() {
		validateReady();
		return nbOptions;
	}

	/**
	 * Checks if this analyzer is prepared / set up. Thus it checks whether a
	 * subject is set or not.
	 *
	 * @return <tt>true</tt> if a subject was set, <tt>false</tt> in any other
	 *         case.
	 */
	public boolean isReady() {
		return args != null && command != null;
	}

	/**
	 * Prepares the analyzer with the given command and its arguments.<br />
	 *
	 * @param cmd
	 *            The command.
	 * @param arguments
	 *            its arguments, without the command name (thus the first of
	 *            <tt>arguments</tt> is the first argument: either a flag
	 *            option, a option-parameter pair or an argument).
	 */
	public void setSubject(final Command cmd,
			final LinkedList<String> arguments) {
		this.args = new LinkedList<String>(arguments);
		this.command = cmd;
	}

	/**
	 * Prepares the analyzer with the given command.
	 *
	 * @param command
	 *            The full command, inclusive command name and its arguments
	 *            (thus the first token of <tt>command</tt> is the command name
	 *            itself).
	 * @throws IllegalArgumentException
	 *             If the first token is not a command.
	 */
	public void setSubject(final LinkedList<String> command)
			throws IllegalArgumentException {
		this.args = new LinkedList<String>(command);
		final String cmdName = args.poll();
		this.command = Command.parse(cmdName);
		if (this.command == null) {
			throw new IllegalArgumentException(
					"Command is either unknown or missing.");
		}
	}

	private void count() {
		final Iterator<String> it = args.iterator();
		int optionsCounter = 0;
		int argumentsCounter = 0;
		while (it.hasNext()) {
			final String curr = it.next();
			if (curr.contains(CommandParser.OPTION_SEPARATOR)) {
				optionsCounter++;
			} else if (Option.parse(curr) != null) {
				optionsCounter++;
			} else {
				argumentsCounter++;
			}
		}
		this.nbOptions = optionsCounter;
		this.nbArguments = argumentsCounter;
	}

	private void validateReady() {
		if (!isReady()) {
			throw new IllegalStateException("Nothing to analyze");
		}
	}
}
