package ch.unibas.informatik.hs15.cs203.datarepository.apps.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import ch.unibas.informatik.hs15.cs203.datarepository.api.MetaData;
import ch.unibas.informatik.hs15.cs203.datarepository.common.PrintUtils;

class OverviewWriter {
	private static final String LF = System.getProperty("line.separator");

	private static final String META_CHARSET = "<meta charset=\"utf-8\"/>";

	private static final String TABLE_HEADER = "<th>ID</th><th>Name</th><th>Timestamp</th><th>Number of Files</th><th>Size</th><th>Description</th>";

	private final static String ROW_CONTENT_TEMPLATE = "<td>%1$s</td><td>%2$s</td><td>%3$s</td><td>%4$s</td><td>%5$s</td><td>%6$s</td>";

	private static final String HTML_OPEN = "<html>";

	private static final String HTML_CLOSE = "</html>";

	private static final String HEAD_OPEN = "<head>";

	private static final String HEAD_CLOSE = "</head>";

	private static final String BODY_OPEN = "<body>";

	private static final String BODY_CLOSE = "</body>";

	private static final String TABLE_OPEN = "<table>";

	private static final String TABLE_CLOSE = "</table>";

	private static final String DOCTYPE = "<!DOCTYPE html>";
	
	private static final String createBody(final String body) {
		return wrapWithTag(body, BODY_OPEN, BODY_CLOSE);
	}

	private static final String createHead(final String head) {
		return wrapWithTag(head, HEAD_OPEN, HEAD_CLOSE);
	}

	private static final String createHeader(final String header) {
		return wrapWithTag(header, "<h1>", "</h1>");
	}

	private static final String createPage(final String content) {
		return wrapWithTag(content, HTML_OPEN, HTML_CLOSE);
	}

	private static final String createRow(final MetaData meta) {
		final String cnt = createRowContent(meta);
		return createRow(cnt);
	}
	
	private static final String createRow(final String content){
		return wrapWithTag(content, "<tr>", "</tr>");
	}

	private static final String createRowContent(final MetaData meta) {
		final String desc = meta.getDescription() != null
				? meta.getDescription() : "";
		return String.format(ROW_CONTENT_TEMPLATE, meta.getId(), meta.getName(),
				PrintUtils.DATE_TIME_FORMAT.format(meta.getTimestamp()), meta.getNumberOfFiles(),
				meta.getSize(), desc);
	}
	
	private static final String createStyle(List<String> rules){
		StringBuilder sb = new StringBuilder();
		for(String s : rules){
			sb.append(s);
			sb.append(LF);
		}
		return wrapWithTag(sb.toString(), "<style>", "</style>");
	}

	private static final String createTable(final String tableBody) {
		return wrapWithTag(tableBody, TABLE_OPEN, TABLE_CLOSE);
	}

	private static final String wrapWithTag(final String toWrap,
			final String openTag, final String closeTag) {
		final StringBuilder sb = new StringBuilder(openTag);
		sb.append(LF);
		sb.append(toWrap);
		sb.append(LF);
		sb.append(closeTag);
		return sb.toString();
	}
	
	private Path overview = null;
	private boolean enabled = true;
	
	public OverviewWriter(Path overview){
		if(overview == null){
			enabled = false;
		}
		this.overview = overview;
		try {
			if(overview.getParent()!=null){
				Files.createDirectories(overview.getParent());
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not create unexistent directories within overview path. ("+overview.toString()+")", e);
		}
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void createHtmlFile(List<MetaData> metas) throws IOException{
		if(!enabled){
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(MetaData meta : metas){
			sb.append(createRow(meta));
			sb.append(LF);
		}
		sb.insert(0, createRow(TABLE_HEADER));
		String table = createTable(sb.toString() );
		String style = createStyle(getDefaultStyleRules() );
		String head = createHead(META_CHARSET+LF+style);
		String header = createHeader("Datasets Overview");
		String body = createBody(header+LF+table);
		String page = createPage(head+LF+body);
		writeFile(DOCTYPE+LF+page);
	}
	
	private List<String> getDefaultStyleRules(){
		ArrayList<String> out = new ArrayList<String>();
		out.add("body {font-family: sans-serif; padding: 10px;}");
		out.add("table {border-collapse: collapse;}");
		out.add("td,th {padding: 4px 8px 2px 8px;"+LF+"font-size: 1em;"+LF+"border: 1px solid black;}");
		out.add("th {text-align: left;"+LF+"padding-top: 6px; padding-bottom: 4px;"+LF+"font-size: 1.1em;"+LF+"background-color: #BBBBBB}");
		out.add("tr:nth-child(odd) td {background-color: #E4E4E4}");
		return out;
	}
	
	private void writeFile(String contents){
		BufferedWriter bw = null;
		try{
			overview.toFile().delete();
			overview.toFile().createNewFile();
			bw = Files.newBufferedWriter(overview, StandardCharsets.UTF_8);
			bw.write(contents);
			bw.flush();
		}catch(Exception ex){
			throw new RuntimeException("Cannot write to html-overview. This is a serious problem!",ex);
		}finally{
			if(bw != null){
				try{
					bw.close();
				}catch(IOException e){
					throw new RuntimeException("Could not close the html-overview file. ", e);
				}
			}
		}
	}
	
	
}
