package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;
import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.cli.HTMLWriter;
import ch.unibas.informatik.hs15.cs203.datarepository.common.DummyProgressListener;
import ch.unibas.informatik.hs15.cs203.datarepository.common.Version;

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
	 * Singleton
	 */
	private static DatasetPort instance = null;

	public static DatasetPort getDatasetPort(final Path repo,
			final DatasetPortConfiguration config, final DataRepository app) {
		if (instance == null) {
			instance = new DatasetPort(repo, config, app);
		}
		return instance;
	}

	/**
	 * The repository path
	 */
	private final Path repo;

	/**
	 * Configuration
	 */
	private final DatasetPortConfiguration config;

	private DataRepository app = null;
	private DatasetPortLogger logger = null;

	// private WatchService service = null;
	//
	// private WatchKey key = null;

	private HTMLWriter writer;

	private DatasetPort(final Path repo, final DatasetPortConfiguration config,
			final DataRepository app) {
		this.config = config;
		this.repo = repo;
		this.app = app;
		try {
			init();
		} catch (final IOException ex) {
			throw new RuntimeException("Could not start server ", ex);
		}
	}

	public void start() {
		try {
			setup();
		} catch (final Throwable t) {
			logger.error("Startup failed due to reason: ", t);
		}
		try {
			run();
		} catch (final Throwable t) {
			logger.error("Server crashed due to:", t);
			String out = "Server crashed for reason "
					+ (t != null && t.getMessage() != null ? t.getMessage()
							: "unknown");
			throw new RuntimeException(out, t);
			
		}
	}

	private void init() throws IOException {

		// service = FileSystems.getDefault().newWatchService();
		// key = incoming.register(service,
		// StandardWatchEventKinds.ENTRY_CREATE,
		// StandardWatchEventKinds.ENTRY_MODIFY);
		// TODO Parse Properties, If invalid shut down Log ERROR on Standard
		// output EDIT: NO, properties are parsed already!
		// TODO Verify existence of directory to be watched

		logger = new DatasetPortLogger(repo, config.getLogFile());
	}

	/**
	 * Public entry point to start a DatasetPort Setup() has been called at this
	 * point
	 * @throws IOException 
	 */
	private void run() throws IOException {
		for (;;) {
			// TODO Check every x seconds for new files in
			// Properties.incoming-dir
			// TODO Use Completeness Detection
			// TODO Execute add with --move
			File file = new File("theFile.txt");//TODO Replace with real file!
			MetaData md = app.add(file, null, true, new DummyProgressListener() );
			logger.info("Successfully added dataset with id: "+md.getId());
			// TODO Update HTML File
			writer.update(app.getMetaData(Criteria.all()));
			// the loop
		}
	}

	private void setup() {
		// TODO Recreate HTML File at Properties.html-overview. If null, don't
		// do anything
		writer = new HTMLWriter(config.getHtmlOverview().toString());

		logger.info("Running data-repository on version: "+Version.VERSION);

		System.out.println("Successfully started server mode...");
	}

}
