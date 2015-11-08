/**
 * 
 */
package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import ch.unibas.informatik.hs15.cs203.datarepository.common.MetaDataWrapper;
import util.logging.Logger;

/**
 * @author Loris
 *
 */
public class SimpleExistsCleanupStrategy implements CleanupStrategy {
	
	
	private final Logger LOG = Logger.getLogger(this.getClass());

	/**
	 * 
	 */
	public SimpleExistsCleanupStrategy() {
		
	}

	/* (non-Javadoc)
	 * @see ch.unibas.informatik.hs15.cs203.datarepository.processing.CleanupStrategy#clean(ch.unibas.informatik.hs15.cs203.datarepository.processing.MetaDataStorage, java.nio.file.Path)
	 */
	@Override
	public int clean(MetaDataStorage storage, Path repo) {
		int counter=0;
		MetaDataWrapper[] metas = storage.getAll();
		//cleanup repo based on id
		for(MetaDataWrapper meta : metas){
			String id = meta.getId();
			Path expected = Paths.get(repo.toString(), id);
			if(!Files.exists(expected, LinkOption.NOFOLLOW_LINKS)){
				storage.remove(meta);
				counter++;
			}
		}
		// TODO vice versa: clean MDS based on repo state
		return counter;
	}

}
