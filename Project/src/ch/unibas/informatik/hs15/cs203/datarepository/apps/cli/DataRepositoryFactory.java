package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.File;

import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;

public interface DataRepositoryFactory {
	public DataRepository create(File repositoryFolder);
}
