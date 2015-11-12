package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.unibas.informatik.hs15.cs203.datarepository.api.Criteria;
import ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository;
import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;
import ch.unibas.informatik.hs15.cs203.datarepository.api.ProgressListener;

public class ClientTest
{
  private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
  private MockDataRepositoryFactory factory;
  
  @Before
  public void setUp()
  {
    factory = new MockDataRepositoryFactory();
  }
  
  @Test
	public void inputTest() {
		
		
		BufferedReader br=setupReader();
		String[] lines;
		while((lines=readCommand(br)).length>0){
		for(String l:lines){
//		System.out.println(l);
		}
		runCommand(lines);
//		System.out.println();
		}
		
		
	}
	
	private void runCommand(String[] lines){
		setUp();
		String commandfull=lines[0];
		ArrayList<String> command=new ArrayList<String>();
		int c=0;
		while(c<commandfull.length()){
			if(commandfull.indexOf('\t',c+1)>0){
				command.add(commandfull.substring(c, commandfull.indexOf('\t',c+1)));
				c=commandfull.indexOf('\t',c+1)+1;
			}else{
				command.add(commandfull.substring(c));
				c=commandfull.length();
			}
		}
		try{
		String message=Client.execute(command.toArray(new String[0]), factory);
		System.out.println(message);
		if(lines.length<4){
			if(lines.length>2){
				assertTrue(lines[0]+"\nThe output of the command isnt correct. The output given was '"+message+"' the output wanted was '"+lines[2].substring(1)+"'.",lines[2].contains(message));
			}
		}else{
			for(int count=2;c<lines.length;count++){
				assertTrue(lines[0]+"\nThe output of the command isnt correct. The output given was '"+message+"' the output wanted was '"+lines[2].substring(1)+"'.",message.contains(lines[count].substring(1)));
			}
		}
		assertTrue(lines[0]+"\nThe command shoult have faild but it worked.",lines[1].contains("SUCCESS"));
		}catch(IllegalArgumentException e){
			System.out.println(e.getMessage());
			if(lines.length==3){
				assertTrue(lines[0]+"\nThe Error Message returned by the command wast'n the one expected. Expected: '"+lines[2].substring(1)+"' returned: '"+e.getMessage()+"'",lines[2].contains(e.getMessage()));
			}
			assertTrue(lines[0]+"\nThe command should have worked but it failed",lines[1].contains("ERROR"));
		}
		
	}
	
	private String[] readCommand(BufferedReader br){
		ArrayList<String> lines=new ArrayList<String>();
		String line="";
		try {
		while((line=br.readLine())!=null){
			if(line.matches("^(\\t| )*$")){
				break;
			}
			if(!line.startsWith("#")){
				lines.add(line);
			}
		}
	} catch (IOException e) {
		fail("Exception while reading File.");
	}
		return lines.toArray(new String[0]);
	}
	
	private BufferedReader setupReader(){
		BufferedReader br=null;
		URL location = ClientTest.class.getResource("cli-test-cases.txt");
		try{
			br=new BufferedReader(new FileReader(new File(location.getPath().replace("%20", " "))));
		}catch (IOException e){
			e.printStackTrace();
			fail("test case file is missing");
		}
		return br;
	}
  

  @Test
  public void testAdd()
  {
    assertExecuteWithSuccess("Data set named 'hello.txt' has been successfully added to the repository. ID: 123", 
            "add(hello.txt, null, false, no-verbose)\n",
            "add", "my-repos", "hello.txt");
  }
  
  @Test
  public void testAddWithDescription()
  {
    assertExecuteWithSuccess("Data set named 'hello.txt' has been successfully added to the repository. ID: 123", 
            "add(hello.txt, example data, false, verbose)\n",
            "add", "--description", "example data", "--verbose", "my-repos", "hello.txt");
  }

  @Test
  public void testAddWithUnknownOption()
  {
    assertExecuteWithException("Error in 2. argument [--descript]: Unknown option.", 
            "no repository created",
            "add", "--descript", "example data", "my-repos", "hello.txt");
  }
  
  @Test
  public void testListAll()
  {
    assertExecuteWithSuccess("ID\tName\tTimestamp\tNumber of Files\tSize\tDescription\n" + 
            "123\thello.txt\t1970-01-01 01:20:34\t1\t11\t\n" + 
            "345\tdata\t1970-01-01 01:00:11\t30\t23345411\texample data\n", 
            "getMetaData(Criteria())\n", 
            "list", "my-repos");
  }
  
  @Test
  public void testDeletById()
  {
    assertExecuteWithSuccess("The following data sets have been deleted: 123, 345", 
            "delete(Criteria(id:abc12))\n", 
            "delete", "my-repos", "abc12");
  }
  
