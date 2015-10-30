/**
 * 
 */
package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import util.logging.Logger;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;

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
		MetaData[] metas = storage.getAll();
		//cleanup repo based on id
		for(MetaData meta : metas){
			String id = meta.getId();
			Path expected = Paths.get(repo.toString(), id);
			if(!Files.exists(expected, LinkOption.NOFOLLOW_LINKS)){
				storage.remove(meta);
				counter++;
			}
		}
		return counter;
	}

}
