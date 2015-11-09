package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.IOException;
import java.text.ParseException;

import util.logging.Logger;

/**
 * Needed for ClientTest Extremely poor written but fits the needs =(
 * 
 * @author Loris
 *
 */
public class Client {

	private Client() {
		// noooooooooooo instantiation
	}

	/**
	 * Executes the given command <tt>args</tt> on the specified
	 * <tt>factory</tt>. <br />
	 * The return value is the return message on success. Ohterwise
	 * <tt>null</tt> is returned and {@link #hasError()} returns <tt>true</tt>.
	 * <br />
	 * To get the error, use {@link #getError()}.
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
		} catch(RuntimeException ex){
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
	 * @return The occurred error with all its details, some may are not provided.
	 */
	public static Throwable getError() {
		return t;
	}
}
