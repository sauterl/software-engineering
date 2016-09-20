package ch.unibas.informatik.hs15.cs203.datarepository.apps.support;

import java.io.IOException;

import ch.unibas.informatik.hs15.cs203.datarepository.apps.support.ManPageGenerator;
import ch.unibas.informatik.hs15.cs203.datarepository.apps.support.Utilities;

public class LineBreakTest {

	public static void main(String[] args) throws IOException {
		ManPageGenerator gen = new ManPageGenerator("list");
		System.out.println(gen.getManPage(true));
		String str = "\t+ The timestamp is defined by the date and time of adding/replacing the data set. It is shown in the following format: YYYY-MM-DD HH:MM:SS.\n";
		System.out.println(Utilities.wrapLinesSensitive(str, 70, null));
	}

}
