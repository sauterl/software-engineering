package util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

class VerboseHandler extends StreamHandler {

	private static class VerboseFormatter extends Formatter {
		private final DateFormat dateFormat = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss");

		/**
		 * Formats the given {@link LogRecord}.
		 * 
		 * <p>
		 * The layout of a single formatted {@link LogRecord} is like so:<br>
		 * {@code dd-MM-yyyy HH:mm:ss [LEVEL] [name] msg NL}<br>
		 * The described time format is in {@link SimpleDateFormat}-notation.<br>
		 * The LEVEL stands for the record's level.<br>
		 * name stands for the name of the logger which created the record.<br>
		 * The placeholder msg will be replaced by the record's message and NL
		 * stands for a new line.
		 * </p>
		 * 
		 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
		 */
		@Override
		public synchronized String format(final LogRecord record) {
			final String dateStr = dateFormat.format(new Date(record
					.getMillis()));
			final String nameStr = record.getLoggerName();
			final String lvlStr = record.getLevel().getName();
			final StringBuffer sb = new StringBuffer(256);
			// sb.append(String.format("[%s]", dateStr));
			sb.append(String.format("[%s]", nameStr));
			sb.append(String.format("[%s]", lvlStr));
			sb.append(" ");
			sb.append(record.getMessage());
			sb.append("\n");
			final Throwable thrown = record.getThrown();
			if (thrown != null) {
				// record has throwable
				final StringWriter strWtr = new StringWriter();
				final PrintWriter pWtr = new PrintWriter(strWtr);
				pWtr.println();
				//thrown.printStackTrace(pWtr);
				pWtr.flush();
				pWtr.close();
				sb.append(strWtr.toString());
			}
			return sb.toString();
		}
	}

	public VerboseHandler() {
		this(LevelX.ERROR);
	}

	public VerboseHandler(final Level lvl) {
		setFormatter(new VerboseFormatter());
		setOutputStream(System.out);
		setLevel(lvl);
	}
	
	/**
     * Publish a <tt>LogRecord</tt>.
     * <p>
     * The logging request was made initially to a <tt>Logger</tt> object,
     * which initialized the <tt>LogRecord</tt> and forwarded it here.
     * <p>
     * @param  record  description of the log event. A null record is
     *                 silently ignored and is not published
     */
    @Override
    public void publish(LogRecord record) {
        super.publish(record);
        flush();
    }

    /**
     * Override <tt>StreamHandler.close</tt> to do a flush but not
     * to close the output stream.  That is, we do <b>not</b>
     * close <tt>System.err</tt>.
     */
    @Override
    public void close() {
        flush();
    }

}
