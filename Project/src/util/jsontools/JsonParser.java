package util.jsontools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JsonParser {
public JsonParser(){
	
}

public Json parseFile(String filepath) throws IOException {
	
	 BufferedReader reader = new BufferedReader( new FileReader (new File(filepath)));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }
	    reader.close();
	    String jsonString = stringBuilder.toString();
	
	return parseJson(jsonString);
}

public Json parseJson(String input){
	JsonReader read =new JsonReader(input);
	Json parsedJson = new Json();
	if(read.getHead()!='{'){
		if(read.getNext()!='{'){
			throw new InvalidJsonException("The opening brackets are missing");
		}
	}
	while(read.getHead()!='}'&&!read.reachedEnd()){
	readEntry(read,parsedJson);
//	System.out.println(read.getHead());
	findNext(read);
	}
	return parsedJson;
}

private void findNext(JsonReader read) {
	if(read.getHead()=='"'){
		read.getNext();
	}
	
}

private void readEntry(JsonReader read, Json parsedJson){
	String name = readName(read);
	char firstChar=read.getNext();
	if(firstChar=='"'){
		String value = readString(read);
		parsedJson.addEntry(name, value);
	}else if(firstChar=='{'){
		Json value=readObject(read);
		parsedJson.addEntry(name, value);
	}else if(firstChar=='['){
		Json[] value=readSet(read);
		parsedJson.addEntry(name, value);
	}else{
		String value=readNumber(read);
		parsedJson.addEntry(name, value);
	}
//	System.out.println(read.getRest());
}

private Json[] readSet(JsonReader read) {
	ArrayList<Json> jsar= new ArrayList<Json>();
	read.getNext();
	while(read.getHead()!=']'){
		if(read.getHead()=='{'){
//			System.out.println(read.getRest());
			jsar.add(readObject(read));
//			System.out.println(read.getRest());
		}else if(read.getHead()=='['){
			throw new InvalidJsonException("Multi dimentional Arrays arent supported!");	
		}else{
			throw new InvalidJsonException("Please check your json lists.");
		}
	}
	read.jumpTo(',');
	
	return jsar.toArray(new Json[0]);
}

private Json readObject(JsonReader read) {
	String obj=read.readToClosing();
	read.jumpTo(',');
	return parseJson(obj);
}

private String readNumber(JsonReader read) {
	String ret="";
	char next=read.getHead();
	while(next!=' '&&next!=','&&next!='}'){
		ret+=next;
		next=read.getNext();
	}
	if(read.getHead()!='}'){
		read.jumpTo(',');
	}
	return ret;
}

private String readString(JsonReader read) {
//	System.out.println(read.getHead());
//	read.getNext();
	String ret=read.readToNotEscaped('"');
	read.jumpTo(',');
	return ret;
}

private String readName(JsonReader read){
	if(read.getNext()!='"'){
		throw new InvalidJsonException("Starting phrences are missing");	
	}
	String name = read.readToNotEscaped('"');
	read.jumpTo(':');
	return name;
}










private int readEntry(int c, String key, String input, Json jsonObj) {
	if(input.charAt(c)=='{'){
		return readObjectEntry( c, key, input,jsonObj);
	}else{
		if(input.charAt(c)=='['){
			return readSetEntry(c, key, input,jsonObj);
		}else{		
			return readSingelEntry(c, key, input,jsonObj);
		}
	}
}

private int readSetEntry(int c, String key, String input, Json jsonObj) {
	ArrayList<Json> set=new ArrayList<Json>();
	try{
	while(input.charAt(c)!=']'&input.charAt(c+1)!=']'){
//		System.out.println(input.charAt(c));
		c++;
		set.add(parseJson(input.substring(c,endOfObjectEntry(input, c))));
		c=endOfObjectEntry(input, c);
	}
	}catch(StringIndexOutOfBoundsException e){
		throw new InvalidJsonException("Invalid Json. Please check your brackets");
	}
	
	Json[] setArray= set.toArray(new Json[0]);
	jsonObj.addEntry(key, setArray);
	c++;
	return c;
}

private int readSingelEntry(int c, String key, String input, Json jsonObj) {
	String value;
	if(input.charAt(c)=='"'){
//		if(input.substring(c,c+21).matches("\"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\"")){
//			c+=22;
//		}else{
		value=input.substring(c+1, endOfSingleEntry(input, c));
		c=endOfSingleEntry(input, c)+1;
//		}
	}else{
		value=input.substring(c,endOfSingleEntry(input, c));
		c=endOfSingleEntry(input, c);
	}
//	System.out.println(value);
//	System.out.println(c);
	value=value.replace("\\\"", "\"");
	jsonObj.addEntry(key, value);
	return c;
}

private int endOfSingleEntry(String input, int startindex){
	if(input.charAt(startindex)=='"'){
		
		return endOfSingleString(input,startindex);
		
	}else{
		return endOfSingleNumber(input,startindex);
	}
	
}

private int endOfSingleNumber(String input, int startindex) {
	try{
	startindex++;
	while(input.charAt(startindex)!=','&&input.charAt(startindex)!='}'){
		startindex++;
	}
	} catch (StringIndexOutOfBoundsException e){
		throw new InvalidJsonException("Invalid Json. Please check your brackents.");
	}
	return startindex;
}

private int endOfSingleString(String input, int startindex) {
	startindex++;
	while(input.charAt(startindex)!='"'||input.charAt(startindex-1)=='\\')
	startindex++;	
	return startindex;
}

private int readObjectEntry(int c, String key, String input, Json jsonObj) {
	int end=endOfObjectEntry(input, c);
	String setentry=input.substring(c, end);
	c=end;
	jsonObj.addEntry(key, parseJson(setentry));
	return c;
}

private int endOfObjectEntry(String input,int startindex){
	int brace=1;
	try{
	while(brace!=0){
		startindex++;
		if(input.charAt(startindex)=='{'){
			brace++;
		}
		if(input.charAt(startindex)=='}'){
			brace--;
		}
	}
	startindex++;
	}catch(StringIndexOutOfBoundsException e){
		throw new InvalidJsonException("Invalid json. Please check your brackets.");
	}
	return startindex;
}

public static String whitespaceRemover(String input){
	String output="";
	try{
	
	int c=0;
	while(c<input.length()){
		if(input.charAt(c)!=' '&&input.charAt(c)!='"'){
			output+=input.charAt(c);
			c++;
		}else{
			if(input.charAt(c)==' '){
				c++;
			}else{
				output+=input.charAt(c);
				c++;
				while(input.charAt(c)!='"'||input.charAt(c-1)=='\\'){
					output+=input.charAt(c);
					c++;
				}
				output+=input.charAt(c);
				c++;
			}
		}
	}
		
	}catch(StringIndexOutOfBoundsException e){
		throw new InvalidJsonException("wasn't able to parse the json file. Check the file for mistakes with \" ");
	}
	output=output.replaceAll("\t", "");
	output=output.replaceAll("\r", "");
	output=output.replaceAll("\n", "");
	return output;
}

}
