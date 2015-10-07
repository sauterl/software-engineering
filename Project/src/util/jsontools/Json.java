package util.jsontools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Json {
	private static int datasetEntryCount=8;
	private static float resizeFactor=0.2f;
private Map data;
public Json(){
	this.data=new HashMap(datasetEntryCount,resizeFactor);
}

public void addEntry(String key, String value){
	data.put(key, value);
}

public void addEntry(String key, double value){
	data.put(key, Double.toString(value));
}

public void addEntry(String key, Json[] value){
	data.put(key, value);
}
public void addEntry(String key, Json value){
	data.put(key, value);
}
public void addEntry(String key, Date value){
	addEntry(key, dateToISO8601(value));
}
public Object getEntry(String key){
	return data.get(key);
}

public void removeEntry(String key){
	data.remove(key);
}

public boolean containsEntry(String key){
	return data.get(key) instanceof String;
}

public boolean containsKey(String key){
	return data.containsKey(key);
}

public boolean containsSet(String key){
	return data.get(key) instanceof Json[];
}

public int getInt(String key){
	String value=(String)data.get(key);
	if(value==null){
		throw new InexistentKeyException();
	}else{
			return Integer.parseInt(value);
	}
}

public double getDouble(String key){
	String value=(String)data.get(key);
	if(value==null){
		throw new InexistentKeyException();
	}else{
			return Double.parseDouble(value);
	}
}

public String getString(String key){
	String value=(String)data.get(key);
	if(value==null){
		throw new InexistentKeyException();
	}else{
			return value;
	}
}

public Json[] getSet(String key){
	Json[] value=(Json[])data.get(key);
	if(value==null){
		throw new InexistentKeyException();
	}else{
			return value;
	}
}

public Json getJsonObject(String key){
	Json value=(Json)data.get(key);
	if(value==null){
		throw new InexistentKeyException();
	}else{
			return value;
	}
}

public Date getDate(String key){
	return iso8601ToDate((String)data.get(key));
}

public static Date iso8601ToDate(String iso8601date){
	int year=Integer.parseInt(iso8601date.substring(0, 4));
	int month=Integer.parseInt(iso8601date.substring(5, 7))-1;
	int day=Integer.parseInt(iso8601date.substring(8, 10));
	int hour=Integer.parseInt(iso8601date.substring(11, 13));
	int minute=Integer.parseInt(iso8601date.substring(14, 16));
	int second=Integer.parseInt(iso8601date.substring(17, 19));
	Calendar cal = Calendar.getInstance();
	cal.set(year, month ,day, hour, minute, second);
	return cal.getTime();
}

public static String dateToISO8601(Date date){
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	return df.format(date);
}

public String toJson(){
	String json="{\n";
	for (Iterator i = data.keySet().iterator(); i.hasNext();){
		String key=(String)i.next();
		Object value=data.get(key);
		json+=entryToJson(key, value);
		if(i.hasNext()){
		json+=",\n";
		}
	}
	json+="\n}";
	return json;
}

private String entryToJson(String key, Object value){
	String json="\""+key+"\" : ";
	if(value instanceof String){
		json=json.concat(stringEntryToJson((String)value));
//		json+=stringEntryToJson((String)value);
	}else{
		if(value instanceof Json){
			json=json.concat(objectEntryToJson(value));
//			json+=objectEntryToJson(value);
		}else{
			if(value instanceof Json[]){
			json=json.concat(setEntryToJson((Json [])value));
//			json+=setEntryToJson((Json [])value);
			}else{
//				if(value instanceof Date){
//					json+=dateToISO8601((Date)value);
//					System.out.println("Here");
//				}else{
					System.out.println("corrupted");
//				}
			}
		}
		
	}
	return json;
}

private String setEntryToJson(Json[] value) {
	String jsonString="[";
	for(int count=0;count<value.length;count++){
			jsonString=jsonString.concat("\n").concat(objectEntryToJson(value[count]));
//			jsonString+="\n"+objectEntryToJson(value[count]);
		if(count < value.length-1){
			jsonString=jsonString.concat(",");
//			jsonString+=",";
		}
	}
	jsonString+="\n]";
	return jsonString;
}

private String objectEntryToJson(Object value) {
	return ((Json)value).toJson();
}

private String stringEntryToJson(String value) {
	if(value.matches("^\\d+(\\.)?\\d*((E|e)-?\\d\\d?\\d?)?$")){//"^\\d+(\\.)?\\d*$"
		return value;
	}else{
		return "\""+jsonEscape(value)+"\"";
	}
}

public static String jsonEscape(String input){
	input=input.replace("\"", "\\\"");

	
	
	return input;
}

public HashMap getMap(){
	return (HashMap)data;
}

public String toString(){
	return toJson();
}
	


}
