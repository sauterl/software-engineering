package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.unibas.informatik.hs15.cs203.datarepository.api.APITestCase;
import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;
import ch.unibas.informatik.hs15.cs203.datarepository.processing.Factory;

public class DatasetPortTest extends APITestCase{
	
	DatasetPort port;
	Path HTMLOverview;
	CompletenessDetection strategy = new MockCompletenessDetection();
	DatasetPortConfiguration config;
	Path logFile;

	@Test
	public void testStart() {
		init();
		port.start();
	}
	
	@Test
	public void testAdd(){
		init();
		(new Thread(new PortRunnable(port))).start();
	}
	
	private void init(){
		HTMLOverview = Paths.get(repository.toString(), "table.html");
		//TODO Add CompletenessDetection
		logFile = Paths.get(repository.toString(), "server.log");
		config = new DatasetPortConfiguration(workingDir.toPath(), HTMLOverview, logFile, 2, strategy.getClass());
		port = DatasetPort.getDatasetPort(workingDir.toPath(), config, dataRepository);
	}

}

/**
 * To test the server
 */
class PortRunnable implements Runnable {
	DatasetPort port;
	
	PortRunnable(DatasetPort port){
		this.port = port;
	}
	
    public void run() {
        port.start();
    }
}
