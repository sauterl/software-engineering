package ch.unibas.informatik.hs15.cs203.datarepository.api;

import static org.junit.Assert.*;

import org.junit.Test;

public class MockProgressListenerTest
{
  @Test
  public void testNormal()
  {
    MockProgressListener progressListener = new MockProgressListener();

    progressListener.start();
    progressListener.progress(0, 10);
    progressListener.progress(2, 10);
    progressListener.progress(2, 10);
    progressListener.progress(10, 10);
    progressListener.finish();

    progressListener.assertNoErrors();
  }

  @Test
  public void testMinimum()
  {
    MockProgressListener progressListener = new MockProgressListener();

    progressListener.start();
    progressListener.progress(0, 10);
    progressListener.progress(10, 10);
    progressListener.finish();

    progressListener.assertNoErrors();
  }

  @Test
  public void testCancelBeforeFirstProgress()
  {
    MockProgressListener progressListener = new MockProgressListener(1);
    
    progressListener.start();
    progressListener.hasCancelBeenRequested();
    progressListener.hasCancelBeenRequested();
    progressListener.canceled();
    
    progressListener.assertNoErrors();
  }
  
  @Test
  public void testCancel()
  {
    MockProgressListener progressListener = new MockProgressListener(1);
    
    progressListener.start();
    progressListener.progress(0, 10);
    progressListener.progress(10, 10);
    progressListener.hasCancelBeenRequested();
    progressListener.hasCancelBeenRequested();
    progressListener.canceled();
    
    progressListener.assertNoErrors();
  }
  
  @Test
  public void testProgressBeforeStart()
  {
    MockProgressListener progressListener = new MockProgressListener();

    progressListener.progress(0, 10);

    assertEquals("progress(0, 10)\nERROR: ProgressListener.start() hasn't been invoked "
            + "before ProgressListener.progress(0, 10).", progressListener.getRecording().trim());
  }

  @Test
  public void testFinishBeforeStart()
  {
    MockProgressListener progressListener = new MockProgressListener();

    progressListener.finish();

    assertEquals("finish()\nERROR: ProgressListener.start() hasn't been invoked before "
            + "ProgressListener.finish().", progressListener.getRecording().trim());
  }

  @Test
  public void testStartTwice()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();

    progressListener.start();

