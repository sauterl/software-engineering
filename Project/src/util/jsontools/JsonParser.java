package util.jsontools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JsonParser {
	public JsonParser() {

	}

	public Json parseFile(String filepath) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		reader.close();
		String jsonString = stringBuilder.toString();

		return parseJson(jsonString);
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
			readEntry(read, parsedJson);
			// System.out.println(read.getHead());
			findNext(read);
		}
		return parsedJson;
	}

	private void findNext(JsonReader read) {
		if (read.getHead() == '"') {
			read.getNext();
		}

	}

	private void readEntry(JsonReader read, Json parsedJson) {
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
		read.getNext();
		while (read.getHead() != ']') {
			if (read.getHead() == '{' || read.getNext() == '{') {
				jsar.add(readObject(read));
				System.out.println(read.getRest());
			} else if (read.getHead() == '[') {
				throw new InvalidJsonException("Multi dimentional Arrays arent supported!");
			} else {
				throw new InvalidJsonException("Please check your json lists.");
			}
		}
		read.jumpTo(',');

		return jsar.toArray(new Json[0]);
	}

	private Json readObject(JsonReader read) {
		String obj = read.readToClosing();
		read.jumpTo(',');
		return parseJson(obj);
	}

	private String readNumber(JsonReader read) {
		String ret = "";
		char next = read.getHead();
		while (next != ' ' && next != ',' && next != '}') {
			ret += next;
			next = read.getNext();
		}
		if (read.getHead() != '}') {
			read.jumpTo(',');
		}
		return ret;
	}

	private String readString(JsonReader read) {
		String ret = read.readToNotEscaped('"');
		read.jumpTo(',');
		return ret;
	}

	private String readName(JsonReader read) {
		if (read.getNext() != '"') {
			throw new InvalidJsonException("Starting phrences are missing");
		}
		String name = read.readToNotEscaped('"');
		read.jumpTo(':');
		return name;
	}
}
