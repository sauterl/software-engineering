package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.File;
import java.util.LinkedList;

import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;
import ch.unibas.informatik.hs15.cs203.datarepository.processing.Factory;

/**
 * The {@link CommandInterpreter} receives a command with its options and
 * arguments and then interprets (and execute) it.
 * 
 * The CommandInterpeter delegates parsing the command to the
 * {@link CommandParser}. So the CommandInterpreter is basically responsible to
 * call the correct part of the application's logic while the
 * {@link CommandParser} simply parses the given command line arguments.
 * 
 * @author Loris
 * 
 */
public class CommandInterpreter {

	/**
	 * Creates a new {@link CommandInterpreter}. Currently nothing more happens.
	 */
	public CommandInterpreter() {

	}

	/**
	 * Interprets the given command line arguments. It is <i>highly</i>
	 * recommended to pass <i>directly</i> the command line arguments array to
	 * this method.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public void interpret(String[] args) {
		LinkedList<String> command = CommandParser.lex(args);
		if (command.size() < 1) {
			throw new IllegalArgumentException("Error while parsing commnd.");
		}
		// TODO change if done.
		Command cmd = Command.parse(command.poll());// no null check since check
		// already done in lex
		switch (cmd) {
		case ADD:
			// Note how the list command already has add removed from the poll
			// above
			executeAdd(command);
			break;
		default:
			throw new UnsupportedOperationException("Command " + cmd
					+ " Not implemented yet");
		}
	}

	/**
	 * Executes the ADD command of the data repository application. The paramter
	 * <code>arguments</code> are the arguments for the command ADD, without ADD
	 * itself. It is highly recommended to only use the
	 * {@link CommandParser#lex(String[])} tokenizer. Since this method relies
	 * on correctly parsed tokens in the <code>arguments</code>-list.
	 * 
	 * @param arguments
	 *            The arguments of the command ADD, in tokenized list form.
	 */
	private void executeAdd(LinkedList<String> arguments) {
		String desc = "", repoLoc = null, file = null;
		boolean move = false;
		ProgressListener listener = new DummyProgressListener();
		String curr;
		int originSize = arguments.size();
		for (int i = 0; i < originSize; i++) {
			curr = arguments.poll();
			if (curr.startsWith(Option.DESCRIPTION.name())) {
				desc = curr.substring(curr
						.indexOf(CommandParser.OPTION_SEPARATOR));
			} else if (curr.startsWith(Option.MOVE.name())) {
				move = true;
			} else if (curr.startsWith(Option.VERBOSE.name())) {
				// TODO listener = new ConsoleProgressListener();
			} else {
				if (i == originSize - 2) {
					repoLoc = curr;
				} else if (i == originSize - 1) {
					file = curr;
				} else {
					throw new RuntimeException("Reached unexpected state.");
				}
			}
		}// endfor
		DataRepository repo = Factory.create(new File(repoLoc));
		repo.add(new File(file), desc, move, listener);
	}

}
