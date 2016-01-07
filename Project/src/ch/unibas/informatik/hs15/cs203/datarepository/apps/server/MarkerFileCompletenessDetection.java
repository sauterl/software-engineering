package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.File;
import java.nio.file.Path;
import java.util.Properties;

import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;

public class MarkerFileCompletenessDetection implements CompletenessDetection{

	private String marker;
	private String key = "completeness-detection.prefix";
	@Override
	public void initializeDetection(Properties properties) {
		if(!properties.containsKey(key)){
			throw new RuntimeException("Prefix has not been specified");
		}
		marker = properties.getProperty(key);
	}

	@Override
	public boolean verifyCompletness(Path file) {
		Path parent = file.getParent();
		if(parent == null){
			throw new RuntimeException("The given file to verify does not have a parent folder");
		}
		for(File child : parent.toFile().listFiles()){
			if(child.getName().startsWith(marker)){
				String fileNameWithoutPrefix = child.getName().substring(marker.length());
				if(fileNameWithoutPrefix.equals(file.getFileName().toString())){
					if(!child.delete()){
						throw new RuntimeException("Error while deleting marker file.");
					}
					return true;
				}
			}
		}
		//TODO Convert DatasetPortLogger to singleton, log Error
		return false;
	}

}
