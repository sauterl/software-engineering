package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	/**
	 * Path to the Repository Folder
	 */
	private File repositoryFolder;
	/**
	 * MetaDataManager for this repository. Handle MetaData with this class only
	 */
	private MetaDataManager mdm;

	protected DataRepositoryImpl(File repositoryFolder) throws IOException {
		this.repositoryFolder = repositoryFolder;
		mdm = MetaDataManager.getMetaDataManager(repositoryFolder
				.getAbsolutePath());
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
				RepoFileUtils.move(file.getAbsoluteFile().toPath(), joinedPath);
			} else {
				progressListener.start();
				progressListener.progress(0, RepoFileUtils.getFileSize(file));
				RepoFileUtils.copyRecursively(file.getAbsoluteFile().toPath(), joinedPath,
						progressListener, 0, RepoFileUtils.getFileSize(file));
				progressListener.finish();
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("File could not be moved/copied");
		}
		MetaData _ret = new MetaData(newID, file.getName(), description,
				RepoFileUtils.getFileCount(joinedPath.toFile()),
				RepoFileUtils.getFileSize(joinedPath.toFile()), new Date());
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
		Path joinedPath = Paths.get(repositoryFolder.getAbsolutePath(), newID);
		return joinedPath;
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
