package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

/**
 * The {@link Option} enum represents available options for commands of this
 * application.
 * 
 * Options have an {@link Option#OPTION_MARKER} which is an indicator that a
 * given argument is an option name. Without this marker, options are not
 * recognized as themselves.
 * 
 * Usually options do have an argument, which is directly followed by the option
 * name, but some options do not have an argument and are called 'flag' options.
 * 
 * An example of an option with argument would be
 * <code>--description "The description"</code> Do note the quotes of the text
 * and the whitespace between the option's name (description) and its argument
 * ("The description").
 * 
 * The option <code>--verbose</code> is a typical 'flag' option.
 * 
 * <b>Note:</b> Option names are case insensitive
 * 
 * @author Loris
 * 
 */
enum Option {

	/**
	 * The description option. The expected argument is a description.
	 */
	DESCRIPTION,
	/**
	 * The option move. This is a flag option.
	 */
	MOVE(true),
	/**
	 * The option verbose THis is a flag option.
	 */
	VERBOSE(true),
	/**
	 * The option id. The expected argument is an id.
	 */
	ID,
	/**
	 * The name option. The expected argument is a name.
	 */
	NAME,
	/**
	 * The text option. The expected argument is a text snippet.
	 */
	TEXT,
	/**
	 * The before option. The expected argument is a date. Read the
	 * specifications for further information.
	 */
	BEFORE,
	/**
	 * The after option. The expected argument is a date. Read the
	 * specifications for further information.
	 */
	AFTER;

	public static final String OPTION_MARKER = "--";

	/**
	 * Tests if a given string is likely an option. This is a simple test, if
	 * the given string starts with the {@link Option#OPTION_MARKER} or not.
	 * 
	 * @param str
	 *            The string to test for.
	 * @return TRUE if the given string starts with {@link Option#OPTION_MARKER}
	 *         , false in any other case. In particular if the input was null or
	 *         had zero length.
	 */
	public static boolean isLikelyOption(final String str) {
		if (str == null || str.length() == 0) {
			return false;
		} else {
			return str.startsWith(OPTION_MARKER);
		}
	}

	/**
	 * Returns the 'criteria-options': {@link #NAME}, {@link #TEXT}, {@link #AFTER} and {@link #BEFORE}
	 * @return
	 */
	public static Option[] getCriteriaOptions(){
		return new Option[]{Option.NAME, Option.TEXT, Option.AFTER, Option.BEFORE};
	}
	/**
	 * Parses a given {@link String} to a matching {@link Option}. This is a
	 * case sensitive matching and the string may has the
	 * {@link Option#OPTION_MARKER} or not. So <code>verbose</code>,
	 * <code>VERBOSE</code>, <code>--verbose</code> and <code>--VERbose</code>
	 * are all mapped to the {@link Option#VERBOSE}. If there is no mapping
	 * found, null is returned. <b>Use this method instead of
	 * java.lang.Enum.valueOf(Str)</b>
	 * 
	 * @param str
	 *            The string to parse (If this is null, null is returned).
	 * @return Returns the matching {@link Option} or <code>null</code> if none
	 *         is found.
	 */
	public static Option parse(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		if (isLikelyOption(str)) {
			str = str.substring(OPTION_MARKER.length());
		}
		for (final Option o : values()) {
			if (str.equalsIgnoreCase(o.name())) {
				return o;
			}
		}
		return null;
	}

	private boolean flag;

	private Option() {
		this(false);
	}

	private Option(final boolean flag) {
		this.flag = flag;
	}

	/**
	 * Checks whether the this {@link Option} is a flag. Flag options do not
	 * have arguments.
	 * 
	 * @return If this option a flag.
	 */
	public boolean isFlag() {
		return flag;
	}
}
