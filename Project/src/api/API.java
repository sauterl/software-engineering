package api;

import java.io.IOException;
import java.util.Map;

/**
 * The interface as an entry point of the data processing layer.
 * 
 * This {@link API} defines the available operations and its parameters. A class
 * implementing this interface may be a singleton.
 */
public interface API {

    /**
     * Adds a given data set to the specified repository. The data set to be
     * added may or may not be moved (depending on the move flag) and causes a
     * may or may not verbose process (depending on the verbose flag).
     * 
     * If the verbose flag is set <code>true</code> and the
     * {@link CallbackInterface} parameter is set to <code>null</code> an
     * {@link NullPointerException} will be thrown.
     * 
     * If either {@code repoPath} or {@code dataSetPath} or both arguments are
     * <code>null</code>, a {@link NullPointerException} will be thrown.
     * 
     * In case the given data set is already part of the repository, it will be
     * copied / moved again with a different identifier. If {@code repoPath}
     * does not point to an already existing repository, a new will be created
     * at the specified location.
     * 
     * @param cb
     *            The {@link CallbackInterface} to receive progress
     *            information.This must be a non-<code>null</code>
     *            {@link CallbackInterface}, otherwise an
     *            {@link NullPointerException} will be thrown.
     * @param move
     *            A flag to specify if the given data set will be moved or not:
     *            To get the data set moved, set this to <code>true</code> and
     *            <code>false</code> otherwise.
     * @param verbose
     *            A flag to specify if progress information gets printed or not:
     *            To get the progress infos set this to <code>true</code> and
     *            <code>false</code> otherwise.
     * @param repoPath
     *            The path to the repository. This path may be absolute (and
     *            thus in OS-dependent notation) or relative to the current
     *            working directory (CWD). In case the CWD points to the root of
     *            a repo, this parameter may be a dot ('.').
     * @param dataSetPath
     *            A relative or absolute path to the repository, the data set
     *            will be added to. This path must be in the OS-specify notation
     *            if absolute or otherwise relative to the path of the current
     *            working directory.
     * 
     * @throws IOException
     *             If the {@code dataSetPath} cannot be resolved to a valid
     *             path, no file or folder could be found or a general exception
     *             occurred while copying / moving the data set.
     * 
     * @return The unique identifier the given data set has received and will be
     *         addressed within the repository.
     */
    public int add(CallbackInterface cb, boolean move, boolean verbose,
	    String repoPath, String dataSetPath) throws IOException;

    /**
     * Adds a given data set with a description to the specified repository.
     * There are no ISO control characters like TAB, CR or LF allowed in the
     * description and it is limited to the length of 1000 characters.
     * 
     * The data set to be added may or may not be moved (depending on the move
     * flag) and causes a may or may not verbose process (depending on the
     * verbose flag).
     * 
     * If the verbose flag is set <code>true</code> and the
     * {@link CallbackInterface} parameter is set to <code>null</code> an
     * {@link NullPointerException} will be thrown.
     * 
     * If either {@code repoPath} or {@code dataSetPath} or both arguments are
     * <code>null</code>, a {@link NullPointerException} will be thrown.
     * 
     * In case the given data set is already part of the repository, it will be
     * copied / moved again with a different identifier. If {@code repoPath}
     * does not point to an already existing repository, a new will be created
     * at the specified location.
     * 
     * @param cb
     *            The {@link CallbackInterface} to receive progress information.
     *            This must be a non-<code>null</code> {@link CallbackInterface}
     *            , otherwise an {@link NullPointerException} will be thrown.
     * @param description
     *            The description of the data set. The length is limited to 1000
     *            characters and any ISO control characters like TAB, CR or LF
     *            are not permitted and lead to an exception. If
     *            <code>null</code> is used as description a
     *            {@link NullPointerException} will be thrown.
     * @param move
     *            A flag to specify if the given data set will be moved or not:
     *            To get the data set moved, set this to <code>true</code> and
     *            <code>false</code> otherwise.
     * @param verbose
     *            A flag to specify if progress information gets printed or not:
     *            To get the progress infos set this to <code>true</code> and
     *            <code>false</code> otherwise.
     * @param repoPath
     *            The path to the repository. This path may be absolute (and
     *            thus in OS-dependent notation) or relative to the current
     *            working directory (CWD). In case the CWD points to the root of
     *            a repo, this parameter may be a dot ('.').
     * @param dataSetPath
     *            A relative or absolute path to the repository, the data set
     *            will be added to. This path must be in the OS-specify notation
     *            if absolute or otherwise relative to the path of the current
     *            working directory.
     * 
     * @throws IOException
     *             If the {@code dataSetPath} cannot be resolved to a valid
     *             path, no file or folder could be found or a general exception
     *             occurred while copying / moving the data set.
     * @throws IllegalArgumentException
     *             If the {@code description} parameter contains ISO control
     *             characters (like TAB, CR or LF).
     * 
     * @return The unique identifier the given data set has received and will be
     *         addressed within the repository.
     */
    public int add(CallbackInterface cb, String description, boolean move,
	    boolean verbose, String repoPath, String dataSetPath)
	    throws IOException, IllegalArgumentException;

