package ch.unibas.informatik.hs15.cs203.datarepository.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import ch.unibas.informatik.hs15.cs203.datarepository.processing.Factory;

public abstract class APITestCase
{
  protected File workingDir;
  protected File repository;
  protected File target;
  protected DataRepository dataRepository;
  protected MockProgressListener progressListener;
  private Date startTime;
  
  @Rule public TestName name = new TestName();
  
  @Before
  public void setUp() throws Exception
  {
    workingDir = new File("test-working-dir");
    Utils.delete(workingDir);
    assertEquals(false, workingDir.exists());
    repository = new File(new File(workingDir, "subdir"), "repository");
    dataRepository = Factory.create(repository);
    progressListener = new MockProgressListener();
    target = new File(workingDir, "target");
    target.mkdirs();
    startTime = new Date();
  }
  
  protected void assertMetaData(MetaData expectedMetaData, MetaData metaData)
  {
    assertEquals(expectedMetaData.getDescription(), metaData.getDescription());
    assertEquals(expectedMetaData.getId(), metaData.getId());
    assertEquals(expectedMetaData.getName(), metaData.getName());
    assertEquals(expectedMetaData.getNumberOfFiles(), metaData.getNumberOfFiles());
    assertEquals(expectedMetaData.getSize(), metaData.getSize());
    assertEquals(expectedMetaData.getTimestamp(), metaData.getTimestamp());
  }
  
  protected void assertContent(String[] expectedPathsAndContents, File fileOrFolder)
  {
      Set<String> paths = new HashSet<String>();
      gatherFilePaths(fileOrFolder, paths);
      for (String expectedPathAndContent : expectedPathsAndContents)
      {
          int indexOfColon = expectedPathAndContent.indexOf(':');
          String expectedPath = expectedPathAndContent.substring(0,
                  indexOfColon);
          String expectedContent = expectedPathAndContent
                  .substring(indexOfColon + 1);
          File file = fileOrFolder;
          if (expectedPath.isEmpty() == false)
          {
              file = new File(fileOrFolder, expectedPath);
          }
          if (paths.remove(file.getAbsolutePath()) == false)
          {
              fail("Unexpected content file: " + file);
          }
          assertEquals(expectedContent, Utils.getContentOf(file));
      }
      if (paths.isEmpty() == false)
      {
          fail("Unexpected files: " + paths);
      }
  }

  protected void assertTimestamp(Date timestamp)
  {
    assertTrue("Time stamp '" + timestamp + "' before start time '" + startTime + "'.", 
            timestamp.getTime() >= startTime.getTime());
    Date now = new Date();
    assertTrue("Time stamp '" + timestamp + "' after current time '" + now + "'.", 
            now.getTime() >= timestamp.getTime());
  }
  
  protected void assertNoProgressErrorAndInitialState()
  {
    progressListener.assertNoErrors();
    progressListener.assertInitialState();
  }

  protected void assertNoProgressErrorAndFinishedState()
  {
    progressListener.assertNoErrors();
    progressListener.assertFinishedState();
  }
  
  protected DataSet createDataSet(String rootPath, String description, String... pathsAndContents)
  {
    File dataSetFile = new File(workingDir, rootPath);
    Utils.createExampleData(dataSetFile, pathsAndContents);
    MetaData metaData = dataRepository.add(dataSetFile, description, true, new DummyProgressListener());
    return new DataSet(metaData, pathsAndContents);
  }
  
  private void gatherFilePaths(File file, Set<String> paths)
  {
      if (file.isDirectory())
      {
          File[] files = file.listFiles();
          for (File child : files)
          {
              gatherFilePaths(child, paths);
          }
      } else
      {
          paths.add(file.getAbsolutePath());
      }
  }

  protected void printErrorMessageAndAssertNoProgressError(IllegalArgumentException e)
  {
    System.out.println("IllegalArgumentException message [" + name.getMethodName() + "]: " 
            + e.getMessage());
    progressListener.assertNoErrors();
  }
  
  protected void printErrorMessageAndAssertNoProgressErrorAndInitialState(IllegalArgumentException e)
  {
    System.out.println("IllegalArgumentException message [" + name.getMethodName() + "]: " 
            + e.getMessage());
    assertNoProgressErrorAndInitialState();
  }

  protected void assertDataSetNames(List<MetaData> metaDataList, String... expectedNames)
  {
    List<String> names = new ArrayList<String>();
    for (MetaData metaData : metaDataList)
    {
      names.add(metaData.getName());
    }
    assertEquals(Arrays.asList(expectedNames).toString(), names.toString());
  }
}
