package ch.unibas.informatik.hs15.cs203.datarepository.api;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class DeleteTest extends APITestCase
{
  @Test
  public void testDeleteDataSetById()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", ":hello test!");
    createDataSet("hi2.txt", "short example2", ":hello test2!");
    MetaData metaData = dataSet.getMetaData();

    List<MetaData> deletedDataSets = dataRepository.delete(Criteria.forId(metaData.getId()));
    
    assertDeletedDataSets(deletedDataSets, "hi.txt");
    assertMetaData(metaData, deletedDataSets.get(0));
    assertNonDeletedDataSets("hi2.txt");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testDeleteNoDataSetById()
  {
    DataSet dataSet = createDataSet("hi.txt", "short example", ":hello test!");
    createDataSet("hi2.txt", "short example2", ":hello test2!");
    MetaData metaData = dataSet.getMetaData();
    
    dataRepository.delete(Criteria.forId(metaData.getId() + "abc"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testDeleteNoDataSetByIdInEmptyRepository()
  {
    dataRepository.delete(Criteria.forId("abc"));
  }
  
  @Test
  public void testDeleteByName()
  {
    createDataSet("hi.txt", "short example", ":hello test!");
    createDataSet("hi2.txt", "short example2", ":hello test2!");
    createDataSet("hi.txt", "short example2", ":hello test2!");

    List<MetaData> deletedDataSets = dataRepository.delete(new Criteria("hi.txt", null, null, null));

    assertDeletedDataSets(deletedDataSets, "hi.txt", "hi.txt");
    assertNonDeletedDataSets("hi2.txt");
  }
  
  @Test
  public void testDeleteByNameAndText()
  {
    createDataSet("hi.txt", "short example", ":hello test!");
    createDataSet("hi2.txt", "short example2", ":hello test2!");
    createDataSet("hi.txt", "short example2", ":hello test2!");
    
    List<MetaData> deletedDataSets = dataRepository.delete(new Criteria("hi.txt", "2", null, null));
    
    assertDeletedDataSets(deletedDataSets, "hi.txt");
    assertNonDeletedDataSets("hi2.txt", "hi.txt");
  }
  
  @Test
  public void testDeleteByText()
  {
    createDataSet("a2.txt", null, ":hello test!");
    createDataSet("b.txt", "short example2", ":hello test2!");
    createDataSet("c.txt", null, ":hello test2!");
    
    List<MetaData> deletedDataSets = dataRepository.delete(new Criteria(null, "2", null, null));
    
    assertDeletedDataSets(deletedDataSets, "a2.txt", "b.txt");
    assertNonDeletedDataSets("c.txt");
  }
  
  @Test
  public void testDeleteNonByAfterDate()
  {
    createDataSet("a.txt", null, ":hello test!");
    createDataSet("b.txt", "short example2", ":hello test2!");
    Date date = new Date(System.currentTimeMillis() + 10000);
    
    List<MetaData> deletedDataSets = dataRepository.delete(new Criteria(null, null, date, null));
    
    assertDeletedDataSets(deletedDataSets);
    assertNonDeletedDataSets("a.txt", "b.txt");
  }
  
  @Test
  public void testDeleteAllByAfterDate()
  {
    Date date = new Date(System.currentTimeMillis() - 10000);
    createDataSet("a.txt", null, ":hello test!");
    createDataSet("b.txt", "short example2", ":hello test2!");
    
    List<MetaData> deletedDataSets = dataRepository.delete(new Criteria(null, null, date, null));
    
    assertDeletedDataSets(deletedDataSets, "a.txt", "b.txt");
    assertNonDeletedDataSets();
  }
  
  @Test
  public void testDeleteByTextAndAfterDate()
  {
    Date date = new Date(System.currentTimeMillis() - 10000);
    createDataSet("a.txt", null, ":hello test!");
    createDataSet("b.txt", "short example2", ":hello test2!");
    
    List<MetaData> deletedDataSets = dataRepository.delete(new Criteria(null, "2", date, null));
    
    assertDeletedDataSets(deletedDataSets, "b.txt");
    assertNonDeletedDataSets("a.txt");
  }
  
  @Test
  public void testDeleteNonByBeforeDate()
  {
    Date date = new Date(System.currentTimeMillis() - 10000);
    createDataSet("a.txt", null, ":hello test!");
    createDataSet("b.txt", "short example2", ":hello test2!");
    
    List<MetaData> deletedDataSets = dataRepository.delete(new Criteria(null, null, null, date));
    
    assertDeletedDataSets(deletedDataSets);
    assertNonDeletedDataSets("a.txt", "b.txt");
  }
  
  @Test
  public void testDeleteAllByBeforeDate()
  {
    createDataSet("a.txt", null, ":hello test!");
    createDataSet("b.txt", "short example2", ":hello test2!");
    Date date = new Date(System.currentTimeMillis() + 10000);
    
    List<MetaData> deletedDataSets = dataRepository.delete(new Criteria(null, null, null, date));
    
    assertDeletedDataSets(deletedDataSets, "a.txt", "b.txt");
    assertNonDeletedDataSets();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testDeleteWithCriteriaAll()
  {
    createDataSet("a.txt", null, ":hello test!");
    createDataSet("b.txt", "short example2", ":hello test2!");
    
    dataRepository.delete(Criteria.all());
  }
  
  
  private void assertDeletedDataSets(List<MetaData> deletedDataSets, String... expectedDeletedDataSets)
  {
    Arrays.sort(expectedDeletedDataSets);
    assertDataSetNamesSorted(deletedDataSets, expectedDeletedDataSets);
  }
  
  private void assertNonDeletedDataSets(String... expectedNonDeletedDataSets)
  {
    Arrays.sort(expectedNonDeletedDataSets);
    List<MetaData> notDeletedDataSets = dataRepository.getMetaData(Criteria.all());
    assertDataSetNamesSorted(notDeletedDataSets, expectedNonDeletedDataSets);
  }
}
