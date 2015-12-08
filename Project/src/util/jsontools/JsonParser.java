package util.jsontools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class JsonParser {
	public JsonParser() {

	}

	public Json parseFile(String filepath) throws IOException {
		return parseJson(readFileToString(new File(filepath)));
	}
	
	public Json parseFile(URL url) throws IOException {
		return parseJson(readFileToString(new File(url.getPath())));
	}
	
	private String readFileToString(File file)throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		reader.close();
		return stringBuilder.toString();
	}
	
	public Json parseJson(String input) {
		JsonReader read = new JsonReader(input);
		Json parsedJson = new Json();
		if (read.getHead() != '{') {
			if (read.getNext() != '{') {
				throw new InvalidJsonException("The opening brackets are missing");
			}
		}
		while (read.getHead() != '}' && !read.reachedEnd()) {
//			System.out.println();
//			System.out.println(read.getRest());
			
			readEntry(read, parsedJson);
			 
			findNext(read);
		}
		return parsedJson;
	}

	private void findNext(JsonReader read) {
//		System.out.println(read.getRest());
//		System.out.println(read.getPrevious()+"\n");
		if (read.getHead() == '"'&&read.getPrevious()!=',') {
			read.getNext();
		}

	}

	private void readEntry(JsonReader read, Json parsedJson) {
//		System.out.println(read.getRest()+"\n");
		String name = readName(read);
		char firstChar = read.getNext();
		if (firstChar == '"') {
			String value = readString(read);
			parsedJson.addEntry(name, value);
		} else if (firstChar == '{') {
			Json value = readObject(read);
			parsedJson.addEntry(name, value);
		} else if (firstChar == '[') {
			Json[] value = readSet(read);
			parsedJson.addEntry(name, value);
		} else {
			String value = readNumber(read);
			parsedJson.addEntry(name, value);
		}
	}

	private Json[] readSet(JsonReader read) {
		ArrayList<Json> jsar = new ArrayList<Json>();
//		System.out.println(read.getRest());
		while (read.getHead() != ']') {
//			System.out.println(read.getRest()+"\n");
			if (read.getHead() == '{' || read.getNext() == '{') {
				jsar.add(readObject(read));
//				System.out.println(read.getRest());
			} else if (read.getHead() == '[') {
				throw new InvalidJsonException("Multi dimentional Arrays arent supported!");
			} else if(read.getHead() == ']'){
				read.getNext();
				break;
			}else{
//				System.out.println(read.getRest());
				throw new InvalidJsonException("Please check your json lists.");
			}
		}
//		System.out.println("left");
		read.jumpTo(',');
//		if(read.getHead()==']'){
//			read.getNext();
//		}
		
		
//		System.out.println(read.getRest()+"\n");
		return jsar.toArray(new Json[0]);
	}

	private Json readObject(JsonReader read) {
		String obj = read.readToClosing();
//		read.jumpTo(',');
		
		read.getNext();
		
		return parseJson(obj);
	}

	private String readNumber(JsonReader read) {
		String ret = "";
		char next = read.getHead();
		while (next != ' ' && next != ',' && next != '}') {
			ret += next;
			next = read.getNext();
		}
		if (read.getHead() == ' ') {
			read.getNext();
		}
		return ret;
	}

	private String readString(JsonReader read) {
		String ret = read.readToNotEscaped('"');
		read.jumpTo(',');
		return unescape(ret);
	}
	
	private String unescape(String input){
		return input.replace("\\\"", "\"");
	}

	private String readName(JsonReader read) {
//		System.out.println(read.getRest());
		if(read.getHead()!='"'){
		if (read.getNext() != '"') {
//			System.out.println(read.getRest());
			throw new InvalidJsonException("Starting phrences are missing");
		}
		}
		
		String name = read.readToNotEscaped('"');
		read.jumpTo(':');
		return name;
	}
}
