package ch.unibas.informatik.hs15.cs203.datarepository.api;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class CancelTest extends APITestCase
{
  @Test
  public void testCancelAdd()
  {
    File folder = new File(workingDir, "my-data");
    Utils.createExampleData(folder,
            "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(1234567),
            "/data/2.dat:" + Utils.createExampleContent(7654321));
    MockProgressListener mockProgressListener = new MockProgressListener(1);

    MetaData metaData = dataRepository.add(folder, "testCancelAdd", false, mockProgressListener);
    
    assertEquals(null, metaData);
    mockProgressListener.assertNoErrors();
    mockProgressListener.assertCanceledState();
    assertEquals("[]", dataRepository.getMetaData(new Criteria(null, "testCancelAdd", null, null)).toString());
  }
  
  @Test
  public void testCancelReplace()
  {
    File file = new File(workingDir, "example.txt");
    Utils.createExampleData(file, ":hello world!");
    String id = dataRepository.add(file, "example file", false, progressListener).getId();
    assertNoProgressErrorAndFinishedState();
    File folder = new File(workingDir, "my-data");
    Utils.createExampleData(folder,
            "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(1234567),
            "/data/2.dat:" + Utils.createExampleContent(7654321));
    MockProgressListener mockProgressListener = new MockProgressListener(1);
    
    MetaData metaData = dataRepository.replace(id, folder, "testCancelReplace", false, mockProgressListener);
    
    assertEquals(null, metaData);
    mockProgressListener.assertNoErrors();
    mockProgressListener.assertCanceledState();
    assertEquals("[]", dataRepository.getMetaData(new Criteria(null, "testCancelReplace", null, null)).toString());
    assertEquals("example.txt", dataRepository.getMetaData(Criteria.forId(id)).get(0).getName());
  }
  
  @Test
  public void testCancelExport()
  {
    File myData1 = new File(workingDir, "my-data1");
    String[] content1 = Utils.createExampleData(myData1,
            "/greetings.txt:hello world!",
            "/data/1.dat:" + Utils.createExampleContent(1234567),
            "/data/2.dat:" + Utils.createExampleContent(7654321));
    MetaData metaData1 = dataRepository.add(myData1, "example 1", false, new MockProgressListener());
    Utils.delay(1100);
    File myData2 = new File(workingDir, "my-data2");
    Utils.createExampleData(myData2,
            "/greetings.txt:hello universe!",
            "/data/1.dat:" + Utils.createExampleContent(2345678));
    dataRepository.add(myData2, "example 2", false, new MockProgressListener());
    MockProgressListener mockProgressListener = createMockListener(8888900);
    File target = new File(workingDir, "target");
    target.mkdirs();
    
    List<MetaData> exported = dataRepository.export(new Criteria(null, "example", null, null), 
            target, mockProgressListener);
    
    assertEquals(metaData1.getId(), exported.get(0).getId());
    assertContent(content1, new File(target, myData1.getName()));
    assertEquals(false, new File(target, myData2.getName()).exists());
    assertEquals(1, exported.size());
    mockProgressListener.assertNoErrors();
    mockProgressListener.assertCanceledState();
  }
  
  private MockProgressListener createMockListener(final long numberOfBytesToRequestCancel)
  {
    return new MockProgressListener(new CancelRequestCriteria()
      {
        
        @Override
        public boolean cancelRequested(int numberOfCancelRequestChecks, long numberOfBytes,
                long totalNumberOfBytes)
        {
          return numberOfBytes >= numberOfBytesToRequestCancel;
        }
      });
  }
}
