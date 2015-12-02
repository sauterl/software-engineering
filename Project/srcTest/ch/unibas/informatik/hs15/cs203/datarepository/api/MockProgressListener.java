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

import java.util.ArrayList;
import java.util.List;

class MockProgressListener implements ProgressListener
{
  private abstract static class State
  {
    State start(List<String> recorder)
    {
      recorder.add("ERROR: ProgressListener.start() has already been invoked.");
      return this;
    }

    State progress(long numberOfBytes, long totalNumberOfBytes, List<String> recorder)
    {
      if (numberOfBytes < 0)
      {
        recorder.add("ERROR: ProgressListener.progress(" + numberOfBytes + ", "
                + totalNumberOfBytes + ") called with negative first argument.");
      }
      if (totalNumberOfBytes < 0)
      {
        recorder.add("ERROR: ProgressListener.progress(" + numberOfBytes + ", "
                + totalNumberOfBytes + ") called with negative second argument.");
      }
      if (totalNumberOfBytes < numberOfBytes)
      {
        recorder.add("ERROR: ProgressListener.progress(" + numberOfBytes + ", "
                + totalNumberOfBytes + ") called with first argument larger "
                + "then the second argument.");
      }
      return this;
    }
    
    State hasCancelBeenRequested(List<String> recorder, boolean cancelBeenRequested)
    {
      return cancelBeenRequested ? new CancelBeenRequestedState() : this;
    }

    State canceled(List<String> recorder)
    {
      recorder.add("ERROR: ProgressListener.hasCancelBeeRrequested() hasn't been invoked before "  
              + "ProgressListener.canceled().");
      return this;
    }

    abstract State finish(List<String> recorder);

  }

  private static final class InitialState extends State
  {
    @Override
    State start(List<String> recorder)
    {
      return new StartedState();
    }

    @Override
    State progress(long numberOfBytes, long totalNumberOfBytes, List<String> recorder)
    {
      recorder.add("ERROR: ProgressListener.start() hasn't been invoked before "
              + "ProgressListener.progress(" + numberOfBytes + ", " + totalNumberOfBytes + ").");
      return this;
    }

    @Override
    State hasCancelBeenRequested(List<String> recorder, boolean cancelBeenRequested)
    {
      recorder.add("ERROR: ProgressListener.start() hasn't been invoked before "
              + "ProgressListener.hasCancelBeenRequested().");
      return this;
    }

    @Override
    State canceled(List<String> recorder)
    {
      recorder.add("ERROR: ProgressListener.start() hasn't been invoked before "
              + "ProgressListener.canceled().");
      return this;
    }
    
    @Override
    State finish(List<String> recorder)
    {
      recorder.add("ERROR: ProgressListener.start() hasn't been invoked before "
              + "ProgressListener.finish().");
      return this;
    }
  }

  private static final class StartedState extends State
  {
    @Override
    State progress(long numberOfBytes, long totalNumberOfBytes, List<String> recorder)
    {
      super.progress(numberOfBytes, totalNumberOfBytes, recorder);
      if (numberOfBytes > 0)
      {
        recorder.add("ERROR: For the first invocation of ProgressListener.progress() "
                + "the argument 'numberOfBytes' has to be 0 instead of " + numberOfBytes);
      }
      return new ProgressState(numberOfBytes, totalNumberOfBytes);
    }

    @Override
    State finish(List<String> recorder)
    {
      recorder.add("ERROR: ProgressListener.finish() should not been invoked right after "
              + "ProgressListener.start()");
      return this;
    }
  }

  private static final class ProgressState extends State
  {
    private long _lastNumberOfBytes;

    private long _lastTotalNumberOfBytes;

    ProgressState(long numberOfBytes, long totalNumberOfBytes)
    {
      _lastNumberOfBytes = numberOfBytes;
      _lastTotalNumberOfBytes = totalNumberOfBytes;
    }

    @Override
    State progress(long numberOfBytes, long totalNumberOfBytes, List<String> recorder)
    {
      super.progress(numberOfBytes, totalNumberOfBytes, recorder);
      if (numberOfBytes < _lastNumberOfBytes)
      {
        recorder.add("ERROR: ProgressListener.progress(" + numberOfBytes + ", "
                + totalNumberOfBytes + ") invalid because the first argument has to equals "
                + "or greater than " + _lastNumberOfBytes);

      }
      if (totalNumberOfBytes != _lastTotalNumberOfBytes)
      {
        recorder.add("ERROR: ProgressListener.progress(" + numberOfBytes + ", "
                + totalNumberOfBytes + ") invalid because the second argument has to be "
                + _lastTotalNumberOfBytes);
      }
      return new ProgressState(numberOfBytes, totalNumberOfBytes);
    }

    @Override
    State finish(List<String> recorder)
    {
      if (_lastNumberOfBytes < _lastTotalNumberOfBytes)
      {
        recorder.add("ERROR: ProgressListener.finished() not allowed because "
                + "last number of bytes (" + _lastNumberOfBytes + ") was less than the "
                + "total number of bytes (" + _lastTotalNumberOfBytes + ").");
      }
      return new FinishedState();
    }
  }
  
