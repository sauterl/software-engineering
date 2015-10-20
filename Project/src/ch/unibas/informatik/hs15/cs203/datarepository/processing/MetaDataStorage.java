package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

import util.jsontools.Json;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;

/**
 * The {@link MetaDataStorage} class provides methods to store a list of meta
 * data.
 * 
 * @author Loris
 * 
 */
class MetaDataStorage {

	private TreeMap<String, MetaData> idMap = new TreeMap<String, MetaData>();
	private TreeMap<Date, Vector<String>> timeMap = new TreeMap<Date, Vector<String>>();

	public MetaDataStorage(MetaData[] entries) {
		if (entries != null && entries.length > 0) {
			
		}
	}

	public MetaDataStorage() {
		this(null);
	}
	
	private void fillIDMap(MetaData[] entries){
		for(MetaData m : entries){
			if(!idMap.containsKey(m.getId() )){
				idMap.put(m.getId(), m);
			}else{
				throw new Error("Fatal error occurred: Duplicate ID encountered!.");
			}
			
		}
	}
	
	private void fillTimeMap(MetaData[] entries){
		for(MetaData m : entries){
			Date d = m.getTimestamp();
			if(!timeMap.containsKey(d) ){
				Vector<String> v = new Vector<String>();
				v.add(m.getId() );
				timeMap.put(d, v);
			}else{
				Vector<String> v = timeMap.get(d);
				if(v!=null){
					v.add(m.getId() );
				}
			}
		}
	}

}
