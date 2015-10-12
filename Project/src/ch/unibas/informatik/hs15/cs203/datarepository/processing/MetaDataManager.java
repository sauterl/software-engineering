package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import util.jsontools.Json;
import util.jsontools.JsonParser;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;

/**
 * The {@link MetaDataManager} class manages meta data.
 * This includes reading of meta data file, manipulating meta data
 * during runtime and finally writing meta data to file.
 * 
 * The design of this class and the processing package does <b>not</b> allow
 * two or more processes manipulating the same repository at the same time.
 * Thus this class will fail initialize when the meta data file of the specified repository is locked.
 * 
 * 
 * @author Loris
 *
 */
public class MetaDataManager implements Closeable {

    private static MetaDataManager instance = null;

    private Json metaDataFile;
    private String repoPath;
    private FileLock lock;
    private HashMap<String, Json> idMetaMap;

    private static final String repositoryKey = "repository";
    private static final String versionKey = "version";
    private static final String nameKey = "name";
    private static final String timestampKey = "timestamp";
    private static final String datasetsKey = "datasets";
    private static final String idKey = "id";
    private static final String descriptionKey = "description";
    private static final String filecountKey = "filecount";
    private static final String sizeKey = "size";
    @SuppressWarnings("unused")
    private static final String filetypeKey = "filetype";

    private static final String tmpLabel = "tmp";
    private static final String lockFile = ".lock";
    private static final String metaDataFileName = ".metadata";

    public static final String VERSION = "0.0.1";

    // TODO before read for manipulation, create lock file. if lock is present,
    // do not read
    /*
     * metadata file structure: { "repository":{ "version":"1.0",
     * "timestamp":"2014-09-18T13:40:18", "datasets":[ {
     * "id":"38141ec3-fcc6-4590-b9cb-dff7a4b7c354", "name":"MyDocuments",
     * "description":"Some of my documents", "filecount":34, "size":2433993827,
     * "timestamp":"2014-09-18T13:42:38" } ] } }
     */

    public static MetaDataManager getMetaDataManager(String repoPath)
	    throws IOException {
	if (instance == null) {
	    instance = new MetaDataManager(repoPath);
	}
	return instance;
    }

    private Json createNewMetaDataFile() {
	Json repo = new Json();
	repo.addEntry(versionKey, VERSION);
	repo.addEntry(timestampKey, new Date());
	Json[] emptyDatasets = new Json[0];
	repo.addEntry(datasetsKey, emptyDatasets);
	Json out = new Json();
	out.addEntry(repositoryKey, repo);
	return out;
    }

    private MetaDataManager(String repoPath) throws IOException {
	this.repoPath = repoPath;
	this.idMetaMap = new HashMap<String, Json>();
	if (!tryLockMetaDataFile()) {
	    throw new RuntimeException(
		    "Could not apply a lock to the metadata. Assuming another data repository accesses it.");
	}
	try {
	    metaDataFile = parseMetaDataFile(metaDataFileName);
	} catch (FileNotFoundException ex) {
	    metaDataFile = createNewMetaDataFile();
	}
	fillMap();
    }
    
    private void fillMap(){
	Json repoJSON = metaDataFile.getJsonObject(repositoryKey);
	for(Json dataset: repoJSON.getSet(datasetsKey) ){
	    idMetaMap.put(dataset.getString(idKey), dataset);
	}
    }

    public void writeMetadata(MetaData data) throws IOException {
	// TODO proper exception handling
	addMetaData(data);
	writeTemporaryMetaDataFile();
    }

    public void addMetaData(MetaData data) {
	if (data == null) {
	    throw new NullPointerException("MetaData to add is null.");
	}
	// assert(data != null);
	Json entry = createJsonMetaEntry(data);
	prependElement(
		metaDataFile.getJsonObject(repositoryKey).getSet(datasetsKey),
		entry);
    }

    /**
     * TODO This is json utils stuff!
     * 
     * @param set
     * @param element
     * @return
     */
    private Json[] prependElement(Json[] set, Json element) {
	Json[] out = new Json[set.length + 1];
	out[0] = element;
	for (int i = 1; i <= set.length; i++) {
	    out[i] = set[i - 1];
	}
	return out;
    }

    private Json createJsonMetaEntry(MetaData data) {
	Json json = new Json();
	json.addEntry(idKey, data.getId());
	json.addEntry(nameKey, data.getName());
	json.addEntry(filecountKey, data.getNumberOfFiles());
	json.addEntry(sizeKey, data.getSize());
	json.addEntry(timestampKey, data.getTimestamp());
	if (data.getDescription() != null) {
	    json.addEntry(descriptionKey, data.getDescription());
	}
	return json;
    }

    private Json parseMetaDataFile(String file) throws IOException {
	JsonParser parser = new JsonParser();
	return parser.parseFile(Paths.get(repoPath, metaDataFileName).toFile()
		.getPath());
    }

    public synchronized final String generateRandomUUID() {
	return UUID.randomUUID().toString();
    }

    public void writeTemporaryMetaDataFile() throws IOException {
	FileWriter fw = new FileWriter(Paths.get(repoPath,
		tmpLabel + metaDataFileName).toFile());
	addMapToJson();
	fw.write(metaDataFile.toJson());
	fw.flush();
	fw.close();

	// TODO change this to correct lock release
	releaseLock();
    }

    private boolean tryLockMetaDataFile() {
	Path lockFilePath = Paths.get(repoPath, lockFile);
	try {
	    FileChannel channel = FileChannel.open(lockFilePath,
		    StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
	    try {
		lock = channel.tryLock();
		return true;
	    } catch (OverlappingFileLockException ex) {
		return false;
	    }
	} catch (IOException e) {
	    return false;
	}
    }

    private boolean releaseLock() throws IOException {
	lock.release();
//	Files.setPosixFilePermissions(Paths.get(repoPath, lockFile), EnumSet.allOf(PosixFilePermission.class));
//	Files.delete(Paths.get(repoPath, lockFile));//throws securityexception?!
	Paths.get(repoPath, lockFile).toFile().delete();
	return true;
    }

    @Override
    public void close() throws IOException {
	releaseLock();
	Files.move(Paths.get(repoPath, tmpLabel+metaDataFileName), Paths.get(repoPath, metaDataFileName), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }
    
    private void updateMetaData(String id, MetaData meta){
	Json entry = createJsonMetaEntry(meta);
	idMetaMap.put(id, entry);
    }
    
    private void addMapToJson(){
	metaDataFile.getJsonObject(repositoryKey).removeEntry(datasetsKey);
	metaDataFile.getJsonObject(repositoryKey).addEntry(datasetsKey, idMetaMap.values().toArray(new Json[0]));
    }
}
