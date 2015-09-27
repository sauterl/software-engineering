package api;

import java.util.Map;

/**
 * Entry point for the data processing layer
 *
 */
public interface API {

	public void add(CallbackInterface cb, boolean move, boolean verbose, String repoPath, String dataSetPath);
	
	/**
	 * @param description Maximum length 1000 characters. No ISO characters allowed
	 * @throws Exception In case of an Invalid Parameter
	 */
	public void add(CallbackInterface cb, String description, boolean move, boolean verbose, String repoPath, String dataSetPath) throws Exception;
	
	public void replace(CallbackInterface cb, boolean move, boolean verbose, String repoPath, int dataSetID, String dataSetPath);
	
	/**
	 * @param description Maximum length 1000 characters. No ISO characters allowed
	 * @throws Exception In case of an Invalid Parameter
	 */
	public void replace(CallbackInterface cb, String description, boolean move, boolean verbose, String repoPath, int dataSetID, String dataSetPath) throws Exception;
	
	/**
	 * Use this method for delete cases where <data set identifier> or option --id has been specified
	 * @throws UnknownIDException no data set with specified identifier exists
	 */
	public void delete(CallbackInterface cb, boolean verbose, String repoPath, int dataSetID) throws UnknownIDException;
	
	/**
	 * 
	 * @return how many data sets have been deleted. It is valid that no data set is deleted
	 */
	public int delete(CallbackInterface cb, OptionsContainer oc, boolean verbose, String repoPath);
	
	/**
	 * 
	 * @throws UnknownIDException If no data set with specified identifier exists
	 */
	public void export(CallbackInterface cb, boolean verbose, String repoPath, String destPath, int dataSetID) throws UnknownIDException;
	
	/**
	 * TODO Define Exception
	 * @return A map with dataset identifiers and their names that have been exported. It is valid that no data set is exported
	 * @throws Exception If at least two data sets have the same name. The error contains the identifiers of all data sets which could not be exported
	 */
	public Map<Integer, String> export(CallbackInterface cb, OptionsContainer oc, boolean verbose, String repoPath, String destFolder) throws Exception;

	/**
	 *
	 * @return meta data as a TAB-seperated table. In case of an empty or non-existing repository only the header line is printed.
	 */
	public String list(int dataSetID, String repoPath);
	
	/**
	 * @param oc If oc is empty, all data sets are listed
	 * @return meta data as a TAB-seperated table.  In case of an empty or non-existing repository only the header line is printed.
	 */
	public String list(OptionsContainer oc, String repoPath);
	
	/**
	 * @param help
	 * @param commandName can also be empty
	 * @return version followed by a list of all commands if the parameter [command name] is empty. For each command the name and a short description is printed. The user is also informed how to get more information for a command.
	 * If the command name is specified a complete synopsis and brief description of the command. If help is also false, the short help information will also be printed.
	 */
	public String info(boolean help, String commandName);
}
