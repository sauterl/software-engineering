/*
 * Copyright 2014 Franz-Josef Elmer
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

import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;

class DummyProgressListener implements ProgressListener
{

  @Override
  public void start()
  {
  }

  @Override
  public void progress(long numberOfBytes, long totalNumberOfBytes)
  {
  }

  @Override
  public void finish()
  {
  }

}
