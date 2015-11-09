package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.util.Iterator;
import java.util.LinkedList;

public class ArgumentsAnalyzer {
	
	private LinkedList<String> args;
	private Command command = null;
	
	private int nbOptions = -1;
	private int nbArguments = -1;
	
	public ArgumentsAnalyzer(){
		args = null;
	}
	
	/**
	 * 
	 * @param command The full command, inclusive command name and its arguments
	 */
	public ArgumentsAnalyzer(LinkedList<String> command){
		setSubject(command);
	}
	
	/**
	 * 
	 * @param cmd The command
	 * @param arguments its arguments
	 */
	public ArgumentsAnalyzer(Command cmd, LinkedList<String> arguments){
		setSubject(cmd, arguments);
	}
	
	public void setSubject(Command cmd, LinkedList<String> arguments){
		this.args = new LinkedList<String>(arguments);
		this.command = cmd;
	}
	
	public void setSubject(LinkedList<String> command) throws IllegalArgumentException{
		this.args = new LinkedList<String>(command);
		String cmdName = args.poll();
		this.command = Command.parse(cmdName);
		if(this.command == null){
			throw new IllegalArgumentException("Command is either unknown or missing.");
		}
	}
	
	public void analyze(){
		validateReady();
		count();
	}
	
	
	private void count(){
		Iterator<String> it = args.iterator();
		int optionsCounter = 0;
		int argumentsCounter = 0;
		while(it.hasNext() ){
			String curr = it.next();
			if(curr.contains(CommandParser.OPTION_SEPARATOR )){
				optionsCounter++;
			}else if(Option.parse(curr)!=null){
				optionsCounter++;
			}else{
				argumentsCounter++;
			}
		}
		this.nbOptions = optionsCounter;
		this.nbArguments = argumentsCounter;
	}
	
	public Command getCommand() {
		validateReady();
		return command;
	}

	public int getNbOptions() {
		validateReady();
		return nbOptions;
	}

	public int getNbArguments() {
		validateReady();
		return nbArguments;
	}

	public int getNbMandatoryArgs() {
		validateReady();
		return command.getMandatoryArgsCount();
	}

	public boolean isReady(){
		return args != null && command != null;
	}
	
	private void validateReady(){
		if(!isReady() ){
			throw new IllegalStateException("Nothing to analyze");
		}
	}
}
