package ch.unibas.informatik.hs15.cs203.datarepository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.unibas.informatik.hs15.cs203.datarepository.api.AddTest;
import ch.unibas.informatik.hs15.cs203.datarepository.api.DeleteTest;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ExportTest;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ListTest;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MockProgressListenerTest;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ReplaceTest;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.cli.ClientInputTest;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.cli.ClientTest;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.server.DatasetPortTest;

@RunWith(Suite.class)
@SuiteClasses({AddTest.class, ExportTest.class, ListTest.class ,MockProgressListenerTest.class, DeleteTest.class, ReplaceTest.class, ClientTest.class, ClientInputTest.class, DatasetPortTest.class})
public class AllTests {

}
