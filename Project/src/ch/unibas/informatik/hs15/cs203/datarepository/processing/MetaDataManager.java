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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.UUID;

import util.jsontools.Json;
import util.jsontools.JsonParser;
import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;

/**
 * The {@link MetaDataManager} class manages meta data. This includes reading of
 * meta data file, manipulating meta data during runtime and finally writing
 * meta data to file.
 * 
 * The design of this class and the processing package does <b>not</b> allow two
 * or more processes manipulating the same repository at the same time. Thus
 * this class will fail initialize when the meta data file of the specified
 * repository is locked.
 * 
 * 
 * @author Loris
 * 
 */
class MetaDataManager implements Closeable {

	/**
	 * Singleton. This is the instance.
	 */
	private static MetaDataManager instance = null;

	/**
	 * The meta data file as {@link Json} object.
	 */
	private Json metaDataFile;

	/**
	 * The path to the repository.
	 */
	private final String repoPath;

	/**
	 * The file lock. This has private visibility to close it at the appropriate
	 * time. (Instead of being local).
	 */
	private FileLock lock;
	/**
	 * Mapping of ID->metaAsJson
	 */
	private final HashMap<String, Json> idMap;
	/**
	 * Mapping of name->metaAsJson
	 */
	private final HashMap<String, Json> nameMap;
	/**
	 * Mapping of timestamp->metaAsJson
	 */
	private final TreeMap<Long, Json> timestampMap;
	/**
	 * Indicates whether this meta data manager is prepared for search queries
	 * or not.
	 */
	private volatile boolean queryReady = false;

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

	/**
	 * A prefix for temporary files on the file system.
	 */
	private static final String tmpLabel = "tmp";
	/**
	 * The name of the lock file, used to lock the metadata file
	 */
	private static final String lockFile = ".lock";
	/**
	 * The name of the meta data file
	 */
	private static final String metaDataFileName = ".metadata";

	/**
	 * Version. Why is this not a property?
	 */
	public static final String VERSION = "0.0.1";

	/*
	 * metadata file structure: { "repository":{ "version":"1.0",
	 * "timestamp":"2014-09-18T13:40:18", "datasets":[ {
	 * "id":"38141ec3-fcc6-4590-b9cb-dff7a4b7c354", "name":"MyDocuments",
	 * "description":"Some of my documents", "filecount":34, "size":2433993827,
	 * "timestamp":"2014-09-18T13:42:38" } ] } }
	 */

	/**
	 * Creates a new {@link MetaDataManager} for the given repository path. If
	 * the given path is not yet recognized as repository (has a meta data file
	 * in it), it will be initialized as one.
	 * 
	 * <b>Note: The {@link MetaDataManager} must be closed before terminating
	 * the application</b>.
	 * 
	 * In case the given repo path is already a repository and said repository
	 * is being manipulated by another process of this tool, this method will
	 * throw a RuntimeException.
	 * 
	 * Other reasons for failing (throwing a IOException) are: Failed reading
	 * the metadata file, Security issues with the JVM which handles this
	 * {@link MetaDataManager} Or anything else that could lead to an
	 * IOException during reading of files.
	 * 
	 * @param repoPath
	 *            The path to the repository. Must be not null.
	 * @return The {@link MetaDataManager} for the given repository.
	 * @throws IOException
	 *             If one of the above mentioned cases occurs.
	 */
	public static MetaDataManager getMetaDataManager(final String repoPath)
			throws IOException {
		if (instance == null) {
			instance = new MetaDataManager(repoPath);
		}
		return instance;
	}

	private MetaDataManager(final String repoPath) throws IOException {
		this.repoPath = repoPath;
		this.idMap = new HashMap<String, Json>();
		nameMap = new HashMap<String, Json>();
		timestampMap = new TreeMap<Long, Json>();
		if (!tryLockMetaDataFile()) {
			throw new RuntimeException(
					"Could not apply a lock to the metadata. Assuming another data repository accesses it.");
		}
		try {
			metaDataFile = parseMetaDataFile(metaDataFileName);
		} catch (final FileNotFoundException ex) {
			metaDataFile = createNewMetaDataFile();
		}
		fillIdMap();
		prepareForQueries();
	}

