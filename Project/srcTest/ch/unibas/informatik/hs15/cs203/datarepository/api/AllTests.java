package ch.unibas.informatik.hs15.cs203.datarepository.api;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import util.logging.LoggerManager;

@RunWith(Suite.class)
@SuiteClasses({ AddTest.class, ExportTest.class, ListTest.class ,MockProgressListenerTest.class, DeleteTest.class, ReplaceTest.class})
public class AllTests {

	
}
