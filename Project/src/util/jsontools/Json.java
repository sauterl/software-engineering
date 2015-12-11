package util.jsontools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * This class represents a JSON object. It enables to access the values by fieldname and is familiar with the most commonly used Json types. This class doesn't suppert encapsulated arrays, yet it is possible to achive a similar result with Json Arrays.
 * @author Eddie
 *
 */
public class Json {
	/**
	 * Start size.
	 */
	private static final int datasetEntryCount = 8;
	/**
	 * factor by which the hashmap grows.
	 */
	private static final float resizeFactor = 0.2f;
	private Map data;
	/**
	 * Initializes the Object in order to be ready to store content.
	 */
	public Json() {
		this.data = new HashMap(datasetEntryCount, resizeFactor);
	}
	/**
	 * Add a entry to the Json Object.
	 * @param key the name of the entry
	 * @param value the value of the entry
	 */
	public void addEntry(String key, String value) {
		data.put(key, value);
	}
	/**
	 * Add a entry to the Json Object.
	 * @param key the name of the entry
	 * @param value the value of the entry
	 */
	public void addEntry(String key, double value) {
		data.put(key, Double.toString(value));
	}
	/**
	 * Add a entry to the Json Object.
	 * @param key the name of the entry
	 * @param value the value of the entry
	 */
	public void addEntry(String key, Json[] value) {
		data.put(key, value);
	}
	/**
	 * Add a entry to the Json Object.
	 * @param key the name of the entry
	 * @param value the value of the entry
	 */
	public void addEntry(String key, Json value) {
		data.put(key, value);
	}
	/**
	 * Add a entry to the Json Object.
	 * @param key the name of the entry
	 * @param value the value of the entry
	 */
	public void addEntry(String key, Date value) {
		addEntry(key, dateToISO8601(value));
	}
/**
 * Get the content of an entry with the given key.
 * @param key the key of the entry wanted
 * @return the entry with the given key ore null if the key wasn't found.
 */
	public Object getEntry(String key) {
		return data.get(key);
	}
/**
 * remove an entry with a given key from the json.
 * @param key the key of the entry to be removed.
 */
	public void removeEntry(String key) {
		data.remove(key);
	}
/**
 * check if an entry exists for a given key
 * @param key key of the entry
 * @return true if it exists
 */
	public boolean containsEntry(String key) {
		return data.get(key) instanceof String;
	}
/**
 * checks if a key exists
 * @param key key
 * @return true if it exists
 */
	public boolean containsKey(String key) {
		return data.containsKey(key);
	}
/**
 * check if an entry is a set
 * @param key the key of the entry
 * @return true if it is a set
 */
	public boolean containsSet(String key) {
		return data.get(key) instanceof Json[];
	}
/**
 * get the int with the given key
 * @param key key
 * @return integer of th value 
 */
	public int getInt(String key) {
		String value = (String) data.get(key);
		if (value == null) {
			throw new InexistentKeyException();
		} else {
			return Integer.parseInt(value);
		}
	}
/**
 * get the double with the given key
 * @param key key
 * @return the double wit the value
 */
	public double getDouble(String key) {
		String value = (String) data.get(key);
		if (value == null) {
			throw new InexistentKeyException();
		} else {
			return Double.parseDouble(value);
		}
	}
/**
 * get the String with the given key
 * @param key key
 * @return the String with the value
 */
	public String getString(String key) {
		String value = (String) data.get(key);
		if (value == null) {
			throw new InexistentKeyException();
		} else {
			return value;
		}
	}
/**
 * get a json array of a set
 * @param key the key of the set
 * @return an array of {@link Json.java} objects.
 */
	public Json[] getSet(String key) {
		Json[] value = (Json[]) data.get(key);
		if (value == null) {
			throw new InexistentKeyException();
		} else {
			return value;
		}
	}
/**
 * get a specific json object
 * @param key key
 * @return json object
 */
	public Json getJsonObject(String key) {
		Json value = (Json) data.get(key);
		if (value == null) {
			throw new InexistentKeyException();
		} else {
			return value;
		}
	}
/**
 * get a Date
 * @param key key of the date
 * @return the date
 */
	public Date getDate(String key) {
		return iso8601ToDate((String) data.get(key));
	}
/**
 * convert a ISO8601 formated date to a {@link Date} object
 * @param iso8601date
 * @return the matching {@link date} object.
 */
	public static Date iso8601ToDate(String iso8601date) {
		int year = Integer.parseInt(iso8601date.substring(0, 4));
		int month = Integer.parseInt(iso8601date.substring(5, 7)) - 1;
		int day = Integer.parseInt(iso8601date.substring(8, 10));
		int hour = Integer.parseInt(iso8601date.substring(11, 13));
		int minute = Integer.parseInt(iso8601date.substring(14, 16));
		int second = Integer.parseInt(iso8601date.substring(17, 19));
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month, day, hour, minute, second);
		return cal.getTime();

	}
