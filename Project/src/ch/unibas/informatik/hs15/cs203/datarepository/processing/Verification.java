package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.File;

import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;
import ch.unibas.informatik.hs15.cs203.datarepository.common.CriteriaWrapper;

/**
 * Collection of static methods for veryifying Parameters. Instead of returning
 * booleans, they throw Exceptions
 * 
 */
class Verification {

	/**
	 * CHecks for null, length>1000 and whether the String contains an ISO
	 * Control Character
	 * 
	 * @throws IllegalArgumentException
	 */
	static void verifyDescription(String description)
			throws IllegalArgumentException {
		if (description == null) {
			return;
		}
		// Check length
		if (description.length() > 1000) {
			throw new IllegalArgumentException(
					"Description is longer than 1000 characters");
		}
		// Check ISO control characters
		for (char c : description.toCharArray()) {
			if (Character.isISOControl(c)) {
				throw new IllegalArgumentException(
						"Description contains an ISO-Control Parameter");
			}
		}
	}

	/**
	 * Compares File Path to Repo Path
	 */
	static void verifyNotRepoPath(File file, File repositoryFolder)
			throws IllegalArgumentException {
		if(file == null || repositoryFolder == null){
			throw new IllegalArgumentException("Either the file or the repository Folder were null");
		}
		if(!file.exists() || !repositoryFolder.exists()){
			throw new IllegalArgumentException("Either the file or the repository Folder do not exist");
		}
		if (file.getAbsolutePath().equals(repositoryFolder.getAbsolutePath())) {
			throw new IllegalArgumentException(
					"The given path points to the repository folder");
		}
	}

	/**
	 * If the given file is null or it doesn't exist, an error is thrown
	 */
	static void verifyExistence(File file) throws IllegalArgumentException {
		if (file == null) {
			throw new IllegalArgumentException("The repository folder is null");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException(
					"The given folder does not exist");
		}
	}

	static void verifyProgressListener(ProgressListener progressListener) {
		if (progressListener == null) {
			throw new IllegalArgumentException("No ProgressListener was given");
		}
	}

	/**
	 * If the given file is already within the repositoryFolder, an exception is thrown.
	 */
	static void verifyNotWithinRepo(File file, File repositoryFolder)
			throws IllegalArgumentException {
		verifyExistence(file);
		if (file.getAbsolutePath().startsWith(
				repositoryFolder.getAbsolutePath())) {
			throw new IllegalArgumentException(
					"The given path points to a folder or file within the repository folder");
		}
	}

	/**
	 * Verifies whether the criteria itself is null and if a criteria is specified at all.
	 * If criteria is null or contains no information, an error is thrown.
	 * If an ID has been specified and any other information has been specified, an error is thrown
	 */
	static void verifyNotNullCriteria(CriteriaWrapper criteria) {
		if (criteria == null || criteria.isNull()) {
			throw new IllegalArgumentException("The given criteria is null");
		}
//		if (criteria.getAfter() == null && criteria.getBefore() == null
//				&& criteria.getId() == null && criteria.getName() == null
//				&& criteria.getText() == null) {
//			throw new IllegalArgumentException(
//					"No criteria have been specified");
//		}
		if (criteria.getId() != null
				&& (criteria.getAfter() != null || criteria.getBefore() != null
						|| criteria.getName() != null || criteria.getText() != null)) {
			throw new IllegalArgumentException(
					"If you specify an ID, no other criteria is allowed");
		}
	}


	/**
	 * Searches the repository for a dataset with a given id.
	 * @return whether a dataset exists or not.
	 */
	static boolean verifyExistence(String id, File repositoryFolder) {
		for(String filename : repositoryFolder.list()){
			if(filename.equals(id)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether a file is null and whether it already exists.
	 * If the file already exists, an error is thrown
	 */
	static void verifyAbsence(File file) {
		if (file == null) {
			throw new IllegalArgumentException("The repository folder is null");
		}
		if (file.exists()) {
			throw new IllegalArgumentException(
					"The given file already exists");
		}
	}
}
