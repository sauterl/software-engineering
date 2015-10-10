package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class CommandParserTest {

      @Test
      public void testLexNull(){
	  try{
	      CommandParser.lex(null);
	      fail("No exception thrown for argument null");
	  }catch(IllegalArgumentException ex){
	      System.out.println(ex);
	  }
      }
      
      @Test
      public void testLexZeroLength(){
	  LinkedList<String> list = new LinkedList<String>();
	  list.add(Command.HELP.name() );
	  LinkedList<String> tokens = CommandParser.lex(new String[0]);
	  assertEquals(list.toString(), tokens.toString());
      }
      
      @Test
      public void testLexInvalidCommand(){
	  try{
	      CommandParser.lex(new String[]{"Hello"});
	      fail("No exception thrown for invalid command");
	  }catch(IllegalArgumentException ex){
	      System.out.println(ex);
	  }
      }
      
      @Test
      public void testLexValidCommandAddLowerCase(){
	  LinkedList<String> list = new LinkedList<String>();
	  list.add(Command.ADD.name() );
	  LinkedList<String> tokens = CommandParser.lex(new String[]{"add"});
	  assertEquals(list.toString(), tokens.toString());
      }
      
      @Test
      public void testLexValidCommandAddUpperCase(){
	  LinkedList<String> list = new LinkedList<String>();
	  list.add(Command.ADD.name() );
	  LinkedList<String> tokens = CommandParser.lex(new String[]{"ADD"});
	  assertEquals(list.toString(), tokens.toString());
      }
      
      @Test
      public void testLexValidCommandAddMixedCase(){
	  LinkedList<String> list = new LinkedList<String>();
	  list.add(Command.ADD.name() );
	  LinkedList<String> tokens = CommandParser.lex(new String[]{"Add"});
	  assertEquals(list.toString(), tokens.toString());
      }
      
      @Test
      public void testLexInexistentOption(){
	  try{
	      CommandParser.lex(new String[]{"ADD", "--hello"});
	      fail("No exception thrown for inexistent option");
	  }catch(IllegalArgumentException ex){
	      System.out.println(ex);
	  }
      }
      
      @Test
      public void testLexValidFlagOptionVerbose(){
	  LinkedList<String> list = new LinkedList<String>();
	  list.add(Command.ADD.name() );
	  list.add(Option.VERBOSE.name());
	  
	  LinkedList<String> tokens = CommandParser.lex(new String[]{"ADD","--verbose"});
	  assertEquals(list.toString(), tokens.toString() );
      }
      
      @Test
      public void testLexValidOptionNoArgument(){
	  try{
	      CommandParser.lex(new String[]{"ADD", "--description"});
	      fail("No exception thrown for argumented-option without argument");
	  }catch(IllegalArgumentException ex){
	      System.out.println(ex);
	  }
      }
      
      @Test
      public void testLexValidOptionForWrongCommand(){
	  try{
	      CommandParser.lex(new String[]{"ADD", "--id"});
	      fail("No exception thrown for valid option for wrong command");
	  }catch(IllegalArgumentException ex){
	      System.out.println(ex);
	  }
      }
      
      @Test
      public void testLexAllValidAdd(){
	  LinkedList<String> list = new LinkedList<String>();
	  list.add("ADD");
	  list.add("VERBOSE");
	  list.add("DESCRIPTION=Some description");
	  list.add("path/to/repo");
	  list.add("file/to/add/now");
	  
	  LinkedList<String> tokens = CommandParser.lex(new String[]{"ADD", "--VERBOSE", "--DESCRIPTION", "Some description", "path/to/repo", "file/to/add/now"});
	  assertEquals(list.toString(), tokens.toString() );
      }

}
