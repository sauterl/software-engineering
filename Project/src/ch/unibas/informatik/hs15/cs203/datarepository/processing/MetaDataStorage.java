package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;
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

	/**
	 * Creates an initially empty {@link MetaDataStorage}.
	 * 
	 */
	public MetaDataStorage() {
		this(null);
	}

	public MetaDataStorage(final MetaData[] entries) {
		if (entries != null && entries.length > 0) {
			initMap(entries);
		}
	}

	/**
	 * Returns <tt>true</tt> if this storage does not contain any meta data
	 * object.
	 * 
	 * @return <tt>true</tt> if this storage does not contain any meta data
	 *         object.
	 */
	public boolean isEmpty() {
		return timeMap.isEmpty();
	}

	/**
	 * Returns a list of meta data objects fulfilling all of the specified
	 * criteria.<br />
	 * This method is designed to use with a specific query for meta data objects
	 * fulfilling a certain criteria. If the <tt>criteria</tt> asks for every
	 * meta data object in this storage, it is recommended to use {@link MetaDataStorage#getAll()} instead.
	 * Further if the criteria queries for a certain ID, the appropriate method would be {@link MetaDataStorage#get(String)}.
	 * For convince this method still returns valid results for the above mentioned cases.<br />
	 * <b>Note: The resulting list is not <tt>null</tt>-proof (therefore may contains null entries).</b>
	 * @param criteria
	 *            The conditions to fulfill.
	 * @return A list of meta data objects fulfilling all of the specified
	 *         criteria or an empty list if no matching meta data object was
	 *         found.
	 */
	public List<MetaData> get(final Criteria criteria) {
		if (criteria == null) {
			throw new IllegalArgumentException("Criteria is null");
		}
		validateNotEmpty();
		final Vector<MetaData> out = new Vector<MetaData>();
		// ALL META DATA WANDED
		final Criteria allRef = Criteria.all();
		if (allRef.equals(criteria)) {
			return Arrays.asList(getAll());
		}
		// SINGLE ID WANTED
		if (criteria.getId() != null) {
			out.add(get(criteria.getId()));
			return out;
		}
		// COMBINATION OF CONDITIONS
		final Vector<List<String>> matching = new Vector<List<String>>();
		if (criteria.getAfter() != null) {
			matching.add(findAfter(criteria.getAfter()));
		}
		if (criteria.getBefore() != null) {
			matching.add(findBefore(criteria.getBefore()));
		}
		if (criteria.getName() != null) {
			matching.add(findForName(criteria.getName()));
		}
		if (criteria.getText() != null) {
			matching.add(findTextContains(criteria.getText()));
		}
		final Vector<String> ids = new Vector<String>();
		for (final List<String> l : matching) {
			// if l is first element: addAll to ids, otherwise only intersection
			if (ids.size() > 0) {
				ids.retainAll(l);
			} else {
				ids.addAll(l);
			}
		}
		return getAll(ids);
	}

	/**
	 * Returns the {@link MetaData} with specified ID. If no meta data with such
	 * an ID was found, <tt>null</tt> is returned.
	 * 
	 * @param id
	 *            The ID of the meta data to get.
	 * @return The found meta data with specified ID or <tt>null</tt> if none
	 *         exists with such an ID.
	 * @see TreeMap#get(Object)
	 */
	public MetaData get(final String id) {
		validateNotEmpty();
		return idMap.get(id);
	}

	/**
	 * Returns all stored {@link MetaData} objects in a single array.
	 * 
	 * @return All stored meta data objects in a single array.
	 */
	public MetaData[] getAll() {
		validateNotEmpty();
		return idMap.values().toArray(new MetaData[0]);
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

	public MetaData replace(final MetaData meta) {
		if (meta == null) {
			throw new IllegalArgumentException("Cannot replace meta data null.");
		}
		validateNotEmpty();
		if (!idMap.containsKey(meta.getId())) {
			throw new IllegalArgumentException(
					"Cannot replace non existing meta data");
		}
		return idMap.put(meta.getId(), meta);
	}

	/**
	 * Returns the size of this storage.
	 * 
	 * @return The size of this storage a.k.a. the number of meta data objects
	 *         stored.
	 */
	public int size() {
		return idMap.size();
	}

	private List<String> findAfter(final Date after) {
		final Vector<String> out = new Vector<String>();
		final Collection<Vector<String>> idLists = timeMap.tailMap(after, true)
				.values();
		for (final Vector<String> ids : idLists) {
			out.addAll(ids);
		}
		return out;
	}

	private List<String> findBefore(final Date before) {
		final Vector<String> out = new Vector<String>();
		final Collection<Vector<String>> idLists = timeMap
				.headMap(before, true).values();
		for (final Vector<String> ids : idLists) {
			out.addAll(ids);
		}
		return out;
	}

	private List<String> findDescriptionContains(final String snippet) {
		final Vector<String> out = new Vector<String>();
		final Iterator<String> idIt = idMap.keySet().iterator();
		while (idIt.hasNext()) {
			final String currID = idIt.next();
			final String currDesc = idMap.get(currID).getDescription();
			if (currDesc.contains(snippet)) {
				out.add(currID);
			}
		}
		return out;
	}

	private List<String> findForName(final String name) {
		final Vector<String> out = new Vector<String>();
		final Iterator<String> idIt = idMap.keySet().iterator();
		while (idIt.hasNext()) {
			final String currID = idIt.next();
			final String currName = idMap.get(currID).getName();
			if (currName.equals(name)) {
				out.add(currID);
			}
		}
		return out;
	}

	private List<String> findNameContains(final String snippet) {
		final Vector<String> out = new Vector<String>();
		final Iterator<String> idIt = idMap.keySet().iterator();
		while (idIt.hasNext()) {
			final String currID = idIt.next();
			final String currName = idMap.get(currID).getName();
			if (currName.contains(snippet)) {
				out.add(currID);
			}
		}
		return out;
	}

	private List<String> findTextContains(final String text) {
		final List<String> names = findNameContains(text);
		final List<String> desc = findDescriptionContains(text);
		names.retainAll(desc);
		return names;
	}

	/**
	 * Currently returning list contains null entries when an non-existent id is
	 * given.
	 * 
	 * @param ids
	 * @return
	 */
	private List<MetaData> getAll(final List<String> ids) {
		final Vector<MetaData> out = new Vector<MetaData>();
		if (ids != null && ids.size() > 0) {
			for (final String id : ids) {
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
			if (v != null && !v.contains(meta.getId())) {
				v.add(meta.getId());
				return true;
			}
			return false;
		}
	}

	private void validateNotEmpty() {
		if (isEmpty()) {
			throw new IllegalStateException(
					"Cannot perform this operation on empty storage.");
		}
	}

}
