package ch.unibas.informatik.hs15.cs203.datarepository.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class ExportTest extends APITestCase
{
//  @Test
  public void testExportSingleFileDataSetById()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", ":hello test!");
    createDataSet("hi.txt", "short example2", ":hello test2!");
    MetaData metaData = dataSet.getMetaData();
    
    List<MetaData> metaDataList = dataRepository.export(Criteria.forId(metaData.getId()), target, progressListener);
    
    assertDataSetNames(metaDataList, "hi.txt");
    assertMetaData(metaData, metaDataList.get(0));
    assertContent(dataSet.getPathsAndContents(), new File(target, metaData.getName()));
    assertEquals(1, metaDataList.size());
    assertNoProgressErrorAndFinishedState();
  }
  
//  @Test
  public void testExportByUnknownId()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", ":hello test!");
    
    try
    {
      dataRepository.export(Criteria.forId(dataSet.getMetaData().getId() + "_unknown"), 
              target, progressListener);
      fail("IllegalArgumentException expetced");
    } catch (IllegalArgumentException e)
    {
      printErrorMessageAndAssertNoProgressError(e);
    }
  }
  
//  @Test
  public void testExportDataSetFailsBecauseAlreadyExported()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", ":hello test!");
    MetaData metaData = dataSet.getMetaData();
    dataRepository.export(Criteria.forId(metaData.getId()), target, progressListener);
    progressListener = new MockProgressListener();

    try
    {
      dataRepository.export(Criteria.forId(metaData.getId()), target, progressListener);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e)
    {
      printErrorMessageAndAssertNoProgressError(e);
    }
  }
  
  @Test
  public void testExportTwoOfThreeDataSetsByText()
  {
    DataSet ds1 = createDataSet("hello", "abc", "hi.txt:hello world!", "data.dat:" + Utils.createExampleContent(6543217));
    Utils.delay(1100);
    DataSet ds2 = createDataSet("tabData", "second example", "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(1234567),
            "/data/2.dat:" + Utils.createExampleContent(7654321));
    createDataSet("someData", "third example", "/greetings.txt:hi test!",
            "/data/ab.dat:" + Utils.createExampleContent(1234567));
    
    List<MetaData> metaDataList = dataRepository.export(new Criteria(null, "ab", null, null), target, progressListener);
    
    assertDataSetNames(metaDataList, "hello", "tabData");
    assertMetaData(ds1.getMetaData(), metaDataList.get(0));
    assertMetaData(ds2.getMetaData(), metaDataList.get(1));
    assertContent(ds1.getPathsAndContents(), new File(target, ds1.getMetaData().getName()));
    assertContent(ds2.getPathsAndContents(), new File(target, ds2.getMetaData().getName()));
    assertEquals(2, metaDataList.size());
    assertNoProgressErrorAndFinishedState();
  }
  
//  @Test
  public void testExportOneOfThreeDataSetsByName()
  {
    DataSet ds1 = createDataSet("hello.txt", "abc", ":hello world!");
    createDataSet("hi.txt", "second hello.txt example", ":hi world!");
    createDataSet("hi.txt", "third hello.txt example", ":hi test!");
    
    List<MetaData> metaDataList = dataRepository.export(new Criteria("hello.txt", null, null, null), target, progressListener);
    
    assertDataSetNames(metaDataList, "hello.txt");
    assertMetaData(ds1.getMetaData(), metaDataList.get(0));
    assertContent(ds1.getPathsAndContents(), new File(target, ds1.getMetaData().getName()));
    assertEquals(1, metaDataList.size());
    assertNoProgressErrorAndFinishedState();
  }
  
//  @Test
  public void testExportNoDataSetsByName()
  {
    createDataSet("hello.txt", "abc", ":hello world!");
    createDataSet("hi.txt", "second hello.txt example", ":hi world!");
    
    List<MetaData> metaDataList = dataRepository.export(new Criteria("abc.txt", null, null, null), target, progressListener);
    
    assertDataSetNames(metaDataList);
    assertEquals(0, metaDataList.size());
    assertNoProgressErrorAndFinishedState();
  }
  
//  @Test
  public void testExportTwoOfDataSetsWithSameNameFails()
  {
    createDataSet("hello.txt", "abc", ":hello world!");
    createDataSet("hello.txt", "second hello.txt example", ":hi world!");
    
    try
    {
      dataRepository.export(new Criteria("hello.txt", null, null, null), target, progressListener);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e)
    {
      printErrorMessageAndAssertNoProgressError(e);
    }
  }
  
