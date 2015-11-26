package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.nio.file.Path;
import java.util.Properties;

import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;

public class NoCompletenessDetection implements CompletenessDetection {

	@Override
	public void initializeDetection(Properties properties) {
		
	}

	@Override
	public boolean verifyCompletness(Path file) {
		return true;
	}

}
