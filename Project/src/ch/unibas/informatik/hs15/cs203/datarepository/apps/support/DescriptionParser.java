/**
 * 
 */
package ch.unibas.informatik.hs15.cs203.datarepository.apps.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Reader for <tt>.desc</tt> files. <br />
 * These files are simple UTF-8 (or ASCII [discouraged]) encoded files, which
 * contain the description of a command.<br />
 * The markup language for those file is pretty simple; it supports four
 * different tags:
 * <ul>
 * <li>{@value #NEW_LINE_TAG}: A tag which will be replaced by the new line
 * character.</li>
 * <li>{@value #NEW_PARAGRAPH_TAG}: A tag to indicate a new paragraph (double
 * new line character).</li>
 * <li>{@value #ORDERED_LIST_ENTRY_TAG}: This tag will be replaced by a
 * ascending sequence of numbers, just like this list. (The number will be reset
 * (start again by 1), if the new paragraph /tag is in between the next
 * {@link #ORDERED_LIST_ENTRY_TAG}) <b>Must</b> be closed with a new line tag,
 * see example below.</li>
 * <li>{@value #TAB_TAG}: A tag which will be replaced by the tab character</li>
 * </ul>
 * Any other control character will be ignored.
 * <p>
 * <h3>Example</h3> The source <tt>.desc</tt> file:
 * 
 * <pre>
 * A single-line paragraph.$br$
 * Some tabbed$t$text.$n$
 * Text
 * which will be on the same line.$n$
 * But this not.$br$
 * $1$ First argument$n$
 * $1$ Second$n$
 * $1$ Last$br$
 * By the way:
 * $1$ Other first argument$br$
 * </pre>
 * 
 * Using this {@link DescriptionParser} on a file with that content, the
 * following output will be produced:
 * 
 * <pre>
 * A single-line paragraph.
 * 
 * Some tabbed	text.
 * Text which will be on the same line.
 * But this not.
 * 1. First argument
 * 2. Second
 * 3. Last
 * 
 * By the way:
 * 1.
 * </pre>
 * </p>
 * 
 * @author Loris
 *
 */
public class DescriptionParser {

	public static final String NEW_PARAGRAPH_TAG = "$br$";

	public static final String ORDERED_LIST_ENTRY_TAG = "$1$";

	public static final String NEW_LINE_TAG = "$n$";

	public static final String TAB_TAG = "$t$";

	private static final String TAG_SIGN = "$";

	private BufferedReader reader = null;
	private File file = null;
	private String input = null;
	private volatile boolean ready = false;

	public DescriptionParser() {
		// for convience;
	}

	public DescriptionParser(File file) throws FileNotFoundException {
		this.file = file;
		reader = new BufferedReader(new FileReader(file));
		ready = true;
	}

	public DescriptionParser(String contents) {
		input = contents;
	}

	public String parseString(String str) throws IndexOutOfBoundsException {
		StringBuffer sb = new StringBuffer(str.length());
		int index = str.indexOf(TAG_SIGN, 0);
		int end = str.indexOf(TAG_SIGN, index + 1)+1;
		int prev = 0;
		while (index < str.length() && index >= 0) {
			sb.append(str.substring(prev, index));
			String tag = str.substring(index, end);
			sb.append(parseTag(tag));
			prev = end;
			index = str.indexOf(TAG_SIGN, end + 1);
			end = str.indexOf(TAG_SIGN, index + 1)+1;
		}
		if(end < str.length() ){
			sb.append(str.substring(end) );
		}
		return sb.toString();
	}

	private String parseTag(String tag) {
		switch (tag) {
			case NEW_PARAGRAPH_TAG:
				return "\n\n";
			case NEW_LINE_TAG:
				return "\n";
			case TAB_TAG:
				return "\n";
			default:
				// TODO: Implement proper list tag
				return "#";
		}
	}

	/**
	 * Closes the reader anyway!
	 * 
	 * @return
	 * @throws IOException
	 */
	private String readFile() throws IOException {
		if (!isReady()) {
			throw new IllegalStateException(
					"Not ready, therefore cannot read.");
		}
		if (!checkStringMode()) {
			throw new IllegalStateException(
					"Cannot read from file when in string mode");
		}
		StringBuffer sb = new StringBuffer();
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(" ");
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				reader.close();
			} catch (IOException ex) {
				// doing nothing for time being.
			}

		}
		return sb.toString();
	}

	public boolean isReady() {
		return ready;
	}

	private boolean checkStringMode() {
		return (file == null && reader == null) && input != null;
	}

}
