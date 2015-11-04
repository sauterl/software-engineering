package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.File;
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
	 * Interprets the given command line arguments. It is <i>highly</i>
	 * recommended to pass <i>directly</i> the command line arguments array to
	 * this method.
	 * 
	 * @param args
	 *            The command line arguments.
	 * @throws ParseException 
	 */
	public String interpret(final String[] args) throws ParseException {
		final LinkedList<String> command = CommandParser.lex(args);
		if (command.size() < 1) {
			throw new IllegalArgumentException("Error while parsing commnd.");
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
	private String executeAdd(final LinkedList<String> arguments) {
		String desc = "", repoLoc = null, file = null;
		boolean move = false;
		final ProgressListener listener = new DummyProgressListener();
		String curr;
		final int originSize = arguments.size();
		for (int i = 0; i < originSize; i++) {
			curr = arguments.poll();
			if (curr.startsWith(Option.DESCRIPTION.name())) {
				desc = curr.substring(curr
						.indexOf(CommandParser.OPTION_SEPARATOR) + 1);
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
		final DataRepository repo = Factory.create(new File(repoLoc));
		MetaData helper=repo.add(new File(file), desc, move, listener);
		String ret="Returned ";
		if(helper==null){
			ret ="Failed";
		}else{
			ret+=helper.getId()+" "+helper.getName();
		}
		return ret;
		
	}

	/**
	 * Executes the List command of the data repository application.The paramter
	 * <code>arguments</code> are the arguments without the command itself
	 * @param arguments
	 * arguments in tokenizer list form
	 * @throws ParseException 
	 */
	private String executeList(final LinkedList<String> arguments) throws IllegalArgumentException, ParseException {
		String repoLoc = arguments.peekLast();
		final DataRepository repo = Factory.create(new File(repoLoc));
		LinkedList<String> Options=new LinkedList<String>(Arrays.asList(Option.ID.name(),Option.NAME.name(),Option.TEXT.name(),Option.BEFORE.name(),Option.AFTER.name()));
		Criteria cr =arguments.size()==1?Criteria.all():criteriaParser(Options, arguments, false);
		List<MetaData> ret=repo.getMetaData(cr);
		String retString="ID\tName\tTimestamp\tNumber of Files\tSize\tDescription\n";
		for(MetaData i:ret){
			retString+=i.getId()+"\t"+i.getName()
					+"\t"+i.getTimestamp()+"\t"+i.getNumberOfFiles()+"\t"+i.getSize()+"\t"+i.getDescription()+"\n";
		}
		return retString;
	}
	/**
	 * Executes the Export command of the data repository application.The paramter
	 * <code>arguments</code> are the arguments without the command itself
	 * @param arguments
	 * arguments in tokenizer list form
	 * @throws ParseException 
	 */
	private String executeExport(final LinkedList<String> arguments) throws IllegalArgumentException, ParseException {
		// TODO Auto-generated method stub
		LinkedList<String> Options=new LinkedList<String>(Arrays.asList(Option.ID.name(),Option.NAME.name(),Option.TEXT.name(),Option.BEFORE.name(),Option.AFTER.name(),Option.VERBOSE.name()));
		Criteria cr =criteriaParser(Options, arguments, true);
		String repoLoc = arguments.get(cr.onlyID() && arguments.size()==3?arguments.size()-2:arguments.size()-3),destLoc = arguments.peekLast();
		final ProgressListener listener = new DummyProgressListener();		
		final DataRepository repo = Factory.create(new File(repoLoc));
		String ret="Exported: \n";
		for(MetaData it:repo.export(cr, new File(destLoc), listener)){
			ret+=it.getId()+" "+it.getName()+"\n";
		}
		return ret;

	}
	/**
	 * It's a thing of style
	 * @param Options
	 * Options that can be searched for in the List ToParse
	 * @param ToParse
	 * List to Parse
	 * @param ID
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ParseException
	 */
	private Criteria criteriaParser(LinkedList<String> Options,LinkedList<String> ToParse,boolean ID) throws IllegalArgumentException, ParseException{
		Criteria crit;
		String a=ToParse.poll();
		Map<String,String> Helper= new HashMap<String,String>();
		for(int it=1;it< ToParse.size();it++){
			if(Options.contains(a)){
				if(Helper.containsKey(a)){
					throw new IllegalArgumentException("Inappropriate number of arguments");
				}else{
					Helper.put(a, ToParse.poll());
				}
			}else{
				if(Options.contains(Option.ID.name()) && ID && Helper.isEmpty() && ToParse.size()>=2){
					Helper.put(Option.ID.name(), ToParse.remove(1));
				}else{
					throw new IllegalArgumentException("Inappropriate number of arguments");
				}
			}
			a=ToParse.poll();
		}
		if(Helper.isEmpty() || Helper.size()>1 && Helper.containsKey(Option.ID.name())){
			throw new IllegalArgumentException("Inappropriate number of arguments");
		}else{
			if(Helper.containsKey(Option.ID.name())){
				crit=Criteria.forId(Helper.get(Option.ID.name()));
			}else{
				
				DateFormat dateFormat1 = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				DateFormat dateFormat2 = new SimpleDateFormat(
						"yyyy-MM-dd");
				
				Date before=Helper.containsKey(Option.BEFORE.name())?Helper.get(Option.BEFORE.name()).length()>10?dateFormat1.parse(Helper.get(Option.BEFORE.name())):dateFormat2.parse(Helper.get(Option.BEFORE.name())):null;
				Date after=Helper.containsKey(Option.AFTER.name())?Helper.get(Option.AFTER.name()).length()>10?dateFormat1.parse(Helper.get(Option.AFTER.name())):dateFormat2.parse(Helper.get(Option.AFTER.name())):null;
				
				crit = new Criteria(Helper.get(Option.NAME.name()), Helper.get(Option.TEXT.name()),before,after);
			}
		}
		return crit;
		
	}


}
