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
import ch.unibas.informatik.hs15.cs203.datarepository.common.MetaDataWrapper;
import util.jsontools.Json;

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
		MetaDataWrapper _ret = new MetaDataWrapper(newID, file.getName(), description,
				RepoFileUtils.getFileCount(file),
				RepoFileUtils.getFileSize(file), dateCutter(new Date()));
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
		return _ret.getWrappedObject();
	}
	
	private Date dateCutter(Date d){
		return Json.iso8601ToDate(Json.dateToISO8601(d));
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
		ProgressListener progressListener){
		try{
		
			List<MetaDataWrapper> wholeMetadata = wrap(exportCheck(exportCriteria, target,
		progressListener));
		long totalNumberOfBytes=0;
		for(MetaDataWrapper md:wholeMetadata){
//			System.out.println(md.getId());
			totalNumberOfBytes+=RepoFileUtils.getFileSize(new File(repositoryFolder.getAbsolutePath()+"/"+md.getId()));
		}
		
		long copiedBytes=0;
		progressListener.start();
		progressListener.progress(copiedBytes, totalNumberOfBytes);
		for(int c=0;c<wholeMetadata.size();c++){
//			System.out.println(repositoryFolder.getAbsolutePath()+"/"+wholeMetadata.get(c).getId()+"/"+wholeMetadata.get(c).getName());
			File source = new File(repositoryFolder.getAbsolutePath()+"/"+wholeMetadata.get(c).getId()+"/"+wholeMetadata.get(c).getName());
//			System.out.println(target.getAbsolutePath());
			File fullTarget=new File(target.getAbsolutePath());
			
			try {
		RepoFileUtils.copyRecursively(source.getAbsoluteFile().toPath(), fullTarget.getAbsoluteFile().toPath(), progressListener, copiedBytes, totalNumberOfBytes);
		} catch (IOException e) {
			progressListener.finish();
		throw new IllegalArgumentException("Something happend while copying");
		}
			copiedBytes+=RepoFileUtils.getFileSize(new File(repositoryFolder.getAbsolutePath()+"/"+wholeMetadata.get(c).getId()));
		}
		progressListener.finish();
		
		
	
		
		//Export all datasets
		return unwrap(wholeMetadata);
		
		}catch (Exception e){
//			e.printStackTrace();
			throw e;
		}
	}

	private List<MetaData> exportCheck(Criteria exportCriteria, File target,
		ProgressListener progressListener) {
	Verification.verifyNotNullCriteria(exportCriteria);
	Verification.verifyProgressListener(progressListener);
//	Verification.verifyAbsence(target);
	// TODO If ID has been specified, check for existence
	if(target!=null){
	if(!target.exists()){
//		System.out.println("Hello");
		throw new IllegalArgumentException("The target path enterd couldn't be found");
	}
	}else{
		throw new IllegalArgumentException("Please define a target.");
	}
	List<MetaDataWrapper> wholeMetadata = wrap(getMetaData(exportCriteria));
	if(exportCriteria.getId()!=null){
		
//		System.out.println(getMetaData(exportCriteria));
		
		if(wholeMetadata.size()==0){
			throw new IllegalArgumentException("The specified ID does not correspond to a dataset within the repository");
		}	
		//Export dataset with given ID
	}
	
	//Check duplicates
	HashSet<String> names = new HashSet<String>();
	
	long size=0;
//	System.out.println(wholeMetadata.size());
	for(int c=0;c<wholeMetadata.size();c++){
//		System.out.println(wholeMetadata.get(c).getName());
		if(!names.add(wholeMetadata.get(c).getName())){
			throw new IllegalArgumentException("The given export Criteria matches datasets with identical names");
		}
		File ft=new File(target.getAbsolutePath()+"/"+wholeMetadata.get(c).getName());
		Verification.verifyAbsence(ft);
	}
	
	if(target.getAbsolutePath().startsWith(repositoryFolder.getAbsolutePath())){
		throw new IllegalArgumentException("The targer path given seems to bo inside the repository.");
	}
	
	return unwrap(wholeMetadata);
	}
	
	
//	/**
//	 * This method returns the size of a {@link File}. This can be either a file ore a folder in the filesystem. The size is returned in bytes and evaluated recoursivly.
//	 * @param data The File ore Folder
//	 * @return The size of the File or Folder in Bytes
//	 */
//	private long getBytesOf(File data) {
//	long size = 0;
//	for (File f : data.listFiles()) {
//		if (f.isFile()) {
//			size+=f.length();
//		} else {
//			size+=getBytesOf(f);
//		}
//	}
//	return size;
//	}

	@Override
	public MetaData replace(String id, File file, String description,
			boolean move, ProgressListener progressListener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetaData> getMetaData(Criteria searchCriteria) {
		List<MetaDataWrapper> _res = new ArrayList<MetaDataWrapper>();
		MetaDataManager mdm=null;
		try{
			mdm = MetaDataManager.getMetaDataManager(repositoryFolder.getAbsolutePath());
			if(searchCriteria==null || searchCriteria.empty()){
				_res = mdm.getAllMetaData();
				Collections.sort(_res, new MetaDataComparator());
				return unwrap(_res);
			}
			if(searchCriteria.getId()!= null && !searchCriteria.onlyID()){
				throw new IllegalArgumentException("If you specify an ID, no other criteria can be specified");
			}
			if(searchCriteria.onlyID()){
				MetaDataWrapper idMatch = mdm.getMeta(searchCriteria.getId());
				if(idMatch!= null){
					_res.add(idMatch);
				}
				return unwrap(_res);
			}
			_res.addAll(mdm.getMatchingMeta(searchCriteria));
			Collections.sort(_res, new MetaDataComparator());
//			try{
//			mdm.close();
//			}catch(IOException e){
//				e.printStackTrace();
//			}
			return unwrap(_res);
		}catch(Exception e){
//			try {
//		mdm.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
////			throw new IllegalArgumentException(e1.getMessage());
//		}
			throw new IllegalArgumentException(e.getMessage());
		}finally{
			if(mdm!=null){
				try{
					mdm.close();
				}catch(IOException e){
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}
	
	private List<MetaData> unwrap(List<MetaDataWrapper> wrappedList){
		ArrayList<MetaData> out = new ArrayList<MetaData>();
		for(MetaDataWrapper w : wrappedList){
			out.add(w.getWrappedObject() );
		}
		return out;
	}

	private List<MetaDataWrapper> wrap(List<MetaData> wrappedList){
		ArrayList<MetaDataWrapper> out = new ArrayList<MetaDataWrapper>();
		for(MetaData w : wrappedList){
			out.add(new MetaDataWrapper(w) );
		}
		return out;
	}
}
