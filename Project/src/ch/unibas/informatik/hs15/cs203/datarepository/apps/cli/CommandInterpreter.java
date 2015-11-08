package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;
import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.support.ManPageGenerator;
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
class CommandInterpreter {

	/**
	 * Creates a new {@link CommandInterpreter}. Currently nothing more happens.
	 */
	public CommandInterpreter() {

	}

	/**
	 * It's a thing of style
	 *
	 * @param Options
	 *            Options that can be searched for in the List ToParse
	 * @param ToParse
	 *            List to Parse
	 * @param ID
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ParseException
	 */
	private Criteria criteriaParser(final LinkedList<String> Options,
			final LinkedList<String> ToParse, final boolean ID)
					throws IllegalArgumentException, ParseException {
		Criteria crit;

		final Map<String, String> Helper = new HashMap<String, String>();
		String a = ToParse.poll();
		for (int it = 1; it < ToParse.size(); it++) {

			if (Options.contains(a)) {
				if (Helper.containsKey(a)) {
					throw new IllegalArgumentException(
							"We already defined a value for this key");
				} else {
					Helper.put(a, ToParse.poll());
				}
			} else {
				if (Options.contains(Option.ID.name()) && ID && Helper.isEmpty()
						&& ToParse.size() >= 2) {
					Helper.put(Option.ID.name(), ToParse.remove(1));
				} else {
					throw new IllegalArgumentException(
							"We don't accept this key");
				}
			}
			a = ToParse.poll();
		}
		if (Helper.isEmpty() && !ID
				|| Helper.size() > 1 && Helper.containsKey(Option.ID.name())) {
			throw new IllegalArgumentException(
					"Inappropria te number of arguments");
		} else {
			if (Helper.containsKey(Option.ID.name())) {
				crit = Criteria.forId(Helper.get(Option.ID.name()));
			} else {

				final DateFormat dateFormat1 = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				final DateFormat dateFormat2 = new SimpleDateFormat(
						"yyyy-MM-dd");

				final Date before = Helper.containsKey(Option.BEFORE.name())
						? Helper.get(Option.BEFORE.name()).length() > 10
								? dateFormat1
										.parse(Helper.get(Option.BEFORE.name()))
								: dateFormat2
										.parse(Helper.get(Option.BEFORE.name()))
						: null;
				final Date after = Helper.containsKey(Option.AFTER.name())
						? Helper.get(Option.AFTER.name()).length() > 10
								? dateFormat1
										.parse(Helper.get(Option.AFTER.name()))
								: dateFormat2
										.parse(Helper.get(Option.AFTER.name()))
						: null;

				crit = new Criteria(Helper.get(Option.NAME.name()),
						Helper.get(Option.TEXT.name()), before, after);
			}
		}
		return crit;

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
	 * @throws IOException
	 */
	private String executeAdd(final LinkedList<String> arguments)
			throws IOException {
		String desc = "", repoLoc = null, file = null;
		boolean move = false;
		final ProgressListener listener = new DummyProgressListener();
		String curr;
		final int originSize = arguments.size();
		for (int i = 0; i < originSize; i++) {
			curr = arguments.poll();
			if (curr.startsWith(Option.DESCRIPTION.name())) {
				desc = curr.substring(
						curr.indexOf(CommandParser.OPTION_SEPARATOR) + 1);
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
		} // endfor
		final DataRepository repo = Factory.create(new File(repoLoc));
		final File add = new File(file);
		add.createNewFile();
		final MetaData helper = repo.add(add, desc, move, listener);
		String ret = "";
		if (helper == null) {
			ret = "Failed";
		} else {
			ret += "Data set named '" + helper.getName()
					+ "' has been successfully added to the repository. ID: "
					+ helper.getId();
		}
		return ret;

	}

	/**
	 *
	 * @param arguments
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ParseException
	 */
	private String executeDelete(final LinkedList<String> arguments)
			throws IllegalArgumentException, ParseException {
		final String repoLoc = arguments.size() == 2 ? arguments.peekFirst()
				: arguments.peekLast();
		final LinkedList<String> Options = new LinkedList<String>(Arrays.asList(
				Option.ID.name(), Option.NAME.name(), Option.TEXT.name(),
				Option.BEFORE.name(), Option.AFTER.name()));
		final Criteria crit = criteriaParser(Options, arguments, true);
		final ProgressListener listener = arguments.contains(Option.VERBOSE)
				? new DummyProgressListener() : null;
		final List<MetaData> ret = Factory.create(new File(repoLoc))
				.delete(crit);
		String retStr = "The following data sets have been deleted: ";
		if (!ret.isEmpty()) {
			for (final MetaData it : ret) {
				retStr += it.getId() + (retStr.endsWith(" ") ? "" : ",");
			}
		}
		return retStr;
	}

	/**
	 * Executes the Export command of the data repository application.The
	 * paramter <code>arguments</code> are the arguments without the command
	 * itself
	 *
	 * @param arguments
	 *            arguments in tokenizer list form
	 * @throws ParseException
	 */
	private String executeExport(final LinkedList<String> arguments)
			throws IllegalArgumentException, ParseException {
		// TODO Auto-generated method stub
		final LinkedList<String> Options = new LinkedList<String>(
				Arrays.asList(Option.ID.name(), Option.NAME.name(),
						Option.TEXT.name(), Option.BEFORE.name(),
						Option.AFTER.name(), Option.VERBOSE.name()));
		final Criteria cr = criteriaParser(Options, arguments, true);
		final String repoLoc = arguments
				.get(cr.onlyID() && arguments.size() == 3 ? arguments.size() - 2
						: arguments.size() - 3),
				destLoc = arguments.peekLast();
		final ProgressListener listener = arguments.contains(Option.VERBOSE)
				? new DummyProgressListener() : null;
		final DataRepository repo = Factory.create(new File(repoLoc));
		String ret = "Exported: \n";
		for (final MetaData it : repo.export(cr, new File(destLoc), listener)) {
			ret += it.getId() + " " + it.getName() + "\n";
		}
		return ret;

	}

	/**
	 * execute help command
	 *
	 * @param arguments
	 * @return
	 * @throws IOException
	 *             If an error occured while reading help files.
	 */
	private String executeHelp(final LinkedList<String> arguments)
			throws IOException {
		String out = null;
		if (arguments.size() > 0) {
			final ManPageGenerator gen = new ManPageGenerator(arguments.get(0));
			out = gen.getManPage();

		} else {
			final ManPageGenerator gen = new ManPageGenerator(null);
			out = gen.getManPage();
		}
		if (out == null) {
			throw new RuntimeException(
					"An unexpected error occured on executing help. [Empty return]");
		}
		return out;
	}

	/**
	 * Executes the List command of the data repository application.The paramter
	 * <code>arguments</code> are the arguments without the command itself
	 *
	 * @param arguments
	 *            arguments in tokenizer list form
	 * @throws ParseException
	 */
	private String executeList(final LinkedList<String> arguments)
			throws IllegalArgumentException, ParseException {
		final String repoLoc = arguments.peekLast();
		final DataRepository repo = Factory.create(new File(repoLoc));
		final LinkedList<String> Options = new LinkedList<String>(Arrays.asList(
				Option.ID.name(), Option.NAME.name(), Option.TEXT.name(),
				Option.BEFORE.name(), Option.AFTER.name()));
		final Criteria cr = arguments.size() == 1 ? Criteria.all()
				: criteriaParser(Options, arguments, false);
		final List<MetaData> ret = repo.getMetaData(cr);
		String retString = "ID\tName\tTimestamp\tNumber of Files\tSize\tDescription\n";
		for (final MetaData i : ret) {
			retString += i.getId() + "\t" + i.getName() + "\t"
					+ i.getTimestamp() + "\t" + i.getNumberOfFiles() + "\t"
					+ i.getSize() + "\t" + i.getDescription() + "\n";
		}
		return retString;
	}

	/**
	 * executes the replace command
	 *
	 * @param arguments
	 * @return
	 * @throws IOException
	 */
	private String executeReplace(final LinkedList<String> arguments)
			throws IOException {
		final String file = arguments.removeLast();
		final String iD = arguments.removeLast();
		final String repoLoc = arguments.removeLast();
		final String description = arguments.size() >= 2
				&& arguments.peek().equals(Option.DESCRIPTION)
						? arguments.get(1) : "";
		final boolean move = arguments.contains(Option.VERBOSE);
		final ProgressListener listener = arguments.contains(Option.VERBOSE)
				? new DummyProgressListener() : null;
		final File add = new File(file);
		add.createNewFile();
		final MetaData ret = Factory.create(new File(repoLoc)).replace(iD, add,
				description, move, listener);
		return "Data set with the ID: '" + iD
				+ "' has been successfully replaced with the file"
				+ ret.getName() + ". ID: " + ret.getId();
	}

	/**
	 * Interprets the given command line arguments. It is <i>highly</i>
	 * recommended to pass <i>directly</i> the command line arguments array to
	 * this method.
	 *
	 * @param args
	 *            The command line arguments.
	 * @throws ParseException
	 * @throws IOException
	 */
	public String interpret(final String[] args)
			throws ParseException, IOException, IllegalArgumentException {
		final LinkedList<String> command = CommandParser.lex(args);
		if (command.size() < 1) {
			// Since commands >data-repository and >data-repository help lead to
			// the same, general help
			// this is a shortcut for that case.
			return executeHelp(null);
		}
		// TODO change if done.
		final Command cmd = Command.parse(command.poll());// no null check since
		// check
		// already done in lex
		switch (cmd) {
			case ADD:
				// Note how the list command already has been removed from the
				// poll
				// above
				return executeAdd(command);
			case EXPORT:
				return executeExport(command);
			case LIST:
				return executeList(command);
			case DELETE:
				return executeDelete(command);
			case HELP:
				return executeHelp(command);
			case REPLACE:
				return executeReplace(command);
			default:
				throw new UnsupportedOperationException(
						"Command " + cmd + " Not implemented yet");
		}
	}

}
