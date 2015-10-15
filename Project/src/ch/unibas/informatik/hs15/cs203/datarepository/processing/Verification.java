package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.File;

import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;

/**
 * Utils for verifying various Parameters
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
	public static void verifyNotRepoPath(File file, File repositoryFolder) throws IllegalArgumentException {
		if (file.getAbsolutePath().equals(repositoryFolder.getAbsolutePath())) {
			throw new IllegalArgumentException(
					"The given path points to the repository folder");
		}
	}
	

	/**
	 * Checks for null and whether a the file exists
	 * @throws IllegalArgumentException
	 */
	public static void verifyExistence(File file) throws IllegalArgumentException {
		if (file == null) {
			throw new IllegalArgumentException("The repository folder is null");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException(
					"The given folder does not exist");
		}
	}
	
	public static void verifyProgressListener(ProgressListener progressListener) {
		if (progressListener == null) {
			throw new IllegalArgumentException("No ProgressListener was given");
		}
	}
	
	public static void verifyNotWithinRepo(File file, File repositoryFolder) throws IllegalArgumentException {
		if (file.getAbsolutePath().startsWith(
				repositoryFolder.getAbsolutePath())) {
			throw new IllegalArgumentException(
					"The given path points to a folder or file within the repository folder");
		}
	}
}
