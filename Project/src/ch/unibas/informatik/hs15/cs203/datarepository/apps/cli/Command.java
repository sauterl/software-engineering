package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

/**
 * The {@link Command} enumeration lists the available commands of the
 * application. Further the appropriate {@link Option}s are listed as well.
 * 
 * @author Loris
 * 
 */
public enum Command {
    /**
     * The ADD command. Appropriate {@link Option}s are:
     * {@link Option#DESCRIPTION}, {@link Option#MOVE} and
     * {@link Option#VERBOSE}
     */
    ADD(Option.DESCRIPTION, Option.MOVE, Option.VERBOSE),

    REPLACE, DELETE, EXPORT, LIST, HELP;

    private final Option[] appropriateOptions;

    private Command(Option... appropriateOptions) {
	this.appropriateOptions = appropriateOptions;
    }

    private Command() {
	appropriateOptions = null;
    }

    /**
     * Returns all appropriate {@link Option}s of this {@link Command}.
     * 
     * @return all appropriate {@link Option}s of this {@link Command} or
     *         <code>null</code> if no {@link Option} is appropriate.
     */
    public Option[] getAppropriateOptions() {
	return appropriateOptions;
    }

    /**
     * Checks whether a given {@link Option} is appropriate or not.
     * 
     * @param option
     *            The {@link Option} to test.
     * @return <code>true</code> if and only if the given {@link Option} is
     *         recognized as appropriate.
     */
    public boolean isOptionAppropriate(Option option) {
	for (Option o : appropriateOptions) {
	    if (option.equals(o)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Parses a given {@link String} to a matching {@link Command}. Unequal
     * cases are ignored, so <code>"ADD"</code>, and <code>"add"</code> will
     * result in {@link Command#ADD}. <b>Use this method instead of
     * {@link Enum#valueOf(String)}</b>
     * 
     * @param str
     *            The string to parse.
     * @return The parsed Command, ignoring case, or <code>null</code> if no
     *         command with this name was found.
     */
    public static Command parse(String str) {
	if (str == null || str.length() == 0) {
	    return null;
	}
	for (Command c : values()) {
	    if (str.equalsIgnoreCase(c.name())) {
		return c;
	    }
	}
	return null;
    }

}