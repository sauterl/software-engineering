package misc;

import util.jsontools.Json;
import util.jsontools.JsonParser;

public class JsonArrayBehaviorTest {

	public static void main(String[] args) {
		JsonParser parser = new JsonParser();
		
		String input = "{"
				+ "\"key\":["
				+ "\"first\","
				+ "\"second\","
				+ "\"third\""
				+ "]"
				+ "}";
		
		Json json = parser.parseJson(input);
		
		System.out.println(json.toString() );
	}

}
