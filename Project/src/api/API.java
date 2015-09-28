package api;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Map;

import api.options.AbstractParameteredOption;
import api.options.BeforeOption;
import api.options.BeforeOption;
import api.options.NameOption;
import api.options.TextOption;

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
     * Deletes data sets matching to all parameter values of the given
     * {@link AbstractParameteredOption}s. It returns how many data sets have
     * been deleted.
     * 
     * The array of {@link AbstractParameteredOption}s must not be
     * <code>null</code> and contain valid subclasses, otherwise a
     * {@link NullPointerException} or {@link IllegalArgumentException} will be
     * thrown. It must contain one or several of the following
     * {@link AbstractParameteredOption}: {@link NameOption}, {@link TextOption}
     * , {@link BeforeOption}, {@link BeforeOption}.
     * 
     * This method deletes all matching data sets, so if no matching sets are
     * found, none will be deleted.
     * 
     * This operation may or may not be a verbose process, depending on the
     * corresponding flag.
     * 
     * If the {@code repoPath} argument does not point to a repository, a
     * {@link NoRepositoryException} will be thrown.
     * 
     * @param cb
     *            The {@link CallbackInterface} to receive progress information.
     *            This must be a non-<code>null</code> {@link CallbackInterface}
     *            , otherwise an {@link NullPointerException} will be thrown.
     * @param verbose
     *            A flag to specify if progress information gets printed or not:
     *            To get the progress infos set this to <code>true</code> and
     *            <code>false</code> otherwise.
     * @param repoPath
     *            The path to the repository. This path may be absolute (and
     *            thus in OS-dependent notation) or relative to the current
     *            working directory (CWD). In case the CWD points to the root of
     *            a repo, this parameter may be a dot ('.').
     * @param options
     *            A variable list of {@link AbstractParameteredOption}s their
     *            parameter's values define conditions. Any data set that
     *            matches to all of these conditions will be deleted. Must not
     *            contain illegal subclasses of
     *            {@link AbstractParameteredOption}.
     * 
     * @throws NoRepositoryException
     *             If the {@code repoPath} argument does not point to a
     *             repository.
     * @throws NullPointerException
     *             If the {@code options}-array or the {@code cb} is
     *             <code>null</code>.
     * @throws IllegalArgumentException
     *             If one of the {@code options}-array's fields is of illegal
     *             type.
     * @return How many data sets have been deleted. It is valid that no data
     *         set is deleted. This is likely if no data set matched to all of
     *         the given conditions.
     */
    public int delete(CallbackInterface cb, boolean verbose, String repoPath,
	    AbstractParameteredOption<?>... options)
	    throws NoRepositoryException, NullPointerException,
	    IllegalArgumentException;

    /**
     * Deletes the data set with given identifier from the specified repository.
     * 
     * Use this method for delete cases where <data set identifier> or option
     * --id has been specified.
     * 
     * This operation may or may not be a verbose process, depending on the
     * corresponding flag.
     * 
     * If the verbose flag is set <code>true</code> and the
     * {@link CallbackInterface} parameter is set to <code>null</code> an
     * {@link NullPointerException} will be thrown.
     * 
     * If {@code repoPath} is <code>null</code>, a {@link NullPointerException}
     * will be thrown.
     * 
     * @param cb
     *            The {@link CallbackInterface} to receive progress information.
     *            This must be a non-<code>null</code> {@link CallbackInterface}
     *            , otherwise an {@link NullPointerException} will be thrown.
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
     *            The identifier for the data set to be deleted. If no data set
     *            with such an identifier exists, a {@link UnknownIDException}
     *            will be thrown.
     * 
     * @throws UnknownIDException
     *             If no data set with the specified {@code dataSetID} exists.
     * @throws NoRepositoryException
     *             If the {@code repoPath} does not point to a valid repository.
     * 
     * @return <code>true</code> if the operation successfully completed or
     *         <code>false</code> if not.
     */
    public boolean delete(CallbackInterface cb, boolean verbose,
	    String repoPath, int dataSetID) throws UnknownIDException,
	    NoRepositoryException;

    /**
     * Exports the data set with given identifier to the specified destination
     * folder.
     * 
     * Use this method for export cases where <data set identifier> or option
     * --id has been specified.
     * 
     * If the verbose flag is set <code>true</code> and the
     * {@link CallbackInterface} parameter is set to <code>null</code> an
     * {@link NullPointerException} will be thrown.
     * 
     * If {@code repoPath} is <code>null</code>, a {@link NullPointerException}
     * will be thrown.
     * 
     * If {@code destPath} is <code>null</code>, a {@link NullPointerException}
     * will be thrown.
     * 
     * @param cb
     *            The {@link CallbackInterface} to receive progress information.
     *            This must be a non-<code>null</code> {@link CallbackInterface}
     *            , otherwise an {@link NullPointerException} will be thrown.
     * @param verbose
     *            A flag to specify if progress information gets printed or not:
     *            To get the progress infos set this to <code>true</code> and
     *            <code>false</code> otherwise.
     * @param repoPath
     *            The path to the repository. This path may be absolute (and
     *            thus in OS-dependent notation) or relative to the current
     *            working directory (CWD). In case the CWD points to the root of
     *            a repo, this parameter may be a dot ('.').
     * @param destPath
     *            The path to the target repository. This path may be absolute
     *            (and thus in OS-dependent notation) or relative to the current
     *            working directory (CWD). In case the CWD points to the root of
     *            a repo, this parameter may be a dot ('.').
     * @param options
     *            A variable list of {@link AbstractParameteredOption}s their
     *            parameter's values define conditions. Any data set that
     *            matches to all of these conditions will be deleted. Must not
     *            contain illegal sublclasses of
     *            {@link AbstractParameteredOption}.
     * 
     * @return A map with dataset identifiers and their names that have been
     *         exported. It is valid that no data set is exported
     * @throws DuplicateException
     *             If at least two data sets have the same name. The error
     *             contains the identifiers of all data sets which could not be
     *             exported
     */
    public Map<Integer, String> export(CallbackInterface cb, boolean verbose,
	    String repoPath, String destFolder,
	    AbstractParameteredOption<?>... options) throws DuplicateException;

    /**
     * Exports the data set with given identifier to the specified destination
     * folder.
     * 
     * Use this method for export cases where <data set identifier> or option
     * --id has been specified.
     * 
     * If the verbose flag is set <code>true</code> and the
     * {@link CallbackInterface} parameter is set to <code>null</code> an
     * {@link NullPointerException} will be thrown.
     * 
     * If {@code repoPath} is <code>null</code>, a {@link NullPointerException}
     * will be thrown.
     * 
     * If {@code destPath} is <code>null</code>, a {@link NullPointerException}
     * will be thrown.
     * 
     * @param cb
     *            The {@link CallbackInterface} to receive progress information.
     *            This must be a non-<code>null</code> {@link CallbackInterface}
     *            , otherwise an {@link NullPointerException} will be thrown.
     * @param verbose
     *            A flag to specify if progress information gets printed or not:
     *            To get the progress infos set this to <code>true</code> and
     *            <code>false</code> otherwise.
     * @param repoPath
     *            The path to the repository. This path may be absolute (and
     *            thus in OS-dependent notation) or relative to the current
     *            working directory (CWD). In case the CWD points to the root of
     *            a repo, this parameter may be a dot ('.').
     * @param destPath
     *            The path to the target repository. This path may be absolute
     *            (and thus in OS-dependent notation) or relative to the current
     *            working directory (CWD). In case the CWD points to the root of
     *            a repo, this parameter may be a dot ('.').
     * @param dataSetID
     *            The identifier for the data set to be exported. If no data set
     *            with such an identifier exists, a {@link UnknownIDException}
     *            will be thrown.
     * 
     * @throws UnknownIDException
     *             If no data set with the specified {@code dataSetID} exists.
     * @throws NoRepositoryException
     *             If the {@code repoPath} does not point to a valid repository.
     * 
     * @return <code>true</code> if the operation successfully completed or
     *         <code>false</code> if not.
     */
    public boolean export(CallbackInterface cb, boolean verbose,
	    String repoPath, String destPath, int dataSetID)
	    throws UnknownIDException, FileAlreadyExistsException,
	    NoRepositoryException;

    /**
     * Returns version of the software and information about commands and/or
     * parameters. The user is also informed how to get more information for a
     * command
     * 
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

    /**
     * Returns the meta data of the data set with given identifier.
     * 
     * The meta data is a TAB-separated table using the newline character to
     * separate rows and the TAB-character to separate columns.
     * <ul>
     * <li>The first row contains always the following header names:
     * <ul>
     * <code>
     * <li>ID</li>
     * <li>Name</li>
     * <li>Timestamp</li>
     * <li>Number of Files</li>
     * <li>Size</li>
     * <li>Description</li>
     * </code>
     * </ul>
     * </li> <li>The name is the original file/folder name.</li> <li>The table
     * is sorted in accordance to the timestamp.</li> <li>If no description has
     * been specified in the add command an empty string is printed.</li> <li>
     * The number of files also includes directories.</li> <li>The size is the
     * sum of the sizes of all files in bytes.</li> <li>The timestamp is defined
     * by the date and time of adding/replacing the data set. It is shown in the
     * following format: <code>YYYY-MM-DD HH:MM:SS</code></li> <li>In case of an
     * empty or non-existing repository only the header line is printed.</li>
     * </ul>
     * 
     * <p>
     * This method in particular does return a two rowed table: the first row
     * being the headers and the second one being the data row with the values
     * of the data set addressed with the given identifier.
     * </p>
     * 
     * @param dataSetID
     *            The identifier of the data set from which the meta data gets
     *            printed. If the given identifier does not point to a data set,
     *            a {@link UnknownIDException} will be thrown.
     * @param repoPath
     *            The path pointing to a repository. If there is no repository
     *            at the given location, a {@link NoRepositoryException}.
     * @return meta data as a TAB-separated table. In case of an empty or
     *         non-existing repository only the header line is printed.
     * @throws UnknownIDException
     *             If the given identifier does not point to a data set.
     * @throws NoRepositoryException
     *             If the given {@code repoPath} does not point to a repository.
     */
    public String list(int dataSetID, String repoPath)
	    throws UnknownIDException, NoRepositoryException;

    /**
     * Returns the meta data of the data set with given identifier.
     * 
     * The meta data is a TAB-separated table using the newline character to
     * separate rows and the TAB-character to separate columns.
     * <ul>
     * <li>The first row contains always the following header names:
     * <ul>
     * <code>
     * <li>ID</li>
     * <li>Name</li>
     * <li>Timestamp</li>
     * <li>Number of Files</li>
     * <li>Size</li>
     * <li>Description</li>
     * </code>
     * </ul>
     * </li> <li>The name is the original file/folder name.</li> <li>The table
     * is sorted in accordance to the timestamp.</li> <li>If no description has
     * been specified in the add command an empty string is printed.</li> <li>
     * The number of files also includes directories.</li> <li>The size is the
     * sum of the sizes of all files in bytes.</li> <li>The timestamp is defined
     * by the date and time of adding/replacing the data set. It is shown in the
     * following format: <code>YYYY-MM-DD HH:MM:SS</code></li> <li>In case of an
     * empty or non-existing repository only the header line is printed.</li>
     * <li>The result will be filtered by the provided options:
     * <ul>
     * <li>A {@link NameOption}'s name leads to a result with data sets only
     * containing the specified name.</li>
     * <li>A {@link TextOption}'s text leads to a result with data sets where
     * the name or the description contains the specified text snippet.</li>
     * <li>A {@link BeforeOption}'s timestamp lead to a result with data sets
     * with a timestamp before the specified one.</li>
     * <li>A {@link BeforeOption}'s timestamp lead to a result with data sets
     * with a timestamp after the specified one.</li>
     * </ul>
     * </li> <li>Each option restricts the result. That is, only data sets
     * fulfilling all options are shown.</li> </ul>
     * 
     * <p>
     * This method in particular does return a two rowed table: the first row
     * being the headers and the second one being the data row with the values
     * of the data set addressed with the given identifier.
     * </p>
     * 
     * @param repoPath
     *            The path pointing to a repository. If there is no repository
     *            at the given location, a {@link NoRepositoryException}.
     * @param options
     *            A vararg list of restricting options of type
     *            {@link BeforeOption}, {@link BeforeOption}, {@link NameOption}
     *            or {@link TextOption} as described in the method description.
     *            If that list is empty, a table with meta data of all data sets
     *            of the given repository is returned.
     * 
     * @return meta data as a TAB-separated table. In case of an empty or
     *         non-existing repository only the header line is printed.
     * @throws NoRepositoryException
     *             If the given {@code repoPath} does not point to a repository.
     */
    public String list(String repoPath, AbstractParameteredOption<?>... options);

    /**
     * Replaces the data set with given identifier completely with the newly
     * specified one. If the old data set had a description, the new data set
     * will receive the same description.
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
     * copied / moved again with a different identifier.
     * 
     * This command will behave like a sequence of a delete and add command.
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
     *            The identifier for the data set to replace. If no data set
     *            with such an identifier exists, a {@link UnknownIDException}
     *            will be thrown.
     * @param dataSetPath
     *            A relative or absolute path to the repository, the data set
     *            will be added to. This path must be in the OS-specify notation
     *            if absolute or otherwise relative to the path of the current
     *            working directory.
     * @throws IOException
     *             If the {@code dataSetPath} cannot be resolved to a valid
     *             path, no file or folder could be found or a general exception
     *             occurred while copying / moving the data set.
     * @throws NoRepositoryException
     *             If the {@code repoPath} does not point to a valid repository.
     * @throws UnknownIDException
     *             If no data set with the specified {@code dataSetID} exists.
     * @return <code>true</code> if the operation successfully completed or
     *         <code>false</code> if not.
     */
    public boolean replace(CallbackInterface cb, boolean move, boolean verbose,
	    String repoPath, int dataSetID, String dataSetPath)
	    throws IOException, NoRepositoryException, UnknownIDException;

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
     * copied / moved again with a different identifier.
     * 
     * This command will behave like a sequence of a delete and add command.
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
     *            The identifier for the data set to replace. If no data set
     *            with such an identifier exists, a {@link UnknownIDException}
     *            will be thrown.
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
     * @throws NoRepositoryException
     *             If the {@code repoPath} does not point to a valid repository.
     * @throws UnknownIDException
     *             If no data set with the specified {@code dataSetID} exists.
     * 
     * @return <code>true</code> if the operation successfully completed or
     *         <code>false</code> if not.
     */
    public boolean replace(CallbackInterface cb, String description,
	    boolean move, boolean verbose, String repoPath, int dataSetID,
	    String dataSetPath) throws IOException, IllegalArgumentException,
	    NoRepositoryException, UnknownIDException;
}