//  @Test
  public void testExportAllDataSets()
  {
    DataSet ds1 = createDataSet("hello.txt", "abc", ":hello world!");
    DataSet ds2 = createDataSet("hi.txt", "second hello.txt example", ":hi world!");
    DataSet ds3 = createDataSet("hi2.txt", "third hello.txt example", ":hi test!");
    
    List<MetaData> metaDataList = dataRepository.export(Criteria.all(), target, progressListener);
    
    Utils.sortMetaData(metaDataList);
    assertDataSetNames(metaDataList, "hello.txt", "hi.txt", "hi2.txt");
    assertMetaData(ds1.getMetaData(), metaDataList.get(0));
    assertMetaData(ds2.getMetaData(), metaDataList.get(1));
    assertMetaData(ds3.getMetaData(), metaDataList.get(2));
    assertContent(ds1.getPathsAndContents(), new File(target, ds1.getMetaData().getName()));
    assertContent(ds2.getPathsAndContents(), new File(target, ds2.getMetaData().getName()));
    assertContent(ds3.getPathsAndContents(), new File(target, ds3.getMetaData().getName()));
    assertEquals(3, metaDataList.size());
    assertNoProgressErrorAndFinishedState();
  }
  
//  @Test
  public void testListAndExportDataSetsBetweenTimestamps()
  {
    createDataSet("hello.txt", "abc", ":hello world!");
    Utils.delay(1100);
    Date afterDate = new Date();
    Utils.delay(1100);
    DataSet ds2 = createDataSet("hi.txt", "second hello.txt example", ":hi world!");
    DataSet ds3 = createDataSet("hi2.txt", null, ":hi test!");
    Utils.delay(1100);
    Date beforeDate = new Date();
    Utils.delay(1100);
    createDataSet("hi3.txt", "4. example", ":hi test 3!");
    Criteria criteria = new Criteria(null, null, afterDate, beforeDate);
    
    List<MetaData> metaDataList = dataRepository.getMetaData(criteria);
    
    Utils.sortMetaData(metaDataList);
    assertDataSetNames(metaDataList, "hi.txt", "hi2.txt");
    assertMetaData(ds2.getMetaData(), metaDataList.get(0));
    assertMetaData(ds3.getMetaData(), metaDataList.get(1));
    assertEquals(2, metaDataList.size());
    
    metaDataList = dataRepository.export(criteria, target, progressListener);
    
    Utils.sortMetaData(metaDataList);
    assertDataSetNames(metaDataList, "hi.txt", "hi2.txt");
    assertMetaData(ds2.getMetaData(), metaDataList.get(0));
    assertMetaData(ds3.getMetaData(), metaDataList.get(1));
    assertContent(ds2.getPathsAndContents(), new File(target, ds2.getMetaData().getName()));
    assertContent(ds3.getPathsAndContents(), new File(target, ds3.getMetaData().getName()));
    assertEquals(2, metaDataList.size());
    assertNoProgressErrorAndFinishedState();
  }
  
//  @Test
  public void testExportWithMissingCriteria()
  {
    assertInvalidExportArguments(null, target, progressListener);
  }
  
//  @Test
  public void testExportWithMissingTarget()
  {
    assertInvalidExportArguments(Criteria.all(), null, progressListener);
  }
  
//  @Test
  public void testExportWithNonExistingTarget()
  {
    assertInvalidExportArguments(Criteria.all(), new File(workingDir, "non-existing"), progressListener);
  }
  
//  @Test
  public void testExportWithTargetInsideRepository()
  {
    createDataSet("ds1", null, "/hi.txt:hello");
    File[] files = repository.listFiles();
    
    assertInvalidExportArguments(Criteria.all(), files[0], progressListener);
  }
  
//  @Test
  public void testExportWithTargetIsAFile()
  {
    File file = new File(workingDir, "hi.txt");
    Utils.writeTo(file, "hi");
    
    assertInvalidExportArguments(Criteria.all(), file, progressListener);
  }
  
//  @Test
  public void testExportWithMissingProgressListener()
  {
    assertInvalidExportArguments(Criteria.all(), target, null);
  }
  
  private void assertInvalidExportArguments(Criteria criteria, File target, ProgressListener progressListener)
  {
    try
    {
      dataRepository.export(criteria, target, progressListener);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e)
    {
      printErrorMessageAndAssertNoProgressErrorAndInitialState(e);
    }
  }
}
