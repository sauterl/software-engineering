package ch.unibas.informatik.hs15.cs203.datarepository.apps.cli;

/**
 * The {@link Command} enumeration lists the available commands of the
 * application. Further the appropriate {@link Option}s are listed as well.
 * 
 * @author Loris
 * 
 */
enum Command {
	/**
	 * The ADD command. Appropriate {@link Option}s are:
	 * {@link Option#DESCRIPTION}, {@link Option#MOVE} and
	 * {@link Option#VERBOSE} Mandatory argument count: 2
	 */
	ADD(2,2,Option.DESCRIPTION, Option.MOVE, Option.VERBOSE),
	/**
	 * The REPLACE command, Appropriate {@link Option}s are:
	 * {@link Option#DESCRIPTION}, {@link Option#MOVE} and
	 * {@link Option#VERBOSE} Mandatory argument count: 3
	 */
	REPLACE(3,3,Option.DESCRIPTION,Option.MOVE,Option.VERBOSE,Option.ID),
	/**
	 * The DELETE command,  Appropriate {@link Option}s are:
	 * {@link Option#AFTER}, {@link Option#BEFORE}, 
	 * {@link Option#ID},{@link Option#ID}, {@link Option#TEXT}, 
	 *  Mandatory argument count: 1
	 */
	DELETE(1,2,true,Option.ID,Option.TEXT,Option.BEFORE,Option.AFTER,Option.NAME),
	/**
	 * The EXPORT command. Appropriate {@link Option}s are:
	 * {@link Option#AFTER}, {@link Option#BEFORE}, 
	 * {@link Option#ID},{@link Option#ID}, {@link Option#TEXT}, 
	 * {@link Option#NAME} and {@link Option#VERBOSE} Mandatory argument count: 2
	 */
	EXPORT(2,3,true, Option.AFTER, Option.BEFORE, Option.ID, Option.TEXT, Option.NAME, Option.VERBOSE), 
	/**
	 * The LIST command. Appropriate {@link Option}s are:
	 * {@link Option#AFTER}, {@link Option#BEFORE}, 
	 * {@link Option#ID},{@link Option#ID}, {@link Option#TEXT} and
	 * {@link Option#NAME} Mandatory argument count: 1
	 */
	LIST(1,1,Option.ID,Option.NAME,Option.TEXT,Option.BEFORE,Option.AFTER), 
	
	/**
	 * The HELP command
	 * Mandatory argument count: 0
	 */
	HELP(0,1),
	
	/**
	 * The SERVER command.
	 * Mandatory argument count: 2
	 * Max arguments: 2
	 * NO Appropriate options.
	 */
	SERVER(2,2),
	
	/**
	 * Just a little easter egg.
	 */
	FORTYTWO();

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
	public static Command parse(final String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		for (final Command c : values()) {
			if (str.equalsIgnoreCase(c.name())) {
				return c;
			}
		}
		try{
			int t = Integer.parseInt(str);
			if(t == 42){
				return FORTYTWO;
			}
		}catch(NumberFormatException ex){
			
		}
		return null;
	}

	private final Option[] appropriateOptions;

	private final int mandatoryArgsCount;
	
	private final int maxArgs;
	
	private final boolean allowsOnlyIDArg;

	private Command() {
		//no mandatory args, infinite max args, no id arg allowed, no appropriate options
		this(Integer.MIN_VALUE,Integer.MAX_VALUE, false, null);
	}
	
	private Command(final int mandatoryArgsCount){
		//mandatory args, infinite max args, no id arg allowed, no appropriate options
		this(mandatoryArgsCount,Integer.MAX_VALUE, false, null);
	}
	
	private Command(final int mandatoryArgsCount, final int maxArgs){
		// mandatory args, max args, no id arg allowed, no appropriate options
		this(mandatoryArgsCount, maxArgs, false, null);
	}

	private Command(final int mandatoryArgsCount, final int maxArgs, final boolean allowsIDArg,
			final Option... appropriateOptions) {
		this.maxArgs = maxArgs;
		this.appropriateOptions = appropriateOptions;
		this.mandatoryArgsCount = mandatoryArgsCount;
		this.allowsOnlyIDArg = allowsIDArg;
	}

	private Command(final Option... appropriateOptions) {
		//no mandatory args, infinite max args, no id arg allowed, appropriate options
		this(Integer.MIN_VALUE, Integer.MAX_VALUE, false, appropriateOptions);
		
	}
	
	private Command(final int mandatoryArgsCount, final Option...appropriateOptions){
		//mandatory args, infinite max args, no id arg allowed, appropriate options
		this(mandatoryArgsCount, Integer.MAX_VALUE, false,appropriateOptions);
	}
	
	private Command(final int mandatoryArgsCount, final int maxArgs, final Option...appropriateOptions){
		//mandatory args, max args, no id arg allowed, appropriate options
		this(mandatoryArgsCount, maxArgs, false, appropriateOptions);
	}
	
	public boolean isIDArgumentAllowed(){
		return allowsOnlyIDArg;
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
	 * Returns the amount of mandatory arguments for this command. If no
	 * arguments are mandatory, then {@link Integer#MIN_VALUE} is returned.
	 * 
	 * @return The amount of mandatory arguments this command has or
	 *         {@link Integer#MIN_VALUE} if no argument is mandatory.
	 */
	public int getMandatoryArgsCount() {
		return mandatoryArgsCount;
	}

	/**
	 * Checks whether a given {@link Option} is appropriate or not.
	 * 
	 * @param option
	 *            The {@link Option} to test.
	 * @return <code>true</code> if and only if the given {@link Option} is
	 *         recognized as appropriate.
	 */
	public boolean isOptionAppropriate(final Option option) {
		for (final Option o : appropriateOptions) {
			if (option.equals(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the maximum of arguments of this command.
	 * @return The maximum of arguments of this command.
	 */
	public int getMaxArgs(){
		return maxArgs;
	}
}
