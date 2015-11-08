package support;

import java.io.IOException;

import ch.unibas.informatik.hs15.cs203.datarepository.apps.support.ManPageGenerator;

public class ManPageGeneratorTest {
	public static void main(String[] args) throws IOException{
		ManPageGenerator gen = new ManPageGenerator("add");
		System.out.println(gen.buildManPage());
	}
}
