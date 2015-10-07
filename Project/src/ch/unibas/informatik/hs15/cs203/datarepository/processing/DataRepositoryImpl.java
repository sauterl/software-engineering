package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;
import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;

class DataRepositoryImpl implements DataRepository {
	private File repositoryFolder;
	/**
	 * get new ID via assignID()
	 */
	private int idcounter = 0;	//TODO
	
	protected DataRepositoryImpl(File repositoryFolder) {
		this.repositoryFolder = repositoryFolder;
		repositoryFolder.mkdirs();
		//TODO Parse current ID counter
	}

	@Override
	public MetaData add(File file, String description, boolean move,
			ProgressListener progressListener) {
		//Verification
		verifyExistence(file);
		verifyNotRepoPath(file);
		verifyNotWithinRepo(file);

		verifyDescription(description);

		verifyProgressListener(progressListener);
		
		//Move File
		int newID = assignID();
		Path IDFolder = Paths.get(repositoryFolder.getAbsolutePath(), String.valueOf(newID));
		IDFolder.toFile().mkdir();
		
		
		Path joinedPath = Paths.get(repositoryFolder.getAbsolutePath(), String.valueOf(newID), file.getName());
		
		try {
			if (move) {
				Files.move(file.getAbsoluteFile().toPath(), joinedPath);
			} else {
				Files.copy(file.getAbsoluteFile().toPath(), joinedPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("File could not be moved/copied");
		}

		// TODO Generate ID
		MetaData _ret = new MetaData(String.valueOf(newID), file.getName(), description,
				getFileCount(joinedPath.toFile()), getFileSize(joinedPath.toFile()), new Date());
		return _ret;
	}

	/**
	 * If the file is a directory: Iterates recursively over the given dir and counts number of files and dirs inside the file
	 * If the file is a file: Return 1 
	 * @param file
	 * @return 1 if it's a file, else no. of folders and files inside given directory
	 */
	private int getFileCount(File file) {
		if(file.isFile()){
			System.out.println(file.getAbsolutePath()+" has 1 element");
			return 1;
		}
		int count = 0;
		File[] subfiles = file.listFiles();

		if (subfiles != null) {
			for (int i = 0; i < subfiles.length; i++) {
				count++;
				System.out.println(count);
				File newfile = subfiles[i];
				if (file.isDirectory()) {
					count += getFileCount(newfile);
				}
			}
		}
		System.out.println(file.getAbsolutePath()+" has "+count+" elements");
		return count;
	}

	private long getFileSize(File file) {
		return file.length();
	}

	private void verifyDescription(String description) {
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

	private void verifyNotWithinRepo(File file) {
		if (file.getAbsolutePath().contains(repositoryFolder.getAbsolutePath())) {
			throw new IllegalArgumentException(
					"The given path points to a folder or file within the repository folder");
		}
	}

	private void verifyNotRepoPath(File file) {
		if (file.getAbsolutePath().equals(repositoryFolder.getAbsolutePath())) {
			throw new IllegalArgumentException(
					"The given path points to the repository folder");
		}
	}

	private void verifyExistence(File file) {
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
	
	private synchronized int assignID(){
		idcounter++;
		return idcounter;
	}

}
