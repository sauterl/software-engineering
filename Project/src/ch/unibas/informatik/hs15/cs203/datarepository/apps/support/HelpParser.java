package ch.unibas.informatik.hs15.cs203.datarepository.apps.support;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import util.jsontools.Json;
import util.jsontools.JsonParser;

/**
 * The HelpParser parses a <code>help_<command>.json</code> file.
 * @author Loris
 *
 */
class HelpParser {
	
	private Json file = null;
	
	private String command;
	
	public static final String PREFIX = "help_";
	public static final String SUFFIX = ".json";
	public static final String FOLDER = "support/";
	
	public static final String NAME_KEY = "name";
	public static final String SYNOPSIS_KEY = "synopsis";
	public static final String PARAMS_KEY = "params";
	public static final String DESC_KEY = "desc";
	public static final String SHORT_KEY = "short";
	
	public HelpParser(String command){
		this.command = command;
	}
	
	public void readFile() throws IOException{
		JsonParser parser = new JsonParser();
		file = parser.parseFile(getClass().getClassLoader().getResource(FOLDER+PREFIX+command+SUFFIX).getPath());
		if(!validateJson() ){
			throw new IllegalArgumentException("Invalid json");
		}
	}
	
	public boolean hasContents(){
		return file != null;
	}
	
	public String getName(){
		return file.getString(NAME_KEY);
	}
	
	public String getSynopsis(){
		return file.getString(SYNOPSIS_KEY);
	}
	
	public String getShort(){
		if(file.containsKey(SHORT_KEY) ){
			return file.getString(SHORT_KEY);
		}else{
			return null;
		}
	}
	
	/**
	 * 
	 * @return A string array, each entry represents a line. OR <tt>null</tt> IF no params are found!
	 */
	public String[] getParamsLines(){
		// TODO Rewrite if jsonutils done
		ArrayList<String> out = new ArrayList<String>();
		Json[] lines = file.getSet(PARAMS_KEY);
		for(Json j : lines){
			Set<String> keys = j.getMap().keySet();
			if(keys != null){
				if(keys.size() != 1){
					throw new RuntimeException("Params JSON *must only* contain ONE key and ONE value");
				}else{
					String key = keys.iterator().next();
					String value = j.getString(key);
					out.add(key+" "+value);
				}
				
			}else{
				return null;
			}
		}
		return out.toArray(new String[0]);
	}
	
	public File getDescriptionFile(){
		File f = new File(getClass().getClassLoader().getResource(FOLDER+file.getString(DESC_KEY)).getPath() );
		return f;
	}
	
	private boolean validateJson(){
		boolean err = false;
		String rawMsg = "Validation failure: Key <%s> does not exist.";
		String type = "";
//		if(!file.containsKey(NAME_KEY)){
//			err = true;
//			type = NAME_KEY;
//		}
		if(!file.containsEntry(SHORT_KEY)){
			err = true;
			type = SHORT_KEY;
		}
		if(!file.containsKey(SYNOPSIS_KEY)){
			err = true;
			type = SYNOPSIS_KEY;
		}
		if(!file.containsKey(DESC_KEY)){
			err = true;
			type = DESC_KEY;
		}
		if(!file.containsSet(PARAMS_KEY)){
			err = true;
			type = PARAMS_KEY;
		}
		if(err){
			throw new RuntimeException(String.format(rawMsg, type));
		}
		return !err;
	}

}
