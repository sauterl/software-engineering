package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;

public class HTMLWriter {
	final String _classpath;
	final String tableName = "myLittleTable.html";
	/**
	 * Initializes the writer with a classpath
	 * @param folder
	 * where should the writer update the file
	 */
	public HTMLWriter(String folder){
		//TODO Verify Folder, create Directory etc.
		_classpath = folder;
	}
	/**
	 * Updates the current HTML Table
	 * @param containedData
	 * List of the data contained in the repository. 
	 * Is obtained with the with {@link ch.unibas.informatik.hs15.cs203.datarepository.api.DataRepository#getMetaData(Criteria) getMetaData}
	 * @throws IOException 
	 */
	public void update(List<MetaData> containedData) throws IOException{
		
		if(containedData.contains(null) || containedData.isEmpty()){
			throw new IOException();
		}
		
		File directory = new File(_classpath);
		//Check whether the file exists
		File table = new File(_classpath+tableName);
		if(!directory.exists()){
			directory.mkdirs();
		}
		if(table.isDirectory()){
			throw new IOException();
		}
		if(table.exists() && !table.isDirectory()){
			table.delete();
		}
		writeNewTable(containedData);
	}
	/**
	 * Creates a new HTML table from the data given
	 * @param containedData
	 *  data which is to be put in the table
	 * @throws IOException 
	 */
	private void writeNewTable(List<MetaData> containedData) throws IOException{
		String template = "<tr>\n<th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th>\n</tr>\n";
		// Use StringBuilder in single thread application!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		StringBuffer output = new StringBuffer (String.format(template,"ID","Name","Timestamp","Number of Files","Size","Description"));
		
		if(containedData !=null &&!containedData.isEmpty()){
			template = template.replaceAll("<th>","<td>").replaceAll("</th>", "</td>");
			for(MetaData files:containedData){
		
				String formatedString = String.format(template,files.getId(),files.getName(),files.getTimestamp().toString(),files.getNumberOfFiles(),files.getSize(),files.getDescription());
				output.append(formatedString);
			}
		}
		output.insert(0,"<table>\n");
		output.append("</table>");
		BufferedWriter out = new BufferedWriter(new FileWriter(_classpath+tableName));
		out.write(output.toString());
		out.flush();
		out.close();
	}
}	 
