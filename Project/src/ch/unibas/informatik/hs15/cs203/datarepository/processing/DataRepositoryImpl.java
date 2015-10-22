package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
		MetaDataManager mdm = null;
		try {
			mdm = MetaDataManager
					.getMetaDataManager(repositoryFolder.getAbsolutePath());
			//Write temporary metadata
			mdm.add(_ret);

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
		} catch (IOException e) {
			throw new IllegalArgumentException("File could not be moved/copied");
		}finally{
			if(mdm != null){
				try {
					mdm.close();
				} catch (IOException e) {
					throw new RuntimeException("An IOException occurred while closing the MetaDataManger",e);
				}
			}
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
		for(MetaData md : getMetaData(exportCriteria)){
			if(names.add(md.getName())){
				throw new IllegalArgumentException("The given export Criteria matches datasets with identical names");
			}
		}
		//Export all datasets
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
		List<MetaData> _res = new ArrayList<MetaData>();

		try{
			MetaDataManager mdm = MetaDataManager.getMetaDataManager(repositoryFolder.getAbsolutePath());
			if(searchCriteria==null || searchCriteria.empty()){
				_res = mdm.getAllMetaData();
				Collections.sort(_res, new MetaDataComparator());
				return _res;
			}
			if(searchCriteria.getId()!= null && !searchCriteria.onlyID()){
				throw new IllegalArgumentException("If you specify an ID, no other criteria can be specified");
			}
			if(searchCriteria.onlyID()){
				MetaData idMatch = mdm.getMeta(searchCriteria.getId());
				if(idMatch!= null){
					_res.add(idMatch);
				}
				return _res;
			}
			_res.addAll(mdm.getMatchingMeta(searchCriteria));
			Collections.sort(_res, new MetaDataComparator());
			return _res;
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}
}
