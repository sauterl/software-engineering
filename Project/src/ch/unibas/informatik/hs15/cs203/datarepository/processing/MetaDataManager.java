package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.FileWriter;
import java.io.IOException;

import util.jsontools.Json;
import util.jsontools.JsonParser;

public class MetaDataManager {
    
    private static MetaDataManager instance = null;
    
    private Json metaDataFile;
    private String fileName;
    
    private int lastID;
    
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
    
    
    public static MetaDataManager getMetaDataManager(String file) throws IOException{
	if(instance == null){
	    instance = new MetaDataManager(file);
	}
	return instance;
    }

    private MetaDataManager(String file) throws IOException{
	this.fileName = file;
	metaDataFile = readMetaDataFile(file);
	lastID = metaDataFile.getInt(lastIdKey);
    }
    
    private Json readMetaDataFile(String file) throws IOException{
	JsonParser parser = new JsonParser();
	return parser.parseFile(file);
    }
    
    public int getNextID(){
	return ++lastID;
    }
    
    public void writeMetaDataFile() throws IOException{
	FileWriter fw = new FileWriter(fileName);
	fw.write(metaDataFile.toJson());
	fw.flush();
	fw.close();
    }

}
