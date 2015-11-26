package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

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
	
//	private WatchService service = null;
//
//	private WatchKey key = null;
	
	private DatasetPort(Path repo, DatasetPortConfiguration config){
		incoming = config.getIncoming();
		this.repo = repo;
		try{
			setup();
		}catch(IOException ex){
			throw new RuntimeException("Could not start server ", ex);
		}
	}

	private void setup() throws IOException {
		app = Factory.create(repo.toFile());	//TODO
		//service = FileSystems.getDefault().newWatchService();
		//key = incoming.register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
		//TODO Parse Properties, If invalid shut down Log ERROR on Standard output
		//TODO Verify existence of directory to be watched
	}

	/**
	 * Public entry point to start a DatasetPort
	 * Setup() has been called at this point
	 */
	public void start() {
		// startup
		//TODO Recreate HTML File at Properties.html-overview. If null, don't do anything
		//TODO Log SUCCESS Message on Standard output
		
		//TODO Redirect Standard output to Logfile
		for(;;){
			//TODO Check every x seconds for new files in Properties.incoming-dir
			//TODO Use Completeness Detection
			//TODO Execute add with --move
			//TODO Update HTML File
			//the loop
		}
		//finishing up
	}

}
