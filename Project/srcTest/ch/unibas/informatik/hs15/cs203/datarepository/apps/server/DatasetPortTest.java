package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.unibas.informatik.hs15.cs203.datarepository.api.APITestCase;
import ch.unibas.informatik.hs15.cs203.datarepository.common.DatasetPortConfiguration;
import ch.unibas.informatik.hs15.cs203.datarepository.processing.Factory;

public class DatasetPortTest extends APITestCase{
	
	DatasetPort port;
	Path HTMLOverview;

	@Test
	public void testStart() {
		HTMLOverview = Paths.get(repository.toString(), "table.html");
		//TODO Add CompletenessDetection
		DatasetPortConfiguration config = new DatasetPortConfiguration(workingDir.toPath(), HTMLOverview, null, 2, null);
		port = DatasetPort.getDatasetPort(workingDir.toPath(), config, dataRepository);
		port.start();
	}

}
