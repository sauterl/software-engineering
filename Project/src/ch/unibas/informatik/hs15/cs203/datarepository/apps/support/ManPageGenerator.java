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
	private static final String TITLE_FORMAT = "Help to command %s";
	private static final String SYNOPSIS_TITLE = "SYNOPSIS";
	private static final String PARAMS_TITLE = "PARAMETERS and OPTIONS";
	private static final String DESC_TITLE = "DESCRIPTION";
	
	private String command;
	
	private HelpParser helpParser = null;
	
	private DescriptionParser descParser = null;
	
	
	public ManPageGenerator(String command){
		this.command = command;
		LOG.config("Set up for command: "+command);
		init();
	}
	
	protected void init(){
		LOG.debug("Initialization");
		readHelpFile();
		readDescFile();
	}
	
	public String buildManPage() throws IOException{
		// TODO safety check
		LOG.debug("Building man page");
		StringBuilder sb = new StringBuilder();
//		sb.append(String.format(TITLE_FORMAT, helpParser.getName() ));
//		newParagraph(sb);
		String cap = helpParser.getShort();
		if(cap != null){
			sb.append(cap);
			newParagraph(sb);
		}
		// SYNOPSIS
		sb.append(SYNOPSIS_TITLE);
		newLine(sb);
		sb.append(helpParser.getSynopsis() );
		newParagraph(sb);
		// PARAMS
		sb.append(PARAMS_TITLE);
		newLine(sb);
		sb.append(buildParams() );
		newParagraph(sb);
		// DESC
		sb.append(DESC_TITLE);
		newLine(sb);
		sb.append(descParser.parse() );
		newLine(sb);
		return sb.toString();
	}
	
	private String buildParams(){
		LOG.debug("Building params");
		StringBuilder sb = new StringBuilder();
		String[] lines = helpParser.getParamsLines();
		for(String l : lines){
			sb.append(l);
			newLine(sb);
		}
		return sb.toString();
	}
	
	private void newLine(StringBuilder sb){
		sb.append("\n");
	}
	
	private void newParagraph(StringBuilder sb){
		sb.append("\n\n");
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
