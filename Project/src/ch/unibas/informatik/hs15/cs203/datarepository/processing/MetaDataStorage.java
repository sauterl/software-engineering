package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import ch.unibas.informatik.hs15.cs203.datarepository.common.CriteriaWrapper;
import ch.unibas.informatik.hs15.cs203.datarepository.common.MetaDataWrapper;

/**
 * The {@link MetaDataStorage} class provides methods to store a list of meta
 * data.<br />
 * Further this class also is capable of handling queries with {@link CriteriaWrapper} objects.<br />
 * 
 * <p>
 * The most commonly used methods of this class are:
 * <ul>
 * <li> {@link #put(MetaDataWrapper)} - To add {@link MetaDataWrapper} to the storage</li>
 * <li> {@link #get(CriteriaWrapper)} - To get all stored {@link MetaDataWrapper} matching the {@link CriteriaWrapper}</li>
 * <li> {@link #get(String)} - To get a {@link MetaDataWrapper} object by its ID</li>
 * <li> {@link #getAll()} - To get every single stored {@link MetaDataWrapper}</li>
 * </ul>
 * But there are more uses.
 * </p>
 * 
 * @author Loris
 * 
 */
public class MetaDataStorage {

	private final TreeMap<String, MetaDataWrapper> idMap = new TreeMap<String, MetaDataWrapper>();
	private final TreeMap<Date, Vector<String>> timeMap = new TreeMap<Date, Vector<String>>();

	/**
	 * Creates an initially empty {@link MetaDataStorage}.
	 * 
	 */
	public MetaDataStorage() {
		this(null);
	}

	public MetaDataStorage(final MetaDataWrapper[] entries) {
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
	 * @throws IllegalArgumentException If the given criteria is <tt>null</tt>.
	 * @throws IllegalStateException If the storage is empty.
	 */
	public List<MetaDataWrapper> get(final CriteriaWrapper criteria) {
		if (criteria == null) {
			throw new IllegalArgumentException("CriteriaWrapper is null");
		}
		if(isEmpty()){
			return new ArrayList<MetaDataWrapper>();
		}
		final Vector<MetaDataWrapper> out = new Vector<MetaDataWrapper>();
		// ALL META DATA WANDED
		final CriteriaWrapper allRef = CriteriaWrapper.all();
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
	 * Returns the {@link MetaDataWrapper} with specified ID. If no meta data with such
	 * an ID was found, <tt>null</tt> is returned.
	 * 
	 * @param id
	 *            The ID of the meta data to get.
	 * @return The found meta data with specified ID or <tt>null</tt> if none
	 *         exists with such an ID.
	 * @see TreeMap#get(Object)
	 * @throws IllegalStateException If the storage is empty.
	 */
	public MetaDataWrapper get(final String id) {
		validateNotEmpty();
		return idMap.get(id);
	}

	/**
	 * Returns all stored {@link MetaDataWrapper} objects in a single array.
	 * 
	 * @return All stored meta data objects in a single array.
	 * @throws IllegalStateException If the storage is empty.
	 */
	public MetaDataWrapper[] getAll() {
		if(isEmpty()){
			return new MetaDataWrapper[0];
		}
		return idMap.values().toArray(new MetaDataWrapper[0]);
	}
	
	/**
	 * Returns all IDs known to this {@link MetaDataStorage}.
	 * <br />It is granted, that to every single entry of the returning list,
	 * the IDs, a meta data entry in this storage exists.
	 * @return All known IDs.
	 * @throws IllegalStateException If the storage is empty.
	 */
	public Set<String> getAllIDs(){
		if(isEmpty()){
			return new HashSet<String>();
		}
		return idMap.keySet();
	}

	/**
	 * Puts the given {@link MetaDataWrapper} to this {@link MetaDataStorage}. The
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
	public boolean put(final MetaDataWrapper meta) {
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
	 * @param meta
	 * @return
	 */
	public MetaDataWrapper replace(final MetaDataWrapper meta) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	/**
	 * Removes the specified {@link MetaDataWrapper} object from this storage.
	 * @param meta The meta data object to remove.
	 * @return The removed meta data or <tt>null</tt> if none got removed.
	 * @throws IllegalStateException If an error occured while removing.
	 */
	public MetaDataWrapper remove(final MetaDataWrapper meta){
		if(removeTime(meta)){
			return removeID(meta);
		}else{
			throw new IllegalStateException("Could not entirely remove meta data with id: "+meta.getId());
		}
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
		names.addAll(desc);
		return names;
	}

	/**
	 * Currently returning list contains null entries when an non-existent id is
	 * given.
	 * 
	 * @param ids
	 * @return
	 */
	private List<MetaDataWrapper> getAll(final List<String> ids) {
		final Vector<MetaDataWrapper> out = new Vector<MetaDataWrapper>();
		if (ids != null && ids.size() > 0) {
			for (final String id : ids) {
				out.add(idMap.get(id));
			}
		}
		return out;
	}

	private void initMap(final MetaDataWrapper[] entries) {
		for (final MetaDataWrapper m : entries) {
			final boolean s = put(m);
			if (!s) {
				throw new Error("Duplicate ID while initMap: " + m.getId());
			}
		}
	}

	private boolean putId(final MetaDataWrapper meta) {
		if (!idMap.containsKey(meta.getId())) {
			idMap.put(meta.getId(), meta);
			return true;
		} else {
			return false;
		}
	}
	
	private MetaDataWrapper removeID(final MetaDataWrapper meta){
		if(!idMap.containsKey(meta.getId() ) ){
			throw new IllegalArgumentException("Cannot remove inexistent meta data with id: "+meta.getId() );
		}else{
			return idMap.remove(meta.getId() );
		}
	}

	private boolean putTime(final MetaDataWrapper meta) {
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
	
	private boolean removeTime(final MetaDataWrapper meta){
		final Date d = meta.getTimestamp();
		if(!timeMap.containsKey(d) ){
			// likely already removed
			return true;
		}else{
			Vector<String> ids = timeMap.get(d);
			if(ids != null){
				if(ids.contains(meta.getId() )){
					if(ids.removeElement(meta.getId() ) ){
						//GOT REMOVED
						return true;
					}else{
						//got NOT removed, was NO ELEMENT, throw an error?
						return false;
					}
				}else{
					// likely already removed
					return true;
				}
			}else{
				// likely already removed
				return true;
			}
		}
	}

	private void validateNotEmpty() {
		if (isEmpty()) {
			throw new IllegalStateException(
					"Cannot perform this operation on empty storage.");
		}
	}

}
