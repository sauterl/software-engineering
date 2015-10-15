package util.logging;

import java.util.logging.Level;
@Deprecated
public class HandlerConfiguration {

	private String ref;
	private final String name;
	private final Level level;

	public HandlerConfiguration(final String name, final Level level) {
		this.name = name;
		this.level = level;
	}

}
