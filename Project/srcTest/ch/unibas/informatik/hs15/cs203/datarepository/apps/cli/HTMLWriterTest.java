package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;

public class HTMLWriterTest {

	@Test
	public void test() {
		HTMLWriter writer = new HTMLWriter("");
		List<MetaData> empty = null;
		try {
			writer.update(empty);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void test2() {
		HTMLWriter writer = new HTMLWriter("");
		List<MetaData> empty = new ArrayList<MetaData>();
		empty.add(new MetaData("Peter", "Ente", "Pofalla", 0, 0,new Date(12, 5, 1993)));
		try {
			writer.update(empty);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
