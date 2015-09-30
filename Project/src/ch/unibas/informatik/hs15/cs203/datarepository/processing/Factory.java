package ch.unibas.informatik.hs15.cs203.datarepository.processing;

import java.io.File;

import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;

/**
 * Static factory for creating a {@link DataRepository}.
 * 
 * @author Franz-Josef Elmer
 *
 */
public class Factory
{
  /**
   * Creates a {@link DataRepository} for the specified repository folder.
   * The repository folder will be created if it does not exit.
   * 
   * @param repositoryFolder
   *          Folder which will contain data sets and meta data.
   * @return an instance of a class implementing {@link DataRepository}.
   * @throws IllegalArgumentException
   *           in the following cases:
   *           <ul><li><code>folder</code> is <code>null</code>
   *           <li><code>folder</code> is a file and not a folder.
   *           <li><code>folder</code> is an existing folder but doesn't looks like
   *           a repository folder.
   *           </ul>
   */
  public static DataRepository create(File repositoryFolder)
  {
    // TODO: Implementation missing
    return null;
  }
  
  private Factory()
  {
  }
}
