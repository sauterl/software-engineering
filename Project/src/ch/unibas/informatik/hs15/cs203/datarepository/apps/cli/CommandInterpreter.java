package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.support.ManPageGenerator;
import ch.unibas.informatik.hs15.cs203.datarepository.common.CriteriaWrapper;

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

	private DataRepositoryFactory factory;

	/**
	 * Creates a new {@link CommandInterpreter}. Currently nothing more happens.
	 * 
	 * @deprecated Since {@link #CommandInterpreter(DataRepositoryFactory)} was
	 *             introduced
	 */
	@Deprecated
	public CommandInterpreter() {

	}

	/**
	 * Constructs a new CommandInterpreter which uses the given factory.
	 * 
	 * @param factory
	 *            The factory the command interpreter should use.
	 */
	public CommandInterpreter(DataRepositoryFactory factory) {
		this.factory = factory;
	}

	/**
	 * It's a thing of style
	 *
	 * @param options
	 *            Options that can be searched for in the List ToParse
	 * @param toParse
	 *            List to Parse
	 * @param ID
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ParseException
	 */
	private CriteriaWrapper criteriaParser(final LinkedList<String> options,
			final LinkedList<String> toParse, final boolean ID)
					throws IllegalArgumentException, ParseException {
		CriteriaWrapper crit;

		final Map<String, String> helper = new HashMap<String, String>();
		String a = toParse.poll();
		for (int it = 1; it < toParse.size(); it++) {

			if (options.contains(a)) {
				if (helper.containsKey(a)) {
					throw new IllegalArgumentException(
							"We already defined a value for this key");
				} else {
					helper.put(a, toParse.poll());
				}
			} else {
				if (options.contains(Option.ID.name()) && ID && helper.isEmpty()
						&& toParse.size() >= 2) {
					helper.put(Option.ID.name(), toParse.remove(1));
				} else {
					throw new IllegalArgumentException(
							"We don't accept this key");
				}
			}
			a = toParse.poll();
		}
		if (helper.isEmpty() && !ID
				|| helper.size() > 1 && helper.containsKey(Option.ID.name())) {
			throw new IllegalArgumentException(
					"Inappropria te number of arguments");
		} else {
			if (helper.containsKey(Option.ID.name())) {
				crit = CriteriaWrapper.forId(helper.get(Option.ID.name()));
			} else {

				Date before = parseDate(helper.get(Option.BEFORE.name()));
				Date after = parseDate(helper.get(Option.AFTER.name() ));

				crit = new CriteriaWrapper(helper.get(Option.NAME.name()),
						helper.get(Option.TEXT.name()), before, after);
			}
		}
		return crit;

	}

	/**
	 * Unused till gets linked into actual code - loris.sauter
	 * @param args
	 * @param onlyID
	 *            If only ID argument is available for this command
	 * @return
	 */
	@SuppressWarnings("unused")
	private CriteriaWrapper parseCriteria(Command cmd,
			LinkedList<String> args) {
		LinkedList<String> command = new LinkedList<String>(args);
		command.addFirst(cmd.name());
		ArgumentsAnalyzer analyzer = new ArgumentsAnalyzer(command);
		Map<Option, String> optVals = parseOptionValues(command);
		// check if only ID
		if (cmd.isIDArgumentAllowed()) {
			// id argument is possible
			boolean hasIDOption = optVals.containsKey(Option.ID);
			boolean hasIDArgument = analyzer.getNbArguments() > analyzer
					.getNbMandatoryArgs();
			if (hasIDOption != hasIDArgument) {
				String id = null;
				if (hasIDOption) {
					id = optVals.get(Option.ID);
				} else {
					id = args.get(1);// always second argument
				}
				return CriteriaWrapper.forId(id);
			} else {
				throw new IllegalArgumentException(
						"Do only specify *either* --id, or give data set identifier argument. But not both nor neither!");
			}
		} else {
			return new CriteriaWrapper(optVals.get(Option.NAME),
					optVals.get(Option.TEXT), parseDate(optVals.get(Option.AFTER)), parseDate(optVals.get(Option.BEFORE)));
		}
	}

	/**
	 * Parses a given arguments list into {@link Option} value pairs. <br />
	 * It is assumed that the given list was created by a {@link CommandParser}.
	 * 
	 * @param args
	 *            The list of arguments, created by a {@link CommandParser}.
	 * @param strict
	 *            Set this to <tt>true</tt> to have an exception thrown, if any
	 *            entry in the list is not an option-value-pair.
	 * @return A Map containing the option and its value.
	 * @throws IllegalArgumentException
	 *             If the given arguments list is ill formatted.
	 */
	private Map<Option, String> parseOptionValues(LinkedList<String> args,
			boolean strict) throws IllegalArgumentException {
		HashMap<Option, String> out = new HashMap<Option, String>();
		Iterator<String> it = args.iterator();
		while (it.hasNext()) {
			String curr = it.next();
			if (curr.contains(CommandParser.OPTION_SEPARATOR)) {
				Option op = Option.parse(curr.substring(0,
						curr.indexOf(CommandParser.OPTION_SEPARATOR)));
				String value = curr.substring(
						curr.indexOf(CommandParser.OPTION_SEPARATOR + 1));
				if (out.containsKey(op)) {
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

	private Map<Option, String> parseOptionValues(LinkedList<String> args) {
		return parseOptionValues(args, false);
	}

	/**
	 * Parses a given String to an appropriate date. <br />
	 * Based on the length of the string, either
	 * <code>yyyy-MM-dd HH:mm:ss</code> or <code>yyyy-MM-dd</code> is used as
	 * {@link DateFormat} to parse the string. Therefore <tt>null</tt> is
	 * returned, if the chosen {@link DateFormat} cannot parse the given string.
	 * 
	 * @param str
	 *            The string to parse.
	 * @return The parsed Date OR <tt>null</tt> if the given string was not
	 *         parseable.
	 * @see DateFormat#parse(String)
	 */
	private Date parseDate(String str) {
		final DateFormat precise = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final DateFormat fuzzy = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (str.length() > 10) {
				return precise.parse(str);
			} else {
				return fuzzy.parse(str);
			}
		} catch (ParseException e) {
			return null;
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
	 * @throws IOException
	 */
	private String executeAdd(final LinkedList<String> arguments)
			throws IOException {
		String desc = null, repoLoc = null, file = null;
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
		final DataRepository repo = factory.create(new File(repoLoc));
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
		final CriteriaWrapper crit = criteriaParser(Options, arguments, true);
		final List<MetaData> ret = factory.create(new File(repoLoc))
				.delete(crit.getWrappedObject());
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
		final LinkedList<String> Options = new LinkedList<String>(
				Arrays.asList(Option.ID.name(), Option.NAME.name(),
						Option.TEXT.name(), Option.BEFORE.name(),
						Option.AFTER.name(), Option.VERBOSE.name()));
		final CriteriaWrapper cr = criteriaParser(Options, arguments, true);
		final String repoLoc = arguments
				.get(cr.onlyID() && arguments.size() == 3 ? arguments.size() - 2
						: arguments.size() - 3),
				destLoc = arguments.peekLast();
		final ProgressListener listener = arguments.contains(Option.VERBOSE)
				? new DummyProgressListener() : null;
		final DataRepository repo = factory.create(new File(repoLoc));
		String ret = "Exported: \n";
		for (final MetaData it : repo.export(cr.getWrappedObject(),
				new File(destLoc), listener)) {
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
					"An unexpected error occured on executing help. [Empty getManPage()]");
		}
		return out;
	}

	/*
	 * WHAT IS THIS FOR CODE? if(helper.isEmpty() && !ID || helper.size()>1 &&
	 * helper.containsKey(Option.ID.name())){ throw new
	 * IllegalArgumentException("Inappropriate number of arguments"); }else{
	 * if(helper.containsKey(Option.ID.name())){
	 * crit=Criteria.forId(helper.get(Option.ID.name())); }else{
	 * 
	 * DateFormat dateFormat1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
	 * DateFormat dateFormat2 = new SimpleDateFormat( "yyyy-MM-dd");
	 * 
	 * Date
	 * before=helper.containsKey(Option.BEFORE.name())?helper.get(Option.BEFORE.
	 * name()).length()>10?dateFormat1.parse(helper.get(Option.BEFORE.name())):
	 * dateFormat2.parse(helper.get(Option.BEFORE.name())):null; Date
	 * after=helper.containsKey(Option.AFTER.name())?helper.get(Option.AFTER.
	 * name()).length()>10?dateFormat1.parse(helper.get(Option.AFTER.name())):
	 * dateFormat2.parse(helper.get(Option.AFTER.name())):null; }
	 */
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
		final DataRepository repo = factory.create(new File(repoLoc));
		final LinkedList<String> Options = new LinkedList<String>(Arrays.asList(
				Option.ID.name(), Option.NAME.name(), Option.TEXT.name(),
				Option.BEFORE.name(), Option.AFTER.name()));
		final CriteriaWrapper cr = arguments.size() == 1 ? CriteriaWrapper.all()
				: criteriaParser(Options, arguments, false);
		final List<MetaData> ret = repo.getMetaData(cr.getWrappedObject());
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
		final MetaData ret = factory.create(new File(repoLoc)).replace(iD, add,
				description, move, listener);
		return "Data set with the ID: '" + iD
				+ "' has been successfully replaced with the file"
				+ ret.getName() + ". ID: " + ret.getId();
	}

	private String handleUnknownCommand(final String cmd)
			throws IllegalArgumentException {
		String type = "null";
		if (cmd != null) {
			Command c = Command.parse(cmd);
			if (c.equals(Command.FORTYTWO)) {
				return "This is the Answer to the Ultimate Question of Life, the Universe and Everything - by Adam Douglas. \"The Hitchhiker's Guide to the Galaxy\" (1979)";
			}
			type = cmd;
		}
		throw new IllegalArgumentException(String.format(
				"Unknown command <%s>: Check your spelling or use command <help> to get more information.",
				type));
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
		final String cmdName = command.poll();// to print accurate error message
		final Command cmd = Command.parse(cmdName);
		if (cmd == null) {
			return handleUnknownCommand(cmdName);
		}
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
				return handleUnknownCommand(cmdName);
		}
	}

}
