package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
	public void interpret(final String[] args) throws ParseException {
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
				executeAdd(command);
				break;
			case EXPORT:
				executeExport(command);
				break;
			case LIST:
				executeList(command);
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
	private void executeAdd(final LinkedList<String> arguments) {
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
		repo.add(new File(file), desc, move, listener);
	}

	/**
	 * Executes the List command of the data repository application.The paramter
	 * <code>arguments</code> are the arguments without the command itself
	 * @param arguments
	 * arguments in tokenizer list form
	 * @throws ParseException 
	 */
	private void executeList(final LinkedList<String> arguments) throws ParseException {
		// TODO Auto-generated method stub
		String curr=null,ID=null,NAME=null,TEXT=null,BEFORE=null,AFTER=null,repoLoc=null;
		final int originSize = arguments.size();
		for (int i = 0; i < originSize; i++) {
			curr=arguments.poll();
			if(curr.startsWith(Option.ID.name())){
				ID=curr;
			}
			else if(curr.startsWith(Option.NAME.name())){
				NAME=curr;
			}else if(curr.startsWith(Option.TEXT.name())){
				TEXT=curr;
			}else if(curr.startsWith(Option.BEFORE.name())){
				BEFORE=curr;
			}else if(curr.startsWith(Option.AFTER.name())){
				AFTER=curr;
			}else{
				if(originSize-i>2){
					throw new IllegalArgumentException(
							"Inappropriate number of arguments");
				}else{
					repoLoc=curr;
				}
			}

		}
		final DataRepository repo = Factory.create(new File(repoLoc));


		Criteria cr;
		if(ID==null && TEXT==null && BEFORE==null && AFTER==null && TEXT == null){
			cr=Criteria.all();
		}else if(ID==null && (TEXT!=null || BEFORE!=null || AFTER!=null || TEXT != null)){
			Date before=null, after=null;
			DateFormat dateFormat1 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			DateFormat dateFormat2 = new SimpleDateFormat(
					"yyyy-MM-dd");

			if(BEFORE!=null){
				if(BEFORE.length()>10){
					before=dateFormat1.parse(BEFORE);
				}else{
					before=dateFormat2.parse(BEFORE);
				}
			}
			if(AFTER!=null){
				if(AFTER.length()>10){
					after=dateFormat1.parse(AFTER);
				}else{
					after=dateFormat2.parse(AFTER);
				}
			}

			cr=new Criteria(NAME, TEXT,before,after);
		}else{
			cr=Criteria.forId(ID);
		}
		List<MetaData> ret=repo.getMetaData(cr);
		System.out.println("ID\tName\tTimestamp\tNumber of Files\tSize\tDescription\n");
		for(MetaData i:ret){
			System.out.println(i.getId()+"\t"+i.getName()
					+"\t"+i.getTimestamp()+"\t"+i.getNumberOfFiles()+"\t"+i.getSize()+"\t"+i.getDescription()+"\n");
		}
		//Use method here

	}
	/**
	 * Executes the Export command of the data repository application.The paramter
	 * <code>arguments</code> are the arguments without the command itself
	 * @param arguments
	 * arguments in tokenizer list form
	 * @throws ParseException 
	 */
	private void executeExport(final LinkedList<String> arguments) throws ParseException {
		// TODO Auto-generated method stub
		String curr,destLoc=null,ID=null,NAME=null,TEXT=null,BEFORE=null,AFTER=null,repoLoc=null;
		final ProgressListener listener = new DummyProgressListener();
		boolean Identifier=false;
		final int originSize = arguments.size();
		for(int i=0;i<originSize;i++){
			curr=arguments.poll();
			if(curr.startsWith(Option.ID.name())){
				Identifier=true;
				ID=curr.substring(curr.indexOf(CommandParser.OPTION_SEPARATOR)+1);
			}
			else if(curr.startsWith(Option.NAME.name())){
				Identifier=true;
				NAME=curr.substring(curr.indexOf(CommandParser.OPTION_SEPARATOR)+1);
			}else if(curr.startsWith(Option.TEXT.name())){
				Identifier=true;
				TEXT=curr.substring(curr.indexOf(CommandParser.OPTION_SEPARATOR)+1);
			}else if(curr.startsWith(Option.BEFORE.name())){
				Identifier=true;
				BEFORE=curr.substring(curr.indexOf(CommandParser.OPTION_SEPARATOR)+1);
			}else if(curr.startsWith(Option.AFTER.name())){
				Identifier=true;
				AFTER=curr.substring(curr.indexOf(CommandParser.OPTION_SEPARATOR)+1);
			}else if(curr.startsWith(Option.VERBOSE.name())){

			}
			else if(!Identifier){
				if(originSize!=3){
					throw new IllegalArgumentException(
							"Inappropriate number of arguments");
				}else{
					switch(i){
						case 0:
							repoLoc=curr;
						case 1:
							ID=curr;
						case 2:
							destLoc=curr;
					}
				}
			} else if (originSize-i>2){
				throw new IllegalArgumentException(
						"Inappropriate number of arguments");
			} else if (i == originSize - 2) {
				repoLoc = curr;
			} else if (i == originSize - 1) {
				destLoc = curr;
			} else {
				throw new RuntimeException("Reached unexpected state.");
			}
		}
		final DataRepository repo = Factory.create(new File(repoLoc));

		Criteria cr;
		if(ID==null && TEXT==null && BEFORE==null && AFTER==null && TEXT == null){
			cr=Criteria.all();
		}else if(ID==null && (TEXT!=null || BEFORE!=null || AFTER!=null || TEXT != null)){
			Date before=null, after=null;
			DateFormat dateFormat1 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			DateFormat dateFormat2 = new SimpleDateFormat(
					"yyyy-MM-dd");

			if(BEFORE!=null){
				if(BEFORE.length()>10){
					before=dateFormat1.parse(BEFORE);
				}else{
					before=dateFormat2.parse(BEFORE);
				}
			}
			if(AFTER!=null){
				if(AFTER.length()>10){
					after=dateFormat1.parse(AFTER);
				}else{
					after=dateFormat2.parse(AFTER);
				}
			} 
			cr=new Criteria(NAME, TEXT,before,after);
		}else{
			cr=Criteria.forId(ID);
		}

		repo.export(cr, new File(destLoc), listener);

		//Use method here

	}


}
