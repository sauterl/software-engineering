package ch.unibas.informatik.hs15.cs203.datarepository.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

public class ListTest extends APITestCase
{
  @Test
  public void testListById()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", ":hello test!");
    createDataSet("hi.txt", "short example2", ":hello test2!");
    MetaData metaData = dataSet.getMetaData();
    
    List<MetaData> metaDataList = dataRepository.getMetaData(Criteria.forId(metaData.getId()));
    
    assertDataSetNames(metaDataList, "hi.txt");
    assertMetaData(metaData, metaDataList.get(0));
    assertEquals(1, metaDataList.size());
  }
  
  @Test
  public void testListByUnknownId()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", ":hello test!");
    
    List<MetaData> metaDataList = dataRepository.getMetaData(
            Criteria.forId(dataSet.getMetaData().getId() + "_unknown")); 

    assertEquals(0, metaDataList.size());
  }
  
  @Test
  public void testListTwoOfThreeDataSetsByText()
  {
    DataSet ds1 = createDataSet("hello", "abc", "hi.txt:hello world!", "data.dat:" + Utils.createExampleContent(6543217));
    Utils.delay(1100);
    DataSet ds2 = createDataSet("tabData", "second example", "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(1234567),
            "/data/2.dat:" + Utils.createExampleContent(7654321));
    createDataSet("someData", "third example", "/greetings.txt:hi test!",
            "/data/ab.dat:" + Utils.createExampleContent(1234567));
    
    List<MetaData> metaDataList = dataRepository.getMetaData(new Criteria(null, "ab", null, null));
    
    assertDataSetNames(metaDataList, "hello", "tabData");
    assertMetaData(ds1.getMetaData(), metaDataList.get(0));
    assertMetaData(ds2.getMetaData(), metaDataList.get(1));
    assertEquals(2, metaDataList.size());
  }
  
  @Test
  public void testListByName()
  {
    createDataSet("hello.txt", "abc", ":hello world!");
    DataSet ds1 = createDataSet("hi.txt", "second hello.txt example", ":hi world!");
    Utils.delay(1100);
    DataSet ds2 = createDataSet("hi.txt", "third hello.txt example", ":hi test!");
    
    List<MetaData> metaDataList = dataRepository.getMetaData(new Criteria("hi.txt", null, null, null));
    
    assertMetaData(ds1.getMetaData(), metaDataList.get(0));
    assertMetaData(ds2.getMetaData(), metaDataList.get(1));
    assertEquals(2, metaDataList.size());
  }
  
  @Test
  public void testListNoDataSetsByName()
  {
    createDataSet("hello.txt", "abc", ":hello world!");
    createDataSet("hi.txt", "second hello.txt example", ":hi world!");
    
    List<MetaData> metaDataList = dataRepository.getMetaData(new Criteria("abc.txt", null, null, null));
    
    assertEquals(0, metaDataList.size());
  }
  
  @Test
  public void testListAllDataSets()
  {
    DataSet ds1 = createDataSet("hello.txt", "abc", ":hello world!");
    DataSet ds2 = createDataSet("hi.txt", "second hello.txt example", ":hi world!");
    DataSet ds3 = createDataSet("hi2.txt", "third hello.txt example", ":hi test!");
    
    List<MetaData> metaDataList = dataRepository.getMetaData(Criteria.all());
    
    Utils.sortMetaData(metaDataList);
    assertMetaData(ds1.getMetaData(), metaDataList.get(0));
    assertMetaData(ds2.getMetaData(), metaDataList.get(1));
    assertMetaData(ds3.getMetaData(), metaDataList.get(2));
    assertEquals(3, metaDataList.size());
  }

  @Test
  public void testListWithMissingCriteria()
  {
    try
    {
      dataRepository.getMetaData(null);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e)
    {
      printErrorMessageAndAssertNoProgressError(e);
    }

  }
}
