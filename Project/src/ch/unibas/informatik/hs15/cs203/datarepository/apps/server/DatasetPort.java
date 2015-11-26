package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import util.logging.Logger;
import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.cli.HTMLWriter;
import ch.unibas.informatik.hs15.cs203.datarepository.common.CriteriaWrapper;
import ch.unibas.informatik.hs15.cs203.datarepository.common.Version;
import ch.unibas.informatik.hs15.cs203.datarepository.processing.Factory;

/**
 * The {@link DatasetPort} class represents the 'server mode' of the data
 * repository application. See the <a href=
 * "http://informatik.unibas.ch/fileadmin/Lectures/HS2015/software-engineering/specifications2.pdf"
 * > extended specifications</a> for further detail.
 * 
 * @author Loris
 */
public class DatasetPort {

	/**
	 * The repository path
	 */
	private final Path repo;

	/**
	 * Configuration
	 */
	private DatasetPortConfiguration config;

	/**
	 * Singleton
	 */
	private static DatasetPort instance = null;

	public static DatasetPort getDatasetPort(Path repo,
			DatasetPortConfiguration config, DataRepository app) {
		if (instance == null) {
			instance = new DatasetPort(repo, config, app);
		}
		return instance;
	}

	private DataRepository app = null;

	// private WatchService service = null;
	//
	// private WatchKey key = null;

	private DatasetPort(Path repo, DatasetPortConfiguration config,
			DataRepository app) {
		this.config = config;
		this.repo = repo;
		this.app = app;
		setup();
	}

	private void setup() throws IllegalArgumentException {
		if (!config.getIncoming().toFile().exists()) {
			throw new IllegalArgumentException(
					"Error while starting Server. Incoming Directory does not exist");
		}
		if (!config.getIncoming().toFile().isDirectory()) {
			throw new IllegalArgumentException(
					"Error while starting Server. Incoming Path is not a directory");
		}
		if (config.getCompletenessDetection() == null) {
			throw new IllegalArgumentException(
					"Error while starting Server. No completeness detection has been specified");
		}
		if (config.getScanInterval() <= 0) {
			throw new IllegalArgumentException("Error while starting Server. Invalid Scan Interval");
		}
	}

	/**
	 * Public entry point to start a DatasetPort Setup() has been called at this
	 * point
	 */
	public void start() {
		if(config.getHtmlOverview()!=null){
			HTMLWriter writer = new HTMLWriter(config.getHtmlOverview().toString());
			try {
				writer.update(app.getMetaData(CriteriaWrapper.all().getWrappedObject()));
			} catch (IOException e) {
				throw new IllegalArgumentException("Error while starting Server. HTML file could not be created.");
			}
		}
		Logger logger = Logger.getLogger(this.getClass());
		
		StringBuilder builder = new StringBuilder();
		builder.append(Version.VERSION);
		builder.append(", "+config.toString());
		logger.info(builder.toString());

		// TODO Print everything to Logfile

		logger.info("Server start successful");
		// TODO Log SUCCESS Message on Standard output
		File directory = config.getIncoming().toFile();
		for (;;) {
			if(directory.listFiles().length!=0){
				for(File file : directory.listFiles()){
					try {
						config.getCompletenessDetection().newInstance().verifyCompletness(file.toPath());
					} catch (InstantiationException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// TODO Execute add with --move
			// TODO Update HTML File
			// the loop
			try {
				Thread.sleep(config.getScanInterval());
			} catch (InterruptedException e) {
				throw new IllegalArgumentException("Server execution interrupted");
			}
		}
		// finishing up
	}

}
