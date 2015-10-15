package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.io.FileInputStream;

import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;
import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;

class DataRepositoryImpl implements DataRepository {
	private File repositoryFolder;
	/**
	 * get new ID via assignID()
	 */
	private int idcounter = 0; // TODO

	protected DataRepositoryImpl(File repositoryFolder) {
		this.repositoryFolder = repositoryFolder;
		// TODO Parse current ID counter
	}

	@Override
	public MetaData add(File file, String description, boolean move,
			ProgressListener progressListener) {
		// Verification
		verifyExistence(file);
		verifyNotRepoPath(file);
		verifyNotWithinRepo(file);

		verifyDescription(description);

		verifyProgressListener(progressListener);

		// Create Folder with ID
		int newID = assignID();
		Path IDFolder = Paths.get(repositoryFolder.getAbsolutePath(),
				String.valueOf(newID));
		IDFolder.toFile().mkdir();

		// add File
		Path joinedPath = Paths.get(repositoryFolder.getAbsolutePath(),
				String.valueOf(newID));
		try {
			if (move) {
				move(file.getAbsoluteFile().toPath(), joinedPath);
			} else {
				progressListener.start();
				progressListener.progress(0, getFileSize(file));
				copyRecursively(file.getAbsoluteFile().toPath(), joinedPath,
						progressListener, 0, getFileSize(file));
				progressListener.finish();
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("File could not be moved/copied");
		}

		// TODO Generate ID
		MetaData _ret = new MetaData(String.valueOf(newID), file.getName(),
				description, getFileCount(joinedPath.toFile()),
				getFileSize(joinedPath.toFile()), new Date());
		return _ret;
	}

	/**
	 * Example usage: Copy mydata to /1/
	 * 
	 * @param source
	 *            mydata
	 * @param target
	 *            /1/
	 * @throws IOException
	 */
	private void copyRecursively(Path source, Path target, ProgressListener pl,
			long alreadyProcessed, long totalSize) throws IOException {
		Path nameTarget = Paths.get(target.toString(), source.getFileName()
				.toString());

		if (source.toFile().isFile()) {
			copy(source, nameTarget, pl, alreadyProcessed, totalSize);
			return;
		}
		
		copy(source, nameTarget, pl, alreadyProcessed, totalSize);
		alreadyProcessed += source.toFile().length();

		for (File file : source.toFile().listFiles()) {
			Path filepath = Paths.get(nameTarget.toString(), file.getName());
			if (file.isFile()) {
				copy(file.toPath(), filepath, pl, alreadyProcessed, totalSize);
				alreadyProcessed += file.length();
				continue;
			}
			copyRecursively(file.toPath(), nameTarget, pl, alreadyProcessed,
					totalSize);
			alreadyProcessed+=getFileSize(file);
		}
	}

	/**
	 * Copies source to target. If source is a directory, just calls mkdirs
	 * Else buffers with an array of 1024 Bytes and calls the progressListener
	 * 
	 * @param source
	 * @param target
	 */
	private void copy(Path source, Path target,
			ProgressListener progressListener, long alreadyProcessed,
			long totalSize) {
		System.out.println("Copying "+source.toString()+" | "+target.toString()+" | "+alreadyProcessed);
		
		if(source.toFile().isDirectory()){
			target.toFile().mkdirs();
			alreadyProcessed+=target.toFile().length();
			progressListener.progress(alreadyProcessed, totalSize);
			return;
		}
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(source.toFile());
			outputStream = new FileOutputStream(target.toFile());
			
			byte[] buffer = new byte[1024];
			int size = 0;
			while ((size = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, size);
				alreadyProcessed+=size;
				progressListener.progress(alreadyProcessed, totalSize);
			}
		} catch (IOException ex) {
			throw new IllegalArgumentException("Error while moving file");
		} finally {
			try {
				inputStream.close();
				outputStream.close();
			} catch (Exception e) {
				// silently ignored
			}
		}
	}

	/**
	 * Utilizes renameTo for maximum efficiency
	 */
	private void move(Path source, Path target) throws IOException {
		Path newTarget = Paths.get(target.toString(), source.getFileName()
				.toString());
		source.toFile().renameTo(newTarget.toFile());
	}

	/**
	 * If the file is a directory: Iterates recursively over the given dir and
	 * counts number of files and dirs inside the file; If the file is a file:
	 * Return 1
	 * 
	 * @param file
	 * @return 1 if it's a file, else no. of folders and files inside given
	 *         directory
	 */
	private int getFileCount(File file) {
		if (file.isFile()) {
			return 1;
		}
		int count = 0;
		File[] subfiles = file.listFiles();

		if (subfiles != null) {
			for (int i = 0; i < subfiles.length; i++) {
				File newfile = subfiles[i];
				if (newfile.isFile()) {
					count++;
					continue;
				}
				count++;
				count += getFileCount(newfile);
			}
		}
		return count;
	}

	/**
	 * Traverses the directory to give total size. If the file is a simple file,
	 * returns length
	 * 
	 * @return file size in bytes
	 */
	private long getFileSize(File directory) {
		if (directory.isFile()) {
			return directory.length();
		}
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				length += file.length();
				continue;
			}
			length += getFileSize(file);
		}
		return length;
	}

	/**
	 * CHecks for null, length>1000 and whether the String contains an ISO
	 * Control Character
	 * 
	 * @throws IllegalArgumentException
	 */
	private void verifyDescription(String description)
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

	private void verifyProgressListener(ProgressListener progressListener) {
		if (progressListener == null) {
			throw new IllegalArgumentException("No ProgressListener was given");
		}
	}

	private void verifyNotWithinRepo(File file) throws IllegalArgumentException {
		if (file.getAbsolutePath().startsWith(
				repositoryFolder.getAbsolutePath())) {
			throw new IllegalArgumentException(
					"The given path points to a folder or file within the repository folder");
		}
	}

	/**
	 * Compares Repo Path to File path
	 */
	private void verifyNotRepoPath(File file) throws IllegalArgumentException {
		if (file.getAbsolutePath().equals(repositoryFolder.getAbsolutePath())) {
			throw new IllegalArgumentException(
					"The given path points to the repository folder");
		}
	}

	/**
	 * Checks for null and whether a the file exists
	 * 
	 * @throws IllegalArgumentException
	 */
	private void verifyExistence(File file) throws IllegalArgumentException {
		if (file == null) {
			throw new IllegalArgumentException("The repository folder is null");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException(
					"The given folder does not exist");
		}
	}

	@Override
	public List<MetaData> delete(Criteria deletionCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetaData> export(Criteria exportCriteria, File target,
			ProgressListener progressListener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetaData replace(String id, File file, String description,
			boolean move, ProgressListener progressListener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetaData> getMetaData(Criteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	private synchronized int assignID() {
		idcounter++;
		return idcounter;
	}

}
