package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.util.Collection;
import java.util.HashSet;

import util.jsontools.Json;

/**
 * The CollectionUtils class provides some utility methods related to the java
 * collection API.
 * 
 * @author Loris
 * 
 */
public final class CollectionUtils {

	/**
	 * Creates the intersection of given Collections.
	 * 
	 * @param collections
	 * @return
	 */
	@SafeVarargs
	public static final Collection<Json> intersect(
			final Collection<Json>... collections) {
		if (collections.length < 1) {
			throw new IllegalArgumentException("Cannot intersect 0 sets");
		} else if (collections.length == 1) {
			return collections[0];
		}
		final HashSet<Json> in = new HashSet<Json>(collections[0]);
		for (int i = 1; i < collections.length; i++) {
			if (collections[i] != null) {
				in.retainAll(collections[i]);
			}
		}
		return in;
	}

	/**
	 * Intersects the given collections. This method needs at least one
	 * collection, if only one is given, it will be returned. If more than one
	 * is given, then this method intersects the given collections by invoking
	 * {@link Collection#retainAll(Collection)} on the first given collection.<br />
	 * <b>Note: This results in the <i>first</i> collection being altered so
	 * make sure that this does not lead into any inconsistency.</b> <br />
	 * The return value of this method is just for convince, but not needed,
	 * since the intersection is returned in the first given parameter. The
	 * parameter <tt>null</tt> is not allowed and leads to an
	 * {@link NullPointerException}.
	 * 
	 * @param collections
	 *            The collections to intersect. The intersection will be done on
	 *            the first given collection.
	 * @return The intersection of the given collections as a result in the
	 *         first collection.
	 * @throws NullPointerException If the one of the given collections is null.
	 * @see Collection#retainAll(Collection)
	 */
	public static final Collection<?> intersectAll(
			final Collection<?>... collections) throws NullPointerException {
		if (collections.length < 1) {
			throw new IllegalArgumentException(
					"Cannot intersect less than one collection");
		} else if (collections.length == 1) {
			return collections[0];
		}
		if (collections[0] == null) {
			throw new NullPointerException(
					"Cannot intersect collection null with others.");
		}
		// assert collections[0] != null;
		for (int i = 1; i < collections.length; i++) {
			if(collections[i] != null){
				collections[0].retainAll(collections[i]);
			}else{
				throw new NullPointerException("Cannot intersect collection null with others.");
			}
		}
		return collections[0];
	}

	/**
	 * No constructor available since no objects are needed.
	 */
	private CollectionUtils() {
		// no objects
	}

}
