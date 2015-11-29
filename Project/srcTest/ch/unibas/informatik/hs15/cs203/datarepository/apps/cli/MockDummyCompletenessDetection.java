package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.nio.file.Path;
import java.util.Properties;

import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;

public class MockDummyCompletenessDetection  implements CompletenessDetection{

	private boolean result;
	
	public MockDummyCompletenessDetection(){
		result = true;
	}
	
	@Override
	public void initializeDetection(Properties properties) {
		
	}

	@Override
	public boolean verifyCompletness(Path file) {
		return result;
	}
	
	public void setResult(boolean result){
		this.result = result;
	}
	

}
