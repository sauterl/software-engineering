/**
 *
 */
package ch.unibas.informatik.hs15.cs203.datarepository.apps.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

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
 *
 *
 * 	1. First argument
 * 	2. Second
 * 	3. Last
 *
 * By the way:
 * 	1. Other first argument
 *
 *
 * </pre>
 * </p>
 *
 * @author Loris
 *
 */
final class DescriptionParser {
	/**
	 * The new paragraph tag as described above.<br />
	 * The tag is: {@value #NEW_PARAGRAPH_TAG}
	 */
	public static final String NEW_PARAGRAPH_TAG = "$br$";
	/**
	 * The ordered list entry tag as described above.<br />
	 * The tag is: {@value #ORDERED_LIST_ENTRY_TAG}
	 */
	public static final String ORDERED_LIST_ENTRY_TAG = "$1$";

	/**
	 * The new line tag as described above.<br />
	 * The tag is: {@value #NEW_LINE_TAG}
	 */
	public static final String NEW_LINE_TAG = "$n$";

	/**
	 * The tab tag as described above.<br />
	 * The tag is: {@value #TAB_TAG}
	 */
	public static final String TAB_TAG = "$t$";

	/**
	 * The start and end character of a tag.<br />
	 * The value is: {@link #TAG_SIGN}
	 */
	private static final String TAG_SIGN = "$";

	private BufferedReader reader = null;
	private File file = null;
	private String input = null;
	private volatile boolean ready = false;
	private int listIndex = 1;

	/**
	 * Creates an unready parser.<br />
	 * Only {@link #parse(String)} is allowed to use with an object created with
	 * this constructor.
	 */
	public DescriptionParser() {
		// for convience;
	}

	/**
	 * Creates a parser for the specified file.<br />
	 * Invoking {@link #parse()} will then parse the contents of the specified
	 * file.
	 *
	 * @param file
	 *            The <tt>desc</tt>-formatted file to parse.
	 * @throws FileNotFoundException
	 *             If the file has not been found.
	 */
	public DescriptionParser(final File file) throws FileNotFoundException {
		this.file = file;
		reader = new BufferedReader(new FileReader(file));
		ready = true;
	}

	/**
	 * Creates a parser for the specified contents. <br />
	 * Invoking {@link #parse()} will then parse the given string.
	 *
	 * @param contents
	 *            A <tt>desc</tt>-formatted string to parse.
	 */
	public DescriptionParser(final String contents) {
		input = contents;
		ready = true;
	}
	
	public DescriptionParser(final Reader reader){
		if(reader == null){
			throw new NullPointerException("Reader must not be null");
		}
		this.reader = new BufferedReader(reader);
		ready = true;
	}

	/**
	 * Returns <tt>true</tt> if this {@link DescriptionParser} is ready, thus
	 * {@link #parse()} can be invoked. <br />
	 * <i>If someone reads this comment, please be kind and send an email to
	 * <code>loris . sauter [a-t] unibas . ch</code> with subject
	 * <tt>coffee</tt>. Thank you very much!</i>
	 *
	 * @return see above.
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * Parses the pre-specified contents. <br />
	 * The specification is during the constructor invocation, so using one of
	 * the parameterized constructors enables this mode.
	 *
	 * @return The parsed contents.
	 * @throws IOException
	 *             If this parser was initialized with a file and an error
	 *             occurs while reading said file.
	 */
	public String parse() throws IOException {
		if (!isReady()) {
			throw new IllegalStateException("Parser not ready");
		}
		if (!checkStringMode()) {
			input = readFile();
		}
		return parse(input);
	}

	/**
	 * Parses the given <tt>desc</tt>-formatted string.
	 *
	 * @param str
	 *            The contents / string in <tt>desc</tt> format to parse.
	 * @return The parsed contents.
	 */
	public String parse(final String str) {
		return parseString(cleanUp(str));
	}

	private boolean checkStringMode() {
		return (file == null && reader == null) && input != null;
	}

	private String cleanUp(final String str) {
		final String[] lines = str.split("\n");
		final StringBuffer sb = new StringBuffer(str.length() + lines.length);
		for (String s : lines) {
			s = s.trim();
			s.replaceAll("\n", "");
			sb.append(s);
			if (!s.endsWith(TAG_SIGN)) {
				sb.append(" ");
			}
		}
		return sb.toString().trim();
	}

	private String parseString(final String str)
			throws IndexOutOfBoundsException {
		final StringBuffer sb = new StringBuffer(str.length());
		int index = str.indexOf(TAG_SIGN, 0);
		int end = str.indexOf(TAG_SIGN, index + 1);
		int prev = 0;
		while (index < str.length() && index >= 0) {
			sb.append(str.substring(prev, index));
			final String tag = str.substring(index, end + 1);
			sb.append(parseTag(tag));
			prev = end + 1;
			index = str.indexOf(TAG_SIGN, end + 1);
			if (index > 0) {
				end = str.indexOf(TAG_SIGN, index + 1);
			}
		}
		if (end < str.length()) {
			sb.append(str.substring(end + 1));
		}
		return sb.toString();
	}

	private String parseTag(final String tag) {
		switch (tag) {
			case NEW_PARAGRAPH_TAG:
				listIndex = 1;
				return "\n\n";
			case NEW_LINE_TAG:
				return "\n";
			case TAB_TAG:
				return "\t";
			case ORDERED_LIST_ENTRY_TAG:
				String out = "";
				if (listIndex == 1) {
					out += "\n";
				}
				return out + "\t" + (listIndex++) + ".";
			default:
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
		// if (!checkStringMode()) {
		// throw new IllegalStateException(
		// "Cannot read from file when in string mode");
		// }
		final StringBuffer sb = new StringBuffer();
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			try {
				reader.close();
			} catch (final IOException ex) {
				// doing nothing for time being.
			}

		}
		return sb.toString();
	}

}