/**
 * convert a {@link date} to a ISO 68601 formated String
 * @param date the date
 * @return ISO8601 date String
 */
	public static String dateToISO8601(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return df.format(date);
	}
/**
 * return a String representation of the json object.
 * @return JSON formated String
 */
	public String toJson() {
		String json = "{\n";
		for (Iterator i = data.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			Object value = data.get(key);
			json += entryToJson(key, value);
			if (i.hasNext()) {
				json += ",\n";
			}
		}
		json += "\n}";
		return json;
	}
/**
 * convert a json entry into a json formated String
 * @param key key of the entry
 * @param value value of the entry
 * @return JSON formated String representing the entry
 */
	private String entryToJson(String key, Object value) {
		String json = "\"" + key + "\" : ";
		if (value instanceof String) {
			json = json.concat(stringEntryToJson((String) value));
			// json+=stringEntryToJson((String)value);
		} else {
			if (value instanceof Json) {
				json = json.concat(objectEntryToJson(value));
				// json+=objectEntryToJson(value);
			} else {
				if (value instanceof Json[]) {
					json = json.concat(setEntryToJson((Json[]) value));
					// json+=setEntryToJson((Json [])value);
				} else {
					// if(value instanceof Date){
					// json+=dateToISO8601((Date)value);
					// System.out.println("Here");
					// }else{
					System.out.println("corrupted");
					// }
				}
			}

		}
		return json;
	}
/**
 * convert a set to a JSOn String
 * @param value Set to be converted
 * @return JSON formated String
 */
	private String setEntryToJson(Json[] value) {
		String jsonString = "[";
		for (int count = 0; count < value.length; count++) {
			jsonString = jsonString.concat("\n")
					.concat(objectEntryToJson(value[count]));
			// jsonString+="\n"+objectEntryToJson(value[count]);
			if (count < value.length - 1) {
				jsonString = jsonString.concat(",");
				// jsonString+=",";
			}
		}
		jsonString += "\n]";
		return jsonString;
	}
/**
 * convert an Object Entry to a json String
 * @param value object to be converted
 * @return JSON formated String
 */
	private String objectEntryToJson(Object value) {
		return ((Json) value).toJson();
	}
/**
 * convert a String entry to a json String
 * @param value the String
 * @return the json formated String
 */
	private String stringEntryToJson(String value) {
		if (value.matches("^\\d+(\\.)?\\d*((E|e)-?\\d\\d?\\d?)?$")) {// "^\\d+(\\.)?\\d*$"
			return value;
		} else {
			return "\"" + jsonEscape(value) + "\"";
		}
	}
/**
 * escape JSOn control sequences 
 * @param input unescaped String
 * @return escaped String
 */
	public static String jsonEscape(String input) {
		input = input.replace("\"", "\\\"");

		return input;
	}
/**
 * get the hashmap containing the data and structure of the Json object
 * @return
 */
	public HashMap getMap() {
		return (HashMap) data;
	}
/**
 * Get a String representation of the json object.
 */
	public String toString() {
		return toJson();
	}

}
