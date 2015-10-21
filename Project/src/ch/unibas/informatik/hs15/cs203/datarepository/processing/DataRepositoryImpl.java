package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;
import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;

class DataRepositoryImpl implements DataRepository {
	/**
	 * Path to the Repository Folder
	 */
	private File repositoryFolder;

	protected DataRepositoryImpl(File repositoryFolder) throws IOException {
		this.repositoryFolder = repositoryFolder;
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
		MetaData _ret = new MetaData(newID, file.getName(), description,
				RepoFileUtils.getFileCount(file),
				RepoFileUtils.getFileSize(file), new Date());
		try {
			MetaDataManager mdm = MetaDataManager
					.getMetaDataManager(repositoryFolder.getAbsolutePath());
			//Write temporary metadata
			mdm.writeMetadata(_ret);

			progressListener.start();
			progressListener.progress(0, RepoFileUtils.getFileSize(file));
			if (move) {
				RepoFileUtils.move(file.getAbsoluteFile().toPath(), joinedPath);
				progressListener.progress(RepoFileUtils.getFileSize(joinedPath.toFile()), RepoFileUtils.getFileSize(joinedPath.toFile()));
			} else {
				RepoFileUtils.copyRecursively(file.getAbsoluteFile().toPath(),
						joinedPath, progressListener, 0,
						RepoFileUtils.getFileSize(file));
			}
			progressListener.finish();
			mdm.close();
		} catch (IOException e) {
			throw new IllegalArgumentException("File could not be moved/copied");
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
		Verification.verifyNotNullCriteria(exportCriteria);
		Verification.verifyProgressListener(progressListener);
		Verification.verifyAbsence(target);
		// TODO If ID has been specified, check for existence
		if(exportCriteria.getId()!=null){
			if(getMetaData(exportCriteria).size()==0){
				throw new IllegalArgumentException("The specified ID does not correspond to a dataset within the repository");
			}
			//Export dataset with given ID
		}
		
		//Check duplicates
		HashSet<String> names = new HashSet<String>();
		MetaData [] wholeMetadata = getMetaData(exportCriteria).toArray(new MetaData[0]);
		long size=0;
		for(MetaData md : wholeMetadata){
			if(names.add(md.getName())){
				throw new IllegalArgumentException("The given export Criteria matches datasets with identical names");
			}
		}
		
		
		//Export all datasets
		return null;
	}
	
	private long getBytesOf(File data) {
	long size = 0;
	for (File f : data.listFiles()) {
		if (f.isFile()) {
			size+=f.length();
		} else {
			size+=getBytesOf(f);
		}
	}
	return size;
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