    assertEquals("start()\nstart()\nERROR: ProgressListener.start() has already been invoked.",
            progressListener.getRecording().trim());
  }

  @Test
  public void testProgressWithNegativeNumberOfBytes()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();

    progressListener.progress(-1, 0);

    assertEquals("start()\nprogress(-1, 0)\n"
            + "ERROR: ProgressListener.progress(-1, 0) called with negative first argument.",
            progressListener.getRecording().trim());
  }

  @Test
  public void testProgressWithNegativeTotalNumberOfBytes()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();

    progressListener.progress(0, -1);

    assertEquals("start()\nprogress(0, -1)\n"
            + "ERROR: ProgressListener.progress(0, -1) called with negative second argument.\n"
            + "ERROR: ProgressListener.progress(0, -1) called with first argument larger then "
            + "the second argument.",
            progressListener.getRecording().trim());
  }

  @Test
  public void testProgressWithNumberOfBytesGreaterThanTotalNumberOfBytes()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();

    progressListener.progress(10, 9);

    assertEquals("start()\nprogress(10, 9)\n"
            + "ERROR: ProgressListener.progress(10, 9) called with first argument "
            + "larger then the second argument.\n"
            + "ERROR: For the first invocation of ProgressListener.progress() the "
            + "argument 'numberOfBytes' has to be 0 instead of 10",
            progressListener.getRecording().trim());
  }

  @Test
  public void testFirstProgressWithNumberOfBytesGreaterThanZero()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();

    progressListener.progress(1, 9);

    assertEquals("start()\nprogress(1, 9)\n"
            + "ERROR: For the first invocation of ProgressListener.progress() the argument "
            + "'numberOfBytes' has to be 0 instead of 1", progressListener.getRecording().trim());
  }

  @Test
  public void testFinishRightAfterStart()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();

    progressListener.finish();
    assertEquals("start()\nfinish()\n"
            + "ERROR: ProgressListener.finish() should not been invoked right after "
            + "ProgressListener.start()", progressListener.getRecording().trim());
  }

  @Test
  public void testProgressWithNumberOfBytesLessThanPreviousValue()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();
    progressListener.progress(0, 10);
    progressListener.progress(2, 10);

    progressListener.progress(1, 10);

    assertEquals("start()\nprogress(0, 10)\nprogress(2, 10)\nprogress(1, 10)\n"
            + "ERROR: ProgressListener.progress(1, 10) invalid because the first argument "
            + "has to equals or greater than 2", progressListener.getRecording().trim());
  }

  @Test
  public void testProgressWithTotalNumberOfBytesUnequalThanPreviousValue()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();
    progressListener.progress(0, 10);

    progressListener.progress(1, 9);

    assertEquals("start()\nprogress(0, 10)\nprogress(1, 9)\n"
            + "ERROR: ProgressListener.progress(1, 9) invalid because the second argument "
            + "has to be 10", progressListener.getRecording().trim());
  }

  @Test
  public void testFinishBeforeLastProgress()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();
    progressListener.progress(0, 10);
    progressListener.progress(8, 10);

    progressListener.finish();

    assertEquals("start()\nprogress(0, 10)\nprogress(8, 10)\nfinish()\n"
            + "ERROR: ProgressListener.finished() not allowed because "
            + "last number of bytes (8) was less than the total number of bytes (10).",
            progressListener.getRecording().trim());
  }

  @Test
  public void testProgressAfterFinish()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();
    progressListener.progress(0, 10);
    progressListener.progress(10, 10);
    progressListener.finish();

    progressListener.progress(10, 10);

    assertEquals("start()\nprogress(0, 10)\nprogress(10, 10)\nfinish()\nprogress(10, 10)\n"
            + "ERROR: ProgressListener.progress(10, 10) not allowed because "
            + "ProgressListener.finish() has already been invoked.", progressListener
            .getRecording().trim());
  }

  @Test
  public void testFinishTwice()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();
    progressListener.progress(0, 0);
    progressListener.finish();
    
    progressListener.finish();
    
    assertEquals("start()\nprogress(0, 0)\nfinish()\nfinish()\n"
            + "ERROR: ProgressListener.finish() not allowed because ProgressListener.finish() has "
            + "already been invoked.", progressListener.getRecording().trim());
  }

  @Test
  public void testCanceledAfterFinish()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();
    progressListener.progress(0, 0);
    progressListener.finish();
    
    progressListener.canceled();
    
    assertEquals("start()\nprogress(0, 0)\nfinish()\ncanceled()\n"
            + "ERROR: ProgressListener.canceled() not allowed because ProgressListener.finish() has "
            + "already been invoked.", progressListener.getRecording().trim());
  }
  
  @Test
  public void testHasCancelBeenRequestedAfterFinish()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();
    progressListener.progress(0, 0);
    progressListener.finish();
    
    progressListener.hasCancelBeenRequested();
    
    assertEquals("start()\nprogress(0, 0)\nfinish()\nhasCancelBeenRequested()\n"
            + "ERROR: ProgressListener.hasCancelBeenRequested() not allowed because ProgressListener.finish() has "
            + "already been invoked.", progressListener.getRecording().trim());
  }
  
  @Test
  public void testHasProgressAfterFinish()
  {
    MockProgressListener progressListener = new MockProgressListener();
    progressListener.start();
    progressListener.progress(0, 0);
    progressListener.finish();
    
    progressListener.progress(0, 0);
    
    assertEquals("start()\nprogress(0, 0)\nfinish()\nprogress(0, 0)\n"
            + "ERROR: ProgressListener.progress(0, 0) not allowed because ProgressListener.finish() has "
            + "already been invoked.", progressListener.getRecording().trim());
  }
  
  @Test
  public void testCanceledWithoutCancelBeenRequestedTwice()
  {
    MockProgressListener progressListener = new MockProgressListener(1);
    progressListener.start();
    progressListener.progress(0, 0);
    progressListener.hasCancelBeenRequested();

    progressListener.canceled();
    
    assertEquals("start()\nprogress(0, 0)\nhasCancelBeenRequested()\ncanceled()\n"
            + "ERROR: ProgressListener.hasCancelBeeRrequested() hasn't been invoked "
            + "before ProgressListener.canceled().", progressListener.getRecording().trim());
  }
  
  @Test
  public void testCanceledTwice()
  {
    MockProgressListener progressListener = new MockProgressListener(1);
    progressListener.start();
    progressListener.progress(0, 0);
    progressListener.hasCancelBeenRequested();
    progressListener.hasCancelBeenRequested();
    progressListener.canceled();
    
    progressListener.canceled();
    
    assertEquals("start()\nprogress(0, 0)\nhasCancelBeenRequested()\nhasCancelBeenRequested()\ncanceled()\ncanceled()\n"
            + "ERROR: ProgressListener.canceled() not allowed because ProgressListener.canceled() has "
            + "already been invoked.", progressListener.getRecording().trim());
  }
  
  @Test
  public void testHasCancelBeenRequestedAfterCanceled()
  {
    MockProgressListener progressListener = new MockProgressListener(1);
    progressListener.start();
    progressListener.progress(0, 0);
    progressListener.hasCancelBeenRequested();
    progressListener.hasCancelBeenRequested();
    progressListener.canceled();
    
    progressListener.hasCancelBeenRequested();
    
    assertEquals("start()\nprogress(0, 0)\nhasCancelBeenRequested()\nhasCancelBeenRequested()\ncanceled()\nhasCancelBeenRequested()\n"
            + "ERROR: ProgressListener.hasCancelBeenRequested() not allowed because ProgressListener.canceled() has "
            + "already been invoked.", progressListener.getRecording().trim());
  }
  
  @Test
  public void testHasCancelBeenRequestedBeforeStart()
  {
    MockProgressListener progressListener = new MockProgressListener(1);

    progressListener.hasCancelBeenRequested();
    
    assertEquals("hasCancelBeenRequested()\n"
            + "ERROR: ProgressListener.start() hasn't been invoked before ProgressListener.hasCancelBeenRequested().", 
            progressListener.getRecording().trim());
  }
  
  @Test
  public void testCanceledBeforeStart()
  {
    MockProgressListener progressListener = new MockProgressListener(1);
    
    progressListener.canceled();
    
    assertEquals("canceled()\n"
            + "ERROR: ProgressListener.start() hasn't been invoked before ProgressListener.canceled().", 
            progressListener.getRecording().trim());
  }
}

