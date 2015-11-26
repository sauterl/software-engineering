package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import ch.unibas.informatik.hs15.cs203.datarepository.api.CompletenessDetection;
import ch.unibas.informatik.hs15.cs203.datarepository.common.DatasetPortConfiguration;

public class PropertiesParserTest {
	
	private String CLASSNAME = "ch.unibas.informatik.hs15.cs203.datarepository.apps.cli.MockDummyCompletenessDetection";
	
	private Properties createRegularProps(){
		Properties props = new Properties();
		props.setProperty(PropertiesParser.INCOMING_DIR_KEY, "test-working-dir");
		props.setProperty(PropertiesParser.HTML_OVERVIEW_KEY, "test-working-dir/overview.html");
		props.setProperty(PropertiesParser.LOG_FILE_KEY, "test-working-dir/mylog.txt");
		props.setProperty(PropertiesParser.CHECKING_INTERVAL_KEY, String.valueOf(10));
		props.setProperty(PropertiesParser.CMPLTNSS_CLASS_KEY, CLASSNAME);
		return props;
	}
	
	private DatasetPortConfiguration createRegularConfig(){
		Path incoming = Paths.get("test-working-dir");
		Path htmlOverview = Paths.get("test-working-dir/overview.html");
		Path logFile = Paths.get("test-working-dir/mylog.txt");
		int scanInterval = 10;
		Class<? extends CompletenessDetection> strategy = MockDummyCompletenessDetection.class;
		return new DatasetPortConfiguration(incoming, htmlOverview, logFile, scanInterval, strategy);
	}
	
	@Before
	public void setUp(){
		PropertiesParser.setClassLoader(getClass().getClassLoader());
	}
	
	@Test
	public void testRegular() {
		DatasetPortConfiguration config = PropertiesParser.parse(createRegularProps());
		assertEquals(createRegularConfig().toString(), config.toString());
	}

}