  @Test
  public void testDeletByNameAndBefore()
  {
    assertExecuteWithSuccess("The following data sets have been deleted: 123, 345", 
            "delete(Criteria(name:my-name, before:2015-09-16 00:00:00, after:2015-09-10 21:12:34))\n", 
            "delete", "--name", "my-name", "--before", "2015-09-16", "--after", "2015-09-10 21:12:34", "my-repos");
  }
  
  private void assertExecuteWithSuccess(String expectedAnswer, String expectedAPICalls, 
          String...args)
  {
    assertEquals(expectedAnswer, Client.execute(args, factory));
    assertEquals(expectedAPICalls, factory.toString());
  }
  
  private void assertExecuteWithException(String expectedExceptionMessage, String expectedAPICalls,
          String... args)
  {
    try
    {
      Client.execute(args, factory);
      fail("An Exception expected");
    } catch (Exception e)
    {
      assertEquals(expectedExceptionMessage, e.getMessage());
    }
    assertEquals(expectedAPICalls, factory.toString());
  }
  
  private static final class MockDataRepositoryFactory implements DataRepositoryFactory
  {
    private File _expectedRepositoryFolder = new File("my-repos");
    private List<MetaData> _returnValue= new ArrayList<MetaData>(Arrays.asList(
            new MetaData("123", "hello.txt", null, 1, 11, new Date(1234567)),
            new MetaData("345", "data", "example data", 30, 23345411, new Date(11144))));
    private MockDataRepository _mockDataRepository;

    @Override
    public DataRepository create(File repositoryFolder)
    {
      assertEquals(_expectedRepositoryFolder, repositoryFolder);
      _mockDataRepository = new MockDataRepository(_returnValue);
      return _mockDataRepository;
    }

    @Override
    public String toString()
    {
      return _mockDataRepository == null ? "no repository created" : _mockDataRepository.toString();
    }
  }
  
  private static final class MockDataRepository implements DataRepository
  {
    private List<MetaData> _returnValue;
    private StringBuilder _recorder = new StringBuilder();
    
    MockDataRepository(List<MetaData> returnValue)
    {
      _returnValue = returnValue;
    }
    
    @Override
    public String toString()
    {
      return _recorder.toString();
    }

    @Override
    public MetaData add(File file, String description, boolean move, ProgressListener progressListener)
    {
      _recorder.append("add(" + file + ", " + description + ", " + move + ", " 
              + renderProgressListener(progressListener) + ")\n");
      return _returnValue.remove(0);
    }

    @Override
    public List<MetaData> delete(Criteria deletionCriteria)
    {
      _recorder.append("delete(" + render(deletionCriteria) + ")\n");
      return _returnValue;
    }

    @Override
    public List<MetaData> export(Criteria exportCriteria, File target, ProgressListener progressListener)
    {
      _recorder.append("export(" + render(exportCriteria) + ", " + target + ", "
              + renderProgressListener(progressListener) + ")\n");
      return _returnValue;
    }

    @Override
    public MetaData replace(String id, File file, String description, boolean move, ProgressListener progressListener)
    {
      _recorder.append("replace(" + id + ", " + file + ", " + description + ", " + move + ", "
              + renderProgressListener(progressListener) + ")\n");
      return _returnValue.remove(0);
    }

    @Override
    public List<MetaData> getMetaData(Criteria searchCriteria)
    {
      _recorder.append("getMetaData(" + render(searchCriteria) + ")\n");
      return _returnValue;
    }
    
    private String render(Criteria criteria)
    {
      StringBuilder builder = new StringBuilder();
      appendTo(builder, "id", criteria.getId());
      appendTo(builder, "name", criteria.getName());
      appendTo(builder, "text", criteria.getText());
      appendTo(builder, "before", criteria.getBefore());
      appendTo(builder, "after", criteria.getAfter());
      return builder.insert(0, "Criteria(").append(")").toString();
    }
    
    private void appendTo(StringBuilder builder, String attribute, Object value)
    {
      if (value != null)
      {
        if (builder.length() > 0)
        {
          builder.append(", ");
        }
        String renderedValue = String.valueOf(value);
        if (value instanceof Date)
        {
          Date date = (Date) value;
          renderedValue = new SimpleDateFormat(FORMAT).format(date);
        }
        builder.append(attribute).append(":").append(renderedValue);
      }
    }
    
    private String renderProgressListener(ProgressListener progressListener)
    {
      if (progressListener == null)
      {
        return null;
      }
      if (progressListener instanceof DummyProgressListener)
      {
        return "no-verbose";
      }
      return "verbose";
    }
    
  }
}
