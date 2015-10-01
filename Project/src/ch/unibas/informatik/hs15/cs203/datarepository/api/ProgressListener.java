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

/**
 * Callback interface reporting progress on copying operations.
 * 
 * @author Franz-Josef Elmer
 *
 */
public interface ProgressListener
{
  /**
   * Starts operation. Should be invoked immediately after the parameters of the operation
   * have been validated.
   */
  public void start();

  /**
   * Informs about the amount of data already being copied. This method is invoked the first
   * time (with <code>numberOfBytes == 0</code>) after invocation of {@link #start()} if the total
   * number of bytes to be copied is known. In the last invocation
   * <code>numberOfBytes == totalNumberOfBytes</code>.
   * 
   * @param numberOfBytes
   *          Number of bytes already copied.
   * @param totalNumberOfBytes
   *          Total number of bytes to be copied.
   */
  public void progress(long numberOfBytes, long totalNumberOfBytes);

  /**
   * Finishes operation. Should be invoked immediately before the operation is finished.
   */
  public void finish();
}