    /**
     * Replaces the data set with given identifier completely with the newly
     * specified one. If the old data set had a description, it will be kept.
     * 
     * The data set to replace the old one may or may not be moved (depending on
     * the move flag) and causes a may or may not verbose process (depending on
     * the verbose flag).
     * 
     * If the verbose flag is set <code>true</code> and the
     * {@link CallbackInterface} parameter is set to <code>null</code> an
     * {@link NullPointerException} will be thrown.
     * 
     * If either {@code repoPath} or {@code dataSetPath} or both arguments are
     * <code>null</code>, a {@link NullPointerException} will be thrown.
     * 
     * In case the given data set is already part of the repository, it will be
     * copied / moved again with a different identifier. If {@code repoPath}
     * does not point to an already existing repository, a new will be created
     * at the specified location.
     * 
     * @param cb
     *            The {@link CallbackInterface} to receive progress
     *            information.This must be a non-<code>null</code>
     *            {@link CallbackInterface}, otherwise an
     *            {@link NullPointerException} will be thrown.
     * @param move
     *            A flag to specify if the given data set will be moved or not:
     *            To get the data set moved, set this to <code>true</code> and
     *            <code>false</code> otherwise.
     * @param verbose
     *            A flag to specify if progress information gets printed or not:
     *            To get the progress infos set this to <code>true</code> and
     *            <code>false</code> otherwise.
     * @param repoPath
     *            The path to the repository. This path may be absolute (and
     *            thus in OS-dependent notation) or relative to the current
     *            working directory (CWD). In case the CWD points to the root of
     *            a repo, this parameter may be a dot ('.').
     * @param dataSetID
     *            The identifier for the data set to replace.
     * @param dataSetPath
     *            A relative or absolute path to the repository, the data set
     *            will be added to. This path must be in the OS-specify notation
     *            if absolute or otherwise relative to the path of the current
     *            working directory.
     * @throws IOException
     *             If the {@code dataSetPath} cannot be resolved to a valid
     *             path, no file or folder could be found or a general exception
     *             occurred while copying / moving the data set.
     */
    public void replace(CallbackInterface cb, boolean move, boolean verbose,
	    String repoPath, int dataSetID, String dataSetPath)
	    throws IOException;

