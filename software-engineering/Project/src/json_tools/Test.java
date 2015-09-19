package json_tools;

import java.io.IOException;

public class Test {

	public static void main(String[] args) {
//		String input="{\"repository\" :\"Hallo\", \n\"version\" : 1,\n\"name\": \"first repo\"}";
//		input="{\"repository\" :{\"version\" : 1,\"name\": \"first repo\",\"timestamp\":\"\",\"datasets\" :[{\"id\":1,\"name\":\"big folder\",\"timestamp\":\"\",\"description\":\"Bla Bla Bla...\",\"filecount\":34,\"size\":9369436,\"filename\":\"\",\"filetype\":\"directory\"},{\"id\":2,\"name\":\"small file\",\"timestamp\":\"\",\"description\":\"Bli Bla Blu...\",\"filecount\":1,\"size\":3058,\"filename\":\"\",\"filetype\":\"file\"}]}}";
//		input="{\"timestamp\" :\"2014-09-18-13-40-18\", \n\"version\" : 1,\n\"name\": \"first \\\" repo\"}";
//		System.out.println(JsonParser.whitespaceRemover(input));
//		JsonParser jps=new JsonParser();
//		Json js=jps.parseJson(input);
		
		JsonParser jps=new JsonParser();
		Json js=null;
		try {
//			js = jps.parseFile("C:/Users/Eddie/Desktop/example.json");
			js = jps.parseFile("src/json_tools/example.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(js.toJson());
	}

}
