package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import util.logging.Logger;
import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;
import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;
import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;
import ch.unibas.informatik.hs15.cs203.datarepository.common.DatasetPortConfiguration;
import ch.unibas.informatik.hs15.cs203.datarepository.common.DummyProgressListener;

/**
 * The {@link DatasetPort} class represents the 'server mode' of the data
 * repository application. See the <a href=
 * "http://informatik.unibas.ch/fileadmin/Lectures/HS2015/software-engineering/specifications2.pdf"
 * > extended specifications</a> for further detail.
 * 
 * @author Loris
 */
public class DatasetPort {

	private static final Logger LOG = Logger.getLogger(DatasetPort.class);

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

	private OverviewWriter htmlGen;

	private boolean running;

	private DatasetPort(final Path repo, final DatasetPortConfiguration config,
			final DataRepository app) {
		this.config = config;
		this.repo = repo;
		this.app = app;
		init();
		LOG.info(String.format("Initialized with configuration: %s",
				config.toString()));
	}

	private void init() {
		logger = new DatasetPortLogger(repo, config.getLogFile());
		running = true;
	}

	public void start() {
		try {
			setup();
			run();
		} catch (final Throwable t) {
			LOG.error("Server crash", t);
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
		htmlGen.createHtmlFile(app.getMetaData(Criteria.all()));
		CompletenessDetection strategy = null;
		try {
			strategy = config.getCompletenessDetection().newInstance();
			strategy.initializeDetection(config.getProperties());
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(
					"Could not create an Instance of Completeness Detection.",
					e);
		}
		System.out.println("Successfully started server mode...");
		File directory = config.getIncoming().toFile();
		while (running) {
			if (directory.listFiles().length != 0) {
				for (File file : directory.listFiles()) {
					try {
						LOG.debug("Verifying new file! Filename: "
								+ file.toString());
						if (!strategy.verifyCompletness(file.toPath())) {
							LOG.warn("File incomplete (" + file.toString()
									+ ")");
							continue;
						}
						LOG.debug("Adding file: " + file.toString());
						MetaData md = app.add(file, null, true,
								new DummyProgressListener());
						logger.info("Successfully added dataset with id: "
								+ md.getId());
						htmlGen.createHtmlFile(app.getMetaData(Criteria.all()));
					} catch (IOException e) {
						throw new RuntimeException(
								"Updating of HTML File failed. ", e);
					}
				}
			}

			try {
				Thread.sleep(config.getScanInterval());
			} catch (InterruptedException e) {
				throw new RuntimeException("Server execution interrupted", e);
			}
		}
	}

	private void logProperties() {
		logger.logHeader(config);
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
		htmlGen = new OverviewWriter(config.getHtmlOverview());
	
	}

	public void shutdown() {
		running = false;
	}

	/**
	 * Resets the current instance to null. Used for unittesting
	 */
	public static void resetInstance() {
		instance = null;
	}
}
