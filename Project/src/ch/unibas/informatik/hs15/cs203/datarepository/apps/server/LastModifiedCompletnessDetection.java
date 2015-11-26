package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.Properties;

import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;

public class LastModifiedCompletnessDetection implements CompletenessDetection {
	private int quietPeriod;

	@Override
	public void initializeDetection(Properties properties) {
	try {
		quietPeriod = Integer.parseInt(properties.getProperty(
			"completeness-detection.quiet-period-in-seconds"));
	} catch (NumberFormatException e) {
		throw new RuntimeException(
			"The completeness-detection.quiet-period-in-seconds must be an integer number.",
			e);
	} catch (NullPointerException e) {
		throw new RuntimeException(
			"The completeness-detection.quiet-period-in-seconds isnt specified.",
			e);
	}
	if (quietPeriod < 0) {
		throw new RuntimeException(
			"The completeness-detection.quiet-period-in-seconds must be a positive integer number.");
	}
	}

	@Override
	public boolean verifyCompletness(Path file) {
	Long lastModified = findLastModifiedDate(file.toFile());
	if(System.currentTimeMillis()-lastModified>quietPeriod*1000){
		return true;
	}
	return false;
	}

	private Long findLastModifiedDate(File file) {
	if (file.isFile()) {
		return file.lastModified();
	} else {
		long mostRecent = 0l;
		for (File f : file.listFiles()) {
		long lastModified = findLastModifiedDate(f);
		if (mostRecent < lastModified) {
			mostRecent = lastModified;
		}
		}
		return mostRecent;
	}
	}

}
