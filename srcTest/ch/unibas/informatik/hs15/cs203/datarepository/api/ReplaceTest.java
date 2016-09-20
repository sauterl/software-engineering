package ch.unibas.informatik.hs15.cs203.datarepository.api;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class ReplaceTest extends APITestCase
{
  @Test
  public void testReplaceSingleFileDataSetByAnotherSingleFileDataSetCopiedAndDescriptionNotChanged()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", ":hello test!");
    MetaData metaData = dataSet.getMetaData();
    File dataSetFile = new File(workingDir, "hello.txt");
    String[] pathsAndContents = Utils.createExampleData(dataSetFile, ":hello second data set");
    Utils.delay(1100);
    setStartTimeToNow();

    MetaData metaData2 = dataRepository.replace(metaData.getId(), dataSetFile, null, false, progressListener);
    
    assertEquals(metaData.getDescription(), metaData2.getDescription());
    assertEquals(metaData.getId(), metaData2.getId());
    assertEquals("hello.txt", metaData2.getName());
    assertEquals(1, metaData2.getNumberOfFiles());
    assertEquals(21, metaData2.getSize());
    assertTimestamp(metaData2.getTimestamp());
    assertContent(pathsAndContents, dataSetFile);
    dataRepository.export(Criteria.forId(metaData.getId()), target, new DummyProgressListener());
    assertContent(pathsAndContents, new File(target, metaData2.getName()));
    assertNoProgressErrorAndFinishedState();
  }
  
  @Test
  public void testReplaceSingleFileDataSetByAnotherSingleFileDataSetMovedAndDescriptionNotChanged()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", ":hello test!");
    MetaData metaData = dataSet.getMetaData();
    File dataSetFile = new File(workingDir, "hello.txt");
    String[] pathsAndContents = Utils.createExampleData(dataSetFile, ":hello second data set");
    Utils.delay(1100);
    setStartTimeToNow();
    
    MetaData metaData2 = dataRepository.replace(metaData.getId(), dataSetFile, null, true, progressListener);
    
    assertEquals(metaData.getDescription(), metaData2.getDescription());
    assertEquals(metaData.getId(), metaData2.getId());
    assertEquals("hello.txt", metaData2.getName());
    assertEquals(1, metaData2.getNumberOfFiles());
    assertEquals(21, metaData2.getSize());
    assertTimestamp(metaData2.getTimestamp());
    assertEquals(false, dataSetFile.exists());
    dataRepository.export(Criteria.forId(metaData.getId()), target, new DummyProgressListener());
    assertContent(pathsAndContents, new File(target, metaData2.getName()));
    assertNoProgressErrorAndFinishedState();
  }
  
  @Test
  public void testReplaceSingleFileDataSetByMultiFileDataSetCopiedAndDescriptionChanged()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", ":hello test!");
    MetaData metaData = dataSet.getMetaData();
    File dataSetFile = new File(workingDir, "data");
    String[] pathsAndContents = Utils.createExampleData(dataSetFile, "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(1234567));
    Utils.delay(1100);
    setStartTimeToNow();
    
    MetaData metaData2 = dataRepository.replace(metaData.getId(), dataSetFile, "replaced", false, progressListener);
    
    assertEquals("replaced", metaData2.getDescription());
    assertEquals(metaData.getId(), metaData2.getId());
    assertEquals("data", metaData2.getName());
    assertEquals(4, metaData2.getNumberOfFiles());
    assertEquals(1234579, metaData2.getSize());
    assertTimestamp(metaData2.getTimestamp());
    assertContent(pathsAndContents, dataSetFile);
    dataRepository.export(Criteria.forId(metaData.getId()), target, new DummyProgressListener());
    assertContent(pathsAndContents, new File(target, metaData2.getName()));
    assertNoProgressErrorAndFinishedState();
  }
  
  @Test
  public void testReplaceSingleFileDataSetByMultiFileDataSetMovedAndDescriptionChanged()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", ":hello test!");
    MetaData metaData = dataSet.getMetaData();
    File dataSetFile = new File(workingDir, "data");
    String[] pathsAndContents = Utils.createExampleData(dataSetFile, "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(1234567));
    Utils.delay(1100);
    setStartTimeToNow();
    
    MetaData metaData2 = dataRepository.replace(metaData.getId(), dataSetFile, "replaced", true, progressListener);
    
    assertEquals("replaced", metaData2.getDescription());
    assertEquals(metaData.getId(), metaData2.getId());
    assertEquals("data", metaData2.getName());
    assertEquals(4, metaData2.getNumberOfFiles());
    assertEquals(1234579, metaData2.getSize());
    assertTimestamp(metaData2.getTimestamp());
    assertEquals(false, dataSetFile.exists());
    dataRepository.export(Criteria.forId(metaData.getId()), target, new DummyProgressListener());
    assertContent(pathsAndContents, new File(target, metaData2.getName()));
    assertNoProgressErrorAndFinishedState();
  }
  
  @Test
  public void testReplaceMultiFileDataSetBySingleFileDataSetCopiedAndDescriptionChanged()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(1234567));
    MetaData metaData = dataSet.getMetaData();
    File dataSetFile = new File(workingDir, "data");
    String[] pathsAndContents = Utils.createExampleData(dataSetFile, ":hello test!");
    Utils.delay(1100);
    setStartTimeToNow();
    
    MetaData metaData2 = dataRepository.replace(metaData.getId(), dataSetFile, "replaced", false, progressListener);
    
    assertEquals("replaced", metaData2.getDescription());
    assertEquals(metaData.getId(), metaData2.getId());
    assertEquals("data", metaData2.getName());
    assertEquals(1, metaData2.getNumberOfFiles());
    assertEquals(11, metaData2.getSize());
    assertTimestamp(metaData2.getTimestamp());
    assertContent(pathsAndContents, dataSetFile);
    dataRepository.export(Criteria.forId(metaData.getId()), target, new DummyProgressListener());
    assertContent(pathsAndContents, new File(target, metaData2.getName()));
    assertNoProgressErrorAndFinishedState();
  }
  
  @Test
  public void testReplaceMultiFileDataSetBySingleFileDataSetMovedAndDescriptionChanged()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(1234567));
    MetaData metaData = dataSet.getMetaData();
    File dataSetFile = new File(workingDir, "data");
    String[] pathsAndContents = Utils.createExampleData(dataSetFile, ":hello test!");
    Utils.delay(1100);
    setStartTimeToNow();
    
    MetaData metaData2 = dataRepository.replace(metaData.getId(), dataSetFile, "replaced", true, progressListener);
    
    assertEquals("replaced", metaData2.getDescription());
    assertEquals(metaData.getId(), metaData2.getId());
    assertEquals("data", metaData2.getName());
    assertEquals(1, metaData2.getNumberOfFiles());
    assertEquals(11, metaData2.getSize());
    assertTimestamp(metaData2.getTimestamp());
    assertEquals(false, dataSetFile.exists());
    dataRepository.export(Criteria.forId(metaData.getId()), target, new DummyProgressListener());
    assertContent(pathsAndContents, new File(target, metaData2.getName()));
    assertNoProgressErrorAndFinishedState();
  }
  
  @Test
  public void testReplaceMultiFileDataSetByMultiFileDataSetCopiedAndDescriptionNotChanged()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(1234567));
    MetaData metaData = dataSet.getMetaData();
    File dataSetFile = new File(workingDir, "data");
    String[] pathsAndContents = Utils.createExampleData(dataSetFile, 
            "/images/img1:hello image1", "/images/img2:hello image2");
    Utils.delay(1100);
    setStartTimeToNow();
    
    MetaData metaData2 = dataRepository.replace(metaData.getId(), dataSetFile, null, false, progressListener);
    
    assertEquals(metaData.getDescription(), metaData2.getDescription());
    assertEquals(metaData.getId(), metaData2.getId());
    assertEquals("data", metaData2.getName());
    assertEquals(4, metaData2.getNumberOfFiles());
    assertEquals(24, metaData2.getSize());
    assertTimestamp(metaData2.getTimestamp());
    assertContent(pathsAndContents, dataSetFile);
    dataRepository.export(Criteria.forId(metaData.getId()), target, new DummyProgressListener());
    assertContent(pathsAndContents, new File(target, metaData2.getName()));
    assertNoProgressErrorAndFinishedState();
  }
  
  @Test
  public void testReplaceMultiFileDataSetByMultiFileDataSetMovedAndDescriptionNotChanged()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(1234567));
    MetaData metaData = dataSet.getMetaData();
    File dataSetFile = new File(workingDir, "data");
    String[] pathsAndContents = Utils.createExampleData(dataSetFile, 
            "/images/img1:hello image1", "/images/img2:hello image2");
    Utils.delay(1100);
    setStartTimeToNow();
    
    MetaData metaData2 = dataRepository.replace(metaData.getId(), dataSetFile, null, true, progressListener);
    
    assertEquals(metaData.getDescription(), metaData2.getDescription());
    assertEquals(metaData.getId(), metaData2.getId());
    assertEquals("data", metaData2.getName());
    assertEquals(4, metaData2.getNumberOfFiles());
    assertEquals(24, metaData2.getSize());
    assertTimestamp(metaData2.getTimestamp());
    assertEquals(false, dataSetFile.exists());
    dataRepository.export(Criteria.forId(metaData.getId()), target, new DummyProgressListener());
    assertContent(pathsAndContents, new File(target, metaData2.getName()));
    assertNoProgressErrorAndFinishedState();
  }
}
