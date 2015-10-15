package util.logging;

import java.util.logging.Level;

/**
 * The {@link LevelX} class is an eXtendend version of {@link Level} class.
 * It introduces thow new levels, the {@link LevelX#DEBUG}, which is designed to
 * identify debug messages and the {@link LevelX#FATAL}, which identifies heavily error messages<br />
 * Further the {@link LevelX} class provides two masks for existing {@link Level}s: {@link LevelX#ERROR}
 * which equals {@link Level#SEVERE} and {@link LevelX#WARN} which stands for {@link Level#WARNING}.
 * <br />
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
 * @author Loris
 *
 */
public class LevelX extends Level {

	/**
	 * Eclipse generated serialID
	 */
	private static final long serialVersionUID = 4867372786151143760L;

	public static Level FATAL = new LevelX("FATAL", Level.SEVERE.intValue() + 100);
	
	public static Level ERROR = new LevelX("ERROR", Level.SEVERE.intValue());

	public static Level WARN = new LevelX("WARN", Level.WARNING.intValue());

	public static Level DEBUG = new LevelX("DEBUG",
			Level.CONFIG.intValue() - 100);

	/**
	 * Parses a given name to a level. The mapping is only to levels ERROR,
	 * WARN, INFO, DEBUG and CONFIG (as well as OFF). The parameter can either
	 * be the name (case gets ignored) or the level's value. If no level mapping
	 * was found, an IllegalArgumentException is thrown.
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

	protected LevelX(final String name, final int value) {
		super(name, value);
	}

}
