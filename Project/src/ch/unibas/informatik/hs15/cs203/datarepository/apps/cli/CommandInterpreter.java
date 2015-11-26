package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.server.DatasetPort;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.server.DatasetPortConfiguration;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.support.ManPageGenerator;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.support.Utilities;
import ch.unibas.informatik.hs15.cs203.datarepository.common.CriteriaWrapper;
import ch.unibas.informatik.hs15.cs203.datarepository.common.DummyProgressListener;

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

	private ArgumentsAnalyzer analyzer;

	private DataRepositoryFactory factory;
	private Map<Option, String> optVals;

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
	public CommandInterpreter(final DataRepositoryFactory factory) {
		this.factory = factory;
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
		// command is in particular ARGUMENTS now!
		final Command cmd = Command.parse(cmdName);
		if (cmd == null) {
			return handleUnknownCommand(cmdName);
		}
		analyzer = new ArgumentsAnalyzer(cmd, command);
		analyzer.analyze();
		validateMandatoryArguments();
		validateArgsLimit();
		optVals = CommandParser.parseOptionValues(command);
		switch (cmd) {
			case ADD:
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
			case SERVER:
				executeServer(command);
				return "Server ended"; // any case this will get printed? Only on failure, right? TODO
			default:
				return handleUnknownCommand(cmdName);
		}
	}

	private String createMsgWithIDs(final String msg,
			final List<MetaData> metas) {
		String retStr = new String(msg);
		if (!metas.isEmpty()) {
			for (int i = 0; i < metas.size(); i++) {
				retStr = retStr.concat(metas.get(i).getId());
				if (i < metas.size() - 1) {
					retStr = retStr.concat(", ");
				}
			}
		}
		return retStr;
	}

	private String createTabbedInfoLine(final MetaData meta) {
		final StringBuilder sb = new StringBuilder();
		sb.append(meta.getId());
		sb.append("\t");
		sb.append(meta.getName());
		sb.append("\t");
		sb.append(ParseUtils.formatDate(meta.getTimestamp()));
		sb.append("\t");
		sb.append(meta.getNumberOfFiles());
		sb.append("\t");
		sb.append(meta.getSize());
		sb.append("\t");
		sb.append(meta.getDescription() == null ? "" : meta.getDescription());
		sb.append("\n");
		return sb.toString();
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
		final String desc = optVals.get(Option.DESCRIPTION);
		final boolean move = arguments.contains(Option.MOVE.name());
		ProgressListener listener = new DummyProgressListener();
		if (arguments.contains(Option.VERBOSE.name())) {
			listener = new SimpleProgressListener();
		}
		final int nbOptions = analyzer.getNbOptions();// last option index in
		// arguments: nbOptions-1
		final String repoLoc = arguments.get(nbOptions);// first mandatory
														// argument
		final String file = arguments.get(nbOptions + 1);// second mandatory
															// argument
		final DataRepository repo = factory.create(new File(repoLoc));
		final File add = new File(file);
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
		final String repoLoc = arguments.get(analyzer.getNbOptions());
		final CriteriaWrapper crit = CommandParser.parseCriteria(Command.DELETE,
				arguments);
		validateCriteriaOptions(optVals);
		if(analyzer.getNbOptions() == 0 && analyzer.getNbArguments() == 1){
			throw new IllegalArgumentException("Too few arguments.");
		}
		final List<MetaData> ids = factory.create(new File(repoLoc))
				.delete(crit.getWrappedObject());
		final String retStr = "The following data sets have been deleted: ";
		return createMsgWithIDs(retStr, ids);
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
		final String repoLoc = arguments.get(analyzer.getNbOptions());
		final CriteriaWrapper crit = CommandParser.parseCriteria(Command.EXPORT,
				arguments);
		validateCriteriaOptions(optVals);
		if(analyzer.getNbOptions() == 0 && analyzer.getNbArguments() == 2){
			throw new IllegalArgumentException("Too few arguments.");
		}
		ProgressListener listener = new DummyProgressListener();
		if (arguments.contains(Option.VERBOSE.name())) {
			listener = new SimpleProgressListener();
		}
		final String destLoc = arguments.getLast();
		final List<MetaData> list = factory.create(new File(repoLoc))
				.export(crit.getWrappedObject(), new File(destLoc), listener);
		return createMsgWithIDs("The following data sets have been exported: ",
				list);
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
		final String repoLoc = arguments.getLast();
		final CriteriaWrapper crit = CommandParser.parseCriteria(Command.LIST,
				arguments);
		validateCriteriaOptions(optVals);
		final List<MetaData> list = factory.create(new File(repoLoc))
				.getMetaData(crit.getWrappedObject());

		final StringBuilder out = new StringBuilder(
				"ID\tName\tTimestamp\tNumber of Files\tSize\tDescription\n");
		for (final MetaData m : list) {
			out.append(createTabbedInfoLine(m));
		}
		return out.toString();
	}
	
	private void executeServer(final LinkedList<String> arguments){
		final String repoLoc = arguments.getFirst();
		final String propertiesFile = arguments.getLast();
		
		final DataRepository repo = factory.create(new File(repoLoc));
		DatasetPortConfiguration config  = PropertiesParser.parse(propertiesFile);
		DatasetPort server = DatasetPort.getDatasetPort(new File(repoLoc).toPath(), config, repo);
		server.start();
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
		final String repoLoc = arguments.get(analyzer.getNbOptions());
		ProgressListener listener = new DummyProgressListener();
		if (arguments.contains(Option.VERBOSE.name())) {
			listener = new SimpleProgressListener();
		}
		final boolean move = arguments.contains(Option.MOVE.name());
		final String ID = arguments.get(analyzer.getNbOptions() + 1);
		final String fileLoc = arguments.getLast();
		final String desc = optVals.get(Option.DESCRIPTION);
		final MetaData replaced = factory.create(new File(repoLoc)).replace(ID,
				new File(fileLoc), desc, move, listener);
		return "Successfully replaced data set with id: " + replaced.getId();
	}

	/**
	 * Throws an {@link IllegalArgumentException} with the given message.
	 *
	 * @param msg
	 *            The message for missing mandatory arguments.
	 */
	private void handleMissingMandatoryArguments(final String msg) {
		throw new IllegalArgumentException(msg);
	}

	private String handleUnknownCommand(final String cmd)
			throws IllegalArgumentException {
		String type = "null";
		if (cmd != null) {
			final Command c = Command.parse(cmd);
			if (c.equals(Command.FORTYTWO)) {
				return Utilities.wrapLine(
						"This is the Answer to the Ultimate Question of Life, the Universe and Everything - by Adam Douglas. \"The Hitchhiker's Guide to the Galaxy\" (1979)",
						80);
			}
			type = cmd;
		}
		throw new IllegalArgumentException(String.format(
				"Unknown command <%s>: Check your spelling or use command <help> to get more information.",
				type));
	}

	private void validateMandatoryArguments() {
		if (analyzer.getNbArguments() < analyzer.getNbMandatoryArgs()) {
			handleMissingMandatoryArguments(String.format(
					"Unexpected end of command, expected %d mandatory arguments (but got %d).",
					analyzer.getNbMandatoryArgs(), analyzer.getNbArguments()));
		}
	}
	
	private void validateArgsLimit(){
		if(analyzer.getNbArguments() > analyzer.getMaxArgs() ){
			throw new IllegalArgumentException(String.format("Too many arguments. Only %d arguments are allowed (but got %d, arg1).",  analyzer.getMaxArgs(), analyzer.getNbArguments()));
		}
	}
	
	private void validateCriteriaOptions(Map<Option, String> optVals){
		if(optVals.containsKey(Option.ID )){
			for(Option o : Option.getCriteriaOptions() ){
				if(optVals.containsKey(o) ){
					throw new IllegalArgumentException("Don't mix --id with another 'crteria options'.");
				}
			}
		}else{
			//not --id
		}
	}

}
