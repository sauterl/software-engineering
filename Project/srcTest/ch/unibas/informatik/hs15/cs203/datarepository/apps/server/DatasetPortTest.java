package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import ch.unibas.informatik.hs15.cs203.datarepository.api.APITestCase;
import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;
import ch.unibas.informatik.hs15.cs203.datarepository.Utils;

public class DatasetPortTest extends APITestCase {

	DatasetPort port;
	Path HTMLOverview;
	CompletenessDetection strategy = new MockCompletenessDetection();
	DatasetPortConfiguration config;
	Path logFile;
	Path incomingDir;

	@Test
	public void testStart() throws IOException, InterruptedException {
		init();
		(new Thread(new PortRunnable(port))).start();
		Thread.sleep(1000);
	}

	@Test
	public void testAdd() throws IOException, InterruptedException {
		init();
		(new Thread(new PortRunnable(port))).start();
		Thread.sleep(1000);
		File file = new File(incomingDir.toFile(), "example.txt");
	    String[] data = Utils.createExampleData(file, ":hello world!");
	}

	private void init() throws IOException {

		incomingDir = new File("test-incoming-dir").toPath();
		Utils.delete(incomingDir.toFile());
		assertEquals(false, incomingDir.toFile().exists());
		incomingDir.toFile().mkdirs();

		HTMLOverview = Paths.get(repository.toString(), "table.html");

		logFile = Paths.get(repository.toString(), "server.log");
		// TODO Remove this line later
		logFile.toFile().createNewFile();

		config = new DatasetPortConfiguration(incomingDir, HTMLOverview,
				logFile, 2, strategy.getClass());

		port = DatasetPort.getDatasetPort(workingDir.toPath(), config,
				dataRepository);
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
