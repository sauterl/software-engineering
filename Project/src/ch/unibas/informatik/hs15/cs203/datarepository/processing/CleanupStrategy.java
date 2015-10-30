/**
 * 
 */
package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.nio.file.Path;

/**
 * Strategy pattern for clean ups of meta data.
 * 
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
	 * @param storage The storage to clean up.
	 * @param repo The repo to which the storage belongs to.
	 * @return The amount of alternations on the storage.
	 */
	public int clean(MetaDataStorage storage, Path repo);

}
