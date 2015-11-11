package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;

/**
 * The Client is the entry point for the user interaction layer. This class
 * passes a given command to an internal {@link CommandInterpreter} which then
 * interprets said command on the {@link DataRepository} the
 * {@link DataRepositoryFactory} provides.
 * 
 * @author Loris
 *
 */
public class Client {

	private Client() {
		/*
		 * No objects of this class are allowed, since Client.execute() is
		 * static. I personally like the ability to make static calls, but this
		 * one I dislike. In particular the complete getError / hasError stuff
		 * is more C than Java and for sure not really OOP.
		 */
	}

	/**
	 * Executes the given command <tt>args</tt> on the specified
	 * <tt>factory</tt>. <br />
	 * The return value is the return message on success. Ohterwise
	 * <tt>null</tt> is returned and {@link #hasError()} returns <tt>true</tt>.
	 * <br />
	 * To get the error, use {@link #getError()}.<br />
	 * This behavior is not true for {@link RuntimeException}; these are caught
	 * and immediately thrown again.
	 * 
	 * @param args
	 *            The command, each word as its own entry in this array.
	 * @param factory
	 *            The factory to perform the execution on.
	 * @return the resulting message or <tt>null</tt> if an error occured.
	 */
	public static String execute(String[] args, DataRepositoryFactory factory) {
		final CommandInterpreter interpreter = new CommandInterpreter(factory);
		String out = null;
		try {
			out = interpreter.interpret(args);
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Throwable e) {
			t = e;
		}
		return out;
	}

	private static Throwable t = null;

	/**
	 * Returns if an error happend.
	 * 
	 * @return <tt>true</tt>whenever an error occurred, thus
	 *         {@link #execute(String[], DataRepositoryFactory)} returned
	 *         <tt>null</tt>. <tt>false</tt> otherwise.
	 */
	public static boolean hasError() {
		return t != null;
	}

	/**
	 * Returns the occurred error with all its details.
	 * 
	 * @return The occurred error with all its details, some may are not
	 *         provided. Or <code>null</code> if no error occurre.
	 */
	public static Throwable getError() {
		return t;
	}
}
