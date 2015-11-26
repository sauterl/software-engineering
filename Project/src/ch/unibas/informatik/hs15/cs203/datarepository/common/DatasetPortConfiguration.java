package ch.unibas.informatik.hs15.cs203.datarepository.common;

import java.nio.file.Path;

import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.server.DatasetPort;

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
	/**
	 * Absolute or relative path to the file which contains a complete static HTML page
	 * with an HTML table with the same content produced by the optionless list command. If not
	 * specifed no overview will be created.
	 */
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

	@Override
	public String toString() {
		return "DatasetPortConfiguration [incoming=" + (incoming!=null?incoming:"null")
				+ ", htmlOverview=" + (htmlOverview!=null?htmlOverview:"null") + ", logFile=" + (logFile!=null?logFile:"null")
				+ ", scanInterval=" + scanInterval + ", completenessDetection="
				+ (completenessDetection!=null?completenessDetection:"null") + "]";
	}
}
