/*
 * Copyright 2015 Franz-Josef Elmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.unibas.informatik.hs15.cs203.datarepository.api;

import java.io.File;
import java.util.List;

/**
 * Interface for a programmatic access to the data repository. Exceptions are thrown if
 * either the arguments are illegal ({@link IllegalArgumentException}) or the requested
 * operation fails ({@link RuntimeException}). All thrown exceptions contain
 * a human-readable message which explain for a user the reason which causes the exception.
 * <p>
 * Methods with a {@link ProgressListener} argument have to report progress of copying data in
 * either regular time intervals or chunks of bytes copied. Non of the methods of
 * {@link ProgressListener} is invoked if the method arguments are invalid (that is, a
 * {@link IllegalArgumentException} has to be thrown).
 * 
 * @author Franz-Josef Elmer
 */
public interface DataRepository
{
  /**
   * Adds the specified file or folder to the repository as a new data set and
   * returns its unique id.
   * <p>
   * Note: In case of <code>move == true</code>
   * 
   * @param file
   *          File or folder.
   * @param description
   *          An optional description of the data set. Can be <code>null</code>. If it is longer
   *          than 1000 characters or it
   *          contains ISO control characters an IllegalArgumentException is thrown.
   * @param move
   *          If <code>true</code> the file/folder will be moved into the
   *          repository. This works only if file/folder and repository
   *          folder are on the same physical file system. If this is not
   *          the case an exception is thrown. If <code>false</code> the
   *          file/folder will be copied (deep copy) into the repository
   *          folder.
   * @param progressListener
   *          Progress callback object. If <code>move == true</code>
   *          {@link ProgressListener#progress(long, long)} is called twice. First,
   *          with <code>numberOfBytes == 0</code> and second
   *          with <code>numberOfBytes == totalNumberOfBytes</code>.
   * @return meta data with unique identifier, number of files and total size.
   * @throws IllegalArgumentException
   *           in the following cases:
   *           <ul>
   *           <li><code>file</code> is <code>null</code> 
   *           <li><code>file</code> does not exists 
   *           <li><code>file</code> couldn't be moved 
   *           <li><code>file</code> is the repository folder or a file inside its hierarchy 
   *           <li><code>description</code> is invalid 
   *           <li><code>progressListener</code> is <code>null</code>
   *           </ul>
   */
  public MetaData add(File file, String description, boolean move,
          ProgressListener progressListener);

  /**
   * Deletes the data sets fulfilling the specified criteria. At least one
   * criterion has to be specified.
   * 
   * @param deletionCriteria
   *          Criteria of all data sets to be deleted have to fulfill.
   * @return meta data of deleted data sets sorted by time stamps. Empty list means
   *         nothing being deleted..
   * @throws IllegalArgumentException
   *           in the following cases:
   *           <ul>
   *           <li><code>deletionCriteria</code> is <code>null</code>
   *           <li>all five criteria are <code>null</code> (i.e. all attributes of
   *           <code>deletionCriteria</code> are <code>null</code>)
   *           <li><code>deletionCriteria.getId() != null</code> but no data set for the specified
   *           id exists
   *           </ul>
   */
  public List<MetaData> delete(Criteria deletionCriteria);

  /**
   * Exports the data sets fulfilling the specified criteria to the specified
   * target folder. If two data sets fulfilling the criteria have the same
   * name an IllegalStateException is thrown.
   * 
   * @param exportCriteria
   *          Criteria of all data sets to be exported have to fulfill.
   * @param target
   *          Destination folder for the data sets to be exported.
   * @param progressListener
   *          Progress callback object.
   * @return meta data of exported data sets sorted by time stamps. Empty list if
   *         nothing exported.
   * @throws IllegalArgumentException
   *           in the following cases:
   *           <ul>
   *           <li><code>exportCriteria</code> is <code>null</code>
   *           <li><code>target</code> is <code>null</code>
   *           <li><code>target</code> isn't a folder
   *           <li><code>exportCriteria.getId() != null</code> but no data set for 
   *           the specified id exists
   *           <li>at least two of the data sets to be exported have the same name
   *           <li>at the destination is already a file/folder with same name as one of the data
   *           sets to be exported
   *           <li><code>progressListener</code> is <code>null</code>
   *           </ul>
   */
  public List<MetaData> export(Criteria exportCriteria, File target,
          ProgressListener progressListener);

  /**
   * Replaces the data set specified by its id completely by the specified
   * file or folder. If the description isn't specified the old description is
   * kept.
   * 
   * @param id
   *          Identifier of the data set to be replaced.
   * @param file
   *          File or folder.
   * @param description
   *          An optional description of the data set. Can be <code>null</code>. If it is longer
   *          than 1000 characters or it
   *          contains ISO control characters an exception is thrown.
   * @param move
   *          If <code>true</code> the file/folder will be moved into the
   *          repository. This works only if file/folder and repository
   *          folder are on the same physical file system. If this is not
   *          the case an exception is thrown. If <code>false</code> the
   *          file/folder will be copied (deep copy) into the repository
   *          folder.
   * @param progressListener
   *          Progress callback object. If <code>move == true</code>
   *          {@link ProgressListener#progress(long, long)} is called twice. First,
   *          with <code>numberOfBytes == 0</code> and second
   *          with <code>numberOfBytes == totalNumberOfBytes</code>.
   * @return meta data of replaced data set with new number of files and total size.
   * @throws IllegalArgumentException
   *           in the following cases:
   *           <ul>
   *           <li>no data set for the specified id exists
   *           <li><code>file</code> is <code>null</code>
   *           <li><code>file</code> does not exists
   *           <li><code>file</code> couldn't be moved
   *           <li><code>file</code> is the repository folder or a file inside its hierarchy
   *           <li><code>description</code> is invalid
   *           <li><code>progressListener</code> is <code>null</code>
   *           </ul>
   */
  public MetaData replace(String id, File file, String description, boolean move,
          ProgressListener progressListener);

  /**
   * Returns a list of meta data of all data sets fulfilling the criteria. The
   * list is ordered by the time stamps.
   * 
   * @param searchCriteria
   *          Criteria of all data sets to be listed.
   * @return an empty list if no data set matches the criteria. List is sorted by time stamps.
   * @throws IllegalArgumentException
   *           if <code>searchCriteria</code> is <code>null</code>.
   */
  public List<MetaData> getMetaData(Criteria searchCriteria);
}