  private static abstract class NotAllowedState extends State
  {
    private final String notAllowed;
    NotAllowedState(String method)
    {
      notAllowed = "not allowed because ProgressListener." + method + " has already been invoked.";
    }
    @Override
    State progress(long numberOfBytes, long totalNumberOfBytes, List<String> recorder)
    {
      recorder.add("ERROR: ProgressListener.progress(" + numberOfBytes + ", "
              + totalNumberOfBytes + ") " + notAllowed);
      return this;
    }

    @Override
    State hasCancelBeenRequested(List<String> recorder, boolean cancelBeenRequested)
    {
      recorder.add("ERROR: ProgressListener.hasCancelBeenRequested() " + notAllowed);
      return this;
    }

    @Override
    State canceled(List<String> recorder)
    {
      recorder.add("ERROR: ProgressListener.canceled() " + notAllowed);
      return this;
    }

    @Override
    State finish(List<String> recorder)
    {
      recorder.add("ERROR: ProgressListener.finish() " + notAllowed);
      return this;
    }
  }

  private static final class CancelBeenRequestedState extends NotAllowedState
  {
    CancelBeenRequestedState()
    {
      super("hasCancelBeenRequested()");
    }

    @Override
    State canceled(List<String> recorder)
    {
      return new CanceledState();
    }
  }
  
  private static final class CanceledState extends NotAllowedState
  {

    CanceledState()
    {
      super("canceled()");
    }
  }
  
  private static final class FinishedState extends NotAllowedState
  {
    FinishedState()
    {
      super("finish()");
    }
  }
  
  private final CancelRequestCriteria _cancelRequestCriteria;
  private int _numberOfCancelRequestChecks;
  private long _numberOfBytes;
  private long _totalNumberOfBytes;
  private State _state = new InitialState();
  private List<String> _recorder = new ArrayList<String>();
  
  MockProgressListener()
  {
    this(new CancelRequestCriteria()
      {
        @Override
        public boolean cancelRequested(int numberOfCancelRequestChecks, long numberOfBytes,
                long totalNumberOfBytes)
        {
          return false;
        }
      });
  }
  
  MockProgressListener(final int numberOfTimesNoCancelHasBeenRequested)
  {
    this(new CancelRequestCriteria()
    {
      @Override
      public boolean cancelRequested(int numberOfCancelRequestChecks, long numberOfBytes,
              long totalNumberOfBytes)
      {
        return numberOfCancelRequestChecks > numberOfTimesNoCancelHasBeenRequested;
      }
    });
  }
  
  
  
  MockProgressListener(CancelRequestCriteria cancelRequestCriteria)
  {
    _cancelRequestCriteria = cancelRequestCriteria;
  }

  @Override
  public void start()
  {
    _recorder.add("start()");
    _state = _state.start(_recorder);
  }

  @Override
  public void progress(long numberOfBytes, long totalNumberOfBytes)
  {
    _numberOfBytes = numberOfBytes;
    _totalNumberOfBytes = totalNumberOfBytes;
    _recorder.add("progress(" + numberOfBytes + ", " + totalNumberOfBytes + ")");
    _state = _state.progress(numberOfBytes, totalNumberOfBytes, _recorder);
  }

  @Override
  public boolean hasCancelBeenRequested()
  {
    _recorder.add("hasCancelBeenRequested()");
    boolean cancelBeenRequested 
        = _cancelRequestCriteria.cancelRequested(++_numberOfCancelRequestChecks, 
                _numberOfBytes, _totalNumberOfBytes);
    _state = _state.hasCancelBeenRequested(_recorder, cancelBeenRequested);
    return cancelBeenRequested;
  }

  @Override
  public void canceled()
  {
    _recorder.add("canceled()");
    _state = _state.canceled(_recorder);
  }

  @Override
  public void finish()
  {
    _recorder.add("finish()");
    _state = _state.finish(_recorder);
  }
  
  void assertInitialState()
  {
    Class<? extends State> stateClass = _state.getClass();
    if (stateClass.equals(InitialState.class) == false)
    {
      throw new AssertionError("Not initial state: " + _state);
    }
  }

  void assertCanceledState()
  {
    Class<? extends State> stateClass = _state.getClass();
    if (stateClass.equals(CanceledState.class) == false)
    {
      throw new AssertionError("Not canceled: " + _state);
    }
  }
  
  void assertFinishedState()
  {
    Class<? extends State> stateClass = _state.getClass();
    if (stateClass.equals(FinishedState.class) == false)
    {
      throw new AssertionError("Not finished: " + _state);
    }
  }
  
  void assertNoErrors()
  {
    StringBuilder builder = new StringBuilder();
    boolean hasError = appendTo(builder);
    if (hasError)
    {
      throw new AssertionError(builder.toString());
    }
  }
  
  String getRecording()
  {
    StringBuilder builder = new StringBuilder();
    appendTo(builder);
    return builder.toString();
  }

  private boolean appendTo(StringBuilder builder)
  {
    boolean hasError = false;
    for (String message : _recorder)
    {
      builder.append(message).append('\n');
      if (message.startsWith("ERROR: "))
      {
        hasError = true;
      }
    }
    return hasError;
  }
}
