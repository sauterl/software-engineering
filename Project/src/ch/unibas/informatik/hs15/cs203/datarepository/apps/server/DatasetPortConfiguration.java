package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.nio.file.Path;

import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;

/**
 * Configuration of {@link DatasetPort}.
 * <br />
 * See the specifications for further configuration detail.
 * @author Loris
 *
 */
public class DatasetPortConfiguration {

	private Path incoming;
	
	private Path htmlOverview;
	private Path logFile;
	
	private int scanInterval; //in seconds
	
	public DatasetPortConfiguration(Path incoming, Path htmlOverview, Path logFile, int scanInterval, Class<? extends CompletenessDetection> strategy){
		this.incoming = incoming;
		this.htmlOverview = htmlOverview;
		this.logFile = logFile;
		this.scanInterval = scanInterval;
		this.completenessDetection = strategy;
	}
	
	public Path getIncoming() {
		return incoming;
	}

	public Path getHtmlOverview() {
		return htmlOverview;
	}

	public Path getLogFile() {
		return logFile;
	}

	public int getScanInterval() {
		return scanInterval;
	}

	public Class<? extends CompletenessDetection> getCompletenessDetection() {
		return completenessDetection;
	}

	private Class<? extends CompletenessDetection> completenessDetection = null;
}
