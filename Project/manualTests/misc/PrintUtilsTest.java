package misc;

import ch.unibas.informatik.hs15.cs203.datarepository.common.PrintUtils;

public class PrintUtilsTest {

	public static void main(String[] args) {
		String test = "asldkjflasfdlajsdflkajslkdfjalksjdflkajsdflkajskfjalksjdfkajsdlkfjalksjfasdflkjf88888888888888";
		System.out.println(PrintUtils.wrapLines(test, 80));
	}

}
