package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import util.jsontools.Json;
import util.jsontools.JsonParser;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;

public class MetaDataManager {
    
    private static MetaDataManager instance = null;
    
    private Json metaDataFile;
    private String fileName;
    
    private static final String lastIdKey = "lastId";
    private static final String repositoryKey = "repository";
    private static final String versionKey = "version";
    private static final String nameKey = "name";
    private static final String timestampKey = "timestamp";
    private static final String datasetsKey = "datasets";
    private static final String idKey ="id";
    private static final String descriptionKey="description";
    private static final String filecountKey = "filecount";
    private static final String sizeKey ="size";
    private static final String filetypeKey="filetype";
    
    //TODO before read for manipulation, create lock file. if lock is present, do not read
    /*
     * metadata file structure:
     * {
     * 	"repository":{
     * 		"version":"1.0",
     * 		"name":"Example Repo",
     * 		"timestamp":"2014-09-18T13:40:18",
     * 		"datasets":[
     * 			{
     * 				"id":"38141ec3-fcc6-4590-b9cb-dff7a4b7c354",
     * 				"name":"MyDocuments",
     * 				"description":"Some of my documents",
     * 				"filecount":34,
     * 				"size":2433993827,
     * 				"timestamp":"2014-09-18T13:42:38"
     * 			}
     * 		]
     * 	}
     * }
     */
    
    public static MetaDataManager getMetaDataManager(String file) throws IOException{
	if(instance == null){
	    instance = new MetaDataManager(file);
	}
	return instance;
    }
    
    private MetaDataManager(String file) throws IOException{
	this.fileName = file;
	metaDataFile = readMetaDataFile(file);
    }
    
    public void addMetaData(MetaData data){
	if(data == null){
	    throw new NullPointerException("MetaData to add is null.");
	}
//	 assert(data != null);
	Json entry = createDataSetEntry(data);
	prependElement(metaDataFile.getSet(datasetsKey), entry);
    }
    
    private Json[] prependElement(Json[] set, Json element){
	Json[] out = new Json[set.length+1];
	out[0] = element;
	for(int i=1; i<=set.length;i++){
	    out[i] = set[i-1];
	}
	return out;
    }
    
    private Json createDataSetEntry(MetaData data){
	Json json = new Json();
	json.addEntry(idKey, data.getId());
	json.addEntry(nameKey, data.getName());
	json.addEntry(filecountKey, data.getNumberOfFiles());
	json.addEntry(sizeKey, data.getSize());
	json.addEntry(timestampKey, data.getTimestamp());
	if(data.getDescription() != null){
	    json.addEntry(descriptionKey, data.getDescription());
	}
	return json;
    }
    
    private Json readMetaDataFile(String file) throws IOException{
	JsonParser parser = new JsonParser();
	return parser.parseFile(file);
    }
    
    public String generateRandomUUID(){
	return UUID.randomUUID().toString();
    }
    
    public void writeMetaDataFile() throws IOException{
	FileWriter fw = new FileWriter(fileName);
	fw.write(metaDataFile.toJson());
	fw.flush();
	fw.close();
    }

    
}
