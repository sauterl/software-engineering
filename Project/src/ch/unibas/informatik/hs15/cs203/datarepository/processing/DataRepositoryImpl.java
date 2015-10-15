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
	MetaDataManager mdm;

	protected DataRepositoryImpl(File repositoryFolder) throws IOException {
		this.repositoryFolder = repositoryFolder;
		mdm = MetaDataManager
				.getMetaDataManager(repositoryFolder.getAbsolutePath());
	}

	@Override
	public MetaData add(File file, String description, boolean move,
			ProgressListener progressListener) {
		// Verification
		Verification.verifyExistence(file);
		Verification.verifyNotRepoPath(file, repositoryFolder);
		Verification.verifyNotWithinRepo(file, repositoryFolder);

		Verification.verifyDescription(description);

		Verification.verifyProgressListener(progressListener);

		String newID = MetaDataManager.generateRandomUUID();
		Path joinedPath = createNewDatasetFolder(newID);
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
		MetaData _ret = new MetaData(newID, file.getName(), description,
				getFileCount(joinedPath.toFile()),
				getFileSize(joinedPath.toFile()), new Date());
		try {
			mdm.writeMetadata(_ret);
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		return _ret;
	}

	/**
	 * Creates a new Folder within the repository using a given ID
	 * 
	 * @return Path of the new Folder
	 */
	private Path createNewDatasetFolder(String newID) {
		Path IDFolder = Paths.get(repositoryFolder.getAbsolutePath(), newID);
		IDFolder.toFile().mkdir();

		// add File
		Path joinedPath = Paths.get(repositoryFolder.getAbsolutePath(), newID);
		return joinedPath;
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
			alreadyProcessed += getFileSize(file);
		}
	}

	/**
	 * Copies source to target. If source is a directory, just calls mkdirs Else
	 * buffers with an array of 1024 Bytes and calls the progressListener
	 * 
	 * @param source
	 * @param target
	 */
	private void copy(Path source, Path target,
			ProgressListener progressListener, long alreadyProcessed,
			long totalSize) {
		if (source.toFile().isDirectory()) {
			target.toFile().mkdirs();
			alreadyProcessed += target.toFile().length();
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
				alreadyProcessed += size;
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
}
