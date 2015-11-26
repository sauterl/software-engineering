package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;
import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.cli.HTMLWriter;
import ch.unibas.informatik.hs15.cs203.datarepository.common.CriteriaWrapper;
import ch.unibas.informatik.hs15.cs203.datarepository.common.Version;
import ch.unibas.informatik.hs15.cs203.datarepository.processing.Factory;
import ch.unibas.informatik.hs15.cs203.datarepository.common.DummyProgressListener;
import ch.unibas.informatik.hs15.cs203.datarepository.common.Version;

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

	private static DatasetPort instance = null;

	public static DatasetPort getDatasetPort(final Path repo,
			final DatasetPortConfiguration config, final DataRepository app) {
		if (instance == null) {
			instance = new DatasetPort(repo, config, app);
		}
		return instance;
	}

	private DataRepository app = null;

	private DatasetPortLogger logger = null;

	private HTMLWriter writer;

	private DatasetPort(final Path repo, final DatasetPortConfiguration config,
			final DataRepository app) {
		this.config = config;
		this.repo = repo;
		this.app = app;
		init();
	}

	private void init() {
		logger = new DatasetPortLogger(repo, config.getLogFile());
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

	/**
	 * Public entry point to start a DatasetPort Setup() has been called at this
	 * point
	 * 
	 * @throws IOException
	 */
	private void run() throws IOException {

		logProperties();

		System.out.println("Successfully started server mode...");

		// TODO Print everything to Logfile

		File directory = config.getIncoming().toFile();
		for (;;) {
			if (directory.listFiles().length != 0) {
				for (File file : directory.listFiles()) {
					try {
						config.getCompletenessDetection().newInstance()
								.verifyCompletness(file.toPath());

						File temp = new File("theFile.txt");// TODO Replace with
															// real file!
						MetaData md = app.add(temp, null, true,
								new DummyProgressListener());
						logger.info("Successfully added dataset with id: "
								+ md.getId());
						writer.update(app.getMetaData(Criteria.all()));

					} catch (InstantiationException
							| IllegalAccessException e) {
						throw new RuntimeException(
								"Could not create an Instance of Completeness Detection.", e);
					}
				}
			}

			try {
				Thread.sleep(config.getScanInterval());
			} catch (InterruptedException e) {
				throw new IllegalArgumentException(
						"Server execution interrupted");
			}
		}
	}

	private void logProperties() {
		StringBuilder builder = new StringBuilder();
		builder.append(Version.VERSION);
		builder.append(", " + config.toString());
		logger.info(builder.toString());
	}

	private void setup() {
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
			throw new IllegalArgumentException(
					"Error while starting Server. Invalid Scan Interval");
		}

		if (config.getHtmlOverview() != null) {
			writer = new HTMLWriter(config.getHtmlOverview().toString());
			try {
				writer.update(app
						.getMetaData(CriteriaWrapper.all().getWrappedObject()));
			} catch (IOException e) {
				throw new IllegalArgumentException(
						"Error while starting Server. HTML file could not be created.");
			}
		}
	}
}
