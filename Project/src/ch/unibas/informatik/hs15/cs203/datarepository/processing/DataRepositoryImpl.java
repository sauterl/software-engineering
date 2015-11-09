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
import ch.unibas.informatik.hs15.cs203.datarepository.common.CriteriaWrapper;
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
		MetaDataWrapper _ret = new MetaDataWrapper(newID, file.getName(),
				description, RepoFileUtils.getFileCount(file),
				RepoFileUtils.getFileSize(file), dateCutter(new Date()));
		MetaDataManager mdm = null;
		try {
			mdm = MetaDataManager.getMetaDataManager(repositoryFolder
					.getAbsolutePath());
			mdm.add(_ret);

			progressListener.start();
			progressListener.progress(0, _ret.getSize());
			if (move) {
				RepoFileUtils.move(file.getAbsoluteFile().toPath(), joinedPath);
				progressListener.progress(
						_ret.getSize(),
						_ret.getSize());
			} else {
				RepoFileUtils.copyRecursively(file.getAbsoluteFile().toPath(),
						joinedPath, progressListener, 0, _ret.getSize());
			}
			progressListener.finish();
		} catch (IOException e) {
			throw new IllegalArgumentException("File could not be moved/copied");
		} finally {
			if (mdm != null) {
				mdm.close();
			}
		}
		return _ret.getWrappedObject();
	}

	private Date dateCutter(Date d) {
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
		CriteriaWrapper deletionTests = new CriteriaWrapper(deletionCriteria);
		if (deletionTests.equals(CriteriaWrapper.all())) {
			throw new IllegalArgumentException("Invalid parameters");
		}
		List<MetaDataWrapper> wholeMetadata = null;
		MetaDataManager mdm = MetaDataManager
				.getMetaDataManager(repositoryFolder.getAbsolutePath());
		try {
			Verification.verifyNotNullCriteria(new CriteriaWrapper(
					deletionCriteria));
			wholeMetadata = wrap(getMetaData(deletionCriteria));
			if (deletionCriteria.getId() != null) {
				if (wholeMetadata.size() == 0) {
					throw new IllegalArgumentException(
							"The specified ID does not correspond to a dataset within the repository");
				}
			}
			for (MetaDataWrapper md : wholeMetadata) {
				File source = new File(repositoryFolder.getAbsolutePath() + "/"
						+ md.getId());
				RepoFileUtils.deleteRecursively(source.getAbsoluteFile()
						.toPath());
				mdm.remove(md);
			}
			mdm.close();
		} catch (Exception e) {
			mdm.close();
			throw new IllegalArgumentException(
					"Something happenened while deleting the files. "
							+ e.getMessage());
		}
		return unwrap(wholeMetadata);
	}

	@Override
	public List<MetaData> export(Criteria exportCriteria, File target,
			ProgressListener progressListener) {
			List<MetaDataWrapper> wholeMetadata = wrap(exportCheck(
					exportCriteria, target, progressListener));
			long totalNumberOfBytes = 0;
			for (MetaDataWrapper md : wholeMetadata) {
				totalNumberOfBytes+= md.getSize();
			}

			long copiedBytes = 0;
			progressListener.start();
			progressListener.progress(copiedBytes, totalNumberOfBytes);
			for (MetaDataWrapper md : wholeMetadata) {
				File source = new File(repositoryFolder.getAbsolutePath() + "/"
						+ md.getId() + "/"
						+ md.getName());
				File fullTarget = new File(target.getAbsolutePath());

				try {
					RepoFileUtils.copyRecursively(source.getAbsoluteFile()
							.toPath(), fullTarget.getAbsoluteFile().toPath(),
							progressListener, copiedBytes, totalNumberOfBytes);
				} catch (IOException e) {
					progressListener.finish();
					throw new IllegalArgumentException(
							"Something happend while exporting a file");
				}
				copiedBytes += md.getSize();
			}
			progressListener.finish();

			return unwrap(wholeMetadata);
	}

	private List<MetaData> exportCheck(Criteria exportCriteria, File target,
			ProgressListener progressListener) {
		Verification.verifyNotNullCriteria(new CriteriaWrapper(exportCriteria));
		Verification.verifyProgressListener(progressListener);
		if (target == null) {
			throw new IllegalArgumentException("Please define a target.");
		}
		if (!target.exists()) {
			throw new IllegalArgumentException(
					"The target path does not exist");
		}
		if(target.isFile()){
			throw new IllegalArgumentException("The given target points to a file, not a directory");
		}
		List<MetaDataWrapper> wholeMetadata = wrap(getMetaData(exportCriteria));
		if (exportCriteria.getId() != null) {
			if (wholeMetadata.size() == 0) {
				throw new IllegalArgumentException(
						"The specified ID does not correspond to a dataset within the repository");
			}
			return unwrap(wholeMetadata);
		}

		// Check duplicates
		HashSet<String> names = new HashSet<String>();

		for (int c = 0; c < wholeMetadata.size(); c++) {
			if (!names.add(wholeMetadata.get(c).getName())) {
				throw new IllegalArgumentException(
						"The given export Criteria matches datasets with identical names");
			}
			File ft = new File(target.getAbsolutePath() + "/"
					+ wholeMetadata.get(c).getName());
			Verification.verifyAbsence(ft);
		}

		if (target.getAbsolutePath().startsWith(
				repositoryFolder.getAbsolutePath())) {
			throw new IllegalArgumentException(
					"The targer path given seems to be inside the repository.");
		}

		return unwrap(wholeMetadata);
	}

	@Override
	public MetaData replace(String id, File file, String description,
			boolean move, ProgressListener progressListener) {
		//TODO Care about System crashes between delete and add
		if(description==null || description ==""){
			MetaDataManager mdm = MetaDataManager.getMetaDataManager(repositoryFolder.getAbsolutePath());
			description = mdm.getMeta(id).getDescription();
			mdm.close();
		}
		this.delete(Criteria.forId(id));
		return this.add(file, description, move, progressListener);
	}