	/**
	 * Adds the given {@link MetaData} object to the internal buffer. <b>NOTE:
	 * The method's visibility may get changed to private in a future
	 * update!</b>
	 * 
	 * @param data
	 *            The {@link MetaData} object to add to the internal buffer.
	 */
	public void addMetaData(final MetaData data) {
		if (data == null) {
			throw new NullPointerException("MetaData to add is null.");
		}
		// assert(data != null);
		final Json entry = createJsonMetaEntry(data);
		idMap.put(data.getId(), entry);
	}

	/**
	 * Closes this {@link MetaDataManager} and releases its resources. <b>Invoke
	 * this method before terminating the application</b> Or otherwise the
	 * repository gets corrupted and will not be accessible for a long time.
	 * 
	 * In particular the lock of the meta data file gets released as well as the
	 * temporary written meta data gets moved to stay permanently.
	 */
	@Override
	public void close() throws IOException {
		releaseLock();
		Files.move(Paths.get(repoPath, tmpLabel + metaDataFileName),
				Paths.get(repoPath, metaDataFileName),
				StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.ATOMIC_MOVE);
	}

	/**
	 * Returns a randomly generated {@link UUID}. Use this method to get the the
	 * ID for a data set.
	 * 
	 * @return A randomly generated UUID.
	 */
	public synchronized final static String generateRandomUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Returns the {@link MetaData} object to the corresponding id. If the ID
	 * does not exist, null is returned.
	 * 
	 * @param id
	 * @return The {@link MetaData} object corresponding to the given id or
	 *         null, if the ID does not exist.
	 */
	public MetaData getMetaDataForID(final String id) {
		if (idMap.containsKey(id)) {
			return extractMetaData(idMap.get(id));
		} else {
			return null;
		}
	}

	/**
	 * Returns the {@link MetaData} object to the corresponding name. If the
	 * name does not exist, null is returned.
	 * 
	 * @param name
	 *            The name.
	 * @return The {@link MetaData object corresponding to the given name or
	 *         null, if the name does not exist.
	 */
	public MetaData getMetaDataForName(final String name) {
		if (nameMap.containsKey(name)) {
			return extractMetaData(nameMap.get(name));
		} else {
			return null;
		}
	}

	/**
	 * Prepares the MetaDataManager to query for a certain {@link Criteria}.
	 */
	public void prepareForQueries() {
		fillNameMap();
		fillTimeMap();
		queryReady = true;
	}

	/**
	 * Searches meta data based on the given {@link Criteria}.
	 * This method is not recommended to query for meta data with a specific name or id,
	 * there are methods exclusively for this purpose. In those cases,
	 * the method returns (if there is a matching meta data entry) arrays with a single entry.
	 * 
	 * @see Criteria
	 * @param criteria
	 * @return {@link MetaData}s which match the given criteria.
	 */
	public MetaData[] query(final Criteria criteria) {
		if (!queryReady) {
			throw new IllegalStateException(
					"The MetaDataManager is not ready for queries.");
		}
		if(criteria == null){
			throw new IllegalArgumentException("No null-criterias allowed!");
		}
		return null;
	}

	/**
	 * Updates the metadata for the given id. This replaces the currently stored
	 * meta data.
	 * 
	 * @param id
	 *            The id of the data set which meta data alter.
	 * @param meta
	 *            The new meta data set.
	 */
	public void replaceMetaData(final String id, final MetaData meta) {
		final Json entry = createJsonMetaEntry(meta);
		idMap.put(id, entry);
	}

	/**
	 * Writes the given {@link MetaData} to the meta data file. <b>This method
	 * suits for most use cases, methods like addMetaData(MetaData) or
	 * writeTemporaryMetaDataFile() are currently visible, but may get hidden in
	 * future updates.</b>
	 * 
	 * Firstly the meta data (referred as data) to the internal buffer which
	 * gets 'flushed' (e.g. written) to the file system using the
	 * {@link Json#toJson()} method.
	 * 
	 * The meta data gets firstly written to a temporary meta data file that
	 * gets eventually moved to the reliable meta data file.
	 * 
	 * @param data
	 *            The {@link MetaData} object to write.
	 * @throws IOException
	 *             If somewhere while writing an i/o error occurs
	 */
	public void writeMetadata(final MetaData data) throws IOException {
		addMetaData(data);
		writeTemporaryMetaDataFile();
	}

