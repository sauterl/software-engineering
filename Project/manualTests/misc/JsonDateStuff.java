package misc;

import java.util.Date;

import util.jsontools.Json;

public class JsonDateStuff {

	public static void main(String[] args){
		Date now = new Date();
		Date jsonNow = Json.iso8601ToDate(Json.dateToISO8601(now ));
		System.out.println("Date: "+now.getTime() );
		System.out.println("Json: "+jsonNow.getTime() );
	}

}