//	/**
//	 * see DataRepositoryImpl.add(file, description, move, progressListener).
//	 * The only difference here is that it takes as an additional parameter an id
//	 */
//	private MetaData add(File file, String id, String description,
//			boolean move, ProgressListener progressListener) {
//		
//	}

	@Override
	public List<MetaData> getMetaData(Criteria searchCriteria) {
		if (searchCriteria == null) {
			throw new IllegalArgumentException(
					"Search Criteria must not be null");
		}
		List<MetaDataWrapper> _res = new ArrayList<MetaDataWrapper>();
		MetaDataManager mdm = null;
		try {
			mdm = MetaDataManager.getMetaDataManager(repositoryFolder
					.getAbsolutePath());
			if (searchCriteria.empty()) {
				_res = mdm.getAllMetaData();
				Collections.sort(_res, new MetaDataComparator());
				return unwrap(_res);
			}
			if (searchCriteria.getId() != null && !searchCriteria.onlyID()) {
				throw new IllegalArgumentException(
						"If you specify an ID, no other criteria can be specified");
			}
			if (searchCriteria.onlyID()) {
				MetaDataWrapper idMatch = mdm.getMeta(searchCriteria.getId());
				if (idMatch != null) {
					_res.add(idMatch);
				}
				return unwrap(_res);
			}
			_res.addAll(mdm
					.getMatchingMeta(new CriteriaWrapper(searchCriteria)));
			Collections.sort(_res, new MetaDataComparator());
			mdm.close();
			return unwrap(_res);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		} finally {
			if (mdm != null) {
				mdm.close();
			}
		}
	}

	private List<MetaData> unwrap(List<MetaDataWrapper> wrappedList) {
		ArrayList<MetaData> out = new ArrayList<MetaData>();
		for (MetaDataWrapper w : wrappedList) {
			out.add(w.getWrappedObject());
		}
		return out;
	}

	private List<MetaDataWrapper> wrap(List<MetaData> wrappedList) {
		ArrayList<MetaDataWrapper> out = new ArrayList<MetaDataWrapper>();
		for (MetaData w : wrappedList) {
			out.add(new MetaDataWrapper(w));
		}
		return out;
	}
}