	/**
	 * Writes the currently buffered meta data to a temporary meta data file.
	 * <b>NOTE: The method's visibility may get changed to private in a future
	 * update!</b>
	 * 
	 * @throws IOException
	 *             If an error occurs while writing the file.
	 */
	public void writeTemporaryMetaDataFile() throws IOException {
		final FileWriter fw = new FileWriter(Paths.get(repoPath,
				tmpLabel + metaDataFileName).toFile());
		addMapToJson();
		fw.write(metaDataFile.toJson());
		fw.flush();
		fw.close();
		releaseLock();
	}

	private Collection<Json> getBefore(Date before){
		return (timestampMap.headMap(before.getTime(), true)).values();
	}
	
	private Collection<Json> getAfter(Date after){
		return (timestampMap.tailMap(after.getTime(), true)).values();
	}
	
	private MetaData[] convertCollectionToMeta(Collection<Json> collection){
		MetaData[] out = new MetaData[collection.size()];
		int i=0;
		for(Json json : collection){
			//could throw InexistentKeyException but json *should* be well formed
			out[i++] = extractMetaData(json);
		}
		return out;
	}
	
	private Collection<Json> intersect(Collection<Json>...collections){
		if(collections.length < 1){
			throw new IllegalArgumentException("Cannot intersect 0 sets");
		}else if(collections.length == 1){
			return collections[0];
		}
		HashSet<Json> in = new HashSet<Json>(collections[0]);
		for(int i=1;i<collections.length; i++){
			in.retainAll(collections[i]);
		}
		return in;
	}
	
	private void addMapToJson() {
		metaDataFile.getJsonObject(repositoryKey).removeEntry(datasetsKey);
		metaDataFile.getJsonObject(repositoryKey).addEntry(datasetsKey,
				idMap.values().toArray(new Json[0]));
	}

	private Json createJsonMetaEntry(final MetaData data) {
		final Json json = new Json();
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

	private Json createNewMetaDataFile() {
		final Json repo = new Json();
		repo.addEntry(versionKey, VERSION);
		repo.addEntry(timestampKey, new Date());
		final Json[] emptyDatasets = new Json[0];
		repo.addEntry(datasetsKey, emptyDatasets);
		final Json out = new Json();
		out.addEntry(repositoryKey, repo);
		return out;
	}

	private MetaData extractMetaData(final Json dataset) {
		final String id = dataset.getString(idKey);
		final String name = dataset.getString(nameKey);
		String description = "";
		if (dataset.containsKey(descriptionKey)) {
			description = dataset.getString(descriptionKey);
		}
		final int numberOfFiles = (int) dataset.getDouble(filecountKey);
		final long size = (long) dataset.getDouble(sizeKey);
		final Date timestamp = dataset.getDate(timestampKey);
		return new MetaData(id, name, description, numberOfFiles, size,
				timestamp);
	}

	private void fillIdMap() {
		final Json repoJSON = metaDataFile.getJsonObject(repositoryKey);
		for (final Json dataset : repoJSON.getSet(datasetsKey)) {
			idMap.put(dataset.getString(idKey), dataset);
		}
	}

	private void fillNameMap() {
		final Json repoJSON = metaDataFile.getJsonObject(repositoryKey);
		for (final Json dataset : repoJSON.getSet(datasetsKey)) {
			nameMap.put(dataset.getString(nameKey), dataset);
		}
	}

	private void fillTimeMap() {
		final Json repoJSON = metaDataFile.getJsonObject(repositoryKey);
		for (final Json dataset : repoJSON.getSet(datasetsKey)) {
			timestampMap.put(dataset.getDate(timestampKey).getTime(), dataset);
		}
	}

	private Json parseMetaDataFile(final String file) throws IOException {
		final JsonParser parser = new JsonParser();
		return parser.parseFile(Paths.get(repoPath, metaDataFileName).toFile()
				.getPath());
	}

	private boolean releaseLock() throws IOException {
		lock.release();
		// Files.setPosixFilePermissions(Paths.get(repoPath, lockFile),
		// EnumSet.allOf(PosixFilePermission.class));
		// Files.delete(Paths.get(repoPath, lockFile));//throws
		Paths.get(repoPath, lockFile).toFile().delete();
		lock.release();
		return true;
	}

	private boolean tryLockMetaDataFile() {
		final Path lockFilePath = Paths.get(repoPath, lockFile);
		try {
			final FileChannel channel = FileChannel.open(lockFilePath,
					StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
			try {
				lock = channel.tryLock();
				return true;
			} catch (final OverlappingFileLockException ex) {
				return false;
			}
		} catch (final IOException e) {
			return false;
		}
	}
}
