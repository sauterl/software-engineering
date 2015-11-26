package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.nio.file.Path;
import java.util.Properties;

import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;

public class MockCompletenessDetection implements CompletenessDetection {

	@Override
	public void initializeDetection(Properties properties) {
		//Ignore
	}

	@Override
	public boolean verifyCompletness(Path file) {
		return true;
	}

}
