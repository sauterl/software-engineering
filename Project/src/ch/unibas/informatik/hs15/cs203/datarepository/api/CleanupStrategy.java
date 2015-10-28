/**
 * 
 */
package ch.unibas.informatik.hs15.cs203.datarepository.api;

import java.nio.file.Path;

import ch.unibas.informatik.hs15.cs203.datarepository.common.MetaDataStorage;

/**
 * Strategy pattern for clean ups of meta data.
 * @author Loris
 *
 */
public interface CleanupStrategy {
	
	/**
	 * Performs a cleanup of the given storage, based on the contents of the repository.
	 * <br />
	 * <b>Note: The cleanup alters the {@link MetaDataStorage}.</b><br />
	 * After the cleanup, the repository and the MetaDataStorage are synchronized, so they
	 * have the same data.
	 * @param repo
	 * @param storage
	 */
	public void clean(Path repo, MetaDataStorage storage);

}
