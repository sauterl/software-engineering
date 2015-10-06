package util.jsontools;

import java.io.FileWriter;
import java.io.IOException;

public class Test {
	static final int MAXITER=1;
	static long st=0;
	public static void main(String[] args) {
		FileWriter stat=null;
		try {
			stat=new FileWriter("C:/Users/Eddie/Desktop/stat.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			stat.write("Iteration;Opentime;inserttime;closetime;totaltime\n");
		} catch (IOException e) {
			e.printStackTrace();
		}		
		JsonParser jps=new JsonParser();
		timeElapsed();
		
		
		
//		Json js = open(jps);
//		System.out.println(js);
//		insert(js,jps);
//		System.out.println(js);
//		close(js);
		
		
		for (int iteration = 0; iteration < MAXITER; iteration++) {
			Json js = open(jps);
			long ot = timeElapsed();
//			js=insert(js, jps);
			long it = timeElapsed();
			close(js);
			long ct = timeElapsed();

			try {
				stat.write(iteration + ";" + ot + ";" + it + ";" + ct + ";" + (ot + it + ct) + "\n");
				System.out.println("Iteration "+iteration+" finished.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			stat.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static long timeElapsed(){
		if(st==0){
			st=System.currentTimeMillis();
			return 0;
		}
		long stl=st;
		st=System.currentTimeMillis();
		return st-stl;
	}
	
	
	public static Json open(JsonParser jps){
		Json js=null;
		try {
			js = jps.parseFile("C:/Users/Eddie/Desktop/example2.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return js;
	}
	
	public static Json insert(Json js, JsonParser jps){
		Json[] ds = js.getJsonObject("repository").getSet("datasets");
		Json[] dsn=new Json[ds.length+1];
		for(int index=0;index<ds.length;index++){
			dsn[index]=ds[index];
		}
		
		try {
			dsn[dsn.length-1]=jps.parseFile("C:/Users/Eddie/Desktop/entry.json");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		js.getJsonObject("repository").addEntry("datasets", dsn);
		return js;
	}

	public static void close(Json js){
		try {
			FileWriter f=new FileWriter("C:/Users/Eddie/Desktop/out.json");
			f.write(js.toJson());
			f.flush();
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
