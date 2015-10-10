package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.File;
import java.util.LinkedList;

import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;
import ch.unibas.informatik.hs15.cs203.datarepository.processing.Factory;

public class CommandInterpreter {

    // DataRepository repo;

    public CommandInterpreter(){
	
    }
    
    @Deprecated
    public CommandInterpreter(DataRepository repo) {
	// this.repo = repo;
    }

    public void execute(String[] args) {
	LinkedList<String> command=CommandParser.lex(args) ;
	if (command.size() < 1) {
	    throw new IllegalArgumentException("Error while parsing commnd.");
	}
	// TODO change if done.
	Command cmd = Command.parse(command.poll());//no null check since check already done in lex
	switch (cmd) {
	case ADD:
	    executeAdd(command);
	break;
	default:
	    throw new UnsupportedOperationException("Not implemented yet");
	}
    }

    private void executeAdd(LinkedList<String> arguments) {
	String desc="", repoLoc=null, file=null;
	boolean move=false;
	ProgressListener listener=null;
	String curr;
	int originSize = arguments.size();
	for(int i=0; i<originSize;i++){
	    curr = arguments.poll();
	    if(curr.startsWith(Option.DESCRIPTION.name() )){
		desc = curr.substring(curr.indexOf(CommandParser.OPTION_SEPARATOR) );
	    }else if(curr.startsWith(Option.MOVE.name() )){
		move = true;
	    }else if(curr.startsWith(Option.VERBOSE.name() )){
		listener = new DummyProgressListener();
	    }else{
		if(i==originSize-2){
		    repoLoc = curr;
		}else if(i==originSize-1){
		    file = curr;
		}else{
		    throw new RuntimeException("Reached unexpected state.");
		}
	    }
	}//endfor
	DataRepository repo = Factory.create(new File(repoLoc));
	repo.add(new File(file), desc, move, listener);
    }
    
}