    /**
     * Replaces the data set with given identifier completely with the newly
     * specified one. There are no ISO control characters like TAB, CR or LF
     * allowed in the description and it is limited to the length of 1000
     * characters.
     * 
     * The data set to replace the old one may or may not be moved (depending on
     * the move flag) and causes a may or may not verbose process (depending on
     * the verbose flag).
     * 
     * If the verbose flag is set <code>true</code> and the
     * {@link CallbackInterface} parameter is set to <code>null</code> an
     * {@link NullPointerException} will be thrown.
     * 
     * If either {@code repoPath} or {@code dataSetPath} or both arguments are
     * <code>null</code>, a {@link NullPointerException} will be thrown.
     * 
     * In case the given data set is already part of the repository, it will be
     * copied / moved again with a different identifier. If {@code repoPath}
     * does not point to an already existing repository, a new will be created
     * at the specified location.
     * 
     * @param cb
     *            The {@link CallbackInterface} to receive progress information.
     *            This must be a non-<code>null</code> {@link CallbackInterface}
     *            , otherwise an {@link NullPointerException} will be thrown.
     * @param description
     *            The description of the data set. The length is limited to 1000
     *            characters and any ISO control characters like TAB, CR or LF
     *            are not permitted and lead to an exception. If
     *            <code>null</code> is used as description a
     *            {@link NullPointerException} will be thrown.
     * @param move
     *            A flag to specify if the given data set will be moved or not:
     *            To get the data set moved, set this to <code>true</code> and
     *            <code>false</code> otherwise.
     * @param verbose
     *            A flag to specify if progress information gets printed or not:
     *            To get the progress infos set this to <code>true</code> and
     *            <code>false</code> otherwise.
     * @param repoPath
     *            The path to the repository. This path may be absolute (and
     *            thus in OS-dependent notation) or relative to the current
     *            working directory (CWD). In case the CWD points to the root of
     *            a repo, this parameter may be a dot ('.').
     * @param dataSetID
     *            The identifier for the data set to replace.
     * @param dataSetPath
     *            A relative or absolute path to the repository, the data set
     *            will be added to. This path must be in the OS-specify notation
     *            if absolute or otherwise relative to the path of the current
     *            working directory.
     * 
     * @throws IOException
     *             If the {@code dataSetPath} cannot be resolved to a valid
     *             path, no file or folder could be found or a general exception
     *             occurred while copying / moving the data set.
     * @throws IllegalArgumentException
     *             If the {@code description} parameter contains ISO control
     *             characters (like TAB, CR or LF).
     */
    public void replace(CallbackInterface cb, String description, boolean move,
	    boolean verbose, String repoPath, int dataSetID, String dataSetPath)
	    throws IOException, IllegalArgumentException;

    /**
     * Use this method for delete cases where <data set identifier> or option
     * --id has been specified
     * 
     * @throws UnknownIDException
     *             no data set with specified identifier exists
     */
    public void delete(CallbackInterface cb, boolean verbose, String repoPath,
	    int dataSetID) throws UnknownIDException;

    /**
     * 
     * @return how many data sets have been deleted. It is valid that no data
     *         set is deleted
     */
    public int delete(CallbackInterface cb, OptionsContainer oc,
	    boolean verbose, String repoPath);

    /**
     * 
     * @throws UnknownIDException
     *             If no data set with specified identifier exists
     */
    public void export(CallbackInterface cb, boolean verbose, String repoPath,
	    String destPath, int dataSetID) throws UnknownIDException;

    /**
     * TODO Define Exception
     * 
     * @return A map with dataset identifiers and their names that have been
     *         exported. It is valid that no data set is exported
     * @throws DuplicateException
     *             If at least two data sets have the same name. The error
     *             contains the identifiers of all data sets which could not be
     *             exported
     */
    public Map<Integer, String> export(CallbackInterface cb,
	    OptionsContainer oc, boolean verbose, String repoPath,
	    String destFolder) throws DuplicateException;

    /**
     * 
     * @return meta data as a TAB-seperated table. In case of an empty or
     *         non-existing repository only the header line is printed.
     */
    public String list(int dataSetID, String repoPath);

    /**
     * @param oc
     *            If oc is empty, all data sets are listed
     * @return meta data as a TAB-seperated table. In case of an empty or
     *         non-existing repository only the header line is printed.
     */
    public String list(OptionsContainer oc, String repoPath);

    /**
     * @param help
     * @param commandName
     *            can also be empty
     * @return version followed by a list of all commands if the parameter
     *         [command name] is empty. For each command the name and a short
     *         description is printed. The user is also informed how to get more
     *         information for a command. If the command name is specified a
     *         complete synopsis and brief description of the command. If help
     *         is also false, the short help information will also be printed.
     */
    public String info(boolean help, String commandName);
}
