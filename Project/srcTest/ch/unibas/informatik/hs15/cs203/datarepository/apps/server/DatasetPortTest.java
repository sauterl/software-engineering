package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;

import ch.unibas.informatik.hs15.cs203.datarepository.api.APITestCase;
import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;
import ch.unibas.informatik.hs15.cs203.datarepository.common.DatasetPortConfiguration;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.cli.MockDummyCompletenessDetection;
import ch.unibas.informatik.hs15.cs203.datarepository.Utils;

public class DatasetPortTest extends APITestCase {

	DatasetPort port;
	Path HTMLOverview;
	MockDummyCompletenessDetection strategy = new MockDummyCompletenessDetection();
	DatasetPortConfiguration config;
	Path logFile;
	Path incomingDir;
	Thread current;

	@After
	public void tearDown(){
		System.gc();
		port.shutdown();
		DatasetPort.resetInstance();
	}

	@Test
	public void testAddSingleFile() throws IOException, InterruptedException {
		init(1);
		current = new Thread(new PortRunnable(port));
		current.start();
		Thread.sleep(500); 		//Give the server time to start
		assertTrue(HTMLOverview.toFile().exists());
		File file = new File(incomingDir.toFile(), "example.txt");
	    Utils.createExampleData(file, ":hello world!");
	    Thread.sleep(1100);
	    assertEquals("example.txt", dataRepository.getMetaData(Criteria.all()).get(0).getName());
	}
	
	@Test
	public void testAddMultipleFiles() throws IOException, InterruptedException{
		init(1);
		current = new Thread(new PortRunnable(port));
		current.start();
		File file = new File(incomingDir.toFile(), "example.txt");
	    Utils.createExampleData(file, ":hello world!");
	    Thread.sleep(1500);
	    assertEquals("example.txt", dataRepository.getMetaData(Criteria.all()).get(0).getName());
	    file = new File(incomingDir.toFile(), "example2.txt");
	    Utils.createExampleData(file, ":hello moon!");
	    Thread.sleep(1100);
	    assertEquals("example2.txt", dataRepository.getMetaData(Criteria.all()).get(1).getName());
	}
	
	//TODO @Test with real completenessdetection
	public void testAddFolderWithContents() throws InterruptedException, IOException{
		init(1);
		current = new Thread(new PortRunnable(port));
		current.start();

		File folder = new File(incomingDir.toFile(), "my-data");
	    Utils.createExampleData(folder,
	            "/greetings.txt:hello world!",
	            "/data/1.dat:" + Utils.createExampleContent(1234567),
	            "/data/2.dat:" + Utils.createExampleContent(7654321));
	    Thread.sleep(1500);
	    assertEquals("my-data", dataRepository.getMetaData(Criteria.all()).get(0).getName());
	    assertEquals(5, dataRepository.getMetaData(Criteria.all()).get(0).getNumberOfFiles());
	}
	
	@Test
	public void testAddEmptyFolder() throws IOException, InterruptedException{
		init(1);
		current = new Thread(new PortRunnable(port));
		current.start();

		File folder = new File(incomingDir.toFile(), "my-data");
	    folder.createNewFile();
	    Thread.sleep(1500);
	    assertEquals("my-data", dataRepository.getMetaData(Criteria.all()).get(0).getName());
	    assertEquals(1, dataRepository.getMetaData(Criteria.all()).get(0).getNumberOfFiles());		
	}

	private void init(int scanInterval) throws IOException {

		incomingDir = new File("test-incoming-dir").toPath();
		Utils.delete(incomingDir.toFile());
		assertEquals(false, incomingDir.toFile().exists());
		incomingDir.toFile().mkdirs();

		HTMLOverview = Paths.get(repository.toString(), "table.html");

		logFile = Paths.get(repository.toString(), "server.log");
		//logFile.toFile().createNewFile();

		config = new DatasetPortConfiguration(incomingDir, HTMLOverview,
				null, scanInterval, strategy.getClass());

		port = DatasetPort.getDatasetPort(repository.toPath(), config,
				dataRepository);
		
		assertTrue(logFile.toFile().exists());
	}

}

/**
 * To test the server
 */
class PortRunnable implements Runnable {
	DatasetPort port;

	PortRunnable(DatasetPort port) {
		this.port = port;
	}

	public void run() {
		port.start();
	}
}
