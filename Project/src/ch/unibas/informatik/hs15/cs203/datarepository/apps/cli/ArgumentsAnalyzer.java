package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.util.Iterator;
import java.util.LinkedList;

public class ArgumentsAnalyzer {
	
	private LinkedList<String> origin;
	private Command command = null;
	
	private int nbOptions = -1;
	private int nbArguments = -1;
	
	public ArgumentsAnalyzer(){
		origin = null;
	}
	
	public ArgumentsAnalyzer(LinkedList<String> origin){
		setSubject(origin);
	}
	
	public void setSubject(LinkedList<String> origin) throws IllegalArgumentException{
		this.origin = new LinkedList<>(origin);
		String cmd = origin.peek();
		this.command = Command.parse(cmd);
		if(command == null){
			throw new IllegalArgumentException("Command is either unknown or missing.");
		}
	}
	
	public void analyze(){
		validateReady();
		count();
	}
	
	
	private void count(){
		validateReady();
		Iterator<String> it = origin.iterator();
		int optionsCounter = 0;
		int argumentsCounter = 0;
		while(it.hasNext() ){
			String curr = it.next();
			if(curr.contains(CommandParser.OPTION_SEPARATOR )){
				optionsCounter++;
			}else{
				argumentsCounter++;
			}
		}
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
		return origin != null;
	}
	
	private void validateReady(){
		if(!isReady() ){
			throw new IllegalStateException("Nothing to analyze");
		}
	}
}
