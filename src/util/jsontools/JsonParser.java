package util.jsontools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
/**
 * This class us used to parse a JSON formated Text in a File ore elswhere into a {@link Json.java} object.
 * @author Eddie
 *
 */
public class JsonParser {
//	public JsonParser() {
//
//	}
	/**
	 * This method parses an JSON formated String in to a {@link Json.java} Object, which makes the content easily accessible.
	 * @param filepath The filepath leading to the Json file.
	 * @return The parsed Json 
	 * @throws IOException If there is a Problem while reading the file with the given path.
	 */
	public Json parseFile(String filepath) throws IOException {
		return parseJson(readFileToString(new File(filepath)));
	}
	/**
	 * This method parses an JSON formated String in to a {@link Json.java} Object, which makes the content easily accessible.
	 * @param url The URL leading to the Json file.
	 * @return The parsed Json
	 * @throws IOException If there is a Problem while reading the file from the given {@link URL.java}.
	 */
	public Json parseFile(URL url) throws IOException {
		return parseJson(readFileToString(new File(url.getPath())));
	}
/**
 * This method read a complete File in to a String.
 * @param file The File to read.
 * @return The content of the given File as String.
 * @throws IOException If a problem occurs while opening ore reading the File.
 */
	private String readFileToString(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			reader.close();
		}
		return stringBuilder.toString();
	}
	/**
	 * This method is used to parse a {@link String.java} with JSON Format into a {@link Json.java} Object.
	 * @param input The JSON formated {@link String.java}
	 * @return A Object with the content given.
	 */
	public Json parseJson(String input) {
		JsonReader read = new JsonReader(input);
		Json parsedJson = new Json();
		if (read.getHead() != '{') {
			if (read.getNext() != '{') {
				throw new InvalidJsonException(
						"The opening brackets are missing");
			}
		}
		while (read.getHead() != '}' && !read.reachedEnd()) {
			// System.out.println();
			// System.out.println(read.getRest());

			readEntry(read, parsedJson);

			findNext(read);
		}
		return parsedJson;
	}
/**
 * This method is used to move the {@link JsonReader}'s head to the beginning of the next statement.
 * @param read The {@link JsonReader.java} to forward.
 */
	private void findNext(JsonReader read) {
		// System.out.println(read.getRest());
		// System.out.println(read.getPrevious()+"\n");
		if (read.getHead() == '"' && read.getPrevious() != ',') {
			read.getNext();
		}

	}
/**
 * This Method reads one the next entry from a {@link JsonReader} and writes it into a {@link Json.java} Object.
 * @param read The reader to read from.
 * @param parsedJson The Object to read to.
 */
	private void readEntry(JsonReader read, Json parsedJson) {
		// System.out.println(read.getRest()+"\n");
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
/**
 * This method is used to read Sets from a JsonReader.
 * @param read read The reader to read from.
 * @return An array containing all {@link Json.java} objects from the set.
 */
	private Json[] readSet(JsonReader read) {
		ArrayList<Json> jsar = new ArrayList<Json>();
		// System.out.println(read.getRest());
		while (read.getHead() != ']') {
			// System.out.println(read.getRest()+"\n");
			if (read.getHead() == '{' || read.getNext() == '{') {
				jsar.add(readObject(read));
				// System.out.println(read.getRest());
			} else if (read.getHead() == '[') {
				throw new InvalidJsonException(
						"Multi dimentional Arrays arent supported!");
			} else if (read.getHead() == ']') {
				read.getNext();
				break;
			} else {
				// System.out.println(read.getRest());
				throw new InvalidJsonException("Please check your json lists.");
			}
		}
		// System.out.println("left");
		read.jumpTo(',');
		// if(read.getHead()==']'){
		// read.getNext();
		// }

		// System.out.println(read.getRest()+"\n");
		return jsar.toArray(new Json[0]);
	}
/**
 * This Method is used to read an encapsulated {@link Json.java} Object from a JsonReader.
 * @param read read The reader to read from.
 * @return An Object containing the input of the.
 */
	private Json readObject(JsonReader read) {
		String obj = read.readToClosing();
		// read.jumpTo(',');

		read.getNext();

		return parseJson(obj);
	}
/**
 * This method is used to read a number.
 * @param read read The reader to read from.
 * @return The Number formated as String.
 */
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
/**
 * This Method is used to read a String
 * @param read read The reader to read from.
 * @return The content of the String without sleeding and ending brackets
 */
	private String readString(JsonReader read) {
		String ret = read.readToNotEscaped('"');
		read.jumpTo(',');
		return unescape(ret);
	}
/**
 * This method is used to remove the escape sequences.
 * @param input Text with JSON escape sequences.
 * @return Text without the JSOn escape sequences.
 */
	private String unescape(String input) {
		return input.replace("\\\"", "\"");
	}
/**
 * This method is usend to read the name of a Field in a from a {@link JsonReader}
 * @param read read The reader to read from.
 * @return The name of the next Field
 */
	private String readName(JsonReader read) {
		// System.out.println(read.getRest());
		if (read.getHead() != '"') {
			if (read.getNext() != '"') {
				// System.out.println(read.getRest());
				throw new InvalidJsonException("Starting phrences are missing");
			}
		}

		String name = read.readToNotEscaped('"');
		read.jumpTo(':');
		return name;
	}
}
