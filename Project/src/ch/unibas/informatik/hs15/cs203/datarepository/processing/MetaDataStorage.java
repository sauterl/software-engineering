package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;

/**
 * The {@link MetaDataStorage} class provides methods to store a list of meta
 * data.
 * 
 * @author Loris
 * 
 */
class MetaDataStorage {

	private final TreeMap<String, MetaData> idMap = new TreeMap<String, MetaData>();
	private final TreeMap<Date, Vector<String>> timeMap = new TreeMap<Date, Vector<String>>();

	public MetaDataStorage() {
		this(null);
	}

	public MetaDataStorage(final MetaData[] entries) {
		if (entries != null && entries.length > 0) {
			initMap(entries);
		}
	}

	/**
	 * Puts the given {@link MetaData} to this {@link MetaDataStorage}. The
	 * method's return value is a success indicator and is <tt>true</tt> if and
	 * only if the meta data's was not previously stored, thus <tt>false</tt>
	 * otherwise.<br />
	 * This detection is done via the meta data's ID. So in other words,
	 * <tt>meta</tt>'s ID must be new to the storage.<br />
	 * <b>Note: If you want to replace a meta data then use the appropriate
	 * method</b><br />
	 * 
	 * @param meta
	 *            The meta data to put. This meta data must not have been put
	 *            previously or be <tt>null</tt>
	 * @return <tt>true</tt> if and only if the meta data was put successfully,
	 *         thus was not stored previously. <tt>false</tt> if there is
	 *         already a meta data object with <tt>meta</tt>'s ID.
	 * @throws IllegalArgumentException
	 *             If <tt>meta</tt> is <tt>null</tt>.
	 */
	public boolean put(final MetaData meta) {
		if (meta == null) {
			throw new IllegalArgumentException("Cannot put meta data null.");
		}
		final boolean idRes = putId(meta);
		boolean timeRes = false;
		if (idRes) {
			timeRes = putTime(meta);
		}
		return idRes && timeRes;
	}
	
	/**
	 * Returns the {@link MetaData} with specified ID.
	 * If no meta data with such an ID was found, <tt>null</tt> is returned.
	 * @param id The ID of the meta data to get.
	 * @return The found meta data with specified ID or <tt>null</tt> if none exists with such an ID.
	 * @see TreeMap#get(Object)
	 */
	public MetaData get(String id){
		return idMap.get(id);
	}
	
	/**
	 * Returns a {@link List} of {@link MetaData}s for the given name.
	 * 
	 * @param name
	 * @return
	 */
	public List<MetaData> findForName(String name){
		List<String> ids = findIDsForName(name);
		return getAll(ids);
	}
	
	
	private List<String> findIDsForName(String name){
		Vector<String> out = new Vector<String>();
		Iterator<String> idIt = idMap.keySet().iterator();
		while(idIt.hasNext() ){
			String currID = idIt.next();
			String currName = idMap.get(currID).getName();
			if(currName.equals(name)){
				out.add(currID);
			}
		}
		return out;
	}
	
	/**
	 * Currently returning list contains null entries when an non-existent id is given.
	 * @param ids
	 * @return
	 */
	private List<MetaData> getAll(List<String> ids){
		Vector<MetaData> out = new Vector<MetaData>();
		if(ids != null && ids.size()>0){
			for(String id : ids){
				out.add(idMap.get(id));
			}
		}
		return out;
	}

	private void initMap(final MetaData[] entries) {
		for (final MetaData m : entries) {
			final boolean s = put(m);
			if (!s) {
				throw new Error("Duplicate ID while initMap: " + m.getId());
			}
		}
	}

	private boolean putId(final MetaData meta) {
		if (!idMap.containsKey(meta.getId())) {
			idMap.put(meta.getId(), meta);
			return true;
		} else {
			return false;
		}
	}

	private boolean putTime(final MetaData meta) {
		final Date d = meta.getTimestamp();
		if (!timeMap.containsKey(d)) {
			final Vector<String> v = new Vector<String>();
			v.add(meta.getId());
			timeMap.put(d, v);
			return true;
		} else {
			final Vector<String> v = timeMap.get(d);
			if (v != null) {
				v.add(meta.getId());
				return true;
			}
			return false;
		}
	}

}
