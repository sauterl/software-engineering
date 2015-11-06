package ch.unibas.informatik.hs15.cs203.datarepository.apps.support;

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
	
	public ManPageGenerator(String command){
		this.command = command;
		LOG.config("Set up for command: "+command);
	}
	
	private void readHelpfile(){
		helpParser = new HelpParser(command);
		try {
			helpParser.readFile();
			LOG.info("Read help file");
		} catch (IOException e) {
			LOG.error("Error while reading helpfile", e);
		}
	}
}
