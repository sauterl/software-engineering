package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.unibas.informatik.hs15.cs203.datarepository.common.DatasetPortConfiguration;
import ch.unibas.informatik.hs15.cs203.datarepository.common.PrintUtils;
import ch.unibas.informatik.hs15.cs203.datarepository.common.Version;

/**
 * Logs messages to a specified file.
 * @author Loris
 *
 */
class DatasetPortLogger {

	private Path path;

	private static final String TEMPLATE = "%1$s %2$s %3$s";

	public static final String INFO_LVL = "[INFO]";
	public static final String ERROR_LVL = "[ERROR]";

	public static final String DEFAULT_FILE_NAME = "server.log";

	public DatasetPortLogger(Path repo, Path path) {
		this.path = confirmPath(repo, path);
		if (Files.notExists(this.path)) {
			try {
				Files.createDirectories(this.path);
			} catch (IOException ex) {
				throw new RuntimeException(
						"Could not create missing directories for logfile.",
						ex);
			}
		}

	}

	private Path confirmPath(Path repo, Path path) {
		// TODO Revisit this code and adapt the specifications:
		/*
		 * Description of log-file property:
		 * =====================================================================
		 * Absolute or relative path to the log- le. If not speci ed it will be
		 * server.log inside the repository folder. If the log le doesn't exist
		 * it will be created. If it already exists new log entries are append.
		 * 
		 * Description of logging in general:
		 * =====================================================================
		 * All output is redirected to a log file after successful start up of the
		 * server. It contains log messages in the format: <time stamp> <log
		 * level> <log message>. Where <time stamp> is the actual date and time
		 * in the same format as for the list command. The <log level> is either
		 * [INFO] or [ERROR]. The log entries are on one line except error logs
		 * with stack traces. The first log entry shows the version of the Data
		 * Repository followed by entries for each value of the configuration
		 * parameters.
		 */
		if (path != null) {
			boolean exists = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
			boolean notExists = Files.notExists(path,
					LinkOption.NOFOLLOW_LINKS);
			if (notExists) {
				return createDefaultLogFile(repo);
			} else if (exists) {
				boolean isDir = Files.isDirectory(path,
						LinkOption.NOFOLLOW_LINKS);
				boolean isFile = Files.isRegularFile(path,
						LinkOption.NOFOLLOW_LINKS);
				if (isFile) {
					return path;
				} else if (isDir) {
					return createDefaultLogFile(path);
				} else {
					throw new IllegalArgumentException(
							"Path is neither a file nor a directory: "
									+ path.toString());
				}
			} else {
				throw new IllegalArgumentException(
						"Cannot access given path: " + path.toString());
			}
		} else {
			return createDefaultLogFile(repo);
		}
	}

	private Path createDefaultLogFile(Path parent) {
		Path out = parent.resolve(DEFAULT_FILE_NAME);
		if (Files.notExists(out, LinkOption.NOFOLLOW_LINKS)) {
			try {
				Files.createFile(out);
			} catch (IOException ex) {
				throw new RuntimeException("Could not create Logfile", ex);
			}
		}

		return out;
	}

	public static final String INCOMING_DIR_KEY = "incoming-directory";
	public static final String HTML_OVERVIEW_KEY = "html-overview";
	public static final String LOG_FILE_KEY = "log-file";
	public static final String CHECKING_INTERVAL_KEY = "checking-interval-in-seconds";

	public static final String CMPLTNSS_CLASS_KEY = "completeness-detection"
			+ "." + "class-name";

	public void logHeader(DatasetPortConfiguration config) {
		info("=========================");
		info("Server started...");
		info("data-repository version: " + Version.VERSION);
		info("Configuraiton:");
		info(createPathEntry("incoming-directory", config.getIncoming()));
		info(createPathEntry("html-overview", config.getHtmlOverview()));
		info(createPathEntry("log-file", config.getLogFile()));
		info("checking-interval-in-seconds: " + config.getScanInterval());
		info(createClassEntry("completeness-detection",
				config.getCompletenessDetection()));
	}

	private String createClassEntry(String key, Class<?> clazz) {
		String c = clazz != null ? clazz.getName() : "null";
		return key + ": " + c;
	}

	private String createPathEntry(String key, Path path) {
		String p = path != null ? path.toString() : "null";
		return key + ": " + p;
	}

	public void info(String msg) {
		log(INFO_LVL, msg);
	}

	public void error(String msg) {
		log(ERROR_LVL, msg);
	}

	public void error(String msg, Throwable t) {
		StringBuilder sb = new StringBuilder(msg + "\n");
		if (t != null) {
			// record has throwable
			final StringWriter strWtr = new StringWriter();
			final PrintWriter pWtr = new PrintWriter(strWtr);
			pWtr.println();
			t.printStackTrace(pWtr);
			pWtr.flush();
			pWtr.close();
			sb.append(strWtr.toString());
		}
		error(sb.toString());
	}

	private void log(String lvl, String msg) {
		String log = createLog(lvl, msg);
		writeLog(log);
	}

	private void writeLog(String log) {
		BufferedWriter bw = null;
		try {
			bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
					StandardOpenOption.WRITE, StandardOpenOption.APPEND);
			bw.append(log);
			bw.newLine();
			bw.flush();
		} catch (IOException ex) {
			throw new RuntimeException(
					"An Error happened while writing the log", ex);
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				throw new RuntimeException(
						"An error happened while closing the logfile ", e);
			} catch (NullPointerException ex) {
				// do nothing, bw is null and thus must not be closed
			}
		}

	}

	private String createLog(String lvl, String msg) {
		return String.format(TEMPLATE,
				PrintUtils.DATE_TIME_FORMAT.format(new Date()), lvl, msg);
	}
}
