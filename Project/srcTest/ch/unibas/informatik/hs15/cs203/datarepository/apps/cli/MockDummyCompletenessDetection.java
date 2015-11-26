package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.nio.file.Path;
import java.util.Properties;

import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;

public class MockDummyCompletenessDetection  implements CompletenessDetection{

	@Override
	public void initializeDetection(Properties properties) {
		
	}

	@Override
	public boolean verifyCompletness(Path file) {
		return false;
	}
	

}
