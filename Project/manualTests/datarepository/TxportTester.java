package datarepository;

import java.io.File;

import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;
import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;
//import ch.unibas.informatik.hs15.cs203.datarepository.apps.cli.DummyProgressListener;
import ch.unibas.informatik.hs15.cs203.datarepository.processing.Factory;

public class TxportTester {
	
	
	public static void main(String[] args) {
		exportTest();
	}
	
//	private static long getBytesOf(File data) {
//		long size = 0;
//		for (File f : data.listFiles()) {
//			if (f.isFile()) {
//				size+=f.length();
//			} else {
//				size+=getBytesOf(f);
//			}
//		}
//	return size;	
//	}
	
	private static void exportTest(){
		System.out.println("ding");
		final ProgressListener listener = new DummyProgressListener();
		final DataRepository repo = Factory.create(new File("C:/Users/Eddie/Desktop/Test"));
		
		repo.export(Criteria.forId("fa0665c3-9a50-4840-91ee-2c490eeed66f"), new File("C:/Users/Eddie/Desktop/aim/"), listener);
	}

}
