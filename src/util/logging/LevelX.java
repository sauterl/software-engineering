package util.logging;

import java.util.logging.Level;

/**
 * The {@link LevelX} class is an eXtendend version of {@link Level} class. It
 * introduces thow new levels, the {@link LevelX#DEBUG}, which is designed to
 * identify debug messages and the {@link LevelX#FATAL}, which identifies
 * heavily error messages<br />
 * Further the {@link LevelX} class provides two masks for existing
 * {@link Level}s: {@link LevelX#ERROR} which equals {@link Level#SEVERE} and
 * {@link LevelX#WARN} which stands for {@link Level#WARNING}. <br />
 * Thus the new ordering of the levels is (in descending order):
 * <ul>
 * <li><code>FATAL</code> (highest value)</li>
 * <li><code>ERROR</code></li>
 * <li><code>WARN</code></li>
 * <li><code>INFO</code></li>
 * <li><code>CONFIG</code></li>
 * <li><code>DEBUG</code></li>
 * </ul>
 * The native {@link Level} class provides levels below <code>DEBUG</code>.
 * 
 * @author Loris
 * 
 */
public class LevelX extends Level {

	/**
	 * Eclipse generated serialID
	 */
	private static final long serialVersionUID = 4867372786151143760L;

	/**
	 * FATAL is a message level indicating fatal failure. <br />
	 * In general FATAL messages should describe events which caused the
	 * application to crash. Those messages should be meanful to system
	 * administrators and end users. The level's value is initialized to
	 * {@link Level#SEVERE}'s value +100.
	 */
	public static Level FATAL = new LevelX("FATAL",
			Level.SEVERE.intValue() + 100);
	/**
	 * ERROR is a message level indicating serious failure. <br />
	 * In general ERROR messages should describe events which caused the
	 * application to stop working as intended. Those messages should be meanful
	 * to system administrators and end users. This level's value is identical
	 * to the value of {@link Level#SEVERE}.
	 */
	public static Level ERROR = new LevelX("ERROR", Level.SEVERE.intValue());

	/**
	 * WARNING is a message level indicating upcoming problems. <br />
	 * In general WARNING messages should describe events which may cause
	 * problems or when a somehow 'lightweight' problem got solved. Those
	 * messages should be meanful to system administrators and end users. This
	 * level's value is identical to the value of {@link Level#WARNING}.
	 */
	public static Level WARN = new LevelX("WARN", Level.WARNING.intValue());

	/**
	 * DEBUG is a message level indicating debugging information. <br />
	 * In general DEBUG messages should contain debug informations to help
	 * understanding the application's behavior. Those messages may be useful to
	 * end users, but should be solely addressed to developers. The level's
	 * value is initialized to {@link Level#CONFIG}'s value - 100.
	 */
	public static Level DEBUG = new LevelX("DEBUG",
			Level.CONFIG.intValue() - 100);

	/**
	 * Parses a given name to a level. The mapping is only to levels with a
	 * higher value than {@link LevelX#DEBUG}. The parameter can either be the
	 * name (case gets ignored) or the level's value. If no level mapping was
	 * found, an IllegalArgumentException is thrown.
	 * 
	 * @param name
	 *            The name of the level or its integer value.
	 * @return The corresponding level
	 * @throws IllegalArgumentException
	 *             If the parameter was null, had zero length (was empty string)
	 *             or no mapping was found.
	 */
	public static Level parse(final String name)
			throws IllegalArgumentException {
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException(
					"Cannot parse null or empty string.");
		}
		try {
			return parseValue(Integer.parseInt(name));
		} catch (final NumberFormatException ex) {
			return parseName(name);
		}
	}

	private static Level parseName(final String name) {
		if (name.equalsIgnoreCase(ERROR.getName())) {
			return ERROR;
		}
		if (name.equalsIgnoreCase(WARN.getName())) {
			return WARN;
		}
		if (name.equalsIgnoreCase(DEBUG.getName())) {
			return DEBUG;
		}
		if (name.equalsIgnoreCase(INFO.getName())) {
			return INFO;
		}
		if (name.equalsIgnoreCase(CONFIG.getName())) {
			return CONFIG;
		}
		if (name.equalsIgnoreCase(OFF.getName())) {
			return OFF;
		}
		throw new IllegalArgumentException("No mapping was found for name: "
				+ name);
	}

	/**
	 * Parses a given integer value to the mapping Level. The levels defined in
	 * LevelX are also considered and if no matching was found, a
	 * illegalargumentexception is thrown.
	 * 
	 * @param value
	 *            The level value
	 * @return The corresponding level.
	 */
	private static Level parseValue(final int value) {
		if (value == ERROR.intValue()) {
			return ERROR;
		}
		if (value == WARN.intValue()) {
			return WARN;
		}
		if (value == DEBUG.intValue()) {
			return DEBUG;
		}
		if (value == INFO.intValue()) {
			return INFO;
		}
		if (value == CONFIG.intValue()) {
			return CONFIG;
		}
		if (value == OFF.intValue()) {
			return OFF;
		}
		throw new IllegalArgumentException("Unknown level value: " + value);
	}

	/**
	 * Creates a new {@link LevelX} with specified name and value. The
	 * constructor is protected to support extensions of this class, but in use
	 * cases the pre-defined constants like {@link LevelX#FATAL} or
	 * {@link LevelX#DEBUG} should get used.
	 * 
	 * @param name
	 *            The displayable name of the level, by convention in upper
	 *            case.
	 * @param value
	 *            The integer value of the level.
	 * @see java.util.logging.Level
	 */
	protected LevelX(final String name, final int value) {
		super(name, value);
	}

}
