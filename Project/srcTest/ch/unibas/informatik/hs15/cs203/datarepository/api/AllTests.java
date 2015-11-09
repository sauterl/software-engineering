package ch.unibas.informatik.hs15.cs203.datarepository.api;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AddTest.class, ExportTest.class, ListTest.class ,MockProgressListenerTest.class, DeleteTest.class})
public class AllTests {

}
