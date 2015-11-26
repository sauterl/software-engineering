package ch.unibas.informatik.hs15.cs203.datarepository;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;

/**
 * Duplicate class
 */
public class Utils
{
    static void createAndCleanUp(File folder)
    {
        if (folder.exists())
        {
            Utils.delete(folder);
        }
        folder.mkdirs();
    }
    
    static void delay(long milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e)
        {
            // ignored
        }
    }
    
    static String createExampleContent(int numberOfCharacters)
    {
        StringBuilder builder = new StringBuilder();
        builder.setLength(numberOfCharacters);
        Random random = new Random();
        for (int i = 0; i < numberOfCharacters; i++)
        {
            builder.setCharAt(i, Integer.toString(random.nextInt(10)).charAt(0));
        }
        return builder.toString();
    }
    
    public static String[] createExampleData(File file, String... pathsAndContents)
    {
        for (String pathAndContent : pathsAndContents)
        {
            int indexOfColon = pathAndContent.indexOf(':');
            String path = pathAndContent.substring(0, indexOfColon);
            String content = pathAndContent.substring(indexOfColon + 1);
            File contentFile = new File(file, path);
            File folder = contentFile.getParentFile();
            if (folder.exists() == false && folder.mkdirs() == false)
            {
                throw new IllegalStateException("Couldn't create " + folder);
            }
            writeTo(contentFile, content);
        }
        return pathsAndContents;
    }
    
    static void writeTo(File file, String text)
    {
        FileWriter writer = null;
        try
        {
            writer = new FileWriter(file);
            new PrintWriter(writer, true).print(text);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        } finally
        {
            if (writer != null)
            {
                try
                {
                    writer.close();
                } catch (IOException e)
                {
                    // ignored
                }
            }
        }
    }
    
    public static void delete(File file)
    {
        try
        {
            deleteRecursively(file);
        } catch (Exception e)
        {
            throw new IllegalStateException("Couldn't delete " + file
                    + ". Try to delete it manually and run the test again.", e);
        }
    }
    
    private static void deleteRecursively(File file)
    {
        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            for (File childFile : files)
            {
                deleteRecursively(childFile);
            }
        }
        if (file.exists() && file.delete() == false)
        {
            throw new IllegalStateException("Couldn't delete " + file + ".");
        }
    }
    
    static void sortMetaData(List<MetaData> metaData)
    {
      Collections.sort(metaData, new Comparator<MetaData>()
        {
          @Override
          public int compare(MetaData m1, MetaData m2)
          {
            String name1 = getName(m1);
            String name2 = getName(m2);
            return name1.compareTo(name2);
          }

          private String getName(MetaData metaData)
          {
            if (metaData == null)
            {
              return "";
            }
            String name = metaData.getName();
            return name == null ? "" : name;
          }
        });
    }

    static String getContentOf(File file)
    {
        if (file.isFile() == false)
        {
            if (file.exists())
            {
                fail("File '" + file + "' is not a file.");
            } else
            {
                fail("File '" + file + "' does not exists.");
            }
            return null;
        }
        Reader reader = null;
        try
        {
            reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
            {
                builder.append(line).append('\n');
            }
            return builder.toString().trim();
        } catch (Exception e)
        {
            fail("Couldn't read file '" + file + "': " + e.getMessage());
            return null;
        } finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                } catch (IOException e)
                {
                    // ignored
                }
            }
        }
    }
}
