package support;

import java.io.IOException;

import ch.unibas.informatik.hs15.cs203.datarepository.apps.support.ManPageGenerator;

public class ManPageGeneratorTest {
	public static void main(String[] args) throws IOException{
		String cmd = null;
		if(args.length >= 1){
			cmd = args[0];
		}
		ManPageGenerator gen = new ManPageGenerator(cmd);
		System.out.println(gen.getManPage());
	}
}
