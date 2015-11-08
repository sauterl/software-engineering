package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.File;

import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;

public interface DataRepositoryFactory {
	
	/**
	 * Creates a {@link DataRepository} for the specified repository folder. The
	 * repository folder will be created if it does not exit.
	 * 
	 * @param repositoryFolder
	 *            Folder which will contain data sets and meta data.
	 * @return an instance of a class implementing {@link DataRepository}.
	 * @throws IllegalArgumentException
	 *             in the following cases:
	 *             <ul>
	 *             <li><code>folder</code> is <code>null</code>
	 *             <li><code>folder</code> is a file and not a folder.
	 *             <li><code>folder</code> is an existing folder but doesn't
	 *             looks like a repository folder.
	 *             </ul>
	 */
	public DataRepository create(File repositoryFolder);
}
