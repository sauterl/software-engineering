package ch.unibas.informatik.hs15.cs203.datarepository.apps.support;

import java.io.FileNotFoundException;
import java.io.IOException;

import util.logging.Logger;

/**
 * Generates ManPages.
 * @author Loris
 *
 */
public class ManPageGenerator {
	// TODO Write JavaDoc
	private final static Logger LOG = Logger.getLogger(ManPageGenerator.class);
	
	private String command;
	
	private HelpParser helpParser = null;
	
	private DescriptionParser descParser = null;
	
	public ManPageGenerator(String command){
		this.command = command;
		LOG.config("Set up for command: "+command);
	}
	
	public String getManPage(){
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
	
	private void readHelpFile(){
		helpParser = new HelpParser(command);
		try {
			helpParser.readFile();
			LOG.info("Read help file");
		} catch (IOException e) {
			LOG.error("Error while reading helpfile", e);
		}
	}
	
	private void readDescFile(){
		if(!checkHelpParserReady() ){
			throw new IllegalStateException("HelpParser not ready");
		}
		try {
			descParser = new DescriptionParser(helpParser.getDescriptionFile() );
			LOG.info("Read desc file");
		} catch (FileNotFoundException e) {
			LOG.error("Error while reading descfile", e);
		}
	}
	
	private boolean checkHelpParserReady(){
		return helpParser != null && helpParser.hasContents();
	}
	
	private boolean checkDescParserReady(){
		return descParser != null && descParser.isReady();
	}
}
