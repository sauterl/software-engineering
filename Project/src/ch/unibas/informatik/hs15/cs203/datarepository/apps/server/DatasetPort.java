package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;

import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.processing.Factory;

/**
 * The {@link DatasetPort} class represents the 'server mode' of the data
 * repository application. See the <a href=
 * "http://informatik.unibas.ch/fileadmin/Lectures/HS2015/software-engineering/specifications2.pdf">
 * extended specifications</a> for further detail.
 * 
 * @author Loris
 */
public class DatasetPort {

	/**
	 * The repository path
	 */
	private final Path repo;
	/**
	 * The path to the incoming folder (as described in the specifications)
	 */
	private final Path incoming;

	/**
	 * Singleton
	 */
	private static DatasetPort instance = null;
	
	public static DatasetPort getDatasetPort(Path repo, DatasetPortConfiguration config){
		if(instance == null){
			instance = new DatasetPort(repo, config);
		}
		return instance;
	}

	private DataRepository app = null;

	private DatasetPort(Path repo, DatasetPortConfiguration config){
		incoming = config.getIncoming();
		this.repo = repo;
	}
	
	private DatasetPort(Path repo, Path incoming) {
		this.repo = repo;
		this.incoming = incoming;
		app = Factory.create(repo.toFile());
		setup();
	}

	private void setup() {
		// do stuff
		// html thingy etc
	}

	public void start() {
		try {
			WatchKey key = incoming.register(
					FileSystems.getDefault().newWatchService(),
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException x) {
			// do stuff
		}
	}

}
