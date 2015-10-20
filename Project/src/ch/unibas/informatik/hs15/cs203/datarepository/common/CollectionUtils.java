package ch.unibas.informatik.hs15.cs203.datarepository.common;

import java.util.Collection;
import java.util.HashSet;

import util.jsontools.Json;

/**
 * The CollectionUtils class provides some utility methods related to the java collection API.
 * @author Loris
 *
 */
public final class CollectionUtils {

	/**
	 * No constructor available since no objects are needed.
	 */
	private CollectionUtils() {
		// no objects
	}
	
	/**
	 * Creates the intersection of given Collections.
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

}
