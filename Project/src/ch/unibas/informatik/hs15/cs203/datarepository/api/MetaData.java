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

import java.util.Date;

/**
 * Meta data of a data set.
 * 
 * @author Franz-Josef Elmer
 */
public final class MetaData
{
  private final String id;

  private final String name;

  private final String description;

  private final int numberOfFiles;

  private final long size;

  private final Date timestamp;

  /**
   * Creates an instance for the specified identifier, name, description,
   * number of files, size (in bytes) and time stamp.
   * 
   * @param id
   *          Unique identifier of the data set in the repository.
   * @param name
   *          Name of the original file/folder defining the data set.
   * @param description
   *          Optional description. Can be <code>null</code>.
   * @param numberOfFiles
   *          Number of files.
   * @param size
   *          Total size in number of bytes.
   * @param timestamp
   *          Time stamp of the data set.
   * @throws IllegalArgumentException
   *           if <code>id</code>, <code>name</code> or <code>timestamp</code>are <code>null</code>.
   */
  public MetaData(String id, String name, String description, int numberOfFiles, long size,
          Date timestamp)
  {
    if (id == null || id.isEmpty())
    {
      throw new IllegalArgumentException("Unspecified identifier.");
    }
    if (name == null || name.isEmpty())
    {
      throw new IllegalArgumentException("Unspecified name.");
    }
    if (timestamp == null)
    {
      throw new IllegalArgumentException("Unspecified timestamp.");
    }
    this.id = id;
    this.name = name;
    this.description = description == null ? "" : description;
    this.numberOfFiles = numberOfFiles;
    this.size = size;
    this.timestamp = timestamp;
  }

  /**
   * Returns the unique identifier.
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the description.
   * 
   * @return an empty string if the description hasn't been specified.
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the number of files and folders.
   */
  public int getNumberOfFiles()
  {
    return numberOfFiles;
  }

  /**
   * Returns the total size of all files in bytes.
   */
  public long getSize()
  {
    return size;
  }

  /**
   * Returns the time stamp of the data set.
   */
  public Date getTimestamp()
  {
    return timestamp;
  }
  
  /**
   * Output of all Attributes
   */
  @Override
  public String toString(){
	  return id+", "+name+", "+description+", "+numberOfFiles+", "+size+", "+timestamp.toString();
  }

}
