package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.File;
import java.io.IOException;

import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;

/**
 * Static factory for creating a {@link DataRepository}.
 * 
 * @author Franz-Josef Elmer
 * 
 */
public class Factory {
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
	public static DataRepository create(File repositoryFolder) {
		//Verify File param
		if (repositoryFolder == null) {
			throw new IllegalArgumentException("The repository folder is null");
		}
		if (repositoryFolder.isFile()) {
			throw new IllegalArgumentException(
					"The given path points to a file and not a folder");
		}
		if (repositoryFolder.exists() && !repositoryFolder.isDirectory()) {
			throw new IllegalArgumentException(
					"The given path does not point to a folder, but to something else");
		}
		if(!repositoryFolder.exists()){
			repositoryFolder.mkdirs();
		}
		//Create Repo and return it
		DataRepositoryImpl _ret;
		try {
			_ret = new DataRepositoryImpl(repositoryFolder);
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		return _ret;
	}

	private Factory() {
	}
}
