package support;

import ch.unibas.informatik.hs15.cs203.datarepository.apps.support.DescriptionParser;

public class DescriptionParserTest {

	public static void main(String[] args) {
		DescriptionParser parser = new DescriptionParser();
		System.out.println(parser.parseString("A single-line paragraph.$br$Some tabbed$t$Text."));

	}

}
