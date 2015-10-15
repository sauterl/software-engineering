package ch.unibas.informatik.hs15.cs203.datarepository.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

public class AddTest extends APITestCase
{
  
	@Test
  public void testAddSingleFile()
  {
    File file = new File(workingDir, "example.txt");
    String[] data = Utils.createExampleData(file, ":hello world!");

    MetaData metaData =  dataRepository.add(file, "example file", false, progressListener);
    
    assertTrue(metaData != null);
    assertTrue(metaData.getId() != null);
    assertTrue(metaData.getId().trim().length() > 0);
    assertEquals("example.txt", metaData.getName());
    assertEquals("example file", metaData.getDescription());
    assertEquals(1, metaData.getNumberOfFiles());
    assertEquals(12, metaData.getSize());
    assertTimestamp(metaData.getTimestamp());
    assertContent(data, file);
    assertNoProgressError();
  }
  
  @Test
  public void testAddSameFileTwice()
  {
    File file = new File(workingDir, "example.txt");
    String[] data = Utils.createExampleData(file, ":hello world!");
    MetaData metaData1 =  dataRepository.add(file, "example file", false, progressListener);
    assertNoProgressError();
    progressListener = new MockProgressListener();
    
    MetaData metaData2 =  dataRepository.add(file, "example file 2", false, progressListener);
    
    assertEquals("example.txt", metaData2.getName());
    assertEquals("example file 2", metaData2.getDescription());
    assertEquals(1, metaData2.getNumberOfFiles());
    assertEquals(12, metaData2.getSize());
    assertTimestamp(metaData2.getTimestamp());
    assertFalse(metaData1.getId().equals(metaData2.getId()));
    assertContent(data, file);
    assertNoProgressError();
  }
  
  @Test
  public void testMoveSingleFile()
  {
    File file = new File(workingDir, "example.txt");
    Utils.createExampleData(file, ":hello world!");
    
    MetaData metaData =  dataRepository.add(file, "example file", true, progressListener);
    
    assertEquals("example.txt", metaData.getName());
    assertEquals("example file", metaData.getDescription());
    assertEquals(1, metaData.getNumberOfFiles());
    assertEquals(12, metaData.getSize());
    assertTimestamp(metaData.getTimestamp());
    assertEquals(false, file.exists());
    assertNoProgressError();
  }
  
  @Test
  public void testAddFolder()
  {
    File folder = new File(workingDir, "my-data");
    String[] data = Utils.createExampleData(folder,
            "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(1234567),
            "/data/2.dat:" + Utils.createExampleContent(7654321));
    
    MetaData metaData =  dataRepository.add(folder, null, false, progressListener);
    
    assertEquals("my-data", metaData.getName());
    assertEquals("", metaData.getDescription());
    assertEquals(5, metaData.getNumberOfFiles());
    assertEquals(8888888 + 12, metaData.getSize());
    assertTimestamp(metaData.getTimestamp());
    assertContent(data, folder);
    assertNoProgressError();
  }
  
  @Test
  public void testMoveFolder()
  {
    File folder = new File(workingDir, "my-data");
    Utils.createExampleData(folder,
            "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(12345),
            "/data/2.dat:" + Utils.createExampleContent(54321));
    String description = Utils.createExampleContent(1000);
    
    MetaData metaData =  dataRepository.add(folder, description, true, progressListener);
    
    assertEquals("my-data", metaData.getName());
    assertEquals(description, metaData.getDescription());
    assertEquals(5, metaData.getNumberOfFiles());
    assertEquals(66678, metaData.getSize());
    assertTimestamp(metaData.getTimestamp());
    assertEquals(false, folder.exists());
    assertNoProgressError();
  }
  
  @Test
  public void testAddUnspecifiedFile()
  {
    assertInvalidAddArguments(null, null, false, progressListener);
  }
  
  @Test
  public void testAddNonExistingFile()
  {
    assertInvalidAddArguments(new File(workingDir, "blabla"), null, false, progressListener);
  }
  
  @Test
  public void testAddRepositoryItself()
  {
    repository.mkdirs();
    assertInvalidAddArguments(repository, null, false, progressListener);
  }
  
  @Test
  public void testAddFileInRepository()
  {
    File dirInRepo = new File(repository, "subdir");
    dirInRepo.mkdirs();
    File fileInRepo = new File(dirInRepo, "hi.txt");
    Utils.writeTo(fileInRepo, "hi");
    
    assertInvalidAddArguments(fileInRepo, null, false, progressListener);
  }
  
  @Test
  public void testAddWithDescriptionTooLong()
  {
    File file = new File(workingDir, "example.txt");
    Utils.createExampleData(file, ":hello world!");
    
    assertInvalidAddArguments(file, Utils.createExampleContent(1001), false, progressListener);
  }
  
  @Test
  public void testAddWithDescriptionWithTabCharacter()
  {
    File file = new File(workingDir, "example.txt");
    Utils.createExampleData(file, ":hello world!");
    
    assertInvalidAddArguments(file, "hello \t", false, progressListener);
  }
  
  @Test
  public void testAddWithDescriptionWithNewLineCharacter()
  {
    File file = new File(workingDir, "example.txt");
    Utils.createExampleData(file, ":hello world!");
    
    assertInvalidAddArguments(file, "hello \n", false, progressListener);
  }
  
  @Test
  public void testAddUnspecifiedProgressListener()
  {
    File file = new File(workingDir, "example.txt");
    Utils.createExampleData(file, ":hello world!");
    
    assertInvalidAddArguments(new File(workingDir, "blabla"), null, false, null);
  }
  
  private void assertInvalidAddArguments(File file, String description, boolean move,
          ProgressListener progressListener)
  {
    try
    {
      dataRepository.add(file, description, move, progressListener);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e)
    {
      System.out.println("IllegalArgumentException message [" + name.getMethodName() + "]: " 
              + e.getMessage());
      assertNoProgressError();
    }
  }
}
