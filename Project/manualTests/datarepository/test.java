package datarepository;

import java.io.File;

public class test {
	
	
	public static void main(String[] args) {
		File data=new File("C:\\Users\\Eddie\\Desktop\\Test\\rep\\fa0665c3-9a50-4840-91ee-2c490eeed66f");
		System.out.println(getBytesOf(data));
	}
	
	private static long getBytesOf(File data) {
		long size = 0;
		for (File f : data.listFiles()) {
			if (f.isFile()) {
				size+=f.length();
			} else {
				size+=getBytesOf(f);
			}
		}
	return size;	
	}

}
